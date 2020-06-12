package com.api.search;

import com.entity.TProduct;
import common.pojo.ResultBean;

import java.util.List;

/**
 * @Author lx
 * @Date 2020/6/12
 **/
public interface ISearchService {

    ResultBean initAllData();

    List<TProduct> searchByKeyWord(String keyWord);

}
