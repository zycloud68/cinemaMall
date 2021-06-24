package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.order.OrderServiceApi;
import org.springframework.stereotype.Component;


@Component
@Service(interfaceClass = OrderServiceApi.class,executes = 10)
public class DefaultOrderServiceApiImpl implements OrderServiceApi {


}
