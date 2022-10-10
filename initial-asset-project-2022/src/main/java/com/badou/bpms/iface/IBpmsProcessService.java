package com.badou.bpms.iface;

import com.badou.bpms.engine.BusinessObject;

 
/**
 * 流程实例服务
 * @author xiangsf 2013-3-29
 *
 */
public interface IBpmsProcessService {
	/**
	 * 流程发起
	 * @param flowId 流程id
	 * @param bo 实例对象
	 * @return true/false
	 * @throws Exception if has error(则回滚事务)
	 */
	public boolean start(String flowId, BusinessObject bo) throws Exception;
	
	/**
	 * 转移待办到指定环节
	 * @param busiObjectId
	 * @param nodeId
	 * @param targetNodeInstanceId
	 * @return true/false
	 */
	public boolean transfer(String busiObjectId,String srcNodeInstanceId, String targetNodeInstanceId);
}
