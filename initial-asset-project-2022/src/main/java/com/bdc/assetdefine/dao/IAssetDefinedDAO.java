package com.bdc.assetdefine.dao;

import com.alibaba.fastjson.JSONArray;
import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.assetdefine.model.AssetDefinedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



/**
 * 描述：资产建模定义 数据库接口
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:45:50
 */
public interface IAssetDefinedDAO extends IBaseHibernateDAO<AssetDefinedEntity, Serializable> {

    /**
     *
     * 描述：根据资产编码获取资产建模定义
     * @author linxiaoqing
     * @Date 2020年1月7日 下午2:46:36
     * @param assetCode
     * @return
     */
    AssetDefinedEntity findByAssetCode(String assetCode);

    /**
     * 根据sql统计资产数量(single)
     * @param sql
     * @return
     */
	int countAssetBySql(String sql);

	/**
	 * 根据sql统计资产数量(multi)
	 * @param sql
	 * @return
	 */
	List statAssetBySql(String sql);

	/**
	 * 通过关键字查找
	 * @param jsonArray
	 * @param keyword
	 * @param time
	 * @return
	 */
	Pagelet findByKeyword (JSONArray jsonArray, String keyword, LocalDateTime time,Pagelet pagelet);

}
