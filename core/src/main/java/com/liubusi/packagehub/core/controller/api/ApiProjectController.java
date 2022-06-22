package com.liubusi.packagehub.core.controller.api;

import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.core.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 项目管理表 前端控制器
 * </p>
 *
 * @author liubusi
 * @since 2022-05-22
 */
@Api(tags = "文件获取")
@RestController
@RequestMapping("/api/core/project")
@Slf4j
public class ApiProjectController {

    @Resource
    private ProjectService projectService;

    @ApiOperation("获取id下的下一级子节点，不递归")
    @GetMapping("/listNextChildNode/{id}/{userId}")
    public Result listNextChildNode(@ApiParam(value = "节点id", required = true)
                                              @PathVariable Long id,
                                               @ApiParam(value = "用户id", required = true)
                                               @PathVariable Long userId){
        List<ProjectVO> projectVOList = projectService.listNextChildNode(id, userId);
        return Result.ok().data("childList", projectVOList);
    }

    @ApiOperation("获取id的所有父节点，递归到顶级节点")
    @GetMapping("/listParentNode/{id}")
    public Result listParentNode(@ApiParam(value = "节点id", required = true)
                                         @PathVariable Long id){
        String result = projectService.listParentNode(id);
        return Result.ok().data("projectPath", result);
    }

    @ApiOperation("根据id递归获取所有子节点")
    @GetMapping("/listAllChildNode/{id}")
    public Result listAllChildNode(
            @ApiParam(value = "项目id", required = true)
            @PathVariable Long id) {
        List<ProjectVO> result = projectService.listAllChildNode(id);
        return Result.ok().data("childList", result);
    }

    @ApiOperation("根据id获取项目名，内容等")
    @GetMapping("/getOne/{id}")
    public Result getOne(
            @ApiParam(value = "项目id", required = true)
            @PathVariable Long id) {
        Project project = projectService.getById(id);
        return Result.ok().data("project", project);
    }

}

