package com.controller;

import com.pojo.Order;
import com.pojo.OrderItem;
import com.pojo.User;
import com.service.OrderItemService;
import com.service.OrderService;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.commons.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class OrderItemController {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;


    // 前端参数：address、post、receiver、mobile、userMessage    URL参数：orderItemId-List类型
    @RequestMapping("crateOrderItem")
    public String createOrderItem(String address, String post, String receiver, String mobile, String userMessage, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> orderItems = (List<OrderItem>) request.getSession().getAttribute("orderItems");
        Order order = new Order();
        order.setAddress(address);
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setCreateDate(new Date());
        order.setOrderCode(orderCode);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setOrderItems(orderItems);
        order.setUid(user.getId());

        // 将order订单信息添加至后台数据库中,状态为待支付
        order.setStatus("waitPay");
        orderService.createOrder(order);

        float total = 0;
        for (OrderItem o: orderItems){
            // 同时，购物车中对应的商品状态需改变，oid不再为-1(位于购物车状态)，而是对应订单order的ID。
            // 每个OrderItem都有oid对应指定Order
            o.setOid(order.getId());
            orderItemService.updateOrderItemInCart(o);
            // 获取订单总金额
            total += o.getProduct().getPromotePrice() * o.getNumber();
        }

        // 将数据重定向至指定请求函数，进行处理
        return "redirect:/aliPayControl?total=" + total + "&oid=" + order.getId();
    }

    @RequestMapping("aliPayControl")
    public String aliPay(){
        return "alipay";
    }

    @RequestMapping("boughtOrderControl")
    public String boughtOrde(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (null == user){
            return "login";
        }
        List<Order> orders = orderItemService.listAllOrder(user);
        orderItemService.fillOrder(orders);
        session.setAttribute("os", orders);

        return "bought";
    }

    @RequestMapping("deleteOrderController")
    @ResponseBody
    public String deleteOrder(@RequestParam("oid") Integer deleteOrderId){
        orderService.deleteOrder(deleteOrderId);
        return "success";
    }

    @RequestMapping("PayedController")
    public String payForOrder(HttpServletRequest request){
        HttpSession session = request.getSession();
        Integer oid = Integer.parseInt(request.getParameter("oid"));
        Float total = Float.parseFloat(request.getParameter("total"));

        Order order = orderService.getOrder(oid);
        // 更新订单状态为：已付款(即变为待发货waitDelivery)
        order.setStatus("waitDelivery");
        order.setPayDate(new Date());
        orderService.updateOrderStatus(order);
        session.setAttribute("o", order);
        session.setAttribute("total", total);
        return "payed";
    }
}
