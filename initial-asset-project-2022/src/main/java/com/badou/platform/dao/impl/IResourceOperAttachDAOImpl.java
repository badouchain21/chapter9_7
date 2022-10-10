package com.badou.platform.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.platform.dao.IResourceOperAttachDAO;
import com.badou.platform.model.ResourceOperAttachEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:49:30
 * @todo 资源操作记录相关附件dao层相关接口实现类
 */
@Repository
public class IResourceOperAttachDAOImpl extends BaseHibernateDAO<ResourceOperAttachEntity, Serializable> implements IResourceOperAttachDAO {

	@Override
	public List<ResourceOperAttachEntity> findByOperId(String id) {
		String hql = "from ResourceOperAttachEntity t where t.operId = ?" ;
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, id);
		List<ResourceOperAttachEntity> list = query.list();
		return list;
	}

}
