package com.badou.project.moduledemo.web.form;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import com.alibaba.fastjson.annotation.JSONField;
import com.badou.brms.tree.ligerui.LigerUIJSONTreeBuilder;
import com.badou.brms.tree.model.ITreeModel;
import com.badou.project.moduledemo.model.Fun3DemoEntity;
import com.badou.tools.common.Globals;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:19:02
 * @todo demo3树接口form表单
 */
public class Fun3DemoTreeForm extends LigerUIJSONTreeBuilder {

	private Fun3DemoEntity fun3;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:52:17
	 * @todo 有参构造函数
	 * @param fun3
	 */
	public Fun3DemoTreeForm(Fun3DemoEntity fun3) {
		this.fun3 = fun3;
	}

	private Collection<Fun3DemoTreeForm> children = new ArrayList<Fun3DemoTreeForm>();

	@Override
	public OutputStream genOutputStream() {
		return null;
	}

	@Override
	@JSONField(name = "name")
	public String getTreeNodeName() {
		return fun3.getName();
	}

	@Override
	@JSONField(name = "type")
	public String getTreeNodeType() {
		return fun3.getTreeType();
	}

	@Override
	@JSONField(name = "id")
	public String getTreeNodeId() {
		return fun3.getId();
	}

	@Override
	@JSONField(name = "children")
	public Collection<ITreeModel> getTreeChildrens() {
		return new ArrayList<ITreeModel>(children);
	}

	@Override
	@JSONField(name = "pid")
	public ITreeModel getTreeParent() {
		if (fun3.getParent() != null){
			return new Fun3DemoTreeForm(fun3.getParent());
		}
		return null;
	}

	@Override
	@JSONField(name = "url")
	public String getTreeClickURL() {
		return fun3.getUrl();
	}

	@Override
	@JSONField(name = "userData")
	public com.alibaba.fastjson.JSONArray getUserJSONData() {
		return null;
	}

	public Collection<Fun3DemoTreeForm> getChildren() {
		return children;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:38:45
	 * @todo 是否可拓展
	 * @return true 是 false 否
	 */
	@JSONField(name = "isexpand")
	public boolean getIsExpand() {
		if (Globals.RESOURCE_ROOT_ID.equalsIgnoreCase(this.getTreeNodeId())) {
			return true;
		}
		return false;
	}

	@JSONField(name = "hasChild")
	public boolean getHasChild() {
		return !children.isEmpty();
	}
}
