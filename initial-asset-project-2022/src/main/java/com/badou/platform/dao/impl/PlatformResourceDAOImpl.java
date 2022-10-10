package com.badou.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.platform.PlatformConst;
import com.badou.platform.dao.IPlatformResourceDAO;
import com.badou.platform.model.PlatformResourceEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:49:52
 * @todo 云中心资源dao层接口实现类
 */
@Repository
public class PlatformResourceDAOImpl extends BaseHibernateDAO<PlatformResourceEntity, Serializable> implements IPlatformResourceDAO {

	@Override
	public List<PlatformResourceEntity> findByResourceId(String resourceId, Integer type) {
		String hql = "from PlatformResourceEntity t where t.resourceId = ? and type = ?" ;
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, resourceId);
		query.setParameter(1, type);
		List<PlatformResourceEntity> list = query.list() ;
		return list;
	}

	@Override
	public PlatformResourceEntity findByplatformResourceId(String platformResourceId) {
		String hql = "from PlatformResourceEntity t where t.platformResourceId = ?" ;
		Query query = this.getSession().createQuery(hql);
		query.setParameter(0, platformResourceId);
		List<PlatformResourceEntity> list = query.list() ;
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void renameTable(String dbTable , String bakTableName) {
		String sql = "rename table " + dbTable + " to " + bakTableName;
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	@Override
	public void executeUpdateSql(String sql) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void copyDataToNewTable(String sql) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findNoExistCenterModule() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM (") // 最外层的select用于排序，使得查询出来的模型顺序与列表的顺序一致
				.append("SELECT")
				.append(" id,")
				.append(" code,")
				.append(" module_name AS moduleName,")
				.append(" null AS versionCode,")
				.append(" null AS version1,")
				.append(" null AS version2,")
				.append(" null AS version3,")
				.append(" 1 AS isNew, ")		// 这个select查出来的都是初次上传的模型
				.append(" db_table AS dbTable,") // 这个与下面的字段用于排序
				.append(" create_time AS createTime")
				.append(" FROM")
				.append(" md_module")
				.append(" WHERE flg_deleted = 0")
				.append(" AND id NOT IN")
				.append(" ( SELECT resource_id FROM platform_resource_link WHERE type = :type )")
				// 上面的SQL查询还没上传过的模型
				.append(" UNION ALL")
				// 下面的SQL查询修改过的模型
				.append(" SELECT")
				.append(" m.id,")
				.append(" m.code,")
				.append(" m.module_name AS moduleName,")
				.append(" p.version_code AS versionCode,")
				.append(" p.version1,")
				.append(" p.version2,")
				.append(" p.version3,")
				.append(" 0 AS isNew,")	// 这个select查询出来的不是初次上传的模型
				.append(" m.db_table AS dbTable,") // 这个与下面的字段用于排序
				.append(" m.create_time AS createTime")
				.append(" FROM md_module m")
				.append(" INNER JOIN platform_resource_link p")
				.append(" ON m.flg_deleted = 0")
				.append(" AND p.type = :type")
				.append(" AND m.id = p.resource_id")
				.append(" AND m.update_time > p.resource_modify_time")
				.append(" ) t")
				.append(" ORDER BY t.dbTable, t.createTime DESC")
				;
		return getSession().createSQLQuery(sql.toString())
				.addScalar("id", StandardBasicTypes.STRING)
				.addScalar("code", StandardBasicTypes.STRING)
				.addScalar("moduleName", StandardBasicTypes.STRING)
				.addScalar("versionCode", StandardBasicTypes.STRING)
				.addScalar("version1", StandardBasicTypes.STRING)
				.addScalar("version2", StandardBasicTypes.STRING)
				.addScalar("version3", StandardBasicTypes.STRING)
				.addScalar("isNew", StandardBasicTypes.INTEGER)
				.setParameter("type", PlatformConst.RESOUTCE_TYPE_MODULE)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.list();
	}

}
