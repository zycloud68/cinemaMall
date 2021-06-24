package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.rest.common.persistence.model.MoocFieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author zycloud
 * @since 2021-06-11
 */
public interface MoocFieldTMapper extends BaseMapper<MoocFieldT> {
    // 获取所有电影的信息和对应的放映场次信息，根据影院编号
    List<FilmInfoVO> getFilmInfos(@Param("cinemaId") int cinemaId);

    //  根据放映场次ID获取放映信息
    HallInfoVO getHallInfos(@Param("fieldId") int fieldId);
    // 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
    FilmInfoVO getFilmInfoById(@Param("fieldId") int fieldId);
}
