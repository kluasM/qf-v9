package com.qf.v9.common.base;

import java.util.List;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/8
 */
public interface IBaseService<T> {
    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);

    List<T> list();
}
