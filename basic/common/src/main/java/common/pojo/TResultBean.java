package common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lx
 * @Date 2020/8/19
 * @Description
 **/
@Data
@AllArgsConstructor
public class TResultBean<T> implements Serializable {

    private String statusCode;
    private T data;
}


