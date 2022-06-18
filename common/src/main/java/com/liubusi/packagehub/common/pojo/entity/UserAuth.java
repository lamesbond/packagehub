package com.liubusi.packagehub.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="UserAuth对象")
public class UserAuth implements Serializable{

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "项目ID列表")
    private List<Long> projectList;

    @ApiModelProperty(value = "文档ID列表")
    private List<Long> docList;
}