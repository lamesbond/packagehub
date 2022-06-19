package com.liubusi.packagehub.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description="DocVO对象")
public class DocVO implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "类型，有doc，category，release_version，menu")
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "发布状态")
    private String pubStatus;

    @ApiModelProperty(value = "排序id")
    private Long position;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "目标id")
    private Long destId;

    @ApiModelProperty(value = "拖拽方法")
    private String dragMethod;

    @ApiModelProperty(value = "数据字典的等级")
    private String level;

    @ApiModelProperty(value = "子集")
    private List<DocVO> children = new ArrayList<>();

    @ApiModelProperty(value = "是否有子集")
    private boolean hasChildren;

}