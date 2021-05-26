package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/film/")
public class FilmController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = FilmServiceApi.class,check = false)
    private FilmServiceApi filmServiceApi;

    @RequestMapping(value = "getIndex",method = RequestMethod.GET)
    public ResponseVO<FilmIndexVO> getIndex(){
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        // 获取banner信息
        filmIndexVO.setBanner(filmServiceApi.getBanners());
        // 获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceApi.getHotFilms(true,8));
        // 获取即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceApi.getSoonFilms(true,8));
        // 获取票房排行
        filmIndexVO.setBoxRanking(filmServiceApi.getBoxRanking());
        // 获取最受期待的电影
        filmIndexVO.setExpectRanking(filmServiceApi.getExpectRanking());
        // 获取经典电影前100名
        filmIndexVO.setTop100(filmServiceApi.getTop());
        return ResponseVO.success(IMG_PRE,filmIndexVO);
    }

    /**
     * 影片条件查询接口
     */
    @RequestMapping(value = "getConditionList",method = RequestMethod.GET)
    public ResponseVO getConditionList(@RequestParam(name = "catId",required = false,defaultValue ="99")String catId,
                                                        @RequestParam(name = "sourceId",required = false,defaultValue ="99")String sourceId,
                                                        @RequestParam(name = "yearId",required = false,defaultValue ="99")String yearId){
        FilmConditionVO filmConditionVO = new FilmConditionVO();
        // 类型集合 catInfo

        // 片源集合 sourceInfo

        // 年代集合 yearInfo

    return ResponseVO.success(filmConditionVO);
    }
}
