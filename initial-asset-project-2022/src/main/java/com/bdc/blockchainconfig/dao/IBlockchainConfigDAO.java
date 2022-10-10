package com.bdc.blockchainconfig.dao;

import java.io.Serializable;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;

/**
 * @author badousoft
 * @created 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_configdao接口
 */
public interface IBlockchainConfigDAO extends IBaseHibernateDAO<BlockchainConfigEntity, Serializable> {
	
}
