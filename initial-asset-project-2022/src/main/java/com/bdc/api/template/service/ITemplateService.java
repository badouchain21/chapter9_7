package com.bdc.api.template.service;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.bdc.api.template.vo.TemplateVO;

/**
 * 描述：接口文档
 *
 * @author linxiaoqing
 * @date 2019年8月15日
 */
@SuppressWarnings("rawtypes")
public interface ITemplateService extends IBaseSpringService {

    /**
     *
     * 描述：根据资产建模定义生成接口文档
     * @author linxiaoqing
     * @Date 2020年1月7日 上午11:44:56
     * @param id
     * @return
     */
    TemplateVO getAssetInterfaceDetail(String id);

}
