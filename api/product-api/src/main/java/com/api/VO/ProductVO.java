package com.api.VO;

import com.entity.TProduct;

import java.io.Serializable;

/**
 * @Author: LiuXing
 * @DATE: 2020/6/3
 */
public class ProductVO implements Serializable {
    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    private TProduct product;
    private String  productDesc;
}
