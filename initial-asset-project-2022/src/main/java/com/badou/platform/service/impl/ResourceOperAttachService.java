package com.badou.platform.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.platform.dao.IResourceOperAttachDAO;
import com.badou.platform.model.ResourceOperAttachEntity;
import com.badou.platform.service.IResourceOperAttachService;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:16:04
 * @todo 资源附件操作service接口实现类
 */
@Service
public class ResourceOperAttachService extends BaseSpringService<ResourceOperAttachEntity, Serializable> implements IResourceOperAttachService {

	@Autowired
	private IResourceOperAttachDAO resourceOperAttachDAO;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:32:15
	 * @todo 设置默认使用的dao对象
	 * @param resourceOperAttachDAO
	 */
	@Autowired
	public void setResourceOperAttachDAO(IResourceOperAttachDAO resourceOperAttachDAO) {
		this.resourceOperAttachDAO = resourceOperAttachDAO;
		super.setBaseDAO(resourceOperAttachDAO);
	}
	@Override
	public ResourceOperAttachEntity findByOperId(String id) {
		List<ResourceOperAttachEntity> list = this.resourceOperAttachDAO.findByOperId(id);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}
	
}
