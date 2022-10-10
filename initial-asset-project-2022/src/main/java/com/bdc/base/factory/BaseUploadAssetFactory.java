package com.bdc.base.factory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseUploadAssetService;
import lombok.NonNull;

import org.springframework.stereotype.Component;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.base.support.spring.IBaseSpringService;

/**
 * 描述：资产上链工厂
 * @author linxiaoqing
 * @Date 2020年1月7日 下午5:02:32
 */
@Component
@SuppressWarnings("unchecked")
public class BaseUploadAssetFactory {
	/**
	 * 业务处理服务map
	 */
    private final Map<String, IBaseSpringService<? extends BaseAssetEntity, Serializable>> businesshandlerMap = new HashMap<>();
    /**
     * 上链处理服务map
     */
    private final Map<String, IBaseUploadAssetService> uploadServicehandlerMap = new HashMap<>();
    /**
     * 存储表与编码的关系map
     */
    private final Map<String, String> tableMap = new HashMap<>();
    /**
     * get
     * @param type
     * @return
     */
    public IBaseSpringService<? extends BaseAssetEntity, Serializable> getBusinessService(@NonNull String type){
    	return businesshandlerMap.get(type);
    }
    /**
     * get
     * @param type
     * @return
     */
    public IBaseUploadAssetService getUploadService(@NonNull String type){
    	return uploadServicehandlerMap.get(type);
    }
    /**
     * set 设置业务服务对象存储到MAP
     * @param type
     * @param businessService
     */
    public void putBusinessService(String type,IBaseSpringService<? extends BaseAssetEntity, Serializable>  businessService){
    	 businesshandlerMap.put(type,businessService);
    }
    /**
     * 设置上链服务对象存储到MAP
     * @param type
     * @param uploadService
     */
    public void putUploadService(String type,IBaseUploadAssetService uploadService){
    	 uploadServicehandlerMap.put(type,uploadService);
    }
    /**
     * 存储表与编码的关系MAP
     * @param type
     * @param tableName
     */
    public void putTableMap(String tableName,String type){
        tableMap.put(tableName,type);
    }

    /**
     * get assetCode by tableName Of Entity
     * @param tableName
     * @return
     */
    public String getCodeByTableName(String tableName){
        return tableMap.get(tableName);
    }

    public int getBussinessMapSize(){
    	return businesshandlerMap.size();
    }

    public Map<String, String> getTableMap() {
        return tableMap;
    }

    /* *//**
     *
     * 描述：获取资产对应的业务服务
     * @author linxiaoqing
     * @Date 2020年1月10日 上午10:19:23
     * @param type
     * @return
     *//*
    public static IBaseSpringService<? extends BaseAssetEntity, Serializable> getBusinessService(@NonNull UploadTypeEnum type) {
        return (IBaseSpringService<? extends BaseAssetEntity, Serializable>) SpringContextUtil.getBean(type.getBusinessService());
    }

    *//**
     *
     * 描述：获取资产对应的上链服务
     * @author linxiaoqing
     * @Date 2020年1月10日 上午10:19:40
     * @param type
     * @return
     *//*
    public static IBaseUploadAssetService getUploadService(@NonNull UploadTypeEnum type) {
        return (IBaseUploadAssetService) SpringContextUtil.getBean(type.getUploadService());
    }*/

}
