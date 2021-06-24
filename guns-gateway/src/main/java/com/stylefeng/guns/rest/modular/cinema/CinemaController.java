package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldInfoResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldsResponseVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {
    @Reference(interfaceClass = CinemaServiceApi.class,
            connections = 10,cache = "lru",check = false)
    private CinemaServiceApi cinemaServiceApi;
    private static final String IMG_PRE = "http://img.meetingshop.cn/";


    /**
     * 1、查询影院列表-根据条件查询所有影院
     * @param cinemaRequestVO
     * @return
     */
    @RequestMapping(value = "getCinemas",method = RequestMethod.GET)
    public ResponseVO getCinemas(CinemaRequestVO cinemaRequestVO){

        Page<CinemaVO> cinemas = null;
        try {
            cinemas = cinemaServiceApi.getCinemas(cinemaRequestVO);
            // 判断是否为空
            if (cinemas.getRecords()==null || cinemas.getRecords().size()==0){
                return ResponseVO.success("影院为空");
            }else{
                return ResponseVO.success(cinemas.getCurrent(),(int)cinemas.getPages(),"",cinemas.getRecords());
            }
        } catch (Exception e) {
            log.error("获取影院列表失败",e);
            return ResponseVO.serviceFail("查询影院列表失败");
        }
    }
    /**
     * 2、获取影院列表查询条件
     */
    @RequestMapping(value = "getCondition",method = RequestMethod.GET)
    public ResponseVO getCondition(CinemaRequestVO cinemaRequestVO){
        try{
            // 获取三个封装的集合
            List<BrandVO> brands = cinemaServiceApi.getBrands(cinemaRequestVO.getBrandId());
            List<AreaVO> areas = cinemaServiceApi.getAreas(cinemaRequestVO.getAreaId());
            List<HallTypeVO> hallTypes = cinemaServiceApi.getHallTypes(cinemaRequestVO.getHallType());
            CinemaConditionResponseVO cinemaConditionResponseVO = new CinemaConditionResponseVO();
            cinemaConditionResponseVO.setBrandList(brands);
            cinemaConditionResponseVO.setAreaList(areas);
            cinemaConditionResponseVO.setHallTypeList(hallTypes);
            return ResponseVO.success(cinemaConditionResponseVO);
        }catch(Exception e){
            log.error("获取影院条件失败",e);
            return ResponseVO.serviceFail("获取影院查询条件失败");
        }
    }

    /**
     * 获取播放场次接口
     * @param cinemaId
     * @return
     */
    @RequestMapping(value = "getFields") // get/post方式都ok
    public ResponseVO getFields(Integer cinemaId){
        try {
            CinemaInfoVO  cinemaInfoById = cinemaServiceApi.getCinemaInfoById(cinemaId);
            List<FilmInfoVO>  filmInfoByCinemaId = cinemaServiceApi.getFilmInfoByCinemaId(cinemaId);
            CinemaFieldsResponseVO cinemaFieldsResponseVO = new CinemaFieldsResponseVO();
            cinemaFieldsResponseVO.setCinemaInfoVO(cinemaInfoById);
            cinemaFieldsResponseVO.setFilmList(filmInfoByCinemaId);
            return ResponseVO.success(IMG_PRE,cinemaFieldsResponseVO);
        } catch (Exception e) {
            log.error("获取播放场次失败",e);
            return ResponseVO.serviceFail("获取播放场次失败");
        }

    }

    /**
     * 获取场次详细信息接口
     * @param cinemaId
     * @param fieldId
     * @return
     */
    @RequestMapping(value = "getFieldInfo",method = RequestMethod.POST)
    public ResponseVO getFieldInfo(Integer cinemaId, Integer fieldId){
        try {
            CinemaInfoVO cinemaInfoById = cinemaServiceApi.getCinemaInfoById(cinemaId);
            HallInfoVO filmFieldInfo = cinemaServiceApi.getFilmFieldInfo(fieldId);
            FilmInfoVO filmInfoByFieldId = cinemaServiceApi.getFilmInfoByFieldId(fieldId);
            // TODO
            // 这是假数据,后续会接订单接口
            filmFieldInfo.setSoldSeats("1,2,3");
            CinemaFieldInfoResponseVO cinemaFieldInfoResponseVO = new CinemaFieldInfoResponseVO();
            cinemaFieldInfoResponseVO.setCinemaInfo(cinemaInfoById);
            cinemaFieldInfoResponseVO.setFilmInfo(filmInfoByFieldId);
            cinemaFieldInfoResponseVO.setHallInfo(filmFieldInfo);
            return ResponseVO.success(IMG_PRE,cinemaFieldInfoResponseVO);
        } catch (Exception e) {
            log.error("获取选座信息失败",e);
            return ResponseVO.serviceFail("获取选座信息失败");
        }
    }

}
