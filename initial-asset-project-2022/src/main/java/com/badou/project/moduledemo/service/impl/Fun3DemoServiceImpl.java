package com.badou.project.moduledemo.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.attach.AttachFactory;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.project.moduledemo.dao.IFun3DemoChildDAO;
import com.badou.project.moduledemo.dao.IFun3DemoDAO;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.project.moduledemo.service.IFun3DemoService;
import com.badou.project.moduledemo.web.form.Fun3DemoTreeForm;

/**
 * 功能1示例业务层接口实现
 * 
 * @author xiangsf 2013-1-18
 * 
 */
@Service
public class Fun3DemoServiceImpl extends
		BaseSpringService<Fun3DemoEntity, Serializable> implements
		IFun3DemoService {
	@Autowired
	private IFun3DemoDAO fun3DemoDAO;
	@Autowired
	private IFun3DemoChildDAO fun3DemoChildDAO;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:57:09
	 * @todo 注入dao
	 * @param fun3DemoDAO
	 */
	@Autowired
	public void setFun3DemoDAO(IFun3DemoDAO fun3DemoDAO) {
		this.fun3DemoDAO = fun3DemoDAO;
		super.setBaseDAO(fun3DemoDAO);
	}

	@Autowired
	private AttachFactory attachFactory;

	@Override
	public List<Fun3DemoTreeForm> getTreeByParentId(String pid) {
		List<Fun3DemoTreeForm> list = new ArrayList<Fun3DemoTreeForm>();
		// 这里要按pid去查出其下所有对象，递归子对象like fullpath
		Fun3DemoEntity rootEntity = fun3DemoDAO.find(pid);
		List<Fun3DemoEntity> alls = fun3DemoDAO.findAll();

		Fun3DemoTreeForm treeRoot = new Fun3DemoTreeForm(rootEntity);
		list.add(treeRoot);
		this.recurrenceChildTree(alls, treeRoot);
		return list;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午6:04:00
	 * @todo 获取当前节点的子节点
	 * @param alls
	 * @param t
	 */
	private void recurrenceChildTree(List<Fun3DemoEntity> alls,
			Fun3DemoTreeForm t) {
		Fun3DemoEntity parentMenu = null;
		Fun3DemoTreeForm mtf = null;
		for (Fun3DemoEntity m : alls) {
			parentMenu = m.getParent();
			// 父节点ID与当前节点ID相同，则放入子集中
			if (parentMenu != null
					&& parentMenu.getId().equalsIgnoreCase(t.getTreeNodeId())) {
				mtf = new Fun3DemoTreeForm(m);
				t.getChildren().add(mtf);
				this.recurrenceChildTree(alls, mtf);
			}
		}
	}

	@Override
	public void saveFun3Demo(Fun3DemoEntity fun3Demo, List<Attach> attachList,
			String imgid) {

		try {
				if (attachList != null && attachList.size() != 0) {
					if (attachList.size() == 1) {
						Attach a = new Attach();
						a = attachList.get(0);
						Attach b=attachFactory.uploadFile(a);
						if (StringUtils.isNotBlank(imgid)) {
							fun3Demo.setSmallIcon(b.getId());
						} else {
							fun3Demo.setSmallIcon(b.getId());
							fun3Demo.setBigIcon(b.getId());
						}	
 					} else {
						for (int i = 0; i < attachList.size(); i++) {
							Attach a = new Attach();
							a = attachList.get(i);
							Attach b=attachFactory.uploadFile(a);
							switch (i) {
							case 0:
								fun3Demo.setSmallIcon(b.getId());
								break;
							case 1:
								fun3Demo.setSmallIcon(b.getId());
								fun3Demo.setBigIcon(b.getId());
								break;
							}
						}
	
					}
				}
			if(StringUtils.isNotBlank(fun3Demo.getId()))
			{
				
				fun3DemoDAO.update(fun3Demo);
			}
			else
			{
				
				fun3DemoDAO.create(fun3Demo);
				 
				fun3Demo.setFullName(fun3Demo.getParent().getFullName()+"-"+fun3Demo.getName());
				fun3Demo.setFullPath(fun3Demo.getParent().getFullPath()+"-"+fun3Demo.getId());
				fun3Demo.setTreeLevel(fun3Demo.getParent().getTreeLevel()+1);
				fun3DemoDAO.update(fun3Demo);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}

	}

}
