package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmServiceApi {
    /**
     * 1. 获取首页接口
     * @return
     */
    // 1. 获取banners
    List<BannerVO> getBanners();
    // 3. 获取正在上映的电影
    FilmVO getHotFilms(boolean isLimit,int nums);
    // 4. 获取即将上映的电影 根据受欢迎的程度来决定
    FilmVO getSoonFilms(boolean isLimit,int nums);
    // 5. 获取票房排行值
    List<FilmInfo> getBoxRanking();
    // 6. 获取受欢迎的榜单
    List<FilmInfo> getExpectRanking();
    // 7. 获取Top100
    List<FilmInfo> getTop();
    /**
     * 2. 影片条件列表查询接口
     */
    // 1. 获取分类类型条件 catInfo
    List<CatVO> getCats();
    // 2. 获取片源条件 sourceInfo
    List<SourceVO> getSources();
    // 3. 获取年份条件 yearInfo
    List<YearVO> getYears();

    /**
     * 3、影片查询接口
     */

}
