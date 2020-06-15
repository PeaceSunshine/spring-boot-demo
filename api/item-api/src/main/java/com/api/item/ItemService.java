package com.api.item;

import common.pojo.ResultBean;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

/**
 * @Author lx
 * @Date 2020/6/15
 **/
public interface ItemService {

    /**
     * 根据Id创建静态html模板
     * @param id
     * @return
     */
    public ResultBean createHtmlById(Long id) ;

    /**
     * 批量创建html模板
     * @param idList
     * @return
     */
    public ResultBean baytchCreateHtml(List<Long> idList);

}
