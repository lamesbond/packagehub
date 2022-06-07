package com.liubusi.packagehub.core.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.liubusi.packagehub.common.exception.BusinessException;
import com.liubusi.packagehub.common.result.ResponseEnum;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.core.pojo.dto.ExcelDocDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.liubusi.packagehub.core.pojo.vo.DocMenuVO;
import com.liubusi.packagehub.core.pojo.vo.DocTitleVO;
import com.liubusi.packagehub.core.service.DocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
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
@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/core/doc")
@Slf4j
public class AdminDocController {

    @Resource
    private DocService docService;

    @ApiOperation("Excel批量导入数据字典")
    @PostMapping("/import")
    public Result batchImport(@ApiParam(value = "Excel文件", required = true)
                                  @RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            docService.importData(inputStream);
            return Result.ok().message("批量导入成功");
        } catch (Exception e) {
            //UPLOAD_ERROR(-103, "文件上传错误"),
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

    @ApiOperation("Excel数据的导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelDocDTO.class).sheet("数据字典").doWrite(docService.listDocData());
    }

    @ApiOperation("根据上级ID获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public Result listByParentId(@ApiParam(value = "上级节点id", required = true)
            @PathVariable Long parentId){
        List<Doc> docList = docService.listByParentId(parentId);
        return Result.ok().data("list", docList);
    }

    @ApiOperation("获取文档详情")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        String result = docService.getDocContent(id);
        return Result.ok().data("docContent", result);
    }

    @ApiOperation("根据文档ID获取文档目录")
    @GetMapping("/showMenu/{id}")
    public Result showMenu(
            @ApiParam(value = "文档id", required = true)
            @PathVariable Long id) {
        List<DocMenuVO> result = docService.getDocMenu(id);
        return Result.ok().data("docMenu", result);
    }

    @ApiOperation("根据文档ID修改文档标题，内容")
    @PostMapping("/updateDoc")
    public Result updateTitle(
            @ApiParam(value = "文档id", required = true)
            @RequestBody DocTitleVO docTitleVO) {
        Doc doc = new Doc();

        docService.saveOrUpdate(doc);
        return Result.ok().message("修改成功");
    }

    @ApiOperation("根据文档ID修改文档排序值")
    @PostMapping("/updatePosition")
    public Result updatePosition(
            @ApiParam(value = "文档id", required = true)
            @RequestBody Map<String, Object> map) {
        Long sourceId = (Long)map.get("id");
        Long destId = (Long)map.get("destId");
        String method = (String) map.get("method");
        docService.updatePosition(sourceId,destId,method);
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

