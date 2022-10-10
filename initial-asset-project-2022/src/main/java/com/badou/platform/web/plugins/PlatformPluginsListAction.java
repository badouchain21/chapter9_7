package com.badou.platform.web.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.designer.jdbc.core.vo.BaseVO;
import com.badou.platform.PlatformConst;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IResourceOperService;
import com.badou.plugins.install.service.IPluginsInstallService;
import com.badou.plugins.install.web.PluginsInstallListAction;

/**
 * 用于有云中心功能的插件列表展示action
 * @ClassName PlatformModuleDesignListAction
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author wjw
 * @Date 2018年3月2日 下午12:19:07
 * @Updator bduser18
 * @UpdateDate 2018年3月2日 下午12:19:07
 * @UpdateDesc (更新内容描述)
 * @version 1.0.0
 */
@Controller
public class PlatformPluginsListAction extends PluginsInstallListAction {

	private static final long serialVersionUID = -7581124566484666360L;

	@Autowired
	private IPluginsInstallService pluginsInstallService;
	
	@Autowired
	private IResourceOperService resourceOperService;
	
	@Override
	protected JSONObject convert(BaseVO t) throws Exception{
		JSONObject obj = super.getListForm((BaseVO)t);
		ResourceOperEntity resourceOper = resourceOperService.findLatestUpload(t.getId());
		if (resourceOper != null){
			obj.put("uploadState", DictionaryLib.getItemName(PlatformConst.DIC_UPLOAD_STATUS, resourceOper.getStatus()));
		} 
		return obj;
	}
}
