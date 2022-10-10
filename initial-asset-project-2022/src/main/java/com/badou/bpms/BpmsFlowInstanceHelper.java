package com.badou.bpms;

import com.badou.bpms.beans.instance.NodeInstanceBean;
import com.badou.bpms.iface.IBpmsFlowInstanceService;
import com.badou.bpms.iface.IBpmsProcessService;
import com.badou.brms.util.InstanceFactory;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:40:42
 * @todo 流程实例相关帮助类
 */
public class BpmsFlowInstanceHelper {
	/**
	 * 流程实例删除
	 * @param busiObjectId
	 */
	public static void deleteFlowInstance(String busiObjectId){
		IBpmsFlowInstanceService service = InstanceFactory.getInstance(IBpmsFlowInstanceService.class);
		service.deleteFlowInstance(busiObjectId);
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:44:07
	 * @todo 转移待办到指定环节
	 * @param busiObjectId 业务对象ID
	 * @param srcNodeInstanceId 环节id
	 * @param targetNodeInstanceId 目标环节实例id
	 */
	public static void transfer(String busiObjectId,String srcNodeInstanceId, String targetNodeInstanceId){
		IBpmsProcessService service = InstanceFactory.getInstance(IBpmsProcessService.class);
		service.transfer(busiObjectId,srcNodeInstanceId, targetNodeInstanceId);
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:44:59
	 * @todo 是否目标环节
	 * @param boId 流程id
	 * @param nodeInstanceId 环节实例id
	 * @param nodeCode 环节code
	 * @return 是或否
	 */
	public static boolean isTargetNode(String boId, String nodeInstanceId, String nodeCode){
		IBpmsFlowInstanceService service = InstanceFactory.getInstance(IBpmsFlowInstanceService.class);
		return service.isCompareNode(boId, nodeInstanceId, nodeCode);
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:45:49
	 * @todo 转移待办到指定的环节
	 * @param busiObjectId 业务对象ID
	 * @param srcNodeInstanceId 环节id
	 * @param targetNodeCode 环节编码
	 */
	public static void rehandle(String busiObjectId,String srcNodeInstanceId, String targetNodeCode){
		IBpmsProcessService service = InstanceFactory.getInstance(IBpmsProcessService.class);
		//获得流程中的唯一修订环节，若有多个时，则要用上面的指定环节了
		IBpmsFlowInstanceService bpmsFlowInstanceService = InstanceFactory.getInstance(IBpmsFlowInstanceService.class);
		NodeInstanceBean nodeInstance = bpmsFlowInstanceService.getNodeInstance(busiObjectId, targetNodeCode);
		service.transfer(busiObjectId,srcNodeInstanceId, nodeInstance.getId());
	}
}
