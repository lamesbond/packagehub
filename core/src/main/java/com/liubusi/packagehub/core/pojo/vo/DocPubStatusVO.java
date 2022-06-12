package com.liubusi.packagehub.core.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@ApiModel(description="DocpubstatusVO对象")
public class DocPubStatusVO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "发布状态")
    private String pubStatus;


}