package com.bdc.api.intermanage.netinformation.service.impl;

import java.io.Serializable;
import java.util.List;

import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.netinformation.dao.INetInformationDao;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.tools.common.Globals;

@Service
public class NetInformationServiceImpl extends BaseSpringService<NetInformationEntity, Serializable>
		implements INetInformationService {

	@Autowired
	private INetInformationDao netInformationDao;
	@Autowired
	public void setNetInformationDao(INetInformationDao netInformationDao) {
		this.netInformationDao = netInformationDao;
		this.setBaseDAO(netInformationDao);
	}
	@Override
	public Pagelet listByInterfaceId(String interfaceId) {
		return netInformationDao.listByInterfaceId(interfaceId);
	}
	@Override
	public String updateCallInterface(String ids) throws Exception {
		String[] idArr = ids.split(Globals.SEPARATOR_COMMA);
		StringBuilder builder = new StringBuilder();
		for (String id : idArr) {
			try{
				NetInformationEntity entity =  netInformationDao.find(id);
			if(entity.getIsCallInterface() == Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.IS_CALL_INTERFACE, "0"))){
				entity.setIsCallInterface(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.IS_CALL_INTERFACE, "1")));
			}else{
				entity.setIsCallInterface(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.IS_CALL_INTERFACE, "0")));
			}
			netInformationDao.update(entity);
			}catch(Exception e){
				builder.append(e.getMessage());
			}
		}
		return builder.toString();
	}

	@Override
	public List<NetInformationEntity> getByAppIdAndAppKey(String appId, String appKey) {
		return netInformationDao.getByAppIdAndAppKey(appId,appKey);
	}

}
