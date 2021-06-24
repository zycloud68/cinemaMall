package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.MoocAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.MoocCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MoocHallDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaServiceApi.class,executes = 10)
public class DefaultCinemaServiceApiImpl implements CinemaServiceApi {

    @Autowired
     private MoocCinemaTMapper moocCinemaTMapper;
    @Autowired
    private MoocAreaDictTMapper moocAreaDictTMapper;
    @Autowired
    private MoocBrandDictTMapper moocBrandDictTMapper;
    @Autowired
    private MoocHallDictTMapper moocHallDictTMapper;
    @Autowired
    private MoocHallFilmInfoTMapper moocHallFilmInfoTMapper;
    @Autowired
    private MoocFieldTMapper moocFieldTMapper;
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
    /**
     * 2、获取影院列表查询条件
     */
    // 2.1 根据条件获取品牌列表[除了就99以外，其他的数字为isActive]
    @Override
    public List<BrandVO> getBrands(int brandId) {
        boolean flag = false; // 文档中默认是否
        List<BrandVO> brandVo = new ArrayList<>();
        MoocBrandDictT moocBrandDictT = moocBrandDictTMapper.selectById(brandId);
        // 判断branId是否存在
        if (brandId == 99 || moocBrandDictT ==null || moocBrandDictT.getUuid() ==null){
            flag =true;
        }
        // 查询所有列表
        List<MoocBrandDictT> moocBrandDictTS = moocBrandDictT.selectList(null);
        // 判断flag如果是true ,则将99设置为isActive
        for (MoocBrandDictT brand : moocBrandDictTS) {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandName(brand.getShowName());
            brandVO.setBrandId(brand.getUuid()+""); // 因为我们这里将brandId设置为int类型,但是返回值为String类型
            // 判断flag==true,则默认为99,如果为false,则匹配isActive
            if (flag){ // 默认为99
                // 则匹配isActive
                if (brand.getUuid() ==99){
                    brandVO.setActive(true);
                }
            }else{
                if (brand.getUuid() ==brandId){
                    brandVO.setActive(true);
                }
            }
            brandVo.add(brandVO);
        }
        return brandVo;
    }
    // 2.2 获取行政区域列表
    @Override
    public List<AreaVO> getAreas(int areaId) {
        boolean flag =false;
        List<AreaVO> areaVOS = new ArrayList<>();
        // 判断areaId是否等于99
        MoocAreaDictT moocAreaDictT = moocAreaDictTMapper.selectById(areaId);
        if (areaId==99 || moocAreaDictT ==null || moocAreaDictT.getUuid()== null){
            // 将显示全部行政区域
            flag =true;
        }
        // 查询所有行政区域列表
        List<MoocAreaDictT> moocAreaDictTS = moocAreaDictT.selectList(null);
        for (MoocAreaDictT areaDictT : moocAreaDictTS) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaId(areaDictT.getUuid()+"");
            areaVO.setAreaName(areaDictT.getShowName());
            // 如果flag为true,则代表是全部显示行政区
            if (flag){
                if (areaDictT.getUuid() ==99){
                    areaVO.setActive(true);
                }
            }else{
                if (areaDictT.getUuid() ==areaId){
                    areaVO.setActive(true);
                }
            }
            areaVOS.add(areaVO);
        }
        return areaVOS;
    }
    // 2.3 获取影厅类型列表
    @Override
    public List<HallTypeVO> getHallTypes(int hallTypeId) {
        boolean flag  =false;
        List<HallTypeVO> hallTypeVOS  = new ArrayList<>();
        // 判断hallTypeId是否为空
        MoocHallDictT moocHallDictT = moocHallDictTMapper.selectById(hallTypeId);
        if (hallTypeId ==99 || moocHallDictT==null || moocHallDictT.getUuid() ==null){
            flag =true;
        }
        // 查询全部内容
        List<MoocHallDictT> moocHallDictTS = moocHallDictTMapper.selectList(null);
        for (MoocHallDictT hallDictT : moocHallDictTS) {
            HallTypeVO hallTypeVO = new HallTypeVO();
            hallTypeVO.setHallTypeId(hallDictT.getUuid() +"");
            hallTypeVO.setHallTypeName(hallDictT.getShowName());
            // 如果flag ==true
            if (flag){
                if (hallDictT.getUuid() ==99){
                    hallTypeVO.setActive(true);
                }
            }else{
                if (hallDictT.getUuid() ==hallTypeId){
                    hallTypeVO.setActive(true);
                }
            }
            hallTypeVOS.add(hallTypeVO);
        }

        return hallTypeVOS;
    }
    // 2.4 根据影院编号，获取影院信息
    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        MoocCinemaT moocCinemaT = moocCinemaTMapper.selectById(cinemaId);
        if (moocCinemaT ==null){
            return  new CinemaInfoVO();
        }
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setImgUrl(moocCinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(moocCinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaAddress(moocCinemaT.getCinemaAddress());
        cinemaInfoVO.setCinemaName(moocCinemaT.getCinemaName());
        cinemaInfoVO.setCinemaId(moocCinemaT.getUuid()+"");
        return cinemaInfoVO;
    }
    // 获取所有电影的信息和对应的放映场次信息，根据影院编号
    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        List<FilmInfoVO> filmInfoVOList = moocFieldTMapper.getFilmInfos(cinemaId);
        return filmInfoVOList;
    }
   // 根据放映场次ID获取放映信息
    @Override
    public HallInfoVO getFilmFieldInfo(int fieldId) {
        // 这里是单场次
        HallInfoVO hallInfoVO = moocFieldTMapper.getHallInfos(fieldId);
        return hallInfoVO;
    }
    // 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        FilmInfoVO filmInfoVO = moocFieldTMapper.getFilmInfoById(fieldId);
        return filmInfoVO;
    }


}
