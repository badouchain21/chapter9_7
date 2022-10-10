package com.badou.designer.control.service;

import java.io.Serializable;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.designer.control.model.ContorlInstructionEntity;


/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:34:41
 * @todo 指令相关service抽象接口类
 */
public interface IContorlInstructionService extends IBaseSpringService<ContorlInstructionEntity, Serializable>{

	/**
	 * 根据关键词查找指令
	 * @param instructionStr
	 * @return 返回对应的指令实体
	 */
	ContorlInstructionEntity findInstruction(String instructionStr);

}
