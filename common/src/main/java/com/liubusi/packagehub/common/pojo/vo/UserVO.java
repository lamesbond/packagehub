package com.liubusi.packagehub.common.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="UserVO对象")
public class UserVO implements Serializable{

    public static final Integer STATUS_NORMAL = 1;
    public static final Integer STATUS_LOCKED = 0;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "用户角色")
    private String roles;

    @ApiModelProperty(value = "用户状态，0被锁定，1正常")
    private Integer status;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "JWT访问令牌")
    private String token;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLogin;

}