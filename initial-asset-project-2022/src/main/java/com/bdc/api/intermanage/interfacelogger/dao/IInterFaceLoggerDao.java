package com.bdc.api.intermanage.interfacelogger.dao;

import java.io.Serializable;

import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;

@Repository
public interface IInterFaceLoggerDao extends IBaseHibernateDAO<InterFaceLoggerEntity, Serializable>  {

}
