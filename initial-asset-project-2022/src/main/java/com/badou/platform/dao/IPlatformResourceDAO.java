package com.badou.platform.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.platform.model.PlatformResourceEntity;

/**
 * 云中心资源表dao接口
 * @ClassName IPlatformResourceDAO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @version 1.0.0
 */
public interface IPlatformResourceDAO extends IBaseHibernateDAO<PlatformResourceEntity, Serializable> {

	/**
	 * 根据资源id获取相应实体类
	 * @param resourceId  资源id
	 * @param type
	 * @author chenjiabao
	 * @return 平台资源实体集合
	 */
	List<PlatformResourceEntity> findByResourceId(String resourceId, Integer type);

	/**
	 * 根据云中心资源id获取平台资源
	 * @param platformResourceId
	 * @return 平台资源实体
	 */
	PlatformResourceEntity findByplatformResourceId(String platformResourceId);

	/**
	 * 将原有的模型对应表名变为备份表
	 * @param dbTable
	 * @author chenjiabao
	 * @param bakTableName 
	 * @date 2018年3月2日下午3:44:16
	 */
	void renameTable(String dbTable, String bakTableName);

	/**
	 * 从备份表中复制数据到新表
	 * @param string
	 * @author chenjiabao
	 * @date 2018年3月2日下午5:20:29
	 */
	void copyDataToNewTable(String sql);

	/**
	 * 执行sql
	 * @param sql
	 */
	void executeUpdateSql(String sql);

	/**
	 * 查询存在于本地，但不存在与云中心的模型
	 * @return 需要上传的模型，列表元素结构：{"id": String, "is_new": Integer} 
	 * 	       id为模型id，is_new是否为初次上传，1是，0否。
	 * @author wujunliang
	 */
	List<Map<String, Object>> findNoExistCenterModule();
}
