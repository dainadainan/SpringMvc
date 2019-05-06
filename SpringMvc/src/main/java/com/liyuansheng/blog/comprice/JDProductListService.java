package com.liyuansheng.blog.comprice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.liyuansheng.blog.comprice.Constants.JDDP;
import static com.liyuansheng.blog.comprice.Constants.JDPRICE;


/**
 * @Author: dainan
 * @Date: 2019/4/25 10:43
 * @Description:
 */
@Component
public class JDProductListService implements ProductListService {


    private static PriceCheckUtil pcu = PriceCheckUtil.getInstance();


    @Override
    public List<ProductInfo> getProductList(String jdUrl, String productName) {
        List<ProductInfo> jdProductList = new ArrayList<ProductInfo>();
        ProductInfo productInfo = null;
        String url = "";
        for (int i = 0; i < 1; i++) {
            try {
                System.out.println("JD Product 第[" + (i + 1) + "]页");
                if (i == 0) {
                    url = jdUrl;
                } else {

                    url = Constants.JDURL + pcu.getGbk(productName) + Constants.JDENC + Constants.JDPAGE + (i + 1);
                }

                System.out.println(url);
                Document document = Jsoup.connect(url).timeout(5000).get();
                System.out.println("京东爬虫结束时间：" + System.currentTimeMillis());
                Elements uls = document.select("ul[class=gl-warp clearfix]");
                Iterator<Element> ulIter = uls.iterator();
                while (ulIter.hasNext()) {
                    Element ul = ulIter.next();
                    Elements lis = ul.select("li[data-sku]");
                    Iterator<Element> liIter = lis.iterator();
                    while (liIter.hasNext()) {
                        Element li = liIter.next();
                        String pid = li.attr("data-sku");
                        Element div = li.select("div[class=gl-i-wrap]").first();
                        Elements title = div.select("div[class=p-name p-name-type-2]>a");
                        String spiderProductName = title.attr("title"); //得到商品名称
                        String shopName = li.select("div[class=p-shop]").text();
                        productInfo = new ProductInfo();
                        Future numFuture = CommonThreadExcutorUtils.getDefaultExecutorService().submit(new ReviewNumThread(pid));
                        Future priceFuture = CommonThreadExcutorUtils.getDefaultExecutorService().submit(new PriceThread(pid));
                        productInfo.setProductName(spiderProductName);
                        productInfo.setProductPrice((String) priceFuture.get());
                        productInfo.setProductid(pid);
                        String reviewNum = String.valueOf(numFuture.get());
                        productInfo.setReviewNum(reviewNum);
                        String trade = (int) (Integer.valueOf(reviewNum) / 0.3) + "到" + (int)(Integer.valueOf(reviewNum) / 0.2);
                        productInfo.setTradeNum(trade);
                        productInfo.setShopName(shopName);
                        productInfo.setEcName("JD");
                        jdProductList.add(productInfo);
                    }
                }
            } catch (Exception e) {
                System.out.println("Get JD product has error [" + url + "]");
                System.out.println(e.getMessage());
            }
        }
        System.out.println("京东结束时间1：" + System.currentTimeMillis());
        return jdProductList;
    }

    /***
     * 获取评论数
     */
    class ReviewNumThread implements Callable {
        /**
         * 商户ID
         */
        String pid;

        ReviewNumThread(String pid) {
            this.pid = pid;
        }

        @Override
        public Object call() throws Exception {

            return getReviewNum(pid);
        }
    }

    /**
     * 价钱线程
     */
    class PriceThread implements Callable {
        /**
         * 商户ID
         */
        String pid;

        PriceThread(String pid) {
            this.pid = pid;
        }

        @Override
        public Object call() throws Exception {

            return getPrice(pid);
        }
    }


    /**
     * 获取评论数
     */
    public String getReviewNum(String pid) {
        Document document = null;
        try {
            document = Jsoup.connect(JDDP + pid).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(document.body().text());
        JSONObject jsonNode = (JSONObject) ((JSONArray) JSONObject.parseObject(String.valueOf(document.body().text())).get("CommentsCount")).get(0);
        return String.valueOf(jsonNode.get("CommentCount"));
    }

    /**
     * 获取商品价钱
     *
     * @return
     */
    public String getPrice(String pid) {
        String url = JDPRICE + pid + "&origin=2";
        ResponseEntity<String> price = getBody(url);
        String text = StringUtils.substringBetween(price.getBody(), "(", ")");
        JSONObject jsonNode = (JSONObject) (JSONObject.parseArray(text)).get(0);
        return (String) jsonNode.get("p");
    }


    /**
     * 根据不同关键字查询
     * 根据URL获取网页信息
     *
     * @param Url
     * @return
     */
    private static ResponseEntity<String> getBody(String Url) {


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.put("user-agent", Collections.singletonList("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"));
        headers.put("content-encoding", Collections.singletonList("gzip"));
        headers.put("content-security-policy", Collections.singletonList("upgrade-insecure-requests"));
        headers.put("content-type", Collections.singletonList("application/json; charset=utf-8"));
        headers.put("proc_node", Collections.singletonList("web-111.mweibo.bx.intra.weibo.cn"));
        headers.put("server", Collections.singletonList("Tengine/2.2.2"));
        headers.put("ssl_node", Collections.singletonList("ssl-010.mweibo.tc.intra.weibo.cn"));
        headers.put("status", Collections.singletonList("200"));
        headers.put("vary", Collections.singletonList("Accept-Encoding"));
        headers.put("x-powered-by", Collections.singletonList("PHP/7.2.1"));

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> httpMessageConverter : list) {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(Charset.forName("utf-8"));
            }
        }

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(params, headers);
        ResponseEntity<String> baseRspBean = null;

        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        final HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);
        try {
            baseRspBean = restTemplate.postForEntity(Url, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseRspBean;
    }


}
