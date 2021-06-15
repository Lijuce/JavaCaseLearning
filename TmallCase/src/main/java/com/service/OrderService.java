package com.service;

import com.pojo.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

/**
 * 订单页面的服务层接口
 */
public interface OrderService {
    // 根据订单ID删除对应订单order
    void deleteOrder(Integer oid);

    // 增加(创建)新订单order
    void createOrder(Order order);

    // 根据订单ID查询得对应订单order
    Order getOrder(Integer oid);

    // 根据订单ID更新订单order状态status
    void updateOrderStatus(Order order);
}
