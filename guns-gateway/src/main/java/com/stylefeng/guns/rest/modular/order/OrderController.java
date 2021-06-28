package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Reference(interfaceClass = OrderServiceApi.class,check = false)
    private OrderServiceApi orderServiceApi;

    /**
     * 1、用户下单购票接口
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @return
     */
    @RequestMapping(value = "buyTickets",method = RequestMethod.POST)
    public ResponseVO buyTickets(Integer fieldId,String soldSeats,String seatsName){
        // 验证售出的票是否为真
        // 已经售出的位置里面,有没有这些座位
        // 创建订单信息
        return null;
    }

    /**
     * 2、获取用户订单信息接口
     * @param nowPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getOrderInfo",method = RequestMethod.POST)
    public ResponseVO getOrderInfo(@RequestParam(name="nowPage",required = false,defaultValue = "1") Integer nowPage,
                                   @RequestParam(name="pageSize",required = false,defaultValue = "5")Integer pageSize){
        return null;
    }
}
