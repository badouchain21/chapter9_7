package com.bdc.api.intermanage.tokenmanage.dao.impl;

import java.io.Serializable;

import com.bdc.api.intermanage.tokenmanage.dao.ITokenManageDao;
import com.bdc.api.intermanage.tokenmanage.model.TokenManageEntity;
import org.springframework.stereotype.Repository;


import com.badou.brms.base.support.hibernate.BaseHibernateDAO;

@Repository
public class TokenManageDaoImpl extends BaseHibernateDAO<TokenManageEntity, Serializable>
		implements ITokenManageDao {

}
