package com.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author lx
 * @Date 2020/8/24
 * @Description 对应视图层 view object
 **/
@Data
@AllArgsConstructor
public class CartItemVO {
    private Long productId;

    private Integer count;

    private Date date;
}
