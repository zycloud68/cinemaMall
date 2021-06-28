package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderQueryVO implements Serializable {
    private static final long serialVersionUID = -3190212108934305094L;
    private String cinemaId;
    private String filmPrice;
}
