package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilmListVO implements Serializable {

    private static final long serialVersionUID = -1045661020103664735L;
    private String uuid; //影院编号
    private String cinemaName; //影院名称
    private String address; // 影院地址
    private double minimumPrice;
}
