package com.liubusi.packagehub.core.controller.api;

import com.liubusi.packagehub.common.pojo.entity.Doc;
import com.liubusi.packagehub.common.pojo.vo.DocVO;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.core.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "项目文档获取")
@RestController
@RequestMapping("/api/core/doc")
@Slf4j
public class ApiDocController {
    @Resource
    private DocService docService;

    @ApiOperation("获取id下的下一级子节点，不递归")
    @GetMapping("/listNextChildNode/{id}/{userId}")
    public Result listNextChildNode(@ApiParam(value = "节点id", required = true)
                                    @PathVariable Long id,
                                    @ApiParam(value = "用户id", required = true)
                                    @PathVariable Long userId){
        List<DocVO> docVOList = docService.listNextChildNode(id, userId);
        return Result.ok().data("childList", docVOList);
    }

    @ApiOperation("获取id的所有父节点，递归到顶级节点")
    @GetMapping("/listParentNode/{id}")
    public Result listParentNode(@ApiParam(value = "节点id", required = true)
                                 @PathVariable Long id){
        String result = docService.listParentNode(id);
        return Result.ok().data("docPath", result);
    }

    @ApiOperation("根据id递归获取所有子节点")
    @GetMapping("/listAllChildNode/{id}")
    public Result listAllChildNode(
            @ApiParam(value = "项目id", required = true)
            @PathVariable Long id) {
        List<DocVO> result = docService.listAllChildNode(id);
        return Result.ok().data("childList", result);
    }

    @ApiOperation("根据id获取文档名，内容等")
    @GetMapping("/getOne/{id}")
    public Result getOne(
            @ApiParam(value = "项目id", required = true)
            @PathVariable Long id) {
        Doc doc = docService.getById(id);
        return Result.ok().data("doc", doc);
    }


}
