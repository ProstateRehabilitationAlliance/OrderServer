package com.prostate.order.mapper.master;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 13:26 2018/7/17
 */
public interface BaseWriteMapper<E> {

    int insertSelective(E e);

    int updateSelective(E e);

    int deleteById(String id);
}
