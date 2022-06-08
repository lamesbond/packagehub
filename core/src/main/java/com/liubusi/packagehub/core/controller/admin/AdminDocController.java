package com.liubusi.packagehub.core.controller.admin;

import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.liubusi.packagehub.core.pojo.vo.DocMenuVO;
import com.liubusi.packagehub.core.pojo.vo.DocInfoVO;
import com.liubusi.packagehub.core.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @ApiOperation("获取id下的子节点，只获取下一子级，不递归")
    @GetMapping("/listChildProjectById/{id}")
    public Result listChildProjectById(@ApiParam(value = "节点id", required = true)
            @PathVariable Long id){
        List<Doc> docList = docService.listChildProjectById(id);
        return Result.ok().data("childList", docList);
    }

    @ApiOperation("获取id的所有父节点，递归到顶级节点")
    @GetMapping("/listParentProjectById/{id}")
    public Result listParentProjectById(@ApiParam(value = "节点id", required = true)
                                       @PathVariable Long id){
        String result = docService.listParentProjectById(id);
        return Result.ok().data("docPath", result);
    }

    @ApiOperation("根据id递归获取文档目录")
    @GetMapping("/listMenuById/{id}")
    public Result listMenuById(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        List<DocMenuVO> result = docService.listMenuById(id);
        return Result.ok().data("docMenu", result);
    }

    @ApiOperation("获取文档详情")
    @GetMapping("/showDocDetail/{id}")
    public Result showDocDetail(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        String result = docService.getDocContent(id);
        return Result.ok().data("docContent", result);
    }

    @ApiOperation("根据文档ID修改文档标题，内容")
    @PostMapping("/updateDocInfo")
    public Result updateDocInfo(
            @ApiParam(value = "文档id", required = true)
            @RequestBody DocInfoVO docInfoVO) {
        Doc doc = new Doc();
        BeanUtils.copyProperties(docInfoVO, doc);
        docService.saveOrUpdate(doc);
        return Result.ok().message("修改成功");
    }

    @ApiOperation("根据文档ID修改文档排序值")
    @PostMapping("/updateDocPosition")
    public Result updateDocPosition(
            @ApiParam(value = "文档id", required = true)
            @RequestBody DocInfoVO docInfoVO) {
        docService.updatePosition(docInfoVO);
        return Result.ok().message("修改成功");
    }

    @ApiOperation("新增文档")
    @PostMapping("/saveDoc")
    public Result saveDoc(
            @ApiParam(value = "文档数据", required = true)
            @RequestBody DocInfoVO docInfoVO) {
        Doc doc = new Doc();
        BeanUtils.copyProperties(docInfoVO, doc);
        System.out.println("==========="+docInfoVO.getIsDoc()+docInfoVO.getDocTitle()+docInfoVO.getId());
        System.out.println("==========="+doc.getIsDoc()+doc.getDocTitle()+doc.getId());
        docService.save(doc);
        return Result.ok().message("修改成功");
    }

    @ApiOperation("删除文档")
    @DeleteMapping("/removeDoc/{id}")
    public Result removeDoc(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        docService.removeById(id);
        return Result.ok().message("删除成功");
    }
}

