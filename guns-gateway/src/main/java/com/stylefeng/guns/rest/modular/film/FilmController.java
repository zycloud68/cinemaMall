package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.CatVO;
import com.stylefeng.guns.api.film.vo.FilmVO;
import com.stylefeng.guns.api.film.vo.SourceVO;
import com.stylefeng.guns.api.film.vo.YearVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

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
        filmIndexVO.setHotFilms(filmServiceApi.getHotFilms(true,8,1,1,99,99,99));
        // 获取即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceApi.getSoonFilms(true,8,1,1,99,99,99));

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
        List<YearVO> years = filmServiceApi.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        flag = false;
        YearVO yearVO= null;
        for (YearVO year : years) {
            if (year.getYearId().equals("99")){
                // 默认值为99,则显示全部信息
                yearVO =year;
                continue;
            }
            if (year.getYearId().equals(yearId)){
                flag = true;
                // 只显示当前年份的信息
                year.setActive(true);
            }else{
                // 显示全部信息
                year.setActive(false);
            }
            yearResult.add(year);
        }
        // 如果不存在的话,则默认将全部变为Active状态
        if (!flag){
            yearVO.setActive(true);
            yearResult.add(yearVO);
        }else{
            yearVO.setActive(false);
            yearResult.add(yearVO);
        }
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);
        return ResponseVO.success(filmConditionVO);
    }
    // 获取getFilms
    @RequestMapping(value = "getFilms",method = RequestMethod.GET)
    public ResponseVO getFilms(FilmRequestVO filmRequestVO){
        String img_pre = "http://img.meetingshop.cn/";
        FilmVO filmVO = null;
        // 根据showType判断影片的查询类型 查询类型，1-正在热映，2-即将上映，3-经典电影
        switch (filmRequestVO.getShowType()){
            case 1 :
                filmVO = filmServiceApi.getHotFilms(
                        false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
            case 2 :
                filmVO = filmServiceApi.getSoonFilms(
                        false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
            case 3 :
                filmVO = filmServiceApi.getClassicFilms(
                        filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),
                        filmRequestVO.getYearId(), filmRequestVO.getCatId());
                break;
            default:
                filmVO = filmServiceApi.getHotFilms(
                        false,filmRequestVO.getPageSize(),filmRequestVO.getNowPage(),
                        filmRequestVO.getSortId(),filmRequestVO.getSourceId(),filmRequestVO.getYearId(),
                        filmRequestVO.getCatId());
                break;
        }
        return ResponseVO.success(filmVO.getNowPage(),filmVO.getTotalPage(),img_pre,filmVO.getFilmInfo());

    }
    /**
     * 影片查询接口
     */
    @RequestMapping(value = "films/{searchParam}",method = RequestMethod.GET)
    public ResponseVO films(@PathVariable("searchParam") String searchParam,
                            int searchType){
        // 请求字段 searchType 0表示按照编号查找,1按照名称查找
        // 以上分析,有两个方法,根据searchType判断查询类型,不同的查询类型,传入的条件略有不同
        // 要查询影片的详细信息
        // 1.根据编号查找
        // 2.根据名称查找

        return null;
    }
}
