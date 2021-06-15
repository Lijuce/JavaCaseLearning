package com.service;

import com.pojo.Order;
import com.pojo.OrderItem;
import com.pojo.User;

import java.util.List;


public interface OrderItemService {

    // 将该订单列表的orderItem进行填充
    void fillOrder(List<Order> order);

    // 列出指定用户的所有订单
    List<Order> listAllOrder(User user);

    // 根据用户ID获取购物车中对应所有订单信息
    List<OrderItem> getOrderItemInCart(Integer uid);

    // 根据用户和商品ID获取单个订单信息
    OrderItem getOneOrderItem(Integer uid, Integer pid);

    // 根据订单ID获取单个订单信息
    OrderItem getOrderItemByOrderId(Integer id);

    // 加入购物车
    void addOrderItem(OrderItem orderItem);

    // 更新购物车中的商品信息
    void updateOrderItemInCart(OrderItem orderItem);

    // 更新购物车的商品数量
    void updateCartNum(OrderItem orderItem);

    // 删除购物车的商品
    void deleteOrderItem(OrderItem orderItem);

//    // 创建新订单
//    void createOrder(Order order);
}
