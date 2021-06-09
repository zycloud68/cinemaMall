package com.stylefeng.guns.rest.modular.film.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultFilmServiceImpl implements FilmServiceApi {
    @Autowired
    private MoocBannerTMapper moocBannerTMapper;
    @Autowired
    private MoocFilmTMapper moocFilmTMapper;
    @Autowired
    private MoocCatDictTMapper moocCatDictTMapper;
    @Autowired
    private MoocSourceDictTMapper moocSourceDictTMapper;
    @Autowired
    private MoocYearDictTMapper moocYearDictTMapper;

    /**
     * 获取首页信息
     * @return
     */
    @Override
    public List<BannerVO> getBanners() {
        // 首先我们应该获取banner的信息
        List<BannerVO> result = new ArrayList<>();
        List<MoocBannerT> moocBanners = moocBannerTMapper.selectList(null);
        for (MoocBannerT moocBanner : moocBanners) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(moocBanner.getUuid()+""); // 将int类型变为String类型
            bannerVO.setBannerUrl(moocBanner.getBannerUrl()); // 获取bannerUrl
            bannerVO.setBannerAddress(moocBanner.getBannerAddress());
            result.add(bannerVO);
        }
        return result;
    }
    /**
     * 判断是否是首页需要的内容
     * 1 如果是,则限制首页显示的条数,限制为热映影片
     * 2 不是,则为列表页,同样需要限制内容为热映影片,但是没有条数限制
     * @param isLimit
     * @param nums
     * @return
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId,int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfo = new ArrayList<>();
        // 热映影片的限制条件
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status","1");
        // 判断是否是首页需要的内容
        if (isLimit){
            // 如果是,则限制条数,限制显示的内容为热映影片
            Page<MoocFilmT> page = new Page<>(1,nums); // 显示为第一页
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, wrapper);
            filmInfo = getFilmInfo(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfo(filmInfo);

        }else {
            Page<MoocFilmT> page = null;
            // 根据sortId的不同，来组织不同的Page对象
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1: //按照热门-->就是票房成绩
                    page= new Page<>(nowPage,nums,"film_box_office");
                    break;
                case 2:  // 2-按时间搜索
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3: //3-按评价搜索
                    page = new Page<>(nowPage,nums,"film_score");
                    break;
                default: //按照票房排行
                    page  = new Page<>(nowPage,nums,"film_box_office");
                    break;
            }
            // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
            if (sourceId !=99){
                wrapper.eq("film_source",sortId);
            }
            if (catId !=99){
                // 一个电影可以有多个类型 数据库当中是按照 #2#4#22来排序的
                // 我们首先应当创建字符串
                String catStr = "%#"+catId+"#%";
                wrapper.like("film_cats",catStr);
            }
            if (yearId !=99){
                wrapper.eq("film_date",yearId);
            }
            // 获取当前页数的方法
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, wrapper);
            // 组织 filmsInfo
            filmInfo = getFilmInfo(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            // 获取总数
            Integer totalCounts = moocFilmTMapper.selectCount(wrapper);
            // 根据条件展示的的页面数
            int totalPages = (totalCounts/nums) +1; //默认从第一页开始展示
            filmVO.setFilmInfo(filmInfo);
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPages);
        }
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sourceId,int sortId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfo = new ArrayList<>();
        // 热映影片的限制条件
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status","2");
        // 判断是否是首页需要的内容
        if (isLimit){
            // 如果是,则限制条数,限制显示的内容为热映影片
            Page<MoocFilmT> page = new Page<>(1,nums); // 显示为第一页
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, wrapper);
            filmInfo = getFilmInfo(moocFilms);
            filmVO.setFilmNum(moocFilms.size());
            filmVO.setFilmInfo(filmInfo);

        }else {
            Page<MoocFilmT> page = null;
            // 根据sortId的不同，来组织不同的Page对象
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1:
                    // 为上映就是按照预售成绩来排名
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                case 2:
                    // 按照时间搜索
                    page = new Page<>(nowPage,nums,"film_time");
                    break;
                case 3:
                    // 按照评价搜索
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage,nums,"film_preSaleNum");
                    break;
            }
            // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
            if (sourceId !=99){
                wrapper.eq("film_source",sortId);
            }
            if (yearId !=99){
                wrapper.eq("film_date",yearId);
            }
            if (catId !=99){
                // 电影分为多种类型 比如一部电影拥有 爱情,战争等等
                String catStr = "%#"+catId+"#%";
                wrapper.eq("film_cats",catStr);
            }
            List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, wrapper);
            filmInfo = getFilmInfo(moocFilms);
            filmVO.setFilmNum(moocFilms.size()); //获取其总数
            Integer totalCounts = moocFilmTMapper.selectCount(wrapper);
            int totalPages = (totalCounts/nums)+1; // 默认从第一页开始显示
            filmVO.setFilmInfo(filmInfo);
            filmVO.setNowPage(nowPage);
            filmVO.setTotalPage(totalPages);

        }
        return filmVO;
    }

    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfo = new ArrayList<>();
        // 热映影片的限制条件
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status","3");
        // 判断是否是首页需要的内容
        Page<MoocFilmT> page = null;
        // 根据sortId的不同，来组织不同的Page对象
        // 1-按热门搜索，2-按时间搜索，3-按评价搜索
        switch (sortId){
            case 1:
                // 为上映就是按照预售成绩来排名
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
            case 2:
                // 按照时间搜索
                page = new Page<>(nowPage,nums,"film_time");
                break;
            case 3:
                // 按照评价搜索
                page = new Page<>(nowPage,nums,"film_score");
                break;
            default:
                page = new Page<>(nowPage,nums,"film_box_office");
                break;
        }
        // 如果sourceId,yearId,catId 不为99 ,则表示要按照对应的编号进行查询
        if (sourceId !=99){
            wrapper.eq("film_source",sortId);
        }
        if (yearId !=99){
            wrapper.eq("film_date",yearId);
        }
        if (catId !=99){
            // 电影分为多种类型 比如一部电影拥有 爱情,战争等等
            String catStr = "%#"+catId+"#%";
            wrapper.eq("film_cats",catStr);
        }
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page, wrapper);
        filmInfo = getFilmInfo(moocFilms);
        filmVO.setFilmNum(moocFilms.size()); //获取其总数
        Integer totalCounts = moocFilmTMapper.selectCount(wrapper);
        int totalPages = (totalCounts/nums)+1; // 默认从第一页开始显示
        filmVO.setFilmInfo(filmInfo);
        filmVO.setNowPage(nowPage);
        filmVO.setTotalPage(totalPages);
        return filmVO;
    }

    /**
     * 这里是热播影片和即将上映电影的公共接口
     */
    private List<FilmInfo> getFilmInfo(List<MoocFilmT> moocFilm){
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (MoocFilmT filmInfo : moocFilm) {
            FilmInfo info = new FilmInfo();
            info.setShowTime(DateUtil.getDay(filmInfo.getFilmTime())); //获取电影时间
            info.setScore(filmInfo.getFilmScore()); //获取电影评分
            info.setImgAddress(filmInfo.getImgAddress()); //获取图片地址
            info.setExpectNum(filmInfo.getFilmPresalenum());// 获取电影期待数量
            info.setFilmType(filmInfo.getFilmType()); //获取电影类型
            info.setFilmScore(filmInfo.getFilmScore()) ; // 获取电影评分
            info.setFilmName(filmInfo.getFilmName()); //获取电影名称
            info.setFilmId(filmInfo.getUuid()+""); //获取电影id
            info.setBoxNum(filmInfo.getFilmBoxOffice());
            // 将转换对象放入结果集合中
            filmInfos.add(info);
        }
        return filmInfos;
    }

    /**
     * 票房排行
     * @return
     */
    @Override
    public List<FilmInfo> getBoxRanking() {
        // 默认是已经是上映电影
       EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
       wrapper.eq("film_status","1");
       Page<MoocFilmT> page = new Page<>(1,10,"film_box_office"); // 影片票房
       List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page,wrapper);
        List<FilmInfo> filmInfo = getFilmInfo(moocFilms);
        return filmInfo;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        //即将上映的电影 --预售前十名 -->未上映
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status","2");
        Page<MoocFilmT> page = new Page<>(1,10,"film_preSaleNum"); //预售前十名
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page,wrapper);
        List<FilmInfo> filmInfo = getFilmInfo(moocFilms);
        return filmInfo;
    }

    @Override
    public List<FilmInfo> getTop() {
        // 经典电影,就是打分前十名-->已经上映
        EntityWrapper<MoocFilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status","1");
        Page<MoocFilmT> page = new Page<>(1,10,"film_score"); //评分前100
        List<MoocFilmT> moocFilms = moocFilmTMapper.selectPage(page,wrapper);
        List<FilmInfo> filmInfo = getFilmInfo(moocFilms);
        return filmInfo;
    }

    /**
     * 获取影片条件接口
     * @return
     */
    // 1. 获取分类条件
    @Override
    public List<CatVO> getCats() {
        List<CatVO> cats = new ArrayList<>();
        // 1. 查询实体对象  MoocCatDictT
        List<MoocCatDictT> catDictTList = moocCatDictTMapper.selectList(null);
        //2. 将实体对象转换为业务对象 --> MoocCatDictT-->CatVO
        for (MoocCatDictT moocCatDictT : catDictTList) {
            CatVO catVO = new CatVO();
            catVO.setCatId(moocCatDictT.getUuid()+"");
            catVO.setCatName(moocCatDictT.getShowName());
            cats.add(catVO);
        }
        return cats;
    }
    // 2. 获取片源条件
    @Override
    public List<SourceVO> getSources() {
        List<SourceVO>  source = new ArrayList<>();
        // 1. 查询实体对象 MoocSourceDictT
        List<MoocSourceDictT> moocSourceDict = moocSourceDictTMapper.selectList(null);
        for (MoocSourceDictT sourceDictT : moocSourceDict) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceId(sourceDictT.getUuid()+"");
            sourceVO.setSourceName(sourceDictT.getShowName());
            source.add(sourceVO);
        }
        return source;
    }
    // 3. 获取年份条件
    @Override
    public List<YearVO> getYears() {
        List<YearVO> yearVo = new ArrayList<>();
        // 1. 获取实体层信息 MoocYearDictT
        List<MoocYearDictT> moocYearDict = moocYearDictTMapper.selectList(null);
        for (MoocYearDictT yearDictT : moocYearDict) {
            YearVO vo = new YearVO();
            vo.setYearId(yearDictT.getUuid()+"");
            vo.setYearName(yearDictT.getShowName());
            yearVo.add(vo);
        }
         return yearVo;
    }

    /**
     * 影片查询接口
     * 根据影片id或者名称来获取影片信息
     * @param searchType
     * @param searchParam
     * @return
     */
    // 1. 获取电影的详细信息
    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
          //searchType : ‘0表示按照编号查找，1表示按照名称查找’
         FilmDetailVO filmDetailVO = null;
         // 首先判断输入的类型searchParam是0还是1
        if (searchType==1){ // 按照电影名称查找
            filmDetailVO = moocFilmTMapper.getFilmDetailByName(searchParam);
        }else{ // 按照电影id来查找
            filmDetailVO = moocFilmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;
    }

    // 2. 根据影片id来获取电影信息的图片来源
    @Override
    public ImgVO getImgVo(String filmId) {

        return null;
    }
    // 3. 根据影片id来获取电影导演信息
    @Override
    public ActorVO getDirectorVo(String filmId) {
        return null;
    }
    // 3. 根据影片id来获取所有电影演员信息
    @Override
    public List<ActorVO> getActors(String filmId) {
        return null;
    }



    // 4. 根据影片Id来获取电影详细描述信息
    @Override
    public FilmDescVO getFilmDescVo(String filmId) {
        return null;
    }

}
