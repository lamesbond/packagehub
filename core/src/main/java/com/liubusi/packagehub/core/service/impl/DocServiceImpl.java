package com.liubusi.packagehub.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liubusi.packagehub.core.listener.ExcelDocDTOListener;
import com.liubusi.packagehub.core.pojo.dto.ExcelDocDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.liubusi.packagehub.core.mapper.DocMapper;
import com.liubusi.packagehub.core.pojo.vo.DocMenuVO;
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
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DocMapper docMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDocDTO.class, new ExcelDocDTOListener(baseMapper)).sheet().doRead();
        log.info("importData finished");
    }

    @Override
    public List<ExcelDocDTO> listDocData() {
        List<Doc> docList = baseMapper.selectList(null);
        //创建ExceldocDTO列表，将doc列表转换成ExceldocDTO列表
        ArrayList<ExcelDocDTO> exceldocDTOList = new ArrayList<>(docList.size());
        docList.forEach(doc -> {

            ExcelDocDTO exceldocDTO = new ExcelDocDTO();
            BeanUtils.copyProperties(doc, exceldocDTO);
            exceldocDTOList.add(exceldocDTO);
        });
        return exceldocDTOList;
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
        docList.forEach(doc -> {
            boolean hasChildren = this.hasChildren(doc.getId());
            doc.setHasChildren(hasChildren);
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
    public List<Doc> findByDocCode(String docCode) {
        QueryWrapper<Doc> docQueryWrapper = new QueryWrapper<>();
        docQueryWrapper.eq("doc_code", docCode);
        Doc doc = baseMapper.selectOne(docQueryWrapper);
        return this.listByParentId(doc.getId());
    }

    @Override
    public String getNameByParentDocCodeAndValue(String docCode, Integer value) {
        QueryWrapper<Doc> docQueryWrapper = new QueryWrapper<>();
        docQueryWrapper.eq("doc_code", docCode);
        Doc parentDoc = baseMapper.selectOne(docQueryWrapper);

        if (parentDoc == null) {
            return "";
        }

        docQueryWrapper = new QueryWrapper<>();
        docQueryWrapper.eq("parent_id", parentDoc.getParentId()).eq("value", value);
        Doc doc = baseMapper.selectOne(docQueryWrapper);

        if (doc == null) {
            return "";
        }

        return doc.getDocTitle();
    }

    @Override
    public String getDocContent(Long id) {
        Doc doc = baseMapper.selectById(id);
        String docContent = doc.getDocContent();
        return docContent;
    }

    @Override
    public List<DocMenuVO> getDocMenu(Long id) {
        List<DocMenuVO> result = docMapper.getDocMenu(id);
        List<DocMenuVO> finalResult = new ArrayList<>();

        for (DocMenuVO firstDocMenuVO : result) {
            System.out.println("========"+firstDocMenuVO);
            for (DocMenuVO secondDocMenuVO : result) {
                if (firstDocMenuVO.getId().equals(secondDocMenuVO.getParentId())) {
                    firstDocMenuVO.getChildren().add(secondDocMenuVO);
                }
            }
            if (firstDocMenuVO.getParentId().equals(id)) {
                finalResult.add(firstDocMenuVO);
            }
        }
        return finalResult;
    }

    private boolean hasChildren(Long id) {
        QueryWrapper<Doc> docQueryWrapper = new QueryWrapper<>();
        docQueryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(docQueryWrapper);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void save(Long id, String docTitle, Long parentId) {
        docMapper.save(id, docTitle, parentId);
    }
}
