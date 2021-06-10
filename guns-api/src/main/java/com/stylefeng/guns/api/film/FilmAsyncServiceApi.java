package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmAsyncServiceApi {

    /**
     * 3、影片查询接口
     */
    // 2. 根据影片id来获取电影信息的图片来源
    ImgVO getImgVo(String filmId);
    // 3. 根据影片id来获取电影演员及导演信息
    ActorVO getDirectorVo(String filmId);
    List<ActorVO> getActors(String filmId);
    // 4. 根据影片Id来获取电影详细描述信息
    FilmDescVO getFilmDescVo(String filmId);

}
