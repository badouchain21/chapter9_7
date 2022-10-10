package com.bdc.scheduling;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.badou.brms.cfg.syscache.SystemCacheContainer;
import com.badou.brms.dictionary.DictionaryCacheContainer;
import com.badou.brms.system.container.InterfaceRegisterContainer;
import com.badou.brms.system.security.UserRoleResourceCacheContainer;
import com.badou.logs.syslog.performace.config.ConfigFactory;
import com.badou.uniapp.cache.AppUserRoleResourceCacheContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.context.WebApplicationContext;

/**
 * 描述：定时器启动类
 *
 * @author lps
 */
@Configuration
@EnableScheduling
@Order(value=100)
public abstract class Scheduler  implements SchedulingConfigurer {

    /**
     * config about if start schedule
     */
	@Value("${scheduling.enabled}")
	private Boolean isStart=false;

    /**
     * the corn string
     */
	private String corn;


    /**
     *
     * THREADPOOL_INIT_COUNT
     */
    private static final int THREADPOOL_INIT_COUNT=10;
    /**
     * THREADPOOL_MAX_COUNT
     */
    private static final int THREADPOOL_MAX_COUNT=100;



    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        if(!isStart)
            return;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(THREADPOOL_INIT_COUNT);
        executor.setMaximumPoolSize(THREADPOOL_MAX_COUNT);
        scheduledTaskRegistrar.setScheduler(executor);
        scheduledTaskRegistrar.addTriggerTask(
                //执行定时任务
                () -> {
                    processTask();
                },
                //设置触发器
                triggerContext -> {
                    CronTrigger trigger = new CronTrigger(getCron());
                    return trigger.nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * @brief 任务的处理函数
     * 本函数需要由派生类根据业务逻辑来实现
     */
    protected abstract void processTask();

    /**
     * @return String
     * @brief 获取定时任务周期表达式
     * 本函数由派生类实现，从配置文件，数据库等方式获取参数值
     */
    protected abstract String getCron();

}
