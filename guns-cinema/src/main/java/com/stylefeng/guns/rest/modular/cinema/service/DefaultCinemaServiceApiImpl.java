package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.CinemaRequestVO;
import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import com.stylefeng.guns.rest.common.persistence.dao.MoocCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocCinemaT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaServiceApi.class,executes = 10)
public class DefaultCinemaServiceApiImpl implements CinemaServiceApi {

    @Autowired
     private MoocCinemaTMapper moocCinemaTMapper;
    // 1、根据CinemaQueryVO，查询影院列表
    @Override
    public Page<CinemaVO> getCinemas(CinemaRequestVO cinemaRequestVO) {
        // 是将对象返回到list中去
        List<CinemaVO> cinemas = new ArrayList<>();
        // 分页和当前页面查询
        Page<MoocCinemaT> page = new Page<>(cinemaRequestVO.getNowPage(),cinemaRequestVO.getPageSize());
        // 判断是否传入查询条件 brandId,hallType,districtId==99
        // 文档中是否,默认为99,全部条件
        EntityWrapper<MoocCinemaT> wrapper = new EntityWrapper<>();
        if (cinemaRequestVO.getBrandId() !=99){
            wrapper.eq("brand_id",cinemaRequestVO.getBrandId());
        }
        if (cinemaRequestVO.getHallType() !=99){
            wrapper.like("hall_ids","%#+"+cinemaRequestVO.getHallType()+"+#%");
        }
        if (cinemaRequestVO.getDistrictId() !=99){
            wrapper.eq("area_id",cinemaRequestVO.getDistrictId());
        }
        // 将数据实体转换为业务实体层
        //List<MoocCinemaT> moocCinemaTS = moocCinemaTMapper.selectPage(page,wrapper);
        List<MoocCinemaT> moocCinemaTS = moocCinemaTMapper.selectPage(page, wrapper);
        for (MoocCinemaT moocCinemaT : moocCinemaTS) {
            CinemaVO cinemaVO = new CinemaVO();
            cinemaVO.setUuid(moocCinemaT.getUuid()+ ""); // 这里的是String类型
            cinemaVO.setCinemaName(moocCinemaT.getCinemaName());
            cinemaVO.setMinimumPrice(moocCinemaT.getMinimumPrice()+"");
            cinemaVO.setAddress(moocCinemaT.getCinemaAddress());
            cinemas.add(cinemaVO);
        }
        // 根据条件,判断影院类表详情
        Integer count = moocCinemaTMapper.selectCount(wrapper);
        // 组织返回对象
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemas);
        result.setSize(cinemaRequestVO.getPageSize());
        result.setTotal(count);
        return result;
    }
}
