package com.service.Impl;

import com.dao.OrderItemDao;
import com.dao.ProductDao;
import com.dao.ProductImageDao;
import com.pojo.*;
import com.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderItemService")
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    ProductImageDao productImageDao;

    @Override
    public List<Order> listAllOrder(User user) {
        return orderItemDao.getAllOrderItemByUserId(user.getId());
    }

    @Override
    public List<OrderItem> getOrderItemInCart(Integer uid) {
        List<OrderItem> orderItems = orderItemDao.getAllOrderItemInCart(uid);
        return orderItems;
    }

    /**
     * 在购物车中添加新商品
     * @param orderItem
     */
    @Override
    public void addOrderItem(OrderItem orderItem){
        orderItemDao.putNewOrderItem(orderItem);
    }

    /**
     * 根据用户和商品ID获取指定购物车中的指定商品
     * 注意商品的订单状态oid，是-1才属于购物车中的订单
     * @param uid
     * @param pid
     * @return
     */
    @Override
    public OrderItem getOneOrderItem(Integer uid, Integer pid){
        int oid = -1;
        List<OrderItem> oneOrderItem = orderItemDao.getOneOrderItem(uid, pid, oid);
        OrderItem orderItem = null;
        if (oneOrderItem.size() > 0){
            orderItem = oneOrderItem.get(0);
            Product product = productDao.getProductById(pid);
            orderItem.setProduct(product);
        }
        return orderItem;
    }

    /**
     * 更新购物车中的商品(目前仅支持更新其数量信息)
     * @param orderItem
     */
    @Override
    public void updateOrderItemInCart(OrderItem orderItem) {
        orderItemDao.updateOneOrderItemInCart(orderItem);
    }

    @Override
    public void updateCartNum(OrderItem orderItem){
        orderItemDao.updateCartNum(orderItem);
    }

    @Override
    public void deleteOrderItem(OrderItem orderItem){
        orderItemDao.removeCartOrderItem(orderItem);
    }

    @Override
    public OrderItem getOrderItemByOrderId(Integer id){
        List<OrderItem> oneOrderItemByOrderId = orderItemDao.getOneOrderItemByOrderId(id);
        OrderItem orderItem = oneOrderItemByOrderId.get(0);
        Product product = productDao.getProductById(orderItem.getPid());
        orderItem.setProduct(product);
        return orderItem;
    }

//    @Override
//    public void createOrder(Order order) {
//        orderItemDao.createOrder(order);
//    }

    @Override
    public void fillOrder(List<Order> orders){
        for(Order o: orders){
            float total = 0;
            int oid = o.getId();
            int num = 0;
            List<OrderItem> orderItems = orderItemDao.getAllOrderItemByOrderId(oid);
            for (OrderItem orderItem: orderItems){
                Product product = productDao.getProductById(orderItem.getPid());
                List<ProductImage> firstImage = productImageDao.getFirstImage(product.getId(), productImageDao.type_single);
                if (firstImage.size() > 0)
                    product.setFirstProductImage(firstImage.get(0));
                orderItem.setProduct(product);
                total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
                num += orderItem.getNumber();
            }
            o.setTotalNumber(num);
            o.setTotal(total);
            o.setOrderItems(orderItems);
        }
    }
}
