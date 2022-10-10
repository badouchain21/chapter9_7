package com.bdc.apple.dao.impl;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.bdc.apple.dao.IAppleDAO;
import com.bdc.apple.model.AppleEntity;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 *	苹果管理dao实现
 * @author zhongzhilong
 * @date 2020-11-04 11:34
 */
@Repository
public class AppleDAOImpl extends
		BaseHibernateDAO<AppleEntity, Serializable> implements IAppleDAO {

}



