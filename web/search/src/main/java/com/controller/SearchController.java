package com.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.search.ISearchService;
import com.entity.TProduct;
import common.pojo.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author lx
 * @Date 2020/6/12
 **/
@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("initAllData")
    @ResponseBody
    public ResultBean initAllData(){

        return searchService.initAllData();
    }

    @RequestMapping("searchByKeyWord")
    //    @ResponseBody
    public String searchByKeyWord(String keyWord, Model model){

        //1.得到商品集合
        List<TProduct> list = searchService.searchByKeyWord(keyWord);
        model.addAttribute("list",list);
        return "search";
    }
}
