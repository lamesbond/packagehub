package com.liubusi.packagehub.core.mapper;

import com.liubusi.packagehub.core.pojo.dto.ExcelDocDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubusi.packagehub.core.pojo.vo.DocMenuVO;

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

    void insertBatch(List<ExcelDocDTO> list);

    List<DocMenuVO> getDocMenu(Long id);

    void save(Long id, String docTitle, Long parentId);

    void updatePositionByBefore(Long id, Long destId);

    void updatePositionByAfter(Long id, Long destId);

    void updatePositionByInner(Long id, Long destId);
}
