package com.liubusi.packagehub.core.mapper;

import com.liubusi.packagehub.core.pojo.dto.ExcelDictDTO;
import com.liubusi.packagehub.core.pojo.entity.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface DictMapper extends BaseMapper<Doc> {

    void insertBatch(List<ExcelDictDTO> list);

}
