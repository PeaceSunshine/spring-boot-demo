package com.cart.service;

import com.user.api.ICartService;
import com.user.pojo.CartItem;
import common.pojo.ResultBean;
import common.pojo.TResultBean;
import org.springframework.data.redis.core.RedisTemplate;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author lx
 * @Date 2020/8/24
 * @Description
 **/
public class CartServiceImpl implements ICartService {

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object>  redisTemplate;

    @Override
    public ResultBean add(String token, Long productId, Integer count) {
        //1.根据token查询到购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
       //2.如果购物已存在，且存在该商品,直接修改数量即可
        if(cart.size()!=0){
            //判断当前购物车是否存在该商品
            if(redisTemplate.opsForHash().hasKey(key.toString(),productId)){
                CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
                //商品存在修改数量
                cartItem.setCount(cartItem.getCount()+count);
                //更新操作时间
                cartItem.setUpdateTime(new Date());
                //保存变更
                redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
                //设置有效期
                redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);
                
                return ResultBean.success("true");
            }
        }
        //其它情况直接添加到购物车
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setCount(count);
        cartItem.setUpdateTime(new Date());
        //添加购物项到购物车中
        redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
        //设置有效期
        redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);
        //返回
        return ResultBean.success("true");
                
    }

    @Override
    public ResultBean del(String token, Long productId) {
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Long delete = redisTemplate.opsForHash().delete(key.toString(), productId);
        if(delete == 1){
            return ResultBean.success("true");
        }
        return ResultBean.error("false");
    }

    @Override
    public ResultBean update(String token, Long productId, Integer count) {
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        //获取该记录是否存在
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
        if(cartItem != null){
            cartItem.setCount(count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
            return ResultBean.success("true");
        }
        return ResultBean.error("false");
    }

    @Override
    public TResultBean query(String token) {
        //1.根据token查询到购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        
        if(cart.size()>0){
            //1.获取到values,没有按照时间顺序排列
            Collection<Object> values = cart.values();
            //2.存放排序后的结果
            TreeSet<CartItem> treeSet = new TreeSet<>();
            for (Object value : values) {
                CartItem cartItem = (CartItem) value;
                treeSet.add(cartItem);
            }
            //3.将treeset转换为list
            ArrayList<CartItem> list = new ArrayList<>(treeSet);
            return new TResultBean("200",list);
        }
        return new TResultBean("200",null);
    }

    @Override
    public ResultBean merge(String nologinKey, String loginKey) {
        //1.判断未登录购物车是否有上坪
        Map<Object, Object> nologinCart = redisTemplate.opsForHash().entries(nologinKey);
        if (nologinCart.size()==0){
            return ResultBean.success("无需合并!");
        }
        Map<Object, Object> loginCart = redisTemplate.opsForHash().entries(loginKey);
        if(loginCart.size()==0){
            //1.购物车为空直接将未登录购物车放入当前购物中
            loginCart.putAll(nologinCart);
            //2.删除未登录购物车
            redisTemplate.delete(nologinKey);
            return ResultBean.success("合并成功!");
        }
        //两辆购物车物品不同则进行比较合并
        Set<Map.Entry<Object, Object>> nologinEntrys = nologinCart.entrySet();
        for (Map.Entry<Object, Object> nologinEntry : nologinEntrys) {
            CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(loginKey, nologinEntry.getKey());
            if(cartItem!=null){
                //说明商品存在，做数量叠加
                CartItem nologinCartItem = (CartItem) nologinEntry.getValue();
                cartItem.setCount(cartItem.getCount()+nologinCartItem.getCount());
                cartItem.setUpdateTime(new Date());
                redisTemplate.opsForHash().put(loginKey,nologinEntry.getKey(),nologinEntry.getValue());
            }else{
                //商品不存在
                redisTemplate.opsForHash().put(loginKey,nologinEntry.getKey(),nologinEntry.getValue());
            }
        }
        //删除购物车
        redisTemplate.delete(loginKey);
        return ResultBean.success("合并成功!");
    }
}
