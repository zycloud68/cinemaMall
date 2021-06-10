package com.stylefeng.guns.rest.modular.film.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.film.FilmAsyncServiceApi;
import com.stylefeng.guns.api.film.FilmServiceApi;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = FilmServiceApi.class)
public class DefaultAsyncFilmServiceImpl implements FilmAsyncServiceApi {

    @Autowired
    private MoocFilmInfoTMapper moocFilmInfoTMapper;
    @Autowired
    private MoocActorTMapper moocActorTMapper;


    // 2. 根据影片id来获取电影信息的图片来源
    @Override
    public ImgVO getImgVo(String filmId) {
        // 获取MoocFilmInfoT里面的数据
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        // 根据filmId来获取信息
        moocFilmInfoT.setFilmId(filmId);
        // 调用持久层的信息
        MoocFilmInfoT moocFilmInfoT1 = moocFilmInfoTMapper.selectOne(moocFilmInfoT);
        String  filmStr = moocFilmInfoT1.getFilmImgs();
        // 图片地址是按照五个逗号为分隔符连接的
        String[] filmImgs= filmStr.split(",");
        // 获取详细图片地址
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filmImgs[0]);
        imgVO.setImg01(filmImgs[1]);
        imgVO.setImg02(filmImgs[2]);
        imgVO.setImg03(filmImgs[3]);
        imgVO.setImg04(filmImgs[4]);
        return imgVO;
    }

    // 3.1 根据影片id来获取电影导演信息
    @Override
    public ActorVO getDirectorVo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        Integer directorId = moocFilmInfoT.getDirectorId();
        // 调用持久层的信息
        MoocActorT moocActorT = moocActorTMapper.selectById(directorId);
        // 根据moocDirectInfo信息来查找director{imgAddress,directorName}
        ActorVO actorVO = new ActorVO();
        actorVO.setDirectorName(moocActorT.getActorName());
        actorVO.setImgAddress(moocActorT.getActorImg());
        return actorVO;
    }

    // 3.2 根据影片id来获取所有电影演员信息
    @Override
    public List<ActorVO> getActors(String filmId) {
        List< ActorVO> actors = moocActorTMapper.getActors(filmId);
        return actors;
    }

    // 4. 根据影片Id来获取电影详细描述信息
    @Override
    public FilmDescVO getFilmDescVo(String filmId) {
        MoocFilmInfoT moocFilmInfoT = new MoocFilmInfoT();
        moocFilmInfoT.setFilmId(filmId);
        // 调用持久层信息
        MoocFilmInfoT filmDescInfo = moocFilmInfoTMapper.selectOne(moocFilmInfoT);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(filmDescInfo.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

}
