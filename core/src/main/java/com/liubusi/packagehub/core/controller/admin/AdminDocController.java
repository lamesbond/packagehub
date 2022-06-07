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

    @ApiOperation("根据父id获取项目列表，只获取下一子级，不递归")
    @GetMapping("/listProjectById/{parentId}")
    public Result listProjectById(@ApiParam(value = "节点id", required = true)
            @PathVariable Long parentId){
        List<Doc> docList = docService.listByParentId(parentId);
        return Result.ok().data("list", docList);
    }

    @ApiOperation("根据项目发行版本id递归获取目录")
    @GetMapping("/listMenuById/{id}")
    public Result listMenuById(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        List<DocMenuVO> result = docService.getDocMenu(id);
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
            @ApiParam(value = "文档id", required = true)
            @RequestBody Map<String, String> map) {

        Long id = Long.valueOf(map.get("id"));
        Long parentId = Long.valueOf(map.get("parentId"));
        String docTitle = map.get("docTitle");
        System.out.println("增加的id："+id);
        System.out.println("增加的parentid："+parentId);
        System.out.println("增加的doctitle："+docTitle);
//        Long id = docTitleVO.getChildren().getId();
//        String docTitle = docTitleVO.getChildren().getDocTitle();
//        Long parentId = docTitleVO.getId();
        Doc doc = new Doc();
//        BeanUtils.copyProperties(docTitleVO, doc);
        docService.save(id, docTitle, parentId);
        return Result.ok().message("修改成功");
    }

    @ApiOperation("删除文档")
    @DeleteMapping("/removeDoc/{id}")
    public Result removeDoc(
            @ApiParam(value = "文档id", required = true)
            @PathVariable String id) {
        docService.removeById(id);
        return Result.ok().message("删除成功");
    }
}

