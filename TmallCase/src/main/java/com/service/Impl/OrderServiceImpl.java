package com.service.Impl;

import com.dao.OrderDao;
import com.pojo.Order;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Override
    public void deleteOrder(Integer oid) {
        orderDao.deleteOrder(oid);
    }

    @Override
    public void createOrder(Order order) {
        orderDao.createOrder(order);
    }

    @Override
    public Order getOrder(Integer oid){
        List<Order> orders = orderDao.getOrder(oid);
        Order order = null;
        if (orders.size() > 0)
            order = orders.get(0);
        return order;
    }

    @Override
    public void updateOrderStatus(Order order){
        orderDao.updateOrderStatus(order);
    }
}
