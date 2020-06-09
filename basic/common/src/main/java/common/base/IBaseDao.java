package common.base;

import java.util.List;

public interface IBaseDao<T> {
    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKey(T t);

    int updateByPrimaryKeySelective(T t);

    List<T> list();
}
