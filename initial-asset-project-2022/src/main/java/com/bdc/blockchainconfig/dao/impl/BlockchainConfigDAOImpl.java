package com.bdc.blockchainconfig.dao.impl;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.bdc.blockchainconfig.dao.IBlockchainConfigDAO;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;

 
/**
 * @author badousoft
 * @date 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_configdao接口实现类
 **/
@Repository
public class BlockchainConfigDAOImpl extends BaseHibernateDAO<BlockchainConfigEntity, Serializable> implements IBlockchainConfigDAO {

}