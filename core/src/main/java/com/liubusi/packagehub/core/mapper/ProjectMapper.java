package com.liubusi.packagehub.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<ProjectVO> listParentCategoryById(Long id);

    List<ProjectVO> listMenuById(Long id);

    void saveFile(Long id, String name, Long parentId, String type, String url);

    void saveMenu(Long id, String title, Long parentId);

    void remove(Long id);

}
