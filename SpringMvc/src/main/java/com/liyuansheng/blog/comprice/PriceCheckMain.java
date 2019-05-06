package com.liyuansheng.blog.comprice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Author: dainan
 * @Date: 2019/4/25 10:47
 * @Description:
 */
@Service
public class PriceCheckMain {

    /**
     * 京东查询服务
     */
    @Autowired
    JDProductListService jdProductListService;

    /**
     * 天猫查询服务
     */
    @Autowired
    TMProductListService tmProductListService;



    private static PriceCheckUtil pcu = PriceCheckUtil.getInstance();

    public List<ProductInfo> getProductList(String productName) {

        String jdUrl = Constants.JDURL + productName + Constants.JDENC;

        String tmUrl = Constants.TMURL + productName;

        return getProducts(jdUrl, tmUrl, productName);
    }

    public List<Map<String, ProductInfo>> getProductFromUrls(String jdUrl, String tbUrl, String productName) {
        List<Map<String, ProductInfo>> retListMap = new ArrayList<Map<String,ProductInfo>>();
        List<ProductInfo> jdProductList = jdProductListService.getProductList(jdUrl, productName);
        List<ProductInfo> tmProductList = tmProductListService.getProductList(tbUrl, productName);
        for(int i = 0; i < jdProductList.size(); i++){
            String jdProductName = jdProductList.get(i).getProductName();
            Map<String, ProductInfo> map = new HashMap<String, ProductInfo>();
            map.put("JD", jdProductList.get(i));
            ProductInfo tbProduct = pcu.getSimilarity(jdProductName, tmProductList);
            map.put("TM", tbProduct);
            retListMap.add(map);
        }

        return retListMap;
    }
    public List< ProductInfo> getProducts(String jdUrl, String tmUrl, String productName) {
        ExecutorService executorService = CommonThreadExcutorUtils.getDefaultExecutorService();
        List<ProductInfo> retList = new ArrayList<ProductInfo>();
        System.out.println("京东开始时间1："+ System.currentTimeMillis());
        Future jdFuture = executorService.submit(new ProductThread(jdProductListService,jdUrl,productName));
        Future tmFuture = executorService.submit(new ProductThread(tmProductListService,tmUrl,productName));
        List<ProductInfo> jdProductList = new ArrayList<>();
        try {
            jdProductList = (List<ProductInfo>) jdFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<ProductInfo> tmProductList = new ArrayList<>();
        try {
            tmProductList = (List<ProductInfo>) tmFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        retList.addAll(jdProductList);
        retList.addAll(tmProductList);

        return retList;
    }

    /**
     *  爬取输入商户信息线程
     */
    class ProductThread implements Callable{
        /**
         * 爬去商品信息服务
         */
        ProductListService productListService;
        /**
         * 地址
         */
        String url;
        /**
         * 商户名
         */
        String productName;

        ProductThread(ProductListService productListService, String url, String productName){
            this.productListService = productListService;
            this.url = url;
            this.productName = productName;
        }

        @Override
        public List<ProductInfo> call() throws Exception {
            return productListService.getProductList(url,productName);
        }
    }



}
