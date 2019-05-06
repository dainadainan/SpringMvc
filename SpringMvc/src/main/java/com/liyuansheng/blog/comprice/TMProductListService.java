package com.liyuansheng.blog.comprice;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dainan
 * @Date: 2019/4/26 14:00
 * @Description:
 */
@Component
public class TMProductListService implements ProductListService {


    @Override
    public List<ProductInfo> getProductList(String tmUrl, String productName) {
        List<ProductInfo> tmProductInfos = new ArrayList<>();
        // 动态模拟请求数据
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(tmUrl + productName);
        // 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        CloseableHttpResponse response = null;
        System.out.println("天猫爬虫开始时间："+ System.currentTimeMillis());
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("天猫爬虫结束时间："+ System.currentTimeMillis());
        // 获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            HttpEntity entity = response.getEntity();
            // 如果状态响应码为200，则获取html实体内容或者json文件
            if (statusCode == 200) {
                String html = null;
                try {
                    html = EntityUtils.toString(entity, Consts.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 提取HTML得到商品信息结果
                Document doc = null;
                // doc获取整个页面的所有数据
                doc = Jsoup.parse(html);
                //输出doc可以看到所获取到的页面源代码
                //      System.out.println(doc);
                // 通过浏览器查看商品页面的源代码，找到信息所在的div标签，再对其进行一步一步地解析
                Elements ulList = doc.select("div[class='view grid-nosku']");
                Elements liList = ulList.select("div[class='product']");
                // 循环liList的数据（具体获取的数据值还得看doc的页面源代码来获取，可能稍有变动）
                for (Element item : liList) {
                    // 商品ID
                    String id = item.select("div[class='product']").select("p[class='productStatus']").select("span[class='ww-light ww-small m_wangwang J_WangWang']").attr("data-item");
                    // 商品名称
                    String name = item.select("p[class='productTitle']").select("a").attr("title");
                    // 商品价格
                    String price = item.select("p[class='productPrice']").select("em").attr("title");
                    String shopName = item.select("div[class='productShop']").select("a").text();
                    // 商品网址
                    String goodsUrl = item.select("p[class='productTitle']").select("a").attr("href");
                    // 商品图片网址
                    String imgUrl = item.select("div[class='productImg-wrap']").select("a").select("img").attr("data-ks-lazyload");
                    //销量
                    String  tradeNum = item.select("p[class='productStatus']").select("span").text();
                    ProductInfo productInfo = new ProductInfo();
                    productInfo.setShopName(shopName);
                    productInfo.setProductid(id);
                    productInfo.setProductName(name);
                    productInfo.setProductPrice(price);
                    String[] trade = tradeNum.split("笔");
                    if(trade != null && trade.length == 2){
                        productInfo.setTradeNum(trade[0]);
                        productInfo.setReviewNum(trade[1]);
                    }

                    productInfo.setEcName("TM");
                    tmProductInfos.add(productInfo);
                }
                // 消耗掉实体
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 消耗掉实体
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("天猫结束时间1："+ System.currentTimeMillis());
        return tmProductInfos;
    }

    public static void main(String[] args) {
        String url = "https://list.tmall.com/search_product.htm?q=";
        new TMProductListService().getProductList(url,"苹果电脑");
        try {

            Document document = Jsoup.connect("https://list.tmall.com/search_product.htm?q=").timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
