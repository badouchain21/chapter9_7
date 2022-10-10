package com.bdc.assetdefine.dao.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.assetdefine.dao.IAssetDefinedDAO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.base.common.BaseAssetConst;
import com.bdc.common.utils.CommonUtils;
import lombok.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;


/**
 * 描述：资产建模定义 数据库实现
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:46:05
 */
@Repository
@SuppressWarnings("all")
public class AssetDefinedDAOImpl extends BaseHibernateDAO<AssetDefinedEntity, Serializable> implements IAssetDefinedDAO {

    @Override
    public AssetDefinedEntity findByAssetCode(@NonNull String assetCode) {
        String hql = "from AssetDefinedEntity where assetCode = ?";
        Query query = this.getSession().createQuery(hql);
        query.setParameter(0, assetCode);
        List<AssetDefinedEntity> list = query.list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }
    @Override
    public int countAssetBySql(String sql){
    	Query query = this.getSession().createSQLQuery(sql);
    	Object count = query.uniqueResult();
    	return count==null?0: CommonUtils.parseInt((BigDecimal)count);
    }

    @Override
    public List statAssetBySql(String sql){
    	Query query = this.getSession().createSQLQuery(sql);
    	List result = query.list();
    	return result;
    }

    @Override
    public Pagelet findByKeyword(JSONArray jsonArray, String keyword, LocalDateTime time,Pagelet pagelet) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT accetCode,assetTypeName,assetName,hash,uploadTime,id FROM (");
        jsonArray.forEach(e -> {
            JSONObject asset = (JSONObject) e;
            stringBuilder.append("SELECT asset_code AS assetCode,asset_name AS assetTypeName,`hash`,upload_time AS uploadTime,upload_status,ID AS id,");
            stringBuilder.append(asset.getString(BaseAssetConst.ASSET_KEY_TYPE_NAME));
            stringBuilder.append(" AS assetName ");
            stringBuilder.append("FROM ");
            stringBuilder.append(asset.getString("table"));
            if (StringUtils.isNotEmpty(keyword)){
                stringBuilder.append(" WHERE ");
                stringBuilder.append("((");
                stringBuilder.append(asset.getString(BaseAssetConst.ASSET_KEY_TYPE_NAME));
                stringBuilder.append(" LIKE '%"+keyword+"%')");
                stringBuilder.append(" OR ");
                stringBuilder.append("(asset_code like '%"+keyword+"%'))");
            }
            stringBuilder.append(" union all ");
        });
        String sqlString =stringBuilder.substring(0, stringBuilder.lastIndexOf("union all"));
        StringBuilder sqlFinal = new StringBuilder();
        sqlFinal.append(sqlString);
        sqlFinal.append(") as a WHERE a.upload_status =2 ");
        if (Objects.nonNull(time)){
            sqlFinal.append(" AND uploadTime >= '");
            sqlFinal.append(time.format(dateTimeFormatter));
            sqlFinal.append("' ");
        }
        sqlFinal.append(" ORDER BY uploadTime DESC");

        Query query = this.getSession().createSQLQuery(sqlFinal.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (pagelet.isUsePage()) {
            List list = query.list();
            Integer totalCount = list == null ? 0 : list.size();
            pagelet.setTotalCount(totalCount);
            final int firstResult = pagelet.getStartOfPage().intValue();
            final int maxResults = pagelet.getPerPageSize().intValue();
            if(maxResults>totalCount) {
                pagelet.setDatas(list);
            }else {
                query.setFirstResult(firstResult).setMaxResults(maxResults);
                pagelet.setDatas(query.list());
            }
        } else {
            List list = query.list();
            Integer totalCount = list == null ? 0 : list.size();
            pagelet.setTotalCount(totalCount);
            pagelet.setDatas(list);
        }
        return pagelet;
    }
}
