package com.badou.brms.cfg.publicity.dao;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.cfg.publicity.model.PublicitySettingEntity;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * 公示平台配置dao接口
 *
 * @author zhongzhilong
 * @date 2020-12-31 11:22:30
 */
public interface IPublicitySettingDao extends IBaseHibernateDAO<PublicitySettingEntity, Serializable> {

}
