package com.stylefeng.guns.rest.modular.film.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVO;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBannerTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocBannerT;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmT;
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
     * 判断是否是首页需要的内容
     * 1 如果是,则限制首页显示的条数,限制为热映影片
     * 2 不是,则为列表页,同样需要限制内容为热映影片,但是没有条数限制
     * @param isLimit
     * @param nums
     * @return
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums) {
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
            /**
             * TODO
             */

        }
        return filmVO;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums) {
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
            /**
             * TODO
             */

        }
        return filmVO;
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
}
