package com.liubusi.packagehub.core.controller.api;

import com.liubusi.packagehub.core.service.DocService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "数据字典")
@RestController
@RequestMapping("/api/core/dict")
@Slf4j
public class ApiDocController {
    @Resource
    private DocService dictService;

//    @ApiOperation("根据dictCode获取下级节点")
//    @GetMapping("/findByDictCode/{dictCode}")
//    public Result findByDictCode(
//            @ApiParam(value = "节点编码", required = true)
//            @PathVariable String dictCode) {
//        List<Doc> list = dictService.findByDocCode(dictCode);
//        return Result.ok().data("dictList", list);
//    }

}
