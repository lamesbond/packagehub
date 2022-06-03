package com.liubusi.packagehub.core.service;

import com.liubusi.packagehub.core.pojo.dto.ExcelDictDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<ExcelDictDTO> listDictData();

    List<Doc> listByParentId(Long parentId);

    List<Doc> findByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode, Integer value);

    Map<String, String> getDocDetail(Long id);
}
