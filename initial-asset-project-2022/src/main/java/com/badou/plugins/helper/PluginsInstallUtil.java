package com.badou.plugins.helper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.badou.core.plugin.PluginsErrorEnum;
import com.badou.core.plugin.PluginsTypeEnum;
import com.badou.core.plugin.agent.IPluginsAgent;
import com.badou.core.plugin.builder.basic.IPluginOperation;
import com.badou.core.plugin.builder.basic.Plugin;
import com.badou.core.plugin.util.PluginsScanUtils;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.plugins.file.model.PluginsFileEntity;
import com.badou.plugins.file.service.IPluginsFileService;
import com.badou.plugins.log.model.PluginsLogEntity;
import com.badou.plugins.log.service.IPluginsLogService;
import com.badou.plugins.msg.model.PluginsMsgEntity;
import com.badou.plugins.msg.service.IPluginsMsgService;
import com.badou.tools.common.util.SpringHelper;
import com.badou.tools.common.util.bean.BeanUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:30:52
 * @todo 插件安装工具类
 */
public class PluginsInstallUtil implements IPluginOperation{

	@Override
	public void createPlugin(Map<String, List<Plugin>> m) throws Exception {
		
		//LINLIN TODO 批量添加插件 需要给创建者赋值 获取web-inf目录下Lib的所有插件的附件，传递备份到插件中心中 插件中心 附件与插件分隔开 一个附件可以对应多个插件 没有直接关系
		IPluginsFileService pluginFileService = SpringHelper.getBean(IPluginsFileService.class);
		//判断数据库中是否存在新版插件附件，同名文件MD5值不一样则新增版本 版本有系统按照当前时间新增 并以最新版本的插件为主 将文件MD5值封装于集合中 增加系统版本号字段
		List<String> pluginsPaths = PluginsScanUtils.listAllPluginsJarPath();
		Map<String,PluginsFileEntity> filesMap = pluginFileService.batchCreatePluginJar(pluginsPaths);
		 
		//将每个插件获取得到之后，同时获取对应的jar包信息，与原来系统中的插件进行比较，由插件对应jar包md5值的变化/插件版本号的变化，决定是否操作对应插件的类型，进而保存进插件表 增加系统版本号字段
		//记录本次系统启动所涉及的插件以及其应用的状态
		
		//以上操作每一步均无关其他功能 每个附件的保存 每个插件的保存均不会影响到其他附件 其他插件
		
		//额外事件，将插件上传至云中心
 	    //IPluginsInstallService pluginsInstallService = SpringHelper.getBean(IPluginsInstallService.class);
		IPluginsMsgService pluginMsgService = SpringHelper.getBean(IPluginsMsgService.class);
		IPluginsLogService pluginLogService = SpringHelper.getBean(IPluginsLogService.class);
		List<PluginsMsgEntity> plugins = new ArrayList<PluginsMsgEntity>();
		List<PluginsLogEntity> pluginlLogs = new ArrayList<PluginsLogEntity>();
		PluginsMsgEntity pluginsMsg = null; 
		PluginsLogEntity pluginsLog = null; 
		int index = 0;
		for(String key : m.keySet()){
			if(m.get(key).size()>0){
				//代表存在多个同名同版本的插件 插件加载日志上次加载的插件全部更改为非本次加载，记录本次加载的插件 同时记录本次加载插件的内容以及对应的jar包
				//添加插件与插件附件 以及的单次加载的插件的内容
				Plugin plugin = m.get(key).get(0);
				pluginsMsg = new PluginsMsgEntity();
				BeanUtils.copyPropertiesNotNull(pluginsMsg, plugin);
				if(filesMap.containsKey(plugin.getJarPath())){
					pluginsMsg.setFileMd5(filesMap.get(plugin.getJarPath()).getFileMd5());
					pluginsMsg.setPluginsFileId(filesMap.get(plugin.getJarPath()).getId());
					
					if(filesMap.get(plugin.getJarPath()).getNeedUpdate()){
						//每一个插件都丢一个线程出去
						Class<?> c =  Class.forName(PluginsTypeEnum.getAgentByType(pluginsMsg.getType()).getAgentName());
		         		Constructor<?> c1= c.getDeclaredConstructor(Plugin.class,Properties.class); 
		         		IPluginsAgent agent = (IPluginsAgent) c1.newInstance(plugin,filesMap.get(plugin.getJarPath()).getProperty());
		          		agent.run();
					}
				}
				plugins.add(pluginsMsg);
				
				for(Plugin pLog :  m.get(key)){
					pluginsLog = new PluginsLogEntity();
					BeanUtils.copyPropertiesNotNull(pluginsLog, pLog);
					if(index > 0){
 						pluginsLog.setState(SystemBoolean.NO.getKey());
						pluginsLog.setFailReason(PluginsErrorEnum.PLUGINS_EXIST.getMsg());
					}
					
					if(filesMap.containsKey(pLog.getJarPath())){
						pluginsLog.setFileMd5(filesMap.get(pLog.getJarPath()).getFileMd5());
						pluginsLog.setPluginsFileId(filesMap.get(pLog.getJarPath()).getId());
					}
					pluginlLogs.add(pluginsLog);
					index ++;
				}
				index = 0;
			} 
		}
		try {
			pluginMsgService.batchSave(plugins);
			pluginLogService.updateTruncateTable();
			pluginLogService.batchCreate(pluginlLogs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updatePlugin(Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlugin(Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enablePlugin(Plugin plugin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forbidPlugin(Plugin plugin) {
		// TODO Auto-generated method stub
 	}
	
	
}
