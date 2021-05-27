package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        // 判断是否存在catId,如果存在,则将对应的的实体变为active状态
        // 如果不存在则将全部变为active状态
        List<CatVO> cats = filmServiceApi.getCats();
        // 将返回的cats 放回到数组list中
        List<CatVO> catResult  = new ArrayList<>();
        boolean flag = false;
        CatVO catvo = null;
        for (CatVO cat : cats) {
            // 循环遍历,每一个cat的catId的值是否为默认的99,全部展示
            if (cat.getCatId().equals("99")){
                // 将cat的值赋予给catvo
                catvo =cat;
                continue;
            }
            if (cat.getCatId().equals(catId)){ // 如果cat的id与catId的值一样,则代表特定的值,就是展示一部分内容
                flag = true;
                cat.setActive(true);
            }else{
                cat.setActive(false);
            }
            catResult.add(cat);
        }
        // 如果不存在,则默认全部显示为active状态
        if (!flag){
            catvo.setActive(true);
            catResult.add(catvo);
        }else{
            catvo.setActive(false);
            catResult.add(catvo);
        }
        // 片源集合 sourceInfo
        List<SourceVO> sources = filmServiceApi.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        flag  = false;
        SourceVO sourceVO = null;
        for (SourceVO source : sources) {
            if (source.getSourceId().equals("99")){ // 默认值99
                sourceVO = source;
                continue;
            }
            if (source.getSourceId().equals(sourceId)){ // 请求的参数
                flag =true;
                source.setActive(true);
            }else{
                source.setActive(false);
            }
            sourceResult.add(source);
        }
        // 如果不存在,则将默认全变为Active状态
        if (!flag){
            sourceVO.setActive(true);
            sourceResult.add(sourceVO);
        }
        // 年代集合 yearInfo

    return ResponseVO.success(filmConditionVO);
    }
}
