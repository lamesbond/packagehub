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

    @ApiModelProperty(value = "目标id")
    private Long destId;

    @ApiModelProperty(value = "标题")
    private String docTitle;

    @ApiModelProperty(value = "排序")
    private Long position;

    @ApiModelProperty(value = "拖拽方法")
    private String method;

}