package item;

import com.entity.TProduct;
import com.mapper.TProductMapper;

import freemarker.template.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemServiceApplicationTests {

    @Autowired
    private Configuration configuration;

    @Autowired
    private TProductMapper productMapper;

    @Test
    public void testFreemarker() {

        //1.获取模板
        Template template = null;
        try {
            template = configuration.getTemplate("item.ftl");
            //2.填充数据
            Map<String, Object> data = new HashMap<>();
            TProduct product = productMapper.selectByPrimaryKey(1L);
            data.put("product", product);
            FileWriter fileWriter = new FileWriter("D:\\InterliJ_IDEA\\IntelliJ IDEA 2019.3\\ideaProject\\html\\1.html");
            //生成模板
            template.process(data, fileWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
