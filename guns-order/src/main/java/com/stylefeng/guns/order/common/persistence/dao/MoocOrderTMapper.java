package com.stylefeng.guns.order.common.persistence.dao;

import com.stylefeng.guns.order.common.persistence.model.MoocOrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author zycloud
 * @since 2021-06-28
 */
public interface MoocOrderTMapper extends BaseMapper<MoocOrderT> {
    String getSeatsByFieldId(@Param("fieldId") String fieldId);

}
