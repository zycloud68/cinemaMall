package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 类型
 * 影片条件列表查询接口
 */
@Data
public class CatVO implements Serializable {
    private String catId;
    private String catName;
    private boolean isActive;
}
