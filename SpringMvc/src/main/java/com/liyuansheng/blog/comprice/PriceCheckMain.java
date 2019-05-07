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



    /**
     * 获取商品数据
     * @param productName
     * @param plateform
     * @return
     */
    public List< ProductInfo> getProducts( String productName,String plateform) {
        ExecutorService executorService = CommonThreadExcutorUtils.getDefaultExecutorService();
        Future future = null;
        if(plateform == "JD"){
            String url = Constants.JDURL + productName + Constants.JDENC;
            future = executorService.submit(new ProductThread(jdProductListService,url,productName));
        }
        if(plateform == "TM"){
            String url = Constants.TMURL + productName;
            future = executorService.submit(new ProductThread(tmProductListService,url,productName));
        }
        List<ProductInfo> productList = new ArrayList<>();
        try {
            productList = (List<ProductInfo>) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return productList;
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
