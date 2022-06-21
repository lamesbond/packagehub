package com.liubusi.packagehub.core.service;

import com.liubusi.packagehub.common.pojo.entity.Doc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.common.pojo.vo.DocVO;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface DocService extends IService<Doc> {

    List<DocVO> listNextChildNode(Long id, Long userId);

    String listParentNode(Long id);

    List<DocVO> listAllChildNode(Long id);

    void save(DocVO docVO);

    void remove(Long id);

    void update(DocVO docVO);

}
