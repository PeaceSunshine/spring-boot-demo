package background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.VO.ProductVO;
import com.api.product.IProductService;
import com.api.search.ISearchService;
import com.entity.TProduct;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: LiuXing
 * @DATE: 2020/6/1
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference
    private IProductService productService;

    @Reference
    private ISearchService searchService;

    @GetMapping("list")
    public String list(Model model) {
        List<TProduct> list = productService.list();
        model.addAttribute("list", list);
        return "product/list";
    }

    @GetMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model, @PathVariable("pageIndex") Integer pageIndex, @PathVariable("pageSize") Integer pageSize) {
        PageInfo<TProduct> pageInfo = productService.page(pageIndex, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        return "product/list";
    }

    @PostMapping("add")
    public String add(ProductVO productVO) {
        Long productId = productService.add(productVO);
        System.out.println("商品controller id:"+productId);
        //更新solr索引库
        searchService.updateById(productId);
        return "redirect:/product/page/1/1";
    }
}
