package com.liubusi.packagehub.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="UserVO对象")
public class UserInfoVO implements Serializable{

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户角色")
    private String roles;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "token")
    private String token;
}