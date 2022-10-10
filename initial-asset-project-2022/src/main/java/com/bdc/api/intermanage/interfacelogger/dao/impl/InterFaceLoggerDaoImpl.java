package com.bdc.api.intermanage.interfacelogger.dao.impl;

import java.io.Serializable;

import com.bdc.api.intermanage.interfacelogger.dao.IInterFaceLoggerDao;
import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;

@Repository
public class InterFaceLoggerDaoImpl extends BaseHibernateDAO<InterFaceLoggerEntity, Serializable>
				implements IInterFaceLoggerDao {

}
