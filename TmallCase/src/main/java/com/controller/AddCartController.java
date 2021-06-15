package com.controller;

import com.pojo.OrderItem;
import com.pojo.Product;
import com.pojo.User;
import com.service.CategoryService;
import com.service.OrderItemService;
import com.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AddCartController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProductImageService productImageService;

    @RequestMapping("AddCartController")
    @ResponseBody
    public String addCart(Integer pid, Integer num, HttpSession session){
        User user = (User) session.getAttribute("user");
        Product product = categoryService.getProductInfoById(pid);
        // 查询购物车是否有该商品
        OrderItem oneOrderItem = orderItemService.getOneOrderItem(user.getId(), product.getId());
        // 用于判断是否原本存在于购物车中
        Boolean isInDB = oneOrderItem != null;

        if (isInDB){
            // 购物车已有该商品，在此基础上进行更新叠加
            num += oneOrderItem.getNumber();
            oneOrderItem.setNumber(num);
            System.out.println(oneOrderItem);
            orderItemService.updateOrderItemInCart(oneOrderItem);
        }else {
            // 不存在购物车中，添加新商品订单
            System.out.println("不存在购物车中，添加新商品订单");
            oneOrderItem = new OrderItem();
            oneOrderItem.setProduct(product);
            oneOrderItem.setPid(pid);
            oneOrderItem.setOid(-1);
            oneOrderItem.setNumber(num);
            oneOrderItem.setUid(user.getId());
            productImageService.setFirstProductImage(product.getId(), product);
            orderItemService.addOrderItem(oneOrderItem);
        }
        System.out.println("加入购物车");
        return "success";
    }

}
