package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CinemaRequestVO implements Serializable {
    private Integer brandId =99; // 影院编号
    private Integer hallType =99; // 影厅类型
    private Integer districtId =99; // 行政区编号
    private Integer pageSize =12; // 每条页数
    private Integer nowPage =1; // 当前页数
    private Integer areaId =99; // 行政区编号
    private Integer cinemaId ; // 影院编号
    private Integer fieldId;  // 场次编号


}
