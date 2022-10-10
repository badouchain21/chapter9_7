package com.badou.platform.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.attach.model.Attach;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.dictionary.form.DictionaryForm;
import com.badou.brms.dictionary.form.DictionaryItemForm;
import com.badou.brms.dictionary.model.DictionaryEntity;
import com.badou.brms.dictionary.model.DictionaryItemEntity;
import com.badou.brms.dictionary.service.IDictionaryService;
import com.badou.brms.fileupload.AttachFactory;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.designer.module.database.ModuleDatabaseConst;
import com.badou.designer.module.design.ModuleConstants;
import com.badou.designer.module.design.model.MdControlEntity;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.badou.designer.module.design.model.MdFlowEntity;
import com.badou.designer.module.design.model.MdFormEntity;
import com.badou.designer.module.design.model.MdIndexEntity;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.model.MdModuleLinkEntity;
import com.badou.designer.module.design.service.IModuleDesignService;
import com.badou.platform.CopyHandle;
import com.badou.platform.dao.IPlatformResourceDAO;
import com.badou.platform.dao.IResourceOperDAO;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.util.ModuleZipVO;
import com.badou.platform.util.PluginZipVO;
import com.badou.plugins.install.model.PluginsInstallEntity;
import com.badou.plugins.install.service.IPluginsInstallService;
import com.badou.tools.common.Globals;
import com.badou.tools.common.util.StringUtils;
import com.badou.tools.common.util.bean.BeanUtils;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:11:15
 * @todo 资源操作service接口实现类
 */
@Service
public class ResourceOperServiceImpl extends BaseSpringService<ResourceOperEntity, Serializable> implements IResourceOperService {

	@Autowired
	private IResourceOperDAO resourceOperDAO;
	
	@Autowired
	private IPlatformResourceDAO platformResourceDAO;
	
	@Autowired
	private IPlatformResourceService platformResourceService;
	
	@Autowired
	private IModuleDesignService moduleDesignService ;
	
	@Autowired
	private IPluginsInstallService pluginsInstallService;
	
	@Autowired
	private IDictionaryService dictionaryService ;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:29:18
	 * @todo 设置默认使用的dao
	 * @param resourceOperDAO
	 */
	@Autowired
	public void setResourceOperDAO(IResourceOperDAO resourceOperDAO) {
		this.resourceOperDAO = resourceOperDAO;
		super.setBaseDAO(resourceOperDAO);
	}
	@Override
	public ResourceOperEntity findByPlatformResourceId(String platformResourceId, String versionCode, String version1, String version2, String version3) {
		List<ResourceOperEntity> list = this.resourceOperDAO.findByPlatformResourceId(platformResourceId , versionCode , version1 , version2 , version3);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * @auth chenjiabo
	 * @date 2019年7月2日下午5:29:41
	 * @todo 更新状态
	 * @param oper
	 * @param status
	 * @param failMsg
	 */
	public void updateStatus(ResourceOperEntity oper, Integer status, String failMsg) {
		oper.setStatus(status);
		oper.setFailMsg(failMsg);
		resourceOperDAO.update(oper);
	}

	@Override
	public String getResourceId(ResourceOperEntity oper) {
		PlatformResourceEntity resource = platformResourceDAO.findByplatformResourceId(oper.getPlatformResourceId());
		if(resource!=null){
			return resource.getResourceId();
		}
		return null;
	}
	
	@Override
	public void updateModule(ModuleZipVO vo,String platformResoueceLinkId) throws Exception {
		MdModuleEntity module = vo.getModule() ;
		//定义备份表的名称，格式为原表名 + _bak + 当前时间
 	//	String bakTableName = vo.getModule().getDbTable() + "_bak_" + System.currentTimeMillis();
		PlatformResourceEntity pf = platformResourceService.find(platformResoueceLinkId);
		MdModuleEntity oldModule = this.moduleDesignService.find(pf.getResourceId()) ;
		//获取就模型字段列表。用于复制数据
		Set<MdFieldEntity> oldFields = oldModule.getFields();
		//执行备份
//		this.bak(vo , bakTableName);
		//创建表
//		this.createTable(module);
		//将旧表中的数据复制到新表中
//		this.copyDataToNewTable(vo.getModule().getDbTable() , bakTableName , oldFields);
		
		//创建表 创建新表
		String tempTableName = module.getDbTable()+"bd_temp";
		this.createTable(module,tempTableName);
				
		//将旧表中的数据复制到新表中(新表名称，旧表名称)
		this.copyDataToNewTable(tempTableName, module ,oldFields); 
				
		//定义备份表的名称，格式为原表名 + _bak + 当前时间
		String bakTableName = vo.getModule().getDbTable() + "_bak_" + System.currentTimeMillis();
		//执行备份 重命名旧表
		this.bak(vo , bakTableName);
				
		//完成备份之后再更新表
		this.renameTable(tempTableName, module.getDbTable());
 		
		//更新数据字典
		this.updateDic(vo.getDicMap());
		//更新当前版本数据
		this.updateVersion(vo,platformResoueceLinkId);
		CopyHandle ch =  CopyHandle.getInstance();
 		oldModule = (MdModuleEntity) ch.copy(module,oldModule);
 		
 		Set<MdFieldEntity> fields = new HashSet<MdFieldEntity>();
 		MdFieldEntity newF = null;
 		MdFormEntity form;
 		MdIndexEntity index;
 		
		for(MdFieldEntity f : module.getFields()){
			newF = (MdFieldEntity) ch.copy(f);
			form = (MdFormEntity)ch.copy(f.getForm());
			index = (MdIndexEntity) ch.copy(f.getIndex());
			
			newF.setForm(form);
			newF.setIndex(index);
			
			newF.setModule(oldModule);
			if(form != null){
				form.setField(newF);
			}
			if(index != null){
				index.setField(newF);
			}
			fields.add(newF);
		}
		oldModule.getFields().clear();
		oldModule.getFields().addAll(fields);
		
 		Set<MdModuleLinkEntity> linkSet = new HashSet<MdModuleLinkEntity>(); 
 		MdModuleLinkEntity newL = null;
		for(MdModuleLinkEntity f : module.getLinkSet()){
			newL = (MdModuleLinkEntity) ch.copy(f);
			newL.setModule(oldModule);
			linkSet.add(newL);
		}
		oldModule.getLinkSet().clear();
		oldModule.getLinkSet().addAll(linkSet);
		
 		Set<MdFlowEntity> flowSet = new HashSet<MdFlowEntity>();
 		MdFlowEntity newFlow = null;
		for(MdFlowEntity f : module.getFlowSet()){
			newFlow = (MdFlowEntity) ch.copy(f);
			newFlow.setModule(oldModule);
			flowSet.add(newFlow);
		}
		oldModule.getFlowSet().clear();
		oldModule.getFlowSet().addAll(flowSet);
		
 		Set<MdControlEntity> control = new HashSet<MdControlEntity>();
 		MdControlEntity newC = null;
		for(MdControlEntity f : module.getControl()){
			newC = (MdControlEntity) ch.copy(f);
			newC.setModule(oldModule);
			control.add(newC);
		}
		oldModule.getControl().clear();
		oldModule.getControl().addAll(control);
		moduleDesignService.update(oldModule);
 	}
	
	/**
	 * @auth chenjiabo
	 * @date 2019年7月2日下午5:30:02
	 * @todo 创建模型
	 * @param vo
	 * @return 模型实体
	 * @throws Exception if has error(直接往外抛)
	 */
	public MdModuleEntity createModule(ModuleZipVO vo) throws Exception {
		MdModuleEntity module = vo.getModule() ;
		
		/*MdModuleEntity oldModule = this.moduleDesignService.get(module.getId()) ;*/
		//获取就模型字段列表。用于复制数据
		Set<MdFieldEntity> oldFields = module.getFields(); 
		
		//创建表 创建新表
		String tempTableName = module.getDbTable()+"bd_temp";
		this.createTable(module,tempTableName);
		
		//将旧表中的数据复制到新表中(新表名称，旧表名称)
		this.copyDataToNewTable(tempTableName , module, oldFields); 
		
		//定义备份表的名称，格式为原表名 + _bak + 当前时间
 		String bakTableName = vo.getModule().getDbTable() + "_bak_" + System.currentTimeMillis();
		//执行备份 重命名旧表
		this.bak(vo , bakTableName);
		
		//完成备份之后再更新表
		this.renameTable(tempTableName, module.getDbTable());
		
		//更新数据字典
		this.updateDic(vo.getDicMap());
		
		//更新当前版本数据
	//	this.updateVersion(vo);
 		CopyHandle ch =  CopyHandle.getInstance();
 		MdModuleEntity newMd = (MdModuleEntity) ch.copy(module);
 		
 		Set<MdFieldEntity> fields = new HashSet<MdFieldEntity>();
 		MdFieldEntity newF = null;
 		MdFormEntity form;
 		MdIndexEntity index;
		for(MdFieldEntity f : module.getFields()){
			newF = (MdFieldEntity) ch.copy(f);
			form = (MdFormEntity)ch.copy(f.getForm());
			index = (MdIndexEntity) ch.copy(f.getIndex());
			
			newF.setForm(form);
			newF.setIndex(index);
			
			newF.setModule(newMd);
			if(form != null){
				form.setField(newF);
			}
			if(index != null){
				index.setField(newF);
			}
			fields.add(newF);
		}
		newMd.getFields().addAll(fields);
		
 		Set<MdModuleLinkEntity> linkSet = new HashSet<MdModuleLinkEntity>(); 
 		MdModuleLinkEntity newL = null;
		for(MdModuleLinkEntity f : module.getLinkSet()){
			newL = (MdModuleLinkEntity) ch.copy(f);
			newL.setModule(newMd);
			linkSet.add(newL);
		}
		newMd.getLinkSet().addAll(linkSet);
		
 		Set<MdFlowEntity> flowSet = new HashSet<MdFlowEntity>();
 		MdFlowEntity newFlow = null;
		for(MdFlowEntity f : module.getFlowSet()){
			newFlow = (MdFlowEntity) ch.copy(f);
			newFlow.setModule(newMd);
			flowSet.add(newFlow);
		}
		newMd.getFlowSet().addAll(flowSet);
		
 		Set<MdControlEntity> control = new HashSet<MdControlEntity>();
 		MdControlEntity newC = null;
		for(MdControlEntity f : module.getControl()){
			newC = (MdControlEntity) ch.copy(f);
			newC.setModule(newMd);
			control.add(newC);
		}
		newMd.getControl().addAll(control);
 		
 		moduleDesignService.create(newMd);
		return newMd;
	}
	
	/**
	 * 更新当前模型版本相关数据
	 * @param vo
	 * @author chenjiabao
	 * @date 2018年3月2日下午5:45:30
	 */
	private void updateVersion(ModuleZipVO vo,String platformResoueceLinkId) {
		MdModuleEntity module = vo.getModule() ;
		//获取资源id
		PlatformResourceEntity source = this.platformResourceService.find(platformResoueceLinkId) ;
		source.setVersionCode(vo.getVersionCode());
		source.setVersion1(vo.getVersion1());
		source.setVersion2(vo.getVersion2());
		source.setVersion3(vo.getVersion3());
		this.platformResourceService.update(source);
	}
	/**
	 * 更新相应数据字典
	 * @param dicMap
	 * @author chenjiabao
	 * @date 2018年3月2日下午5:36:39
	 * @updator zlz 2018年5月8日
	 * @updator zlz 2018年5月21日 修复当数据字典为空的时候下载模型报错的问题
	 */
	private void updateDic(Map<String, DictionaryEntity> dicMap) {
 		List<DictionaryEntity> list = new ArrayList<DictionaryEntity>();
		dicMap.forEach((k , v) -> {
		     if(v != null){
		    	 DictionaryForm form = null;
				 form = new DictionaryForm();
				 form.setEntityToFormProperties(v);
			  	 DictionaryEntity d  = this.dictionaryService.findByCode(form.getCode());
	 			 try {
					if(d == null || d.getId() == null){
						 form.setId(null);
					} else {
						 form.setEntityToFormProperties(d);
					}
				} catch (Exception e) {
					 form.setId(null);
				}  
	 			 List<DictionaryItemForm> items = new ArrayList<DictionaryItemForm>();
				 for(DictionaryItemEntity itemEntity : v.getItems()){
					 DictionaryItemForm item  = new DictionaryItemForm();
					 item.setEntityToFormProperties(itemEntity);
					 items.add(item);
				 }
				 this.dictionaryService.save(form, items);
		     }
		});
 	}
	/**
	 * 从备份表中复制数据到新的数据表中
	 * 
	 * 2018-11-5 17:33:44 update by wujunliang
	 * <br/>
	 * 新的数据表中删除了字段，会导致新表插入数据不成功，添加了判断，
	 * 插入数据时，只插入新表和旧表中都存在的字段
	 * 
	 * @author chenjiabao
	 * @date 2018年3月2日下午4:46:16
	 * 
	 */
	private void copyDataToNewTable(String dbTable, MdModuleEntity module, Set<MdFieldEntity> oldFields) {
		try {
			// 获取新模型中的数据库字段
			Set<String> newTableFiled = new HashSet<>();
			module.getFields()
					.stream()
					.filter(f -> SystemBoolean.YES.getKey().equals(f.getIsDbField()))
					.forEach(f -> newTableFiled.add(f.getFieldName()));
			
			// 拼接字段SQL，过滤掉非数据库字段以及在新模型中没有的字段
			StringBuilder builder = new StringBuilder();
			oldFields.stream()
					.filter(f -> {
						return SystemBoolean.YES.getKey().equals(f.getIsDbField()) 
								&& newTableFiled.contains(f.getFieldName());
					})
					.forEach(field -> {
						builder.append(",");
						builder.append(field.getFieldName());
					});
			String field = builder.substring(1);
			
			builder.delete(0, builder.length());
			builder.append("insert into ")
					.append(dbTable)
					.append(" ( ")
					.append(field)
					.append(" ) ")
					.append(" select ")
					.append(field)
					.append(" from ")
					.append(module.getDbTable());
			this.platformResourceDAO.copyDataToNewTable(builder.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	/**
	 * 创建表
	 * @param module
	 * @param fields
	 * @throws Exception
	 * @author chenjiabao
	 * @date 2018年3月2日下午4:33:15
	 * @throws Exception if has error(直接往外抛)
	 */
	private void createTable(MdModuleEntity module,String tempDBTableName) throws Exception {
		String newTableName = tempDBTableName == null ? module.getDbTable() : tempDBTableName;
		StringBuilder sql = new StringBuilder();
		sql.append(" create table ").append(newTableName);
		
		/*
		 * 根据字段排序号排序，空值放在后面
		 * add by wujunliang 2018-11-5 15:16:33
		 */
		List<MdFieldEntity> list = new ArrayList<MdFieldEntity>(module.getFields());
		Collections.sort(list, (f1, f2) -> {
			Integer o1 = f1.getForm().getPriority();
			Integer o2 = f2.getForm().getPriority();
			
			if (o1 == null && o2 == null) {
				return 0;
			}
			if (o1 != null && o2 == null){
				return -1;
			}
			if (o1 == null && o2 != null){
				return 1;
			}
			return o1.compareTo(o2);
		});
		
		StringBuffer fields = new StringBuffer();
		
		fields = genFieldSql(list);
		String prikey = genPriKeySql(false,module);
		sql.append("( ").append(fields.toString()).append(fields.length()>0&&StringUtils.isNotBlank(prikey)?",":"").append(prikey).append(" ) ");
		this.platformResourceDAO.executeUpdateSql(sql.toString());
	}
	
	/**
	 * 拼凑SQL语句的字段<br>
	 * edit by chenchuangzhang<br>
	 * 2017年8月9日下午8:40:15<br>
	 * 
	 * <br/>
	 * 2018-11-5 15:51:20 update by wujunliang
	 * <br/>
	 * 去掉了sqlType参数，在该方法调用中，sqlType都是传进null，也没有说明参数作用。
	 * <br/>
	 * 添加了 databaseType 参数，用于判断数据库类型。
	 * <br/>
	 * @param fields		字段集合
	 * 	<ul>
	 *   <li>1：Mysql 常量： {@link ModuleDatabaseConst#DATABASETYPE_MYSQL}</li>
	 *   <li>2：Oracle 常量： {@link ModuleDatabaseConst#DATABASETYPE_ORACLE}</li>
	 *   <li>3：SQLServer 常量： {@link ModuleDatabaseConst#DATABASETYPE_SQLSERVER}</li>
	 *  </ul>
	 * @return SQL
	 */
	private StringBuffer genFieldSql(List<MdFieldEntity> fields) {
		StringBuffer sql = new StringBuffer();
		int i = 0;
		for(MdFieldEntity field : fields){
			if(field.getIsDbField() != SystemBoolean.YES.getKey()){
				continue;
			}
			if(i!=0){
				sql.append(", ");
			}
			String dataType = field.getDisplayType();
			if(dataType.contains(Globals.SEPARATOR_LINE)){
				String[] values = dataType.split(Globals.SEPARATOR_LINE);
				dataType = values[0];
			}
			sql.append(field.getFieldName()).append(" ").append(dataType);
			
			String type = dataType.toString().toLowerCase();
			
			if(field.getLength()!=null && field.getLength()>0){
				/**
				 * edit by ccz
				 * @date 2017-08-09 20:30:20
				 * 如果是日期，则不需要添加长度，否则数据库报错
				 * <h3>MySQL情况下</h3>double类型的要么不指定长度，要么以（5,2）方式指定，否则数据库报错
				 * 如果是blob,clob，也不需要设置长度
				 */
				if(!(dataType.indexOf("date")!=-1 || dataType.indexOf("datetime")!=-1
						|| dataType.indexOf("timestamp")!=-1
						|| dataType.indexOf("blob")!=-1
						|| dataType.indexOf("clob")!=-1)){
					//针对double ,fload 特殊处理
					if((type.equals("float")||type.equals("double")) && (field.getAccuracy()!=null)){
						sql.append("(").append(field.getLength());
						sql.append(",").append(field.getAccuracy());
						sql.append(" ) ");
					}else{
						sql.append("(").append(field.getLength());
						if((field.getAccuracy()!=null) && (type.equals("decimal")||type.equals("numeric"))){
							sql.append(",").append(field.getAccuracy());
						}
						sql.append(" ) ");
					}
				}
			}
			// timestamp 类型需要设置默认值 
			if ("timestamp".equals(type)) {
				sql.append(" DEFAULT CURRENT_TIMESTAMP ");
			}
			
			if(ModuleConstants.COMMON_NO.equals(field.getNullable())){
				sql.append(" NOT NULL ");
			}
			i++;
		}
		return sql;
	}
	
	/**
	 * 拼凑主键sql<br>
	 * 当模型基本信息和字段分为两个tab的情况下时<br>
	 * 当为修改表时，可能新增进来的字段为主键字段，此时拼凑的sql为
	 * alter table test1  add zj varchar2(64 )  NOT NULL  add  constraint pk_key primary key(zj)<br>
	 * 很明显是错误的，已解决，带完善
	 * edit by chenchuangzhang
	 * 2017年8月10日下午2:40:47
	 * @param isAdd
	 * @return 主键sql
	 */
	private String genPriKeySql(boolean isAdd,MdModuleEntity moduleBean) {
		StringBuffer prikey = new StringBuffer();	
		if(StringUtils.isNotBlank(moduleBean.getPriKey())){
			if(isAdd){	//如果是后期新增的主键，则应该alter table
				prikey.append("alter table ").append(moduleBean.getDbTable()).append(" add ");
			}
			/**
			 * oracle数据库中，每一个表都有一个唯一主键，而主键是不允许重复的。<br>
			 * 已经存在pk_key主键的话，新建的表的主键还是pk_key就会报错。<br>
			 * 采用pk_key+'_'+表名来分辨
			 */
			prikey.append(" constraint pk_key_").append(moduleBean.getDbTable()).append(" primary key(").append(moduleBean.getPriKey()).append(")");
			if(isAdd){
				prikey.append(";");
			}
		}
		return prikey.toString();
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:31:14
	 * @todo 备份
	 * @param vo
	 * @param bakTableName
	 */
	private void bak(ModuleZipVO vo, String bakTableName) {
		try{
			//执行重命名操作，将数据库中现有的表改为备份表
			MdModuleEntity module = vo.getModule() ;
			/*ExecuteDDLUtil ddl = new ExecuteDDLUtil(module);
			ddl.renameTable(module.getDbTable(), bakTableName);*/
			this.platformResourceDAO.renameTable(module.getDbTable() , bakTableName);
		}catch(Exception e){
			logger.info(e.getMessage(),e);
		}
	}
	
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:31:26
	 * @todo 重命名表
	 * @param oldName
	 * @param newName
	 */
	private void renameTable(String oldName,String newName){
		this.platformResourceDAO.renameTable(oldName, newName);
	}
	
	@Override
	public ResourceOperEntity findLatestUpload(String moduleId) throws Exception {
		return resourceOperDAO.findLatestUpload(moduleId);
	}
	
	@Override
	public PluginsInstallEntity updatePlugin(PluginZipVO vo,String platformResoueceLinkId) throws Exception {
		PluginsInstallEntity plugin = vo.getPlugin();
		PlatformResourceEntity pf = platformResourceService.find(platformResoueceLinkId);
		PluginsInstallEntity oldPlugin = this.pluginsInstallService.find(pf.getResourceId()) ;
		BeanUtils.copyProperties(oldPlugin, plugin);
		pluginsInstallService.update(oldPlugin);
		return oldPlugin;
 	}
	
	/**
	 * @auth chenjiabo
	 * @date 2019年7月2日下午5:31:37
	 * @todo 创建插件
	 * @param vo
	 * @return 插件实体
	 * @throws Exception if has error(直接往外抛)
	 */
	public PluginsInstallEntity createPlugin(PluginZipVO vo) throws Exception {
		PluginsInstallEntity plugin = vo.getPlugin() ;
 		plugin.setId(null);
 		plugin.setXmlId(null);
 		plugin.setJarId(null);
 		pluginsInstallService.create(plugin);
 		
 		if(StringUtils.isNotBlank(vo.getXmlFilePath())){
 			File file = new File(vo.getXmlFilePath());
 			Attach xml = AttachFactory.upload(plugin.getId(), file);
 			plugin.setXmlId(xml.getId());
 		}
 		
 		if(StringUtils.isNotBlank(vo.getJarFilePath())){
 			File file = new File(vo.getJarFilePath());
 			Attach jar = AttachFactory.upload(plugin.getId(), file);
 			plugin.setJarId(jar.getId());
 		}
 		pluginsInstallService.update(plugin);
		return plugin;
	}
}
