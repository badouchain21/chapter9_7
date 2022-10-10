package com.bdc.blockchainconfig.service.impl;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.tools.common.Globals;
import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;

import com.bdc.blockchainconfig.dao.IBlockchainConfigDAO;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.blockchainconfig.service.IBlockchainConfigService;


/**
 * @author badousoft
 * @date 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_config Service接口实现类
 **/
@Service
public class BlockchainConfigServiceImpl extends BaseSpringService<BlockchainConfigEntity, Serializable> implements IBlockchainConfigService {

	@Autowired
	private IBlockchainConfigDAO blockchainConfigDAO;

	@Autowired
	public void setBlockchainConfigDAO(IBlockchainConfigDAO blockchainConfigDAO) {
		this.blockchainConfigDAO = blockchainConfigDAO;
		super.setBaseDAO(blockchainConfigDAO);
	}
	@Override
	public BlockchainConfigEntity findByConfigName(@NonNull String configName) throws Exception {
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("configName",configName,null, QueryOperSymbolEnum.eq));

		List<BlockchainConfigEntity> configs = blockchainConfigDAO.find(queryCriterion);
		if (configs!=null && configs.size()>0) {
			return configs.get(0);
		}
		return null;
	}

	@Override
	public String updateConfigStatus(String ids) {
		StringBuilder builder = new StringBuilder();
		try {
			for (String id : ids.split(Globals.SEPARATOR_COMMA)) {
				BlockchainConfigEntity entity =  blockchainConfigDAO.get(id);
				if(entity.getStatus() == Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "0"))){
					entity.setStatus(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "1")));
				}else{
					entity.setStatus(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.INTERFACE_PERMISSION, "0")));
				}
				blockchainConfigDAO.update(entity);
			}
		} catch (Exception e) {
			builder.append(e.getMessage() + Globals.SEPARATOR_WELLNO);
		}
		return builder.toString();
	}

}

