package com.badou.brms.cfg.web;

import java.util.List;

import com.badou.brms.cfg.service.IBaseDefineService;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.global.SpringUtil;
import com.badou.designer.jdbc.common.util.IModuleEvents;
import com.badou.designer.jdbc.core.vo.BaseVO;

public class BaseDefineModuleEvent implements IModuleEvents{

	@Override
	public void saveAfter(List<String> ids, List<BaseVO> voList) throws Exception {
		IBaseDefineService baseDefineService = InstanceFactory.getInstance(IBaseDefineService.class);
		for (String id : ids) {
			baseDefineService.saveAfter(baseDefineService.get(id));
		}
	}
}
