package com.bdc.api.intermanage.netinformation.service;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.IBaseSpringService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;

public interface INetInformationService extends IBaseSpringService<NetInformationEntity, Serializable>{

	/**
	 *
	 * @Description (查询接口可以添加权限的外网系统，除已有权限的外网系统)
	 * @author zzk
	 * @Date 2017年9月18日 上午10:36:50
	 * @Updator zzk
	 * @UpdateDate 2017年9月18日 上午10:36:50
	 * @UpdateDesc (更新内容描述)
	 * @param interfaceId
	 * @return
	 */
	Pagelet listByInterfaceId(String interfaceId);

	/**
	 * 修改访问接口的权限
	 * @author lxq
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	String updateCallInterface(String ids) throws Exception;

	/**
	 * 根据APPID 与appkey查询应用
	 */
	List<NetInformationEntity> getByAppIdAndAppKey(String appId, String appKey);
}
