package common.pojo;

import jdk.internal.dynalink.beans.StaticClass;

/**
 * @Author lx
 * @Date 2020/6/10
 **/
public class ResultBean {

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
    private String date;
    //失败返回错误信息
    private String msg;

    public static  ResultBean success(String data){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatusCode(200);
        resultBean.setDate(data);
        return  resultBean;
    }
    public static ResultBean error(String msg){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatusCode(500);
        resultBean.setDate(msg);
        return  resultBean;

    }
}
