package com.liubusi.packagehub.core.mapper;

import com.liubusi.packagehub.common.pojo.entity.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubusi.packagehub.common.pojo.vo.DocVO;

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

    List<DocVO> listNextChildNode(Long id, Long userId);

    List<DocVO> listParentNode(Long id);

    List<DocVO> listAllChildNode(Long id);

    void saveMenu(Long id, String title, Long parentId, String type);

    void remove(Long id);

    void updatePositionByBefore(Long id, Long destId);

    void updatePositionByAfter(Long id, Long destId);

    void updatePositionByInner(Long id, Long destId);

}
