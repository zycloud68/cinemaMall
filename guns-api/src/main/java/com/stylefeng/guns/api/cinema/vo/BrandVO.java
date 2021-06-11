package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVO implements Serializable {
    private static final long serialVersionUID = -6084747966907885964L;
    private String brandId;
    private String brandName;
    private boolean isActive;

}
