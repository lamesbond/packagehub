package com.liubusi.packagehub.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
@Data
//@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Doc对象", description="文档数据字典")
public class Doc implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "是不是文档，是就不展示下一级了，0不是，1是")
    private String isDoc;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "排序id")
    private Long position;

    @ApiModelProperty(value = "内容")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "发布状态")
    private String pubStatus;

}
