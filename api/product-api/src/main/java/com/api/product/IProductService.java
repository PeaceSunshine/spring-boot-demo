package com.api.product;

import com.api.VO.ProductVO;
import com.entity.TProduct;
import common.base.IBaseService;

/**
 * @Author: LiuXing
 * @DATE: 2020/6/1
 */
public interface IProductService extends IBaseService<TProduct> {
    com.github.pagehelper.PageInfo<TProduct> page(Integer pageIndex, Integer pageSize);

    Long add(ProductVO productVO);
}
