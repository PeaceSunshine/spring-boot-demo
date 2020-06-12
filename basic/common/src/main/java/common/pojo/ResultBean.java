package common.pojo;

import jdk.internal.dynalink.beans.StaticClass;

import java.io.Serializable;

/**
 * @Author lx
 * @Date 2020/6/10
 **/
public class ResultBean implements Serializable {


    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //状态码
    private Integer statusCode;
    //成功返回数据
    private String data;
    //失败返回错误信息
    private String msg;

    public static  ResultBean success(String data){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatusCode(200);
        resultBean.setData(data);
        return  resultBean;
    }
    public static ResultBean error(String msg){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatusCode(500);
        resultBean.setMsg(msg);
        return  resultBean;

    }
}
