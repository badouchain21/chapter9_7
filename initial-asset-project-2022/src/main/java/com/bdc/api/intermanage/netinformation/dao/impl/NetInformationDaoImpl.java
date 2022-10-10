package com.bdc.api.intermanage.netinformation.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.bdc.api.intermanage.netinformation.dao.INetInformationDao;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.tools.common.util.StringUtils;


@Repository
@SuppressWarnings("unchecked")
public class NetInformationDaoImpl  extends BaseHibernateDAO<NetInformationEntity, Serializable>
		implements INetInformationDao {

    @Override
	public Pagelet listByInterfaceId(String interfaceId) {
		//从线程变量中取出分页参数
		if(StringUtils.isEmpty(interfaceId)){
			return null;
		}

		Pagelet pagelet = (Pagelet)PaginationTheadLocal.getDefaultPagelet();
		int firstResult = pagelet.getStartOfPage().intValue();
	    int maxResults = pagelet.getPerPageSize().intValue();

		String hql = "select A from NetInformationEntity A where A.id  not in (select netInformationEntity.id from InterFacePermissionEntity where interFaceDetailEntity.id=?)";
		Query query = this.getSession().createQuery(hql).setString(0, interfaceId).setFirstResult(firstResult).setMaxResults(maxResults);


		pagelet.setDatas(query.list());
		pagelet.setTotalCount(this.countSize(hql, interfaceId));
		return pagelet;
	}

	@Override
	public List<NetInformationEntity> getByAppIdAndAppKey(String appId, String appKey) {
		String sql = "select * from  i_net_information  where (FLG_DELETED=0) and 1=1 and app_id=? and appkey=?";
		Query query = this.getSession().createSQLQuery(sql).setString(0, appId).setString(1,appKey);
		List<NetInformationEntity> netInformationEntityList = query.list();
		return netInformationEntityList;
	}

}
