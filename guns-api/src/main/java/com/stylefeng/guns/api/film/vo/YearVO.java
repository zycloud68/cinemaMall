package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影片条件列表查询接口
 */
@Data
public class YearVO implements Serializable {
    private String yearId;
    private String yearName;
    private boolean isActive;


}
