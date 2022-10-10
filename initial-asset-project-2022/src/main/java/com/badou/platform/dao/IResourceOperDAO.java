package com.badou.platform.dao;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.platform.model.ResourceOperEntity;

/**
 * 资源操作记录实体dao接口
 * @ClassName IResourceOperDAO
 * @UpdateDate 2018年3月2日 上午10:13:49
 * @UpdateDesc (更新内容描述)
 * @version 1.0.0
 */
public interface IResourceOperDAO extends IBaseHibernateDAO<ResourceOperEntity, Serializable> {

	/**
	 * 根据云中心资源id获取数据
	 * @param platformResourceId
	 * @return 资源操作集合
	 * @author chenjiabao
	 * @param version3 
	 * @param version2 
	 * @param version1 
	 * @param versionCode 
	 * @date 2018年3月2日上午10:55:31
	 */
	List<ResourceOperEntity> findByPlatformResourceId(String platformResourceId, String versionCode, String version1, String version2, String version3);

	/**
	 * 根据模型id获取最后一次上传的资源操作记录
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年3月2日 下午2:12:19
	 * @Updator bduser18
	 * @UpdateDate 2018年3月2日 下午2:12:19
	 * @UpdateDesc (更新内容描述)
	 * @param moduleId
	 * @return 资源操作记录
	 * @throws Exception if has error(则无法查询到相关数据)
	 */
	ResourceOperEntity findLatestUpload(String moduleId) throws Exception;
}
