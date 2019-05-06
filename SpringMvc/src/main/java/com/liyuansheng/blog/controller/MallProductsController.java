package com.liyuansheng.blog.controller;

import com.alibaba.fastjson.JSON;
import com.liyuansheng.blog.comprice.PriceCheckMain;
import com.liyuansheng.blog.comprice.ProductInfo;
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

    @Autowired
    private UserService userService;
    /**
     * 商品价格
     */
    @Autowired
    PriceCheckMain priceCheckMain;

    @RequestMapping(value ="/mall", method = RequestMethod.GET)
    public ModelAndView helloSpringBoot(@RequestParam String productName){
        ModelAndView modelAndView = new ModelAndView();
        List< ProductInfo> products =  priceCheckMain.getProductList(productName);
        PageRequest request = new PageRequest(0,5);
        Page<UserDTO> userDTOPage = userService.findAll(request);
        modelAndView.addObject("userDTOPage",userDTOPage);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("/admin/product/list");
        return modelAndView;
    }
}
