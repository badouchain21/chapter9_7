package com.badou.platform.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.platform.PlatformConst;
import com.badou.platform.dao.IResourceOperDAO;
import com.badou.platform.model.ResourceOperEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:47:28
 * @todo 资源操作记录dao层相关接口实现类
 */
@Repository
public class ResourceOperDAOImpl extends BaseHibernateDAO<ResourceOperEntity, Serializable> implements IResourceOperDAO {

	@Override
	public List<ResourceOperEntity> findByPlatformResourceId(
			String platformResourceId, String versionCode, String version1, String version2, String version3) {
		String hql = "from ResourceOperEntity t where t.platformResourceId = ? and versionCode = ? and version1 = ? and version2 = ? and version3 = ?";
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, platformResourceId);
		query.setParameter(1, versionCode);
		query.setParameter(2, version1);
		query.setParameter(3, version2);
		query.setParameter(4, version3);
		List<ResourceOperEntity> list = query.list() ;
		return list;
	}
	
	@Override
	public ResourceOperEntity findLatestUpload(String moduleId) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("select oper from ResourceOperEntity oper,PlatformResourceEntity plat");
		hql.append(" where oper.platformResourceId=plat.id and plat.resourceId=?");
		hql.append(" and oper.operType=? order by oper.createTime desc");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter(0, moduleId);
		query.setParameter(1, PlatformConst.OPER_TYPE_UPLOAD);
		query.setFirstResult(0);
		query.setMaxResults(1);
		
		List<ResourceOperEntity> list = query.list();
		if(null==list || list.size()<=0){
			return null;
		}
		return list.get(0);
	}

}
