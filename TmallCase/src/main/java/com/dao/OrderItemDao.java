package com.dao;

import com.pojo.Order;
import com.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemDao {

    List<OrderItem> getAllOrderItemInCart(Integer id);

    void putNewOrderItem(OrderItem orderItem);

    List<OrderItem> getOneOrderItem(@Param("uid") Integer uid, @Param("pid") Integer pid, @Param("oid") Integer oid);

    void updateOneOrderItemInCart(OrderItem orderItem);

    void updateCartNum(OrderItem orderItem);

    /**
     * 根据订单ID删除购物车订单
     * @param orderItem
     */
    void removeCartOrderItem(OrderItem orderItem);

    /**
     * 根据订单ID获取指定完整订单
     * @param id
     * @return
     */
    List<OrderItem> getOneOrderItemByOrderId(Integer id);

    /**
     * 创建新订单
     * @param order
     */
    void createOrder(Order order);

    /**
     * 获取指定用户ID的所有订单信息
     * @param id
     * @return
     */
    List<Order> getAllOrderItemByUserId(Integer id);

    /**
     * 获取指定OrderID所包含的所有商品信息
     * @param oid
     * @return
     */
    List<OrderItem> getAllOrderItemByOrderId(Integer oid);
}
