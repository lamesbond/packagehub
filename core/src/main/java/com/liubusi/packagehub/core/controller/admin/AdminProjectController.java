package com.liubusi.packagehub.core.controller.admin;

import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;
import com.liubusi.packagehub.core.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@Api(tags = "项目包管理")
@RestController
@RequestMapping("/admin/core/project")
@Slf4j
public class AdminProjectController {

    @Resource
    private ProjectService projectService;

    @ApiOperation("获取id下的下一级子节点，不递归")
    @GetMapping("/listChildCategoryById/{id}")
    public Result listChildCategoryById(@ApiParam(value = "节点id", required = true)
            @PathVariable Long id){
        List<ProjectVO> projectVOList = projectService.listChildCategoryById(id);
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

    @ApiOperation("新增文档")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "文档数据")
            @RequestBody ProjectVO projectVO) {
        projectService.save(projectVO);
        return Result.ok().message("新增成功");
    }

    @ApiOperation("递归删除文档所有子集")
    @DeleteMapping("/remove/{id}")
    public Result remove(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        projectService.remove(id);
        return Result.ok().message("删除成功");
    }

    @ApiOperation("根据文档ID修改文档标题，位置，内容等")
    @PostMapping("/update")
    public Result update(
            @ApiParam(value = "文档id", required = true)
            @RequestBody ProjectVO projectVO) {
        Project project = new Project();
        BeanUtils.copyProperties(projectVO, project);
        log.info("更新的project：" + project.toString());
        projectService.updateById(project);
        return Result.ok().message("修改成功");
    }

}

