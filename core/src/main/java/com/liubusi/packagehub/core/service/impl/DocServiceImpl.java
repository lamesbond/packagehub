package com.liubusi.packagehub.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liubusi.packagehub.core.listener.ExcelDictDTOListener;
import com.liubusi.packagehub.core.pojo.dto.ExcelDictDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.liubusi.packagehub.core.mapper.DictMapper;
import com.liubusi.packagehub.core.service.DocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
@Service
@Slf4j
public class DocServiceImpl extends ServiceImpl<DictMapper, Doc> implements DocService {

    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("importData finished");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Doc> docList = baseMapper.selectList(null);
        //创建ExcelDictDTO列表，将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(docList.size());
        docList.forEach(dict -> {

            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }

    @Override
    public List<Doc> listByParentId(Long parentId) {
        //先查询redis中是否存在数据列表
        List<Doc> docList = null;
        try {
            docList = (List<Doc>)redisTemplate.opsForValue().get("packagehub:core:doc:" + parentId);
            if (docList != null) {
                log.info("从redis中取值");
                return docList;
            }
        } catch (Exception e) {
            log.error("redis服务异常：" + ExceptionUtils.getStackTrace(e));;//此处不抛出异常，继续执行后面的代码
        }

        log.info("从数据库中取值");
        docList = baseMapper.selectList(new QueryWrapper<Doc>().eq("parent_id", parentId));
        docList.forEach(dict -> {
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("packagehub:core:doc:" + parentId, docList, 5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        } catch (Exception e) {
            log.error("redis服务器异常：" + ExceptionUtils.getStackTrace(e));//此处不抛出异常，继续执行后面的代码
        }

        return docList;
    }

    @Override
    public List<Doc> findByDictCode(String dictCode) {
        QueryWrapper<Doc> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Doc doc = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(doc.getId());
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Doc> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Doc parentDoc = baseMapper.selectOne(dictQueryWrapper);

        if (parentDoc == null) {
            return "";
        }

        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", parentDoc.getParentId()).eq("value", value);
        Doc doc = baseMapper.selectOne(dictQueryWrapper);

        if (doc == null) {
            return "";
        }

        return doc.getDocTitle();
    }

    @Override
    public Map<String, String> getDocDetail(Long id) {
        //查询标的对象
        Doc doc = baseMapper.selectById(id);
        //组装数据
        String docTitle = doc.getDocTitle();
        String docContent = doc.getDocContent();

        //组装数据
        Map<String, String> result = new HashMap<>();
        result.put("docTitle", docTitle);
        result.put("docContent", docContent);

        return result;
    }

    private boolean hasChildren(Long id) {
        QueryWrapper<Doc> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(dictQueryWrapper);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
}
