package com.bdc.api.intermanage.interfacedetail.dao;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;


public interface IInterFaceDetailDao extends IBaseHibernateDAO<InterFaceDetailEntity, Serializable> {


	/**
	 *
	 * @Description (获取外网没有权限的接口分页列表)
	 * @author zzk
	 * @Date 2017年9月16日 下午2:12:35
	 * @Updator zzk
	 * @UpdateDate 2017年9月16日 下午2:12:35
	 * @UpdateDesc (更新内容描述)
	 * @param netId
	 * @return
	 */
	public Pagelet findOtherByNetId(String netId,String searchSql,Boolean isOther,String userId);

	/**
	 * 通过接口名称获得实体
	 * @param name 接口名
	 * @param userId 用户ID
	 * @return
	 */
	List<InterFaceDetailEntity> getEntityByName(String name,String userId);
}
