package com.bdc.api.intermanage.netinformation.dao;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;

public interface INetInformationDao extends IBaseHibernateDAO<NetInformationEntity, Serializable>  {

	/**
	 *
	 * @Description (查询接口可以添加权限的外网系统，除已有权限的外网系统)
	 * @author zzk
	 * @Date 2017年9月18日 上午10:39:10
	 * @Updator zzk
	 * @UpdateDate 2017年9月18日 上午10:39:10
	 * @UpdateDesc (更新内容描述)
	 * @param interfaceId
	 * @return
	 */
	Pagelet listByInterfaceId(String interfaceId);
	/**
	 * 根据APPID 与appkey查询应用
	 * @author lps
	 * @param appId
	 * @param appKey
	 */
	List<NetInformationEntity> getByAppIdAndAppKey(String appId, String appKey);

}
