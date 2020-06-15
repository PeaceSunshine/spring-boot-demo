package com.api.search;

import com.entity.TProduct;
import common.pojo.PageResultBean;
import common.pojo.ResultBean;

import java.util.List;

/**
 * @Author lx
 * @Date 2020/6/12
 **/
public interface ISearchService {

    ResultBean initAllData();

    List<TProduct> searchByKeyWord(String keyWord);

    /**
     * 带分页的搜索功能
     * @param keyword
     * @param pageIndex
     * @param pageSized
     * @return
     */
    PageResultBean<TProduct> searchByKeyWord(String keyword, Integer pageIndex, Integer pageSized);

    /**
     *
     * 增量复制更新solr索引库
     * @return
     */
    ResultBean updateById(Long id);
}
