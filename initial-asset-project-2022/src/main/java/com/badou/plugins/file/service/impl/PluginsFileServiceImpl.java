package com.badou.plugins.file.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.attach.AttachFactory;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.core.plugin.PluginsConst;
import com.badou.core.plugin.util.JarRead;
import com.badou.plugins.file.dao.IPluginsFileDAO;
import com.badou.plugins.file.model.PluginsFileEntity;
import com.badou.plugins.file.service.IPluginsFileService;
import com.badou.tools.common.util.FileUtils;


/**
 *	系统插件文件 Service接口实现类
 * 
 * Copyright 2014 Company Name, badousoft
 * @author badousoft
 * @created 2019-01-30 15:55:40.739
 * @version v1.0
 * @revision 
 */
@Service
public class PluginsFileServiceImpl extends
		BaseSpringService<PluginsFileEntity, Serializable> implements IPluginsFileService {
		
	@Autowired
	private IPluginsFileDAO pluginsFileDAO;
	
	@Autowired
	private AttachFactory attachFactory ;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入dao
	 * @param pluginsFileDAO
	 */
	@Autowired
	public void setPluginsFileDAO(IPluginsFileDAO pluginsFileDAO) {
		this.pluginsFileDAO = pluginsFileDAO;
		super.setBaseDAO(pluginsFileDAO);
	}
	
	@Override
	public Map<String, PluginsFileEntity> batchCreatePluginJar(List<String> pluginsPaths ) throws Exception{
		Map<String, PluginsFileEntity> map = new HashMap<String,PluginsFileEntity>();
		Attach attach = null;
		PluginsFileEntity file = null;
		List l = null;
		StringBuilder ps  = null;
		boolean createAttach = false;
 		for(String s : pluginsPaths){
 			//逐个循环
			 try {
				 String fileName = getFileName(s);
				 l = findPluginsFile(fileName.substring(0, fileName.lastIndexOf(".")),FileUtils.getFileMD5StringByPath(s));
				 
				 if(l != null && l.size() > 0){
					 file = (PluginsFileEntity) l.get(0);
					 if(!FileUtils.getFileMD5StringByPath(s).equals(file.getFileMd5())){
						 createAttach = true;
					 }
				 }else{
					 file = new PluginsFileEntity();
					 createAttach = true;
					 file.setFileMd5(FileUtils.getFileMD5StringByPath(s));
					 file.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
				 }
				 
				//加载jar包中配置
          		Properties properties = JarRead.loadPropertiesFromJar(s, PluginsConst.PLUGIN_CFG_PROPERTIES);
          		file.setProperty(properties);
	          	ps = new StringBuilder();
	           	for(Object e : properties.keySet()){
	           			ps.append(e+"="+properties.getProperty((String) e)+"\r\n");
	            };
	            if(file.getProperties() !=null  &&  !file.getProperties().equals(ps.toString())){
	            	file.setProperties(ps.toString());
	            	file.setNeedUpdate(true);
	            }
            	 
				if(createAttach){
					 attach = assembleAttach(s);
					 attachFactory.uploadFile(attach);
					 file.setAttachId(attach.getId());
				}
				
				this.pluginsFileDAO.save(file);
				createAttach = false;
				 
				map.put(s, file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:46:55
	 * @todo 查找插件文件
	 * @param fileName
	 * @param md5
	 * @return PluginsFileEntity集合
	 */
	private List  findPluginsFile(String fileName,String md5){
		String hql = "from PluginsFileEntity where fileName like ? or fileMd5 like ? ";
		List<Object> objs = new ArrayList<Object>();
		objs.add(fileName);
		objs.add(md5);
		List  files = this.pluginsFileDAO.findByHQL(hql, objs);
		return files;
	}
	 
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:47:50
	 * @todo 组装文件
	 * @param filePath
	 * @return 文件实体
	 * @throws Exception if has error(直接往外抛)
	 */
	private Attach assembleAttach(String filePath ) throws Exception{
	 		 Attach attach = null;
			 InputStream is = null;
			 byte[] content = null;
			 String fileName = getFileName(filePath);
			 is = new FileInputStream(filePath);
			 if(is != null){
				 attach = new Attach();
				 content = new byte[is.available()];
				 is.read(content);
				 attach.setName(fileName.substring(0, fileName.lastIndexOf(".")));
				 attach.setFileName(fileName);
				 attach.setFileContent(content);
				 attach.setResourceId("");
				 attach.setExtendName(fileName.substring(fileName.lastIndexOf(".")));
			}		
	 		return attach;
	  	}
		
	 /**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:47:53
	 * @todo 获取文件名称
	 * @param filePath
	 * @return 文件名
	 */
	private String getFileName(String filePath ){
			String  fileName = filePath.substring(filePath.lastIndexOf("/")+1);
			return fileName;
		}
}
 
 
 
 