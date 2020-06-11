package background.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import common.pojo.MultiUploadResultBean;
import common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.sql.SQLOutput;

/**
 * @Author lx
 * @Date 2020/6/10
 **/
@Controller
@RequestMapping("file")
public class FileController {

    @Value("${image.server}")
    private String imageServer;

    @Autowired
    private FastFileStorageClient client;

    @RequestMapping("upload")
    @ResponseBody
    public ResultBean upload(MultipartFile file) {
        //1.获取文件后缀名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //2.使用fastdfs上传图片
        try {
            StorePath storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extName, null);
            System.out.println("文件路径:" + storePath.getFullPath());
            String filePath = new StringBuffer(imageServer).append(storePath.getFullPath()).toString();
            return ResultBean.success(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.error("您的网络不顺畅，请稍后再试!!!");
        }
    }

    @RequestMapping("multiUpload")
    @ResponseBody
    public MultiUploadResultBean upload(MultipartFile[] files) {

        String[] data = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            //1.获取文件后缀名
            String originalFilename = files[i].getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //2.使用fastdfs上传图片
            try {
                StorePath storePath = client.uploadImageAndCrtThumbImage(files[i].getInputStream(), files[i].getSize(), extName, null);
                System.out.println("文件路径:" + storePath.getFullPath());
                String filePath = new StringBuffer(imageServer).append(storePath.getFullPath()).toString();
                data[i] = filePath;

            } catch (Exception e) {
                e.printStackTrace();
                return new MultiUploadResultBean("-1", null);
            }
        }
        return new MultiUploadResultBean("0", data);
    }

}
