package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubusi.packagehub.core.mapper.ProjectMapper;
import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;
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


    @Override
    public List<ProjectVO> listChildCategoryById(Long id) {
        //先查询redis中是否存在数据列表
        List<Project> projectList = null;
        List<ProjectVO> projectVOList = new ArrayList<>();
//        try {
//            projectList = (List<Project>)redisTemplate.opsForValue().get("packagehub:core:project:" + id);
//            if (projectList != null) {
//                log.info("从redis中取值");
//                return projectList;
//            }
//        } catch (Exception e) {
//            log.error("redis服务异常：" + ExceptionUtils.getStackTrace(e));;//此处不抛出异常，继续执行后面的代码
//        }

        log.info("从数据库中取值");
        projectList = baseMapper.selectList(new QueryWrapper<Project>().eq("parent_id", id));

        projectList.forEach(project -> {
            ProjectVO projectVO1 = new ProjectVO();
            BeanUtils.copyProperties(project,projectVO1);
            projectVOList.add(projectVO1);
        });
        projectVOList.forEach(projectVO -> {
//            if (projectVO.getType().equals("release_version")) {
//                projectVO.setHasChildren(false);
//            } else {
                boolean hasChildren = this.hasChildren(projectVO.getId());
                projectVO.setHasChildren(hasChildren);
//            }
        });

        //将数据存入redis
        try {
            redisTemplate.opsForValue().set("packagehub:core:project:" + id, projectList, 5, TimeUnit.MINUTES);
            log.info("数据存入redis");
        } catch (Exception e) {
            log.error("redis服务器异常：" + ExceptionUtils.getStackTrace(e));//此处不抛出异常，继续执行后面的代码
        }

        return projectVOList;
    }

    @Override
    public String listParentCategoryById(Long id) {

        log.info("从数据库中取值");
        List<ProjectVO> projectVOList = projectMapper.listParentCategoryById(id);
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
    public List<ProjectVO> listMenuById(Long id) {
        List<ProjectVO> result = projectMapper.listMenuById(id);
        List<ProjectVO> finalResult = new ArrayList<>();

        for (ProjectVO firstprojectVO : result) {
            System.out.println("========"+firstprojectVO);
            for (ProjectVO secondprojectVO : result) {
                if (firstprojectVO.getId().equals(secondprojectVO.getParentId())) {
                    firstprojectVO.getChildren().add(secondprojectVO);
                }
            }
            if (firstprojectVO.getParentId().equals(id)) {
                finalResult.add(firstprojectVO);
            }
        }
        return finalResult;
    }

    @Override
    public void save(ProjectVO projectVO) {
        Long id = projectVO.getId();
        Long parentId = projectVO.getParentId();
        String name = projectVO.getName();
        String department = projectVO.getDepartment();
        String description = projectVO.getDescription();
        String type = projectVO.getType();
        String pubStatus = projectVO.getPubStatus();
        String url = projectVO.getUrl();

        if (type.equals("file")) {
            projectMapper.saveFile(id, name, parentId, type, url);
        } else {
            projectMapper.saveMenu(id, name, parentId);
        }
    }

    @Override
    public void remove(Long id) {
        projectMapper.remove(id);
    }

//    @Override
//    public void update(ProjectVO projectVO) {
//        Long id = projectVO.getId();
//        Project project = new Project();
//        BeanUtils.copyProperties(projectVO, project);
//        UpdateWrapper<Project> projectUpdateWrapper= new UpdateWrapper<>();
//        projectUpdateWrapper.set();
//        projectMapper.update();
//    }

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
