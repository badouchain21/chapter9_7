package com.badou.plugins.agent.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.system.security.service.IResourceService;
import com.badou.brms.system.security.vo.Resource;
import com.badou.core.plugin.agent.AbstractPluginsAgent;
import com.badou.core.plugin.builder.basic.Plugin;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.tools.common.util.SpringHelper;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:37:20
 * @todo 插件 菜单类型的实现类
 */
public class MenuAgent extends AbstractPluginsAgent{
	
	 /**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:31:37
	 * @todo 有参构造参数
	 * @param plugin
	 * @param properties
	 */
	public MenuAgent(Plugin plugin,Properties properties){
		 this.plugin = plugin;
		 this.properties = properties;
	 }
	 
	@Override
	public void run() {
		if(properties == null){
			return;
		}
		if(properties.get(plugin.getCode()+".menu.layer") == null){
			return;
		}
		Integer menuLayer = Integer.valueOf((String) properties.get(plugin.getCode()+".menu.layer"));
		String parentCode = properties.getProperty(plugin.getCode()+".menu.parent.code");
		
		List<Resource> list = new ArrayList<Resource>();
		
		Resource resource = null;
		String j = null;
		JSONObject jo = null;
		
		for(int i = 0; i < properties.size()  ; i ++ ){
 			j = properties.getProperty(plugin.getCode()+".menus["+i+"]");
			jo = JSONObject.parseObject(j);
			if(jo == null){
				continue;
			}
			resource =  new Resource();
			resource.setBigIconPath(jo.getString("icon"));
			resource.setSmallIconPath(jo.getString("icon"));
			resource.setCode(jo.getString("code"));
			resource.setName(jo.getString("name"));
			resource.setUrl(jo.getString("url"));
			resource.setFlgDeleted(SystemBoolean.NO.getKey());
			resource.setResourceType(0);//类型为0代表菜单
			resource.setPriority(jo.getIntValue("priority"));
			list.add(resource);
		}
		
 		IResourceService service =  SpringHelper.getBean(IResourceService.class);
 		
 		if(plugin == null){//代表卸载
 			uninstall(menuLayer,service,list,parentCode);
 		}else if(plugin.getStatus().equals(SystemBoolean.YES.getKey())){//代表安装 激活
 			install(menuLayer,service,list,parentCode);
 		}else if(plugin.getStatus().equals(SystemBoolean.NO.getKey())){//代表禁用
 			uninstall(menuLayer,service,list,parentCode);
 		}
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:39:44
	 * @todo 卸载
	 * @param menuLayer
	 * @param service
	 * @param list
	 * @param parentCode
	 */
	public void uninstall(Integer menuLayer,IResourceService service,List<Resource> list,String parentCode ){
 		if(menuLayer == 0 ){//代表是一级菜单
 			service.removePluginsResource(list, null);
 		}else{//代表不是一级菜单
 			service.removePluginsResource(list, parentCode);
 		}
 	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:39:57
	 * @todo 安装
	 * @param menuLayer
	 * @param service
	 * @param list
	 * @param parentCode
	 */
	public void install(Integer menuLayer,IResourceService service,List<Resource> list,String parentCode ){
 		if(menuLayer == 0 ){//代表是一级菜单
 			service.addPluginsResource(list, null);
 		}else{//代表不是一级菜单
 			service.addPluginsResource(list, parentCode);
 		}
	}
	
}
