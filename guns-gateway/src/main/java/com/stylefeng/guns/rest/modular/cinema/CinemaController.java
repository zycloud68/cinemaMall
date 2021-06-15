package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceApi;
import com.stylefeng.guns.api.cinema.vo.CinemaVO;
import com.stylefeng.guns.api.cinema.vo.CinemaRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {
    @Reference(interfaceClass = CinemaServiceApi.class,
            connections = 10,cache = "lru",check = false)

    private CinemaServiceApi cinemaServiceApi;
    private static final String IMG_PRE = "http://img.meetingshop.cn/";

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

}
