package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class FilmRequestVO implements Serializable {
    private Integer showType=1; // 查询类型,1-正在热映，2-即将上映，3-经典影片
    private Integer sortId =1; // 排序方式，1-按热门搜索，2-按时间搜索，3-按评价搜索
    private Integer catId=99; // 类型编号
    private Integer sourceId=99; // 区域编号
    private Integer yearId=99; // 年代编号
    private Integer nowPage=1; // 影片列表当前页，翻页使用
    private Integer pageSize=18; //每页显示条数
}
