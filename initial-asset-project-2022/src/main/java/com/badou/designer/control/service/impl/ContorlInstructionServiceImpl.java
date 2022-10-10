package com.badou.designer.control.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.designer.control.dao.IContorlInstructionDAO;
import com.badou.designer.control.model.ContorlInstructionEntity;
import com.badou.designer.control.service.IContorlInstructionService;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:35:09
 * @todo 指令相关service实现类
 */
@Service
public class ContorlInstructionServiceImpl extends
BaseSpringService<ContorlInstructionEntity, Serializable> implements IContorlInstructionService{

	@Autowired
	private IContorlInstructionDAO instructionDAO ;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:35:17
	 * @todo 注入对应的dao服务
	 * @param instructionDAO 该模块对应的dao
	 */
	@Autowired
	public void setInstructionDAO(IContorlInstructionDAO instructionDAO) {
		this.instructionDAO = instructionDAO;
		super.baseDAO = instructionDAO ;
	}

	@Override
	public ContorlInstructionEntity findInstruction(String instructionStr) {
		List<ContorlInstructionEntity> list = instructionDAO.findInstruction(instructionStr);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	
}
