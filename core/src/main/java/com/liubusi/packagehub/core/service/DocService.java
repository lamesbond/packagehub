package com.liubusi.packagehub.core.service;

import com.liubusi.packagehub.core.pojo.dto.ExcelDocDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.core.pojo.vo.DocMenuVO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface DocService extends IService<Doc> {
    void importData(InputStream inputStream);

    List<ExcelDocDTO> listDocData();

    List<Doc> listByParentId(Long parentId);

    List<Doc> findByDocCode(String dictCode);

    String getNameByParentDocCodeAndValue(String docCode, Integer value);

    String getDocContent(Long id);

    List<DocMenuVO> getDocMenu(Long id);

    void save(Long id, String docTitle, Long parentId);

}
