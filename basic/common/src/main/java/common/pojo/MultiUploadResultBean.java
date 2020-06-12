package common.pojo;

import javax.print.attribute.standard.PrinterURI;
import java.security.PublicKey;

/**
 * @Author lx
 * @Date 2020/6/11
 **/
public class MultiUploadResultBean {

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    private String errno;

    private String[] data;

   public MultiUploadResultBean(String errno,String[] data){
       this.data = data;
       this.errno = errno;
   }

}
