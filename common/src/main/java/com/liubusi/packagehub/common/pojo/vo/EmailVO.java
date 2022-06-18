package com.liubusi.packagehub.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="EmailVO对象")
public class EmailVO {

    @ApiModelProperty(value = "邮箱地址")
    private String address;

    @ApiModelProperty(value = "邮件标题")
    private String title;

    @ApiModelProperty(value = "邮件正文")
    private String content;

}
