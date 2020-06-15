package item.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.item.ItemService;
import com.entity.TProduct;
import com.mapper.TProductMapper;
import common.pojo.ResultBean;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author lx
 * @Date 2020/6/15
 **/
@Component
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private Configuration configuration;

    @Autowired
    private TProductMapper productMapper;
    //CPU核数
    private int coreSize = Runtime.getRuntime().availableProcessors();

    //创建线程池
    private ExecutorService pool = new ThreadPoolExecutor(coreSize, coreSize * 2, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

    @Override
    public ResultBean createHtmlById(Long id)  {
        //1.获取模板
        Template template = null;
        try {
            template = configuration.getTemplate("item.ftl");
            //2.填充数据
            Map<String, Object> data = new HashMap<>();
            TProduct product = productMapper.selectByPrimaryKey(id);
            data.put("product",product);
            FileWriter fileWriter = new FileWriter("D:\\InterliJ_IDEA\\IntelliJ IDEA 2019.3\\ideaProject\\html");
            //生成模板
            template.process(data,fileWriter);
            return ResultBean.success("ok!!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBean.error("IO异常");
        } catch (TemplateException e) {
            e.printStackTrace();
            return ResultBean.error("Freemaker异常");
        }

    }

    @Override
    public ResultBean baytchCreateHtml(List<Long> idList) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Long i = 0L; i <8L ; i++) {
            ids.add(i);
        }

        List<Future<Long>> results = new ArrayList<>(ids.size());

        for (Long id : ids) {
            results.add(pool.submit(new createHtmlTask(id)));
        }

        //后续根据处理结果进行处理
        List<Long> errors = new ArrayList<>();
        for (Future<Long> future : results) {
            try {
                //获取执行结果，阻塞等待
                Long result = future.get();
                if(result != 0){
                    errors.add(result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        for (Long error : errors) {
            //做错误处理的工作
            //1.输出日志
            log.error("批量生成页面失败，失败的页面为[{}]",errors);
            //2.将处理错误的id信息保存到日志表中
            // id product_id retry_times create_update update_time
            // 1    1            3           2019        2019

            //3.通过定时任务，扫描这张表
            //select * from t_create_html_log where retry_time<3;
            //update retry_time=retry_time+1

            //4.超过3次的记录，需要人工介入
        }
        return ResultBean.success("批量生成页面成功！");
    }

    private class createHtmlTask implements Callable<Long> {

        private Long id;

        public createHtmlTask(Long id) {
            this.id = id;
        }

        @Override
        public Long call() throws Exception {
            //1.获取模板
            Template template = null;
            //2.获取商品信息
            TProduct product = productMapper.selectByPrimaryKey(id);
            try {
                template = configuration.getTemplate("item.ftl");
                //2.填充数据
                Map<String, Object> data = new HashMap<>();
                data.put("product",product);
                FileWriter fileWriter = new FileWriter("D:\\InterliJ_IDEA\\IntelliJ IDEA 2019.3\\ideaProject\\html");
                //生成模板
                template.process(data,fileWriter);
            } catch (Exception e) {
                e.printStackTrace();
                return id;
            }
            return 0L;
        }
    }
}
