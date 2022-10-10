package com.badou.designer.control.dao;

import java.io.Serializable;
import java.util.List;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.designer.control.model.ContorlInstructionEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:31:08
 * @todo 指令相关dao层接口抽象类
 */
public interface IContorlInstructionDAO extends IBaseHibernateDAO<ContorlInstructionEntity, Serializable>{

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:12:59
	 * @todo 根据关键词查找指令
	 * @param instructionStr 指令
	 * @return List<ContorlInstructionEntity> 指令集合
	 */
	List<ContorlInstructionEntity> findInstruction(String instructionStr);

}
