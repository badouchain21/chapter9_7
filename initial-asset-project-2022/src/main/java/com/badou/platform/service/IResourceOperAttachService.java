package com.badou.platform.service;

import java.io.Serializable;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.platform.model.ResourceOperAttachEntity;

/**
 * 资源备份实体类service接口
 * @ClassName IResourceOperAttachService
 * @Date 2018年3月2日 上午10:26:45
 * @version 1.0.0
 */
public interface IResourceOperAttachService extends IBaseSpringService<ResourceOperAttachEntity, Serializable> {

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:24:56
	 * @todo 根据操作id获取对应实体
	 * @param id
	 * @return 资源操作记录相关附件实体
	 */
	ResourceOperAttachEntity findByOperId(String id);

}
