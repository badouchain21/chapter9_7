package com.badou.bpms.iface;

import java.util.List;

import com.badou.bpms.beans.instance.NodeInstanceBean;
import com.badou.bpms.beans.instance.RouteInstanceBean;

/**
 * 流程实例服务
 * @author xiangsf 2013-3-29
 *
 */
public interface IBpmsFlowInstanceService {
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:46:55
	 * @todo 获得指定待办的路由信息
	 * @param worklistId 代办id
	 * @return 路由实例集合
	 */
	public List<RouteInstanceBean> getWorklistRoutes(String worklistId);
	/**
	 * 根据指定待办获得环节实例
	 * @param worklistId
	 * @return 环节实例
	 */
	public NodeInstanceBean getNodeInstance(String worklistId);
	/**
	 * 获得指定业务对象在流程中指定环节编码的环节实例
	 * @param boId 业务对象id
	 * @param nodeCode 环节编码
	 * @return 环节实例
	 */
	public NodeInstanceBean getNodeInstance(String boId, String nodeCode);
	/**
	 * 判断指定环节实例ID是否某个指定的环节
	 * @param boId 业务对象ID
	 * @param nodeInstanceId 环节实例ID
	 * @param nodeCode 指定的某个特殊环节编码
	 * @return 是/否
	 */
	public boolean isCompareNode(String boId, String nodeInstanceId, String nodeCode);
	/**
	 * 删除流程实例
	 * @param busiObjectId 对象实例id
	 */
	public void deleteFlowInstance(String busiObjectId) ;
}
