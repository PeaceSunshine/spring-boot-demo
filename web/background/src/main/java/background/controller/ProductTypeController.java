package background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.product.IProductTypeService;
import com.entity.TProductType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: LiuXing
 * @DATE: 2020/5/28
 */
@RestController
@RequestMapping("productType")
public class ProductTypeController {

    @Reference
    private IProductTypeService productTypeService;

    @GetMapping("list")
    public List<TProductType> list() {
        return productTypeService.list();
    }
}
