package com.liyuansheng.blog.comprice;

import java.util.List;

/**
 * @Author: dainan
 * @Date: 2019/4/25 10:42
 * @Description:
 */
public interface ProductListService {
    /**
     * 爬取商品列表
     * @return
     */
    public List<ProductInfo> getProductList(String Url, String productName);
}
