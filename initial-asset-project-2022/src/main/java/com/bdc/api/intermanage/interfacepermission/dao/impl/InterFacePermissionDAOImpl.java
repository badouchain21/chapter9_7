package com.bdc.api.intermanage.interfacepermission.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bdc.api.intermanage.interfacepermission.dao.IInterFacePermissionDAO;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.dboperation.query.ICriterion;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOrderby;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.QueryParamGroup;
import com.badou.core.standard.enums.SystemBoolean;


@Repository
public class InterFacePermissionDAOImpl  extends BaseHibernateDAO<InterFacePermissionEntity, Serializable>
				implements IInterFacePermissionDAO {

	@Override
	public void delByInterAndNet(String interfaceId, String netInformationId) {
		StringBuffer hql = new StringBuffer();
		if(interfaceId!=null || netInformationId!=null){
			hql.append("update InterFacePermissionEntity set flgDeleted = " + SystemBoolean.YES.getKey() + " where 1=1");
				if(interfaceId!=null){
					hql.append(" and interFaceDetailEntity.id='"+interfaceId+"'");
				}
				if(netInformationId!=null){
					hql.append(" and netInformationEntity.id='"+netInformationId+"'");
				}
			}
			Query query = this.getSession().createQuery(hql.toString());
			query.executeUpdate();
	}

    @Override
	public Pagelet findPages(ICriterion criterion){
        if(criterion != null && criterion instanceof QueryCriterion){
            QueryCriterion queryCriterion = null;
            StringBuilder hql = null;
            List<Object> objs = null;
            try{
                 queryCriterion = (QueryCriterion)criterion;
                Class<InterFacePermissionEntity> _clazz = getGenericType();
                 hql = new StringBuilder().append("from ").append(_clazz.getName()).append(" t where 1=1 ");
                 objs = new ArrayList<Object>();
                 for(QueryParamGroup g : queryCriterion.getQueryParamsGroup() ){
                     hql.append(" and (");
                     int i = 0;
                     for(QueryParam p : g.getParams()){
                         if(i++ > 0) hql.append(g.getOp().getName()).append(" ");
                        hql.append(p.toQueryString()).append(" ");
                        p.addParamValuesTo(objs);
                     }
                     hql.append(") ");
                 }
                for(QueryParam p : queryCriterion.getQueryParams()){
                    hql.append(" and ").append(p.toQueryString());
                    p.addParamValuesTo(objs);
                }
                if(queryCriterion.getQueryOrderbys() == null || queryCriterion.getQueryOrderbys().isEmpty()){
                    /*com.badou.core.globals.annotation.DefaultOrderBy orderby = _clazz.getAnnotation(com.badou.core.globals.annotation.DefaultOrderBy.class);
                    if(orderby != null){
                        hql.append( " order by ").append(orderby.clause());
                    }*/
                }else{
                    hql.append( " order by ");
                    int i = queryCriterion.getQueryOrderbys().size() - 1;
                    for(QueryOrderby o : queryCriterion.getQueryOrderbys()){
                        hql.append(o.toQueryString());
                        if(i > 0) hql.append(",");
                        i--;
                    }
                }
                return super.queryPagedResult(hql.toString(), objs.toArray(new Object[]{}));
            }finally{
                 queryCriterion = null;
                 hql = null;
                 objs = null;
            }

        }
        return this.findPages();
    }

}
