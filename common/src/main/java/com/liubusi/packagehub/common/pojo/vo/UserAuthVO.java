package com.liubusi.packagehub.common.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(description="UserAuthVO对象")
public class UserAuthVO implements Serializable{

    @ApiModelProperty(value = "id")
    @TableId(value = "id")
    private Long userId;

    @ApiModelProperty(value = "项目ID列表")
    private List<Long> projectList;

    @ApiModelProperty(value = "文档ID列表")
    private List<Long> docList;

}