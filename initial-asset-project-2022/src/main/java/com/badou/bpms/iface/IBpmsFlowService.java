package com.badou.bpms.iface;

import java.util.List;

import com.badou.bpms.beans.FlowBean;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:41:12
 * @todo 流程相关接口类
 */
public interface IBpmsFlowService {
	/**
	 * 根据业务类型，获得流程
	 * <p>如果业务类型下有多个流程，只返回创建时间最新的一个
	 * @param busiType 业务类型
	 * @return 流程对象
	 */
	public FlowBean getFlow(String busiType);
	
	/**
	 * 根据业务类型，获得流程
	 * @param busiType 业务类型
	 * @return 流程对象集合
	 */
	public List<FlowBean> getFlows(String busiType);
	/**
	 * 根据业务对象Id删除流程实例
	 * @param boId
	 */
	public void deleteFlowInstance(String boId);
}
