package com.bdc.api.intermanage.interfacepermission.web.form;

import com.badou.brms.dictionary.DictionaryLib;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import lombok.Getter;
import lombok.Setter;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;

import java.util.Dictionary;

@Getter
@Setter
public class InterFacePermissionForm extends BaseStrutsEntityForm<InterFacePermissionEntity>{

	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -1776371715117922687L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 接口详情
	 */
	/**
	 * 接口Id
	 */
	private String interfaceId;
	/**
	 * 接口名称
	 */
	private String name;

	/**
	 * 接口使用状态
	 */
	private Integer status;
	/**
	 * 接口描述
	 */
	private String interfaceDetail;
	/**
	 * 接口交互类型
	 */
	private Integer type;

	/**
	 * 接口创建人
	 */
	private String  interCreator;

	/**
	 * 接口所属系统
	 */
	private String belong_sys;

	/**
	 * 系统详情
	 */
	/**
	 * 外网信息Id
	 */
	private String netInformationId;
	/**
	 * appId
	 */
	private String appId;
	/**
	 * appKey
	 */
	private String appkey;
	/**
	 * 是否能访问接口
	 */
	private Integer IsCallInterface;
	/**
	 * 外网创建人
	 */
	private String sysCreator;
	/**
	 * 部门编号
	 */
	private String depanum;
	/**
	 * 接口访问权限
	 */
	private String interfacePermission;

	/**
	 * 企业名称
	 */
	private String companyName;

	@Override
	public void setEntityToFormProperties(InterFacePermissionEntity instance) {
		InterFaceDetailEntity interFaceDetailEntity = instance.getInterFaceDetailEntity();
		NetInformationEntity netInformationEntity = instance.getNetInformationEntity();
		if(interFaceDetailEntity!=null){
			this.setInterfaceId(interFaceDetailEntity.getId());
			this.setName(interFaceDetailEntity.getName());
			this.setStatus(interFaceDetailEntity.getStatus());
			this.setInterfaceDetail(interFaceDetailEntity.getInterfaceDetail());
			this.setType(interFaceDetailEntity.getType());
			this.setInterCreator(interFaceDetailEntity.getCreatorName());
			this.setBelong_sys(interFaceDetailEntity.getBelong_sys());
		}
		if(netInformationEntity!=null){
			this.setNetInformationId(netInformationEntity.getId());
			this.setAppId(netInformationEntity.getAppId());
			this.setAppkey(netInformationEntity.getAppkey());
			this.setIsCallInterface(netInformationEntity.getIsCallInterface());
			this.setDepanum(netInformationEntity.getDepanum());
			this.setSysCreator(netInformationEntity.getCreator());
			this.setCompanyName(netInformationEntity.getCompanyName());
		}
		this.setId(instance.getId());
		this.setInterfacePermission(DictionaryLib.getItemNameByItemCode("INTERFACE_PERMISSION",instance.getInterfacePermission().toString()));
	}

	@Override
	public void setFormToEntityProperties(InterFacePermissionEntity instance) {
		InterFaceDetailEntity interFaceDetailEntity = new InterFaceDetailEntity();
		NetInformationEntity netInformationEntity = new NetInformationEntity();

		interFaceDetailEntity.setId(this.getInterfaceId());
		interFaceDetailEntity.setName(this.getName());
		interFaceDetailEntity.setStatus(this.getStatus());
		interFaceDetailEntity.setInterfaceDetail(this.getInterfaceDetail());
		interFaceDetailEntity.setType(this.getType());
		interFaceDetailEntity.setBelong_sys(this.getBelong_sys());

		netInformationEntity.setAppId(this.getAppId());
		netInformationEntity.setAppkey(this.getAppkey());
		netInformationEntity.setIsCallInterface(this.getIsCallInterface());
		netInformationEntity.setDepanum(this.getDepanum());
		netInformationEntity.setId(this.getNetInformationId());

		instance.setId(this.getId());
//		instance.setInterfacePermission(this.getInterfacePermission());
		instance.setInterFaceDetailEntity(interFaceDetailEntity);
		instance.setNetInformationEntity(netInformationEntity);
	}

}
