package com.prostate.order.mapper.slave;

import java.util.List;

/**
 * @Author: developerfengrui
 * @Description:
 * @Date: Created in 13:26 2018/7/17
 */
public interface BaseReadMapper<E> {
    E selectById(String id);

    List<E> selectByParams(E e);
}
