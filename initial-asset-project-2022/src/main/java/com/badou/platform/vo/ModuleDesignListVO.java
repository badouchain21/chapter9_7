package com.badou.platform.vo;

import com.badou.brms.dictionary.DictionaryLib;
import com.badou.platform.PlatformConst;

/**
 * 模型数据vo
 * @ClassName ModuleDesignListVO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author wjw
 * @Date 2018年3月2日 下午3:40:09
 * @Updator bduser18
 * @UpdateDate 2018年3月2日 下午3:40:09
 * @UpdateDesc (更新内容描述)
 * @version 1.0.0
 */
public class ModuleDesignListVO {

	private String id;
	/**
	 * 数据源Id
	 */
	private String dataSourceId;
	
	/**
	 * 数据源名称
	 */
	private String dataSourceName;
	
	/**
	 * 数据源类型
	 */
	private Integer dataSourceType;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 编码
	 */
	private String code;
	
	/**
	 * 模块名称
	 */
	private String moduleName;
	
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 表名称
	 */
	private String dbTable;
	
	/**
	 * 上传状态
	 */
	private Integer uploadState;
	
	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public Integer getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(Integer dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDbTable() {
		return dbTable;
	}

	public void setDbTable(String dbTable) {
		this.dbTable = dbTable;
	}

	public Integer getUploadState() {
		return uploadState;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:13:15
	 * @todo 转换上传状态值
	 * @return 状态值描述
	 */
	public String getUploadStateDic() {
		if(null!=uploadState){
			return DictionaryLib.getItemName(PlatformConst.DIC_UPLOAD_STATUS, uploadState);
		} else {
			return "";
		}
	}

	public void setUploadState(Integer uploadState) {
		this.uploadState = uploadState;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
