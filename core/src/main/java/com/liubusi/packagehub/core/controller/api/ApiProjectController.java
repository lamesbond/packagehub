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
@Api(tags = "获取公共和私有项目")
@RestController
@RequestMapping("/api/core/project")
@Slf4j
public class ApiProjectController {

    @Resource
    private ProjectService projectService;

    @ApiOperation("获取id下的下一级子节点，不递归")
    @GetMapping("/listChildCategoryById/{id}/{userId}")
    public Result listChildCategoryById(@ApiParam(value = "节点id", required = true)
                                              @PathVariable Long id,
                                               @ApiParam(value = "用户id", required = true)
                                               @PathVariable Long userId){
        List<ProjectVO> projectVOList = projectService.listChildCategoryById(id, userId);
        return Result.ok().data("childList", projectVOList);
    }

    @ApiOperation("获取id的所有父节点，递归到顶级节点")
    @GetMapping("/listParentCategoryById/{id}")
    public Result listParentCategoryById(@ApiParam(value = "节点id", required = true)
                                         @PathVariable Long id){
        String result = projectService.listParentCategoryById(id);
        return Result.ok().data("projectPath", result);
    }

    @ApiOperation("根据id递归获取文档目录")
    @GetMapping("/listMenuById/{id}")
    public Result listMenuById(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        List<ProjectVO> result = projectService.listMenuById(id);
        return Result.ok().data("projectMenu", result);
    }

    @ApiOperation("根据id获取文档标题，内容等")
    @GetMapping("/getOne/{id}")
    public Result getOne(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        Project project = projectService.getById(id);
        return Result.ok().data("project", project);
    }

}

