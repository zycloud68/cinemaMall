package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallTypeVO implements Serializable {

    private static final long serialVersionUID = -888333161128962909L;
    private String hallTypeId;
    private String hallTypeName;
    private boolean isActive;

}
