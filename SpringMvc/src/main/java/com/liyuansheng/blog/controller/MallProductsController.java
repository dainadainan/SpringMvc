package com.liyuansheng.blog.controller;

import com.alibaba.fastjson.JSON;
import com.liyuansheng.blog.comprice.*;
import com.liyuansheng.blog.dto.UserDTO;
import com.liyuansheng.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Author: dainan
 * @Date: 2019/4/28 16:46
 * @Description:
 */
@RestController
@RequestMapping(value = "/admin/user")
public class MallProductsController {

    /**
     * 商品价格
     */
    @Autowired
    PriceCheckMain priceCheckMain;

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

    @RequestMapping(value ="/mall", method = RequestMethod.GET)
    public ModelAndView helloSpringBoot(@RequestParam String productName){
        ModelAndView modelAndView = new ModelAndView();
        List< ProductInfo> jdproducts =  priceCheckMain.getProducts(productName,"JD");
        List< ProductInfo> tmproducts =  priceCheckMain.getProducts(productName,"TM");

        List<ShopInfo> shopInfos = jdProductListService.caculateShopInfos(jdproducts);
        shopInfos.addAll(jdProductListService.caculateShopInfos(tmproducts));

        List<ProductInfo> products = new ArrayList<>();
        products.addAll(jdproducts);
        products.addAll(tmproducts);
        modelAndView.addObject("shopInfos",shopInfos);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("/admin/product/list");
        return modelAndView;
    }
}
