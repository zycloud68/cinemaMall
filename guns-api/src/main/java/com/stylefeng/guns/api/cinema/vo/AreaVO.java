package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaVO implements Serializable {

    private static final long serialVersionUID = 3580397869300792992L;
    private String areaId;
    private String areaName;
    private boolean isActive;
}
