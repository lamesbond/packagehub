package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubusi.packagehub.core.mapper.ProjectMapper;
import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;
import com.liubusi.packagehub.core.mapper.UserAuthMapper;
import com.liubusi.packagehub.core.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @Override
    public List<ProjectVO> listNextChildNode(Long id, Long userId) {
        List<Project> projectList = null;
        List<ProjectVO> projectVOList = new ArrayList<>();

        //先查询redis中是否存在数据列表
        try {
            projectVOList = (List<ProjectVO>)redisTemplate.opsForValue().get("packagehub:core:projectVOList:" + id);
            if (projectVOList != null) {
                log.info("从redis中取值");
                return projectVOList;
            }
        } catch (Exception e) {
            log.error("redis服务异常：" + ExceptionUtils.getStackTrace(e));;//此处不抛出异常，继续执行后面的代码
        }

        log.info("从数据库中取值");
        projectVOList = projectMapper.listNextChildNode(id, userId);

        projectVOList.forEach(projectVO1 -> {
            boolean hasChildren = this.hasChildren(projectVO1.getId());
            projectVO1.setHasChildren(hasChildren);
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("packagehub:core:projectVOList:" + id, projectVOList, 5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        } catch (Exception e) {
            log.error("redis服务器异常：" + ExceptionUtils.getStackTrace(e));//此处不抛出异常，继续执行后面的代码
        }

        return projectVOList;
    }

    @Override
    public String listParentNode(Long id) {
        List<ProjectVO> projectVOList = projectMapper.listParentNode(id);
        String result = "";

        for (ProjectVO projectVO : projectVOList) {
            if (projectVOList.size() -1 == projectVOList.indexOf(projectVO)) {
                result += projectVO.getName();
            } else {
                result += projectVO.getName() + "-";
            }
        }
        return result;
    }

    @Override
    public List<ProjectVO> listAllChildNode(Long id) {
        List<ProjectVO> result = projectMapper.listAllChildNode(id);
        List<ProjectVO> finalResult = new ArrayList<>();
        
        for (ProjectVO projectVO1 : result) {
            if (projectVO1.getType().equals("release_version")) {
                continue;
            } else {
                for (ProjectVO projectVO2 : result) {
                    if (projectVO1.getId().equals(projectVO2.getParentId())) {
                        projectVO1.getChildren().add(projectVO2);
                    }
                }
                if (projectVO1.getParentId().equals(id)) {
                    finalResult.add(projectVO1);
                }
            }
        }
        return finalResult;
    }

    @Override
    public void save(ProjectVO projectVO) {
        Project project = new Project();
        BeanUtils.copyProperties(projectVO, project);

        Long id = projectVO.getId();
        Long userId = projectVO.getUserId();
        String type = projectVO.getType();
        List<Long> projectList = new ArrayList<>();
        projectList.add(id);

        if (type.equals("file")) {
            projectMapper.insert(project);
        } else if (type.equals("project") || type.equals("category")){
            projectMapper.insert(project);
            userAuthMapper.authProject(userId, projectList);
        } else {
            log.info("不知所措");
        }
    }

    @Override
    public void remove(Long id) {
        projectMapper.remove(id);
    }

    private boolean hasChildren(Long id) {
        QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
        projectQueryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(projectQueryWrapper);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }

}
