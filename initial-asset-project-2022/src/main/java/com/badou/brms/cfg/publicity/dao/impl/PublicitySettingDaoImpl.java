package com.badou.brms.cfg.publicity.dao.impl;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.brms.cfg.publicity.dao.IPublicitySettingDao;
import com.badou.brms.cfg.publicity.model.PublicitySettingEntity;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 公示平台配置dao实现
 *
 * @author zhongzhilong
 * @date 2020-12-31 11:22:30
 */
@Repository
public class PublicitySettingDaoImpl extends BaseHibernateDAO<PublicitySettingEntity, Serializable> implements IPublicitySettingDao {
}
