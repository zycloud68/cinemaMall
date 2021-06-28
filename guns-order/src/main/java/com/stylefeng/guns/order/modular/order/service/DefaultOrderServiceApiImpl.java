package com.stylefeng.guns.order.modular.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.api.order.OrderServiceApi;
import com.stylefeng.guns.api.order.vo.OrderVO;
import com.stylefeng.guns.core.util.UuidUtil;
import com.stylefeng.guns.order.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.order.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.order.common.utils.FTPUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Component
@Service(interfaceClass = OrderServiceApi.class)
public class DefaultOrderServiceApiImpl implements OrderServiceApi {
    @Autowired
    private MoocOrderTMapper moocOrderTMapper;
    @Autowired
    private CinemaServiceApi cinemaServiceApi;
    @Autowired
    private FTPUtils ftpUtils;

    // 验证售出的票是否为真
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
     /*
        String seatsPath = moocOrderTMapper.getSeatsByFieldId(fieldId);
        // 读取位置图.判断seats是否为真
        String fileStrAddress = ftpUtils.getFileStrByAddress(seatsPath);
        // 将fileStrAddress转化为json对象
        JSONObject jsonObject = JSONObject.parseObject(fileStrAddress);
        // seats =1,2,3 ids = 1,2,3,4,5
        String ids = jsonObject.get("ids").toString();
        // 判断ids里面是否有值
        String[] seatArrs = seats.split(",");
        String[] idArrs = ids.split(",");
        // boolean flag = true;
        int isTrue = 0;
        for (String idArr : idArrs) {
            for (String seatArr : seatArrs) {
                if (seatArr.equalsIgnoreCase(idArr)){
                    isTrue ++;
                }
            }
        }
        // 如果匹配上的数量和已经售的座位一致,则代表全部匹配上了
        if (seatArrs.length == isTrue){
            return true;
        }else{
            return false;
        }

      */
        return false;
    }
    // 验证已经售出的座位里面,有没有这些座位
    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        // 根据fieldId找到对应的位置图
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("field_id",fieldId);
        List<MoocOrderT> list = moocOrderTMapper.selectList(wrapper);
        String[] seatArrs = seats.split(",");
        // 有任何一个编号匹配上,则直接返回失败
        for (MoocOrderT moocOrderT : list) {
            String[] ids = moocOrderT.getSeatsIds().split(",");
            for (String id : ids) {
                for (String seat: seatArrs){
                    if (id.equalsIgnoreCase(seat)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    // 根据用户信息创建订单信息
    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        // 订单信息不能使用uuid
        // 编号
        String uuid = UuidUtil.getUuid();
        FilmInfoVO filmInfo= cinemaServiceApi.getFilmInfoByFieldId(fieldId);
        Integer filmId = Integer.parseInt( filmInfo.getFilmId());

        // 获取影院信息
        OrderQueryVO orderQueryVO = cinemaServiceApi.getOrderInfoNeed(fieldId);
        Integer cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        double filmPrice = Double.parseDouble(orderQueryVO.getFilmPrice());
        // 根据售卖的票数来计算订单总金额
        int sold = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(sold, filmPrice);
        // 返回包装数据
        MoocOrderT moocOrderT = new MoocOrderT();
        moocOrderT.setUuid(uuid);
        moocOrderT.setSeatsName(seatsName);
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setOrderUser(userId);
        moocOrderT.setOrderPrice(totalPrice);
        moocOrderT.setCinemaId(cinemaId);
        moocOrderT.setFilmPrice(filmPrice);
        moocOrderT.setFilmId(filmId);
        moocOrderT.setFieldId(fieldId);
        Integer insert = moocOrderTMapper.insert(moocOrderT);
        if (insert>0){
            //订单插入成功,返回查询结果
        }else {
            log.error("订单插入失败");
        }
        return null;
    }
    private static double getTotalPrice(int sold,double filmPrice){
        BigDecimal bigDecimalSold = new BigDecimal(sold);
        BigDecimal bigDecimalFilmPrice = new BigDecimal(filmPrice);
        //计算
        BigDecimal multiply = bigDecimalSold.multiply(bigDecimalFilmPrice);
        // 针对小数点后面去除两位,或者说只保留两位信息
        BigDecimal bigDecimal = multiply.setScale(2, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
 // 测试 价格
    /**
    public static void main(String[] args) {
        double totalPrice = getTotalPrice(2, 13.245443);
        System.out.println(totalPrice);
    }
     */

    // 使用当前登录人获取已经购买的信息
    @Override
    public List<OrderVO> getOrderByUserId(Integer userId) {
        return null;
    }
    // 根据FieldId获取所有已经销售的座位编号
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        return null;
    }
}
