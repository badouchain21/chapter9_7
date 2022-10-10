package com.badou.project.moduledemo.web.fun3;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.badou.brms.attach.model.Attach;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.struts.struts2.JsonSaveTemplateAction;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.service.IFun1DemoChildService;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoForm;
/**
 * 功能1示例保存新增事件
 * <p>对应的请求路径是：${context}/moduledemo/Fun2/Fun2demosave/save.do
 * @author xiangsf 2013-1-18
 *
 */
@RestController
public class Fun3DemoSaveAction extends JsonSaveTemplateAction<Fun3DemoEntity, Serializable, Fun3DemoForm> {

	@Autowired
	private IFun3DemoService fun3DemoService;
	
	@Autowired 
	private IFun1DemoChildService fun1DemoChildService;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入service
	 * @param fun3DemoService
	 */
	@Autowired
	public void setFun3DemoService(IFun3DemoService fun3DemoService) {
		this.fun3DemoService = fun3DemoService;
		super.setBaseService(fun3DemoService);
	}
	

	File[] attach;
	String[] attachFileName;
	String[]  taxName;
	
	public String[] getTaxName() {
		return taxName;
	}
	public void setTaxNames(String[] taxName) {
		this.taxName = taxName;
	}

	public File[] getAttach() {
		return attach;
	}

	public void setAttach(File[] attach) {
		this.attach = attach;
	}

	public String[] getAttachFileName() {
		return attachFileName;
	}

	public void setAttachFileName(String[] attachFileName) {
		this.attachFileName = attachFileName;
	}
	
	@Override
	public JsonReturnBean save() {
		try{
			returnBean = new JsonReturnBean();
			
			String imgid=request.getParameter("type1");
			String pid = com.badou.tools.common.util.ParamUtils.getParameter(request, "pid");
			String id = com.badou.tools.common.util.ParamUtils.getParameter(request, "id");
			List<Attach> attachList = null;
			if(attach!= null && attach.length!= 0){
				attachList = new ArrayList<Attach>();
				for(int i=0; i<attach.length; i++) {
					attachList.add(this.getAttach(attach[i], attachFileName[i]));
				}
			}
			custForm=new Fun3DemoForm();
			super.setCustFormProperties();
			Fun3DemoEntity fun3Demo= null;
			if(StringUtils.isNotBlank(id)){
				fun3Demo = this.fun3DemoService.find(id);
			}else{
				fun3Demo = new Fun3DemoEntity();
				if(StringUtils.isNotBlank(pid)){
					Fun3DemoEntity parent = this.fun3DemoService.find(pid);
					fun3Demo.setParent(parent);
				}
			}
				
			custForm.setFormToEntityProperties(fun3Demo);
			fun3DemoService.saveFun3Demo(fun3Demo, attachList, imgid);
			returnBean.setHasOk(true);
			returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
			returnBean.setBean(custForm);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			returnBean.setHasOk(false);
			returnBean.setTip(JsonReturnBean.TIP_FAIL);
			returnBean.setMessage(e.getMessage());
		}
		return returnBean;
	}
	

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:27
	 * @todo 获取附件
	 * @param file
	 * @param fileName
	 * @return 附件实体
	 * @throws Exception if has error(包装好错误信息给前台提示用户)
	 */
	private Attach getAttach(File file,String fileName) throws Exception{
		byte[] content = null;
		Attach attach = new Attach();
		try(InputStream is = new FileInputStream(file)) {
			content = new byte[is.available()];
			is.read(content);
			LogonCertificate logon = LogonCertificateHolder.getLogonCertificate();
			String suffix = fileName.substring(fileName.indexOf("."));
			attach.setName(fileName);
			attach.setFileContent(content);
			attach.setFileSize(content.length);
			attach.setExtendName(suffix);
			attach.setGenPersonId(logon.getUserId());
			attach.setGenPersonName(logon.getLoginId());
			return attach;
		}
	}
	
	
}
	
