package com.liubusi.packagehub.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value="Project对象", description="项目包字典")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long id;

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
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "发行日志")
    private String release_note;

    @ApiModelProperty(value = "发布状态，0没发布，1发布了")
    private String pubStatus;

    @ApiModelProperty(value = "文件url")
    private String url;

}
