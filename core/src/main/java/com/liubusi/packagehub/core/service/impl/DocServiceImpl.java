package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liubusi.packagehub.common.pojo.entity.Doc;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;
import com.liubusi.packagehub.core.mapper.DocMapper;
import com.liubusi.packagehub.common.pojo.vo.DocVO;
import com.liubusi.packagehub.core.mapper.UserAuthMapper;
import com.liubusi.packagehub.core.mapper.UserMapper;
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

    @Resource
    private UserAuthMapper userAuthMapper;

    @Override
    public List<DocVO> listNextChildNode(Long id, Long userId) {
        List<Doc> docList = null;
        List<DocVO> docVOList = new ArrayList<>();

        log.info("从数据库中取值");
        docVOList = docMapper.listNextChildNode(id, userId);
        
        docVOList.forEach(docVO -> {
            if (docVO.getType().equals("release_version")) {
                docVO.setHasChildren(false);
            } else {
                boolean hasChildren = this.hasChildren(docVO.getId());
                docVO.setHasChildren(hasChildren);
            }
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("packagehub:core:docVO:" + id, docList, 5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        } catch (Exception e) {
            log.error("redis服务器异常：" + ExceptionUtils.getStackTrace(e));//此处不抛出异常，继续执行后面的代码
        }

        return docVOList;
    }

    @Override
    public String listParentNode(Long id) {
        log.info("从数据库中取值");
        List<DocVO> docVOList = docMapper.listParentNode(id);
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
    public List<DocVO> listAllChildNode(Long id) {
        List<DocVO> result = docMapper.listAllChildNode(id);
        List<DocVO> finalResult = new ArrayList<>();

        for (DocVO docVO1 : result) {
            if (docVO1.getType().equals("release_version")) {
                continue;
            } else {
                for (DocVO docVO2 : result) {
                    if (docVO1.getId().equals(docVO2.getParentId())) {
                        docVO1.getChildren().add(docVO2);
                    }
                }
                if (docVO1.getParentId().equals(id)) {
                    finalResult.add(docVO1);
                }
            }
        }
        return finalResult;
    }

    @Override
    public void save(DocVO docVO) {
        Doc doc = new Doc();
        BeanUtils.copyProperties(docVO, doc);

        Long id = docVO.getId();
        Long userId = docVO.getUserId();
        Long parentId = docVO.getParentId();
        String title = docVO.getTitle();
        String type = docVO.getType();

        List<Long> docList = new ArrayList<>();
        docList.add(id);

        if (type.equals("doc")) {
            docMapper.insert(doc);
            userAuthMapper.authDoc(userId, docList);
        } else if (type.equals("category") || type.equals("release_version")){
            docMapper.insert(doc);
        } else if (type.equals("menu")) {
            docMapper.saveMenu(id, title, parentId, type);
        } else {
            log.info("不知所措");
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
