package com.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.product.IProductTypeService;
import com.entity.TProductType;
import common.pojo.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author lx
 * @Date 2020/6/11
 **/
@Controller
@RequestMapping("index")
public class IndexController {

    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping("show")
    public String showIndex(Model model){
        //1.获取到数据
        List<TProductType> list = productTypeService.list();
        //2.传递到前端进行展示
        model.addAttribute("list", list);
        //
        System.out.println(list.toString());
        return "index";
    }

    @RequestMapping("listType")
    @ResponseBody
    public ResultBean listType(){
        //1.获取到数据
        List<TProductType> list = productTypeService.list();
        //2.封装返回
        return ResultBean.success("list");
    }
}
