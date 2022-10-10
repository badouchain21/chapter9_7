package com.badou.platform.dao;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.platform.model.ResourceOperAttachEntity;

/**
 * 资源操作记录相关附件实体dao接口
 * @ClassName IResourceOperAttachDAO
 * @Date 2018年3月2日 上午10:25:26
 * @version 1.0.0
 */
public interface IResourceOperAttachDAO extends IBaseHibernateDAO<ResourceOperAttachEntity, Serializable> {

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:23:34
	 * @todo 根据操作id获取对应实体
	 * @param id
	 * @return 资源操作实体
	 */
	List<ResourceOperAttachEntity> findByOperId(String id);

}
