package com.bdc.api.intermanage.interfacepermission.dao;

import java.io.Serializable;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;


public interface IInterFacePermissionDAO extends IBaseHibernateDAO<InterFacePermissionEntity, Serializable>  {

	public void delByInterAndNet(String interfaceId, String netInformationId);

}
