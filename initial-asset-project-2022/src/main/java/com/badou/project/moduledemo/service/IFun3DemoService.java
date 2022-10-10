package com.badou.project.moduledemo.service;
import java.io.Serializable;
import java.util.List;

import com.badou.brms.attach.model.Attach;
import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.web.form.Fun3DemoTreeForm;
/**
 * 功能1示例业务层接口
 * @author xiangsf 2013-1-18
 *
 */
public interface IFun3DemoService extends IBaseSpringService<Fun3DemoEntity, Serializable> {
	/**
	 * 根据父级ID加载出一棵树结构
	 * @param pid
	 * @return 对象集合
	 */
	public List<Fun3DemoTreeForm> getTreeByParentId(String pid);
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:05:03
	 * @todo 保存相关数据
	 * @param fun3Demo
	 * @param attachList
	 * @param imgid
	 */
	public void saveFun3Demo (Fun3DemoEntity fun3Demo,List<Attach> attachList,String imgid);
}
