package com.bdc.api.rest.dao.impl;

import com.badou.designer.jdbc.core.dao.impl.BaseCommonDAO;
import com.badou.designer.jdbc.core.dao.impl.BaseModuleDAO;
import com.bdc.api.rest.dao.IBaseRestDAO;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:39:29
 * @todo 基础restDao实现类
 */
@Transactional
@Repository
public class BaseRestDAO extends BaseCommonDAO implements IBaseRestDAO {

    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午5:40:58
     * @todo 注入会话工厂
     * @param sessionFactory
     */
    @Resource
    public void setSessionFacotry(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
}
