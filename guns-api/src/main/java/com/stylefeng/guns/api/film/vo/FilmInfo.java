package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

// 电影信息实体
@Data
public class FilmInfo implements Serializable {
    private String filmId;
    private int filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
    private int expectNum;
    private String showTime;
    private int boxNum; //Ranking 排名
    private String score; //top100
}
