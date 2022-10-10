package com.badou.project.common;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import com.badou.brms.org.service.IEmployeeService;
import com.badou.brms.quartz.jobbean.worker.AbstractJboWorker;
import com.badou.brms.util.InstanceFactory;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:22:00
 * @todo 定时解冻账号任务
 */
@Component("removeFreezeJobWorker")
public class RemoveFreezeJobWorker extends AbstractJboWorker{
	
	private static final long serialVersionUID = 179307680651851740L;
	
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext context, String templateId) {
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
