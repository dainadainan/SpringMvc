package com.liyuansheng.blog.comprice;

/**
 * @Author: dainan
 * @Date: 2019/4/25 10:45
 * @Description:
 */
public class Constants {

    /**
     * JDURL
     */
    public static String JDURL = "http://search.jd.com/Search?keyword=";
    /**
     * JD汉字编码格式
     */
    public static String JDENC = "&enc=utf-8";
    /**
     * JD分页
     */
    public static String JDPAGE ="&page=";
    /**
     * TBURL
     */
    public static String TBURL = "https://s.taobao.com/search?q=";
    /**
     * 淘宝分页
     */
    public static String TBPAGE = "&s=";
    /**
     * 超时时间
     */
    public static int TIMEOUT = 50000;

    /**
     * 天猫
     */
    public static  String TMURL = "https://list.tmall.com/search_product.htm?q=";

    /**
     * 京东评论获取url
     */
    public static  String JDDP = "https://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds=";

    /**
     * 京东Price
     */
    public static  String JDPRICE = "http://pm.3.cn/prices/pcpmgets?callback=jQuery&skuids=";
}
