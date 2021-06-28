package com.stylefeng.guns.api.order;

import com.stylefeng.guns.api.order.vo.OrderVO;

import java.util.List;

/**
 * 订单模块API
 */
public interface OrderServiceApi {
    // 验证售出的票是否为真
    boolean isTrueSeats(String fieldId,String seats);
    // 验证已经售出的座位里面,有没有这些座位
    boolean isNotSoldSeats(String fieldId,String seats);
    // 根据用户信息创建订单信息
    OrderVO saveOrderInfo(Integer fieldId,String soldSeats,String seatsName,Integer userId);
    // 使用当前登录人获取已经购买的信息
    List<OrderVO > getOrderByUserId(Integer userId);
    // 根据FieldId获取所有已经销售的座位编号
    String getSoldSeatsByFieldId(Integer fieldId);
}
