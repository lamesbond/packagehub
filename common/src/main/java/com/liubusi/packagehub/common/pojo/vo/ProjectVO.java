package com.liubusi.packagehub.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="ProjectVO对象")
public class ProjectVO implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "userId")
    private Long userId;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "category，release_version，，os_version，file")
    private String type;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "描述")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "发行日志")
    private String releaseNote;

    @ApiModelProperty(value = "发布状态，0没发布，1发布了")
    private String pubStatus;

    @ApiModelProperty(value = "发行范围，0私有，1公开")
    private String pubRange;

    @ApiModelProperty(value = "文件url")
    private String url;

    @ApiModelProperty(value = "目标id")
    private Long destId;

    @ApiModelProperty(value = "拖拽方法")
    private String dragMethod;

    @ApiModelProperty(value = "数据字典的等级")
    private String level;

    @ApiModelProperty(value = "子集")
    private List<ProjectVO> children = new ArrayList<>();

    @ApiModelProperty(value = "是否有子集")
    private boolean hasChildren;

}