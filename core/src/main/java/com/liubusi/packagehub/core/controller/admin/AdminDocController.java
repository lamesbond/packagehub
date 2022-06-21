package com.liubusi.packagehub.core.controller.admin;

import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.common.pojo.entity.Doc;
import com.liubusi.packagehub.common.pojo.vo.DocVO;
import com.liubusi.packagehub.core.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 字典管理表 前端控制器
 * </p>
 *
 * @author liubusi
 * @since 2022-05-22
 */
@Api(tags = "项目文档管理")
@RestController
@RequestMapping("/admin/core/doc")
@Slf4j
public class AdminDocController {

    @Resource
    private DocService docService;

    @ApiOperation("获取id下的下一级子节点，不递归")
    @GetMapping("/listNextChildNode/{id}/{userId}")
    public Result listChildNodeById(@ApiParam(value = "节点id", required = true)
            @PathVariable Long id, @PathVariable Long userId){
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

    @ApiOperation("根据id递归获取文档目录")
    @GetMapping("/listAllChildNode/{id}")
    public Result listAllChildNode(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        List<DocVO> result = docService.listAllChildNode(id);
        return Result.ok().data("docMenu", result);
    }

    @ApiOperation("根据id获取文档标题，内容等")
    @GetMapping("/getOne/{id}")
    public Result getOne(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        Doc doc = docService.getById(id);
        return Result.ok().data("doc", doc);
    }

    @ApiOperation("新增文档")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "文档数据")
            @RequestBody DocVO docVO) {
        docService.save(docVO);
        return Result.ok().message("新增成功");
    }

    @ApiOperation("递归删除文档所有子集")
    @DeleteMapping("/remove/{id}")
    public Result remove(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        docService.remove(id);
        return Result.ok().message("删除成功");
    }

    @ApiOperation("根据文档ID修改文档标题，位置，内容等")
    @PostMapping("/update")
    public Result update(
            @ApiParam(value = "文档id", required = true)
            @RequestBody DocVO docVO) {
        docService.update(docVO);
        return Result.ok().message("修改成功");
    }

}

