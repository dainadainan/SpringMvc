package com.liyuansheng.blog.comprice;

/**
 * @Author: dainan
 * @Date: 2019/5/7 15:58
 * @Description:
 */
public class ShopInfo {

    /**
     * 商品网店名称
     */
    private String shopName;
    /**
     * 电商名称
     */
    private String ecName;

    /**
     * 月销售笔数
     */
    private float tradeNum;
    /**
     * 评论数
     */
    private int reviewNum;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }





    public int getReviewNum() {
        return reviewNum;
    }

    public float getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(float tradeNum) {
        this.tradeNum = tradeNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }
}
