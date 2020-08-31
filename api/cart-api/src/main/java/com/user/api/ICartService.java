package com.user.api;

import common.pojo.ResultBean;
import common.pojo.TResultBean;

/**
 * @Author lx
 * @Date 2020/8/24
 * @Description
 **/
public interface ICartService {

    /**
     *
     * @param token 购物车的标识
     * @param productId 购买的商品id
     * @param count 购买的商品数量
     * @return
     */
    public ResultBean add(String token, Long productId, Integer count);

    public ResultBean del(String token,Long productId);

    public ResultBean update(String token,Long productId,Integer count);

    public TResultBean query(String token);

    public ResultBean merge(String nologinKey,String loginKey);
}
