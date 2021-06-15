package com.dao;

import com.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDao {

    /**
     * 创建新订单
     * @param order
     */
    void createOrder(Order order);

    /**
     * 删除订单
     * @param oid
     */
    void deleteOrder(Integer oid);

    /**
     * 获得订单
     * @param oid
     * @return
     */
    List<Order> getOrder(Integer oid);

    void updateOrderStatus(Order order);
}
