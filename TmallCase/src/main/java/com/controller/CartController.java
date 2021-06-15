package com.controller;

import com.dao.ProductImageDao;
import com.pojo.OrderItem;
import com.pojo.Product;
import com.pojo.User;
import com.service.Impl.CategoryServiceImpl;
import com.service.Impl.ProductImageServiceImpl;
import com.service.OrderItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    CategoryServiceImpl categoryService;
    @Autowired
    ProductImageServiceImpl productImageService;

    @RequestMapping("CartShow")
    public String showCart(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");

        List<OrderItem> orderItems = orderItemService.getOrderItemInCart(user.getId());
        for (OrderItem o: orderItems){
            int pid = o.getPid();
            Product product = categoryService.getProductInfoById(pid);
            o.setProduct(product);
            productImageService.setFirstProductImage(product.getId(), product);
        }
        model.addAttribute("orderItems", orderItems);

        return "cart";
    }

    @RequestMapping("changeCartNum")
    @ResponseBody
    public String changeCartNum(Integer pid, Integer number, HttpSession session){
        System.out.println("执行....changeCartFunc");
        User user = (User) session.getAttribute("user");
        String msg = "fail";  // 商品数量更新状态
        // TODO:检查用户是否登录(cookie是否已过期)

        OrderItem cartItem = orderItemService.getOneOrderItem(user.getId(), pid);
        System.out.println(cartItem.getProduct().getStock());
        // 检查是否超过库存量
        if (cartItem.getProduct().getStock() >= number){
            cartItem.setNumber(number);  // 更新购物车指定商品数量
            System.out.println(cartItem);
            orderItemService.updateOrderItemInCart(cartItem);
            msg = "success";
        }
        return msg;
    }

    @RequestMapping("deleteOrderItem")
    @ResponseBody
    public String deleteCartOrderItem(@RequestParam("orderItem") Integer deleteOrderItemId, HttpSession session){
        System.out.println("执行....deleteCartOrderItem");
        User user = (User) session.getAttribute("user");
        System.out.println(deleteOrderItemId);
        OrderItem deleteOrderItem = orderItemService.getOrderItemByOrderId(deleteOrderItemId);
        System.out.println(deleteOrderItem);
        orderItemService.deleteOrderItem(deleteOrderItem);

        // TODO:检查用户是否登录(cookie是否已过期)

        return "success";
    }

    @RequestMapping("readyOrderItem")
    public String createOrderItem(String[] orderItemId, HttpServletRequest request){
        HttpSession session = request.getSession();
        List<OrderItem> orderItems = new ArrayList<>();
        float total = 0;
        for (String o: orderItemId){
            int oid = Integer.parseInt(o);
            OrderItem orderItem = orderItemService.getOrderItemByOrderId(oid);
            orderItems.add(orderItem);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
        }
        session.setAttribute("orderItems", orderItems);
        request.setAttribute("total", total);
        return "buy";
    }

}
