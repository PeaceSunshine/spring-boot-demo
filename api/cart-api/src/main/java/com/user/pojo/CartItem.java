package com.user.pojo;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeSet;

/**
 * @Author lx
 * @Date 2020/8/24
 * @Description 保存在redis中购物车数据
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable,Comparable<CartItem> {

    private Long productId;

    private Integer count;

    private Date updateTime;


    /**
    * 小于0 放在左边
    * 大于0 放在右边
    * 定义比较规则
    *
    * */
    @Override
    public int compareTo(CartItem o) {
        int result = (int) (o.getUpdateTime().getTime() - this.getUpdateTime().getTime());
        if(result==0){
            return -1;
        }
        return result;
    }

    public static void main(String[] args) {
        CartItem cartItem1 = new CartItem(1L,100,new Date());
        CartItem cartItem2 = new CartItem(11L,1100,new Date());
        CartItem cartItem3 = new CartItem(2L,200,new Date(System.currentTimeMillis()+10000000));

        TreeSet<CartItem> treeSet = new TreeSet<>();
        treeSet.add(cartItem1);
        treeSet.add(cartItem2);
        treeSet.add(cartItem3);

        for (CartItem cartItem : treeSet) {
            System.out.println(cartItem);
        }


    }
}
