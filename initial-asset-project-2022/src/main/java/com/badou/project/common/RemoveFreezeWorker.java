package com.badou.project.common;


import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.badou.brms.org.service.IEmployeeService;
import com.badou.brms.util.InstanceFactory;
/**
 * 定时将因连续失败被冻结的帐号重新启用
 * @author cjb
 * update by wjw 2018-10-23 已改成使用RemoveFreezeJobWorker
 */
@Deprecated
public class RemoveFreezeWorker implements Job{

	protected final Logger log = Logger.getLogger(getClass());
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		long stime = System.currentTimeMillis();
		try {
			log.info("定时解冻帐号任务开始....");
			IEmployeeService employeeService = InstanceFactory.getInstance(IEmployeeService.class);
			employeeService.removePwdErrorFreeze();
			//查找没有匹配的所有数据
		} catch (Exception e) {
			log.error(e);
		}
		log.info("定时解冻帐号任务结束....耗时ms:"
				+ (System.currentTimeMillis() - stime));
	}

}
