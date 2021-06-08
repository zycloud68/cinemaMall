package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmDetailVO implements Serializable {
    private String filmName; // 影片名称
    private String filmEnName; //影片英文名称
    private String imgAddress; //图片地址
    private String score;
    private String scoreNum;
    private String totalBox; //票房
    private String info01;
    private String info02;
    private String info03;
    private InfoRequestVO info04;


}
