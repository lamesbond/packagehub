package com.liubusi.packagehub.core.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="文档信息对象")
public class DocInfoVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "目标id")
    private Long destId;

    @ApiModelProperty(value = "标题")
    private String docTitle;

    @ApiModelProperty(value = "排序")
    private Long position;

    @ApiModelProperty(value = "拖拽方法")
    private String method;

    @ApiModelProperty(value = "数据字典的等级")
    private String level;

    @ApiModelProperty(value = "是否是文档，0不是，1是")
    private String isDoc;

}