package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.liubusi.packagehub.core.mapper.DocMapper;
import com.liubusi.packagehub.core.pojo.vo.DocVO;
import com.liubusi.packagehub.core.service.DocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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


    @Override
    public List<DocVO> listChildCategoryById(Long id) {
        //先查询redis中是否存在数据列表
        List<Doc> docList = null;
        List<DocVO> docVOList = new ArrayList<>();
//        try {
//            docList = (List<Doc>)redisTemplate.opsForValue().get("packagehub:core:doc:" + id);
//            if (docList != null) {
//                log.info("从redis中取值");
//                return docList;
//            }
//        } catch (Exception e) {
//            log.error("redis服务异常：" + ExceptionUtils.getStackTrace(e));;//此处不抛出异常，继续执行后面的代码
//        }

        log.info("从数据库中取值");
        docList = baseMapper.selectList(new QueryWrapper<Doc>().eq("parent_id", id));

        docList.forEach(doc -> {
            log.info("docccc"+doc.toString());
            DocVO docVO1 = new DocVO();
            BeanUtils.copyProperties(doc,docVO1);
            docVOList.add(docVO1);
        });
        docVOList.forEach(docVO -> {
            log.info("docVVOVOV"+docVO.toString());
            if (docVO.getIsDoc().equals("1")) {
                docVO.setHasChildren(false);
            } else {
                boolean hasChildren = this.hasChildren(docVO.getId());
                docVO.setHasChildren(hasChildren);
            }
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("packagehub:core:doc:" + id, docList, 5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        } catch (Exception e) {
            log.error("redis服务器异常：" + ExceptionUtils.getStackTrace(e));//此处不抛出异常，继续执行后面的代码
        }

        return docVOList;
    }

    @Override
    public String listParentCategoryById(Long id) {

        log.info("从数据库中取值");
        List<DocVO> docVOList = docMapper.listParentCategoryById(id);
        String result = "";

        for (DocVO docVO : docVOList) {
            if (docVOList.size() -1 == docVOList.indexOf(docVO)) {
                result += docVO.getTitle();
            } else {
                result += docVO.getTitle() + "-";
            }
        }

        return result;
    }

    @Override
    public List<DocVO> listMenuById(Long id) {
        List<DocVO> result = docMapper.listMenuById(id);
        List<DocVO> finalResult = new ArrayList<>();

        for (DocVO firstDocVO : result) {
            System.out.println("========"+firstDocVO);
            for (DocVO secondDocVO : result) {
                if (firstDocVO.getId().equals(secondDocVO.getParentId())) {
                    firstDocVO.getChildren().add(secondDocVO);
                }
            }
            if (firstDocVO.getParentId().equals(id)) {
                finalResult.add(firstDocVO);
            }
        }
        return finalResult;
    }

    @Override
    public void save(DocVO docVO) {
        Long id = docVO.getId();
        Long parentId = docVO.getParentId();
        String title = docVO.getTitle();
        String department = docVO.getDepartment();
        String description = docVO.getDescription();
        String isDoc = docVO.getIsDoc();
        String pubStatus = docVO.getPubStatus();

        if (!StringUtils.isEmpty(department) || !StringUtils.isEmpty(description) || !StringUtils.isEmpty(isDoc)) {
            docMapper.saveCategory(id, title, parentId, department, description, isDoc, pubStatus);
        } else {
            docMapper.saveMenu(id, title, parentId);
        }
    }

    @Override
    public void remove(Long id) {
        docMapper.remove(id);
    }

    @Override
    public void update(DocVO docVO) {
        Long id = docVO.getId();
        Long destId = docVO.getDestId();
        Doc doc = new Doc();
        BeanUtils.copyProperties(docVO,doc);
        String dragMethod = docVO.getDragMethod();
        if (StringUtils.isEmpty(dragMethod)) {
            docMapper.updateById(doc);
        } else if (dragMethod.equals("before")) {
            docMapper.updatePositionByBefore(id, destId);
        } else if (dragMethod.equals("after")) {
            docMapper.updatePositionByAfter(id, destId);
        } else if (dragMethod.equals("inner")) {
            docMapper.updatePositionByInner(id, destId);
        } else {
            log.info("dragmethod参数错误");
        }
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

}
