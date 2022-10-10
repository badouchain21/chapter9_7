package com.bdc.api.intermanage.interfacedetail.service;

import java.io.Serializable;

import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import lombok.NonNull;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.IBaseSpringService;


public interface IInterFaceDetailService extends IBaseSpringService<InterFaceDetailEntity, Serializable>{

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
	public Pagelet findOtherByNetId(String netId,String searchHql,Boolean isOther);

	/**
	 * 通过模型初始化接口
	 */
	public void initInterFaceDetail(String id) throws Exception;
}
