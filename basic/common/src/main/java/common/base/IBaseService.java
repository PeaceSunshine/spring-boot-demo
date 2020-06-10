package common.base;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IBaseService<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKey(T t);

    int updateByPrimaryKeySelective(T t);

    List<T> list();

    public PageInfo<T> page(Integer pageIndex, Integer pageSize);
}
