package com.liubusi.packagehub.core.mapper;

import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubusi.packagehub.core.pojo.vo.DocVO;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface DocMapper extends BaseMapper<Doc> {

    List<DocVO> listParentCategoryById(Long id);

    List<DocVO> listMenuById(Long id);

    void saveCategory(Long id, String title, Long parentId, String department, String description, String isDoc);

    void saveMenu(Long id, String title, Long parentId);

    void remove(Long id);

    void updatePositionByBefore(Long id, Long destId);

    void updatePositionByAfter(Long id, Long destId);

    void updatePositionByInner(Long id, Long destId);


}
