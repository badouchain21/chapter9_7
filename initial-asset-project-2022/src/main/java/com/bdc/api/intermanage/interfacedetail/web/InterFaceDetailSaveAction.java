package com.bdc.api.intermanage.interfacedetail.web;

import java.io.Serializable;
import java.util.Objects;

import com.badou.designer.jdbc.common.web.BaseCommonSaveAction;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.api.intermanage.interfacedetail.web.form.InterFaceDetailForm;
import com.bdc.api.rest.container.InterfaceDetailContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.brms.base.support.struts.struts2.JsonListTemplateAction;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/interfaceDetailSave")
public class InterFaceDetailSaveAction extends BaseCommonSaveAction {

	@Autowired
	private IInterFaceDetailService interFaceDetailService;

	@Override
	protected void exeBeforeSave() throws Exception {
		if (!this.create){
			InterFaceDetailEntity interFaceDetailEntity = interFaceDetailService.get(this.custForm.getId());
			String name = this.custForm.getFieldValue("name");
			if (!Objects.equals(interFaceDetailEntity.getName(),name)){
				throw new Exception("接口名称不可以修改");
			}
		}
	}

	@Override
	protected void exeAfterSave() throws Exception {
		if (!this.create){
			InterFaceDetailEntity interFaceDetailEntity = interFaceDetailService.get(this.custForm.getId());
			InterfaceDetailContainer.getInstance().put(interFaceDetailEntity);
		}
	}
}
