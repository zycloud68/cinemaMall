package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影片条件列表查询接口
 */
@Data
public class SourceVO implements Serializable {
    private String sourceId;
    private String sourceName;
    private boolean isActive;
}
