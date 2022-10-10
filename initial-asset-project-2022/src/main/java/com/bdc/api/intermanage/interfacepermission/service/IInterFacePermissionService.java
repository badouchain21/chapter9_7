package com.bdc.api.intermanage.interfacepermission.service;

import java.io.Serializable;


import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.brms.dboperation.query.ICriterion;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;


public interface IInterFacePermissionService extends IBaseSpringService<InterFacePermissionEntity, Serializable>{


	public void delByInterAndNet(String interfaceId, String netInformationId);

	/**
	 * 修改权限表中访问接口的权限
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	String updateInterfacaPermission(String ids) throws Exception;

	/**
	 * 初始化一个企业的权限表
	 * @param netId
	 */
	void initFacePermission(String netId);

	/**
	 *
	 * 描述：初始化一个接口的权限表
	 * @author linxiaoqing
	 * @date 2019年8月29日
	 * @param interFaceDetailEntity
	 */
    void initFacePermission(InterFaceDetailEntity interFaceDetailEntity);

    /**
     *
     * 描述：分页列表
     * @author linxiaoqing
     * @date 2019年8月30日
     * @param criterion
     * @return
     */
    Pagelet findPages(ICriterion criterion);
}
