package com.badou.brms.cfg.publicity.service.impl;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.cfg.publicity.dao.IPublicitySettingDao;
import com.badou.brms.cfg.publicity.model.PublicitySettingEntity;
import com.badou.brms.cfg.publicity.service.IPublicitySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 公示平台配置service实现
 *
 * @author zhongzhilong
 * @date 2020-12-31 11:22:30
 */
@Service
public class PublicitySettingServiceImpl extends BaseSpringService<PublicitySettingEntity, Serializable> implements IPublicitySettingService {

    private IPublicitySettingDao publicitySettingDao;

    @Autowired
    public void setBaseDAO(IPublicitySettingDao publicitySettingDao) {
        super.setBaseDAO(publicitySettingDao);
        this.publicitySettingDao = publicitySettingDao;
    }

}
