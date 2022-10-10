package com.badou.designer.control.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.designer.control.dao.IContorlInstructionDAO;
import com.badou.designer.control.model.ContorlInstructionEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:31:35
 * @todo 指令相关dao层接口实现类
 */
@Repository
public class ContorlInstructionDAOImpl extends
BaseHibernateDAO<ContorlInstructionEntity, Serializable> implements IContorlInstructionDAO{

	@Override
	public List<ContorlInstructionEntity> findInstruction(String instructionStr) {
		StringBuilder hql = new StringBuilder();
		hql.append("from ContorlInstructionEntity t where t.name = '" + instructionStr + "'");
		Query query = this.getSession().createQuery(hql.toString());
		List<ContorlInstructionEntity> list = query.list();
		return list;
	}

}
