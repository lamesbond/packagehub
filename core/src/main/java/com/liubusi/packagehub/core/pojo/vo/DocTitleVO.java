package com.liubusi.packagehub.core.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="文档标题对象")
public class DocTitleVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "标题")
    private String docTitle;

    @ApiModelProperty(value = "排序")
    private Long position;

    @ApiModelProperty(value = "拖拽方法")
    private String method;

    @TableField(exist = false)
    private DocTitleVO children = new DocTitleVO();

}