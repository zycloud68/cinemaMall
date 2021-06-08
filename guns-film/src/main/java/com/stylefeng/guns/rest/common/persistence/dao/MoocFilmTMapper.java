package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.FilmDetailVO;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author zycloud
 * @since 2021-05-26
 */
public interface MoocFilmTMapper extends BaseMapper<MoocFilmT> {
    // 根据影片名称 FilmName来查询详细信息
    FilmDetailVO getFilmDetailByName(@Param("filmName") String filmName);
    // 根据影片Id来查询影片详细信息
    FilmDetailVO getFilmDetailById(@Param("uuid") String uuid);

}
