package com.bdc.api.intermanage.interfacedetail.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.api.intermanage.interfacedetail.dao.IInterFaceDetailDao;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.common.utils.BdLoginUtil;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.tools.common.util.StringUtils;



@Repository
@SuppressWarnings("unchecked")
public class InterFaceDetailDaoImpl extends BaseHibernateDAO<InterFaceDetailEntity, Serializable>
		implements IInterFaceDetailDao {

	@Override
	public Pagelet findOtherByNetId(String netId,String searchSql,Boolean isOther,String userId) {
		String paramString="not in";
		//从线程变量中取出分页参数
		if(StringUtils.isEmpty(netId)){
			return null;
		}
		if(!isOther){
			paramString ="in";
		}

		Pagelet pagelet = (Pagelet)PaginationTheadLocal.getDefaultPagelet();
		int firstResult = pagelet.getStartOfPage().intValue();
		int maxResults = pagelet.getPerPageSize().intValue();
		StringBuilder hqlBuilder = new StringBuilder();
		String hql = "select A from InterFaceDetailEntity A where A.id  "+paramString+" (select interFaceDetailEntity.id from InterFacePermissionEntity where netInformationEntity.id=? and flg_deleted=0) ";
		hqlBuilder.append(hql);
		if(StringUtils.isNotBlank(searchSql)){
			hqlBuilder.append("and ");
			hqlBuilder.append(searchSql);
		}
		hqlBuilder.append(" and creator = '"+userId+"' ");
		hqlBuilder.append("order by create_time");
		Query query = this.getSession().createQuery(hqlBuilder.toString()).setString(0, netId).setFirstResult(firstResult).setMaxResults(maxResults);

		List result = query.list();
		pagelet.setDatas(result);
		pagelet.setTotalCount(this.countSize(hqlBuilder.toString(), netId));

		return pagelet;
	}

	@Override
	public List<InterFaceDetailEntity> getEntityByName(String name,String userId){
		String sql = "select * from i_interface_detail where FLG_DELETED = 0 and BINARY name='"+name+"' and creator='"+userId+"' ";
		Query query = getSession().createSQLQuery(sql);
		List<InterFaceDetailEntity> list = query.list();
		return list;
	}
}
