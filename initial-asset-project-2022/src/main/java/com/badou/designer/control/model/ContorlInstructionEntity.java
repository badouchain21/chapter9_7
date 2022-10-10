package com.badou.designer.control.model;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.core.standard.base.extend.IUniqueCodeEntity;
import com.badou.tools.vendors.annotation.DefaultOrderBy;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:29:49
 * @todo 指令相关实体类
 */
@javax.persistence.Entity
@Table(name = "contorl_instruction")
@DefaultOrderBy(clause="UPDATE_TIME desc")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
@Where(clause = "FLG_DELETED=0")
public class ContorlInstructionEntity extends AppBaseEntity implements IUniqueCodeEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8004088025470492925L;

	/**
	 * 指令名称
	 */
	@Column(name = "name", unique = false, nullable = true, insertable = true, updatable = true)
	protected String name ;
	
	/**
	 * 指令编码
	 */
	@Column(name = "code", unique = false, nullable = true, insertable = true, updatable = true)
	protected String code ;
	
	/**
	 * 指令方法名
	 */
	@Column(name = "function_name", unique = false, nullable = true, insertable = true, updatable = true)
	protected String functionName ;
	
	/**
	 * 指令状态
	 */
	@Column(name = "status", unique = false, nullable = true, insertable = true, updatable = true)
	protected int status = 0;

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

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
