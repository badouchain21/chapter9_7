package com.badou.project.moduledemo.service;
import java.io.Serializable;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.project.moduledemo.dao.IFun1DemoChildDAO;
import com.badou.project.moduledemo.model.Fun3DemoChildEntity;
/**
 * 功能1示例子对象业务层接口
 * <p>该接口可以没有，将子对象业务接口纳入到父对象{@link IFun1DemoServiceImpl}中,在父对象中注入{@link IFun1DemoChildDAO}
 * @author xiangsf 2013-1-18
 *
 */
public interface IFun3DemoChildService extends IBaseSpringService<Fun3DemoChildEntity, Serializable> {

}
