package com.bdc.api.rest.container;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.tools.common.cache.CacheFactory;
import com.badou.tools.common.cache.CacheManager;
import com.badou.tools.common.util.SpringHelper;
import com.badou.tools.common.util.StringUtils;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.common.utils.EmptyUtil;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 外网权限缓存
 *
 * @author zhongzhilong
 * @date 2020-12-3 11:31
 */
public class InterfacePermissionContainer implements CacheManager {

    private static final long serialVersionUID = -3404793818265854947L;

    private String _className = this.getClass().getName();

    /**
     * 实例对象
     */
    private final static InterfacePermissionContainer _instance = new InterfacePermissionContainer();

    /**
     * 获取实例
     * @return
     */
    public static InterfacePermissionContainer getInstance (){
        return _instance;
    }

    /**
     * 初始化缓存
     */
    public void init (){
        IInterFacePermissionService iInterFacePermissionService = SpringHelper.getBean(IInterFacePermissionService.class);
        iInterFacePermissionService.listAll()
                .stream()
                .filter(e -> StringUtils.isNotEmpty(e.getNetInformatId()))
                .collect(Collectors.groupingBy(a -> a.getNetInformatId()))
                .forEach((k,v) -> {
                    CacheFactory.getInstance().setValue(_className, k, v, false);
                });
    }

    /**
     * 通过接口ID和外网ID获取权限
     * @param interfaceId 接口ID
     * @param netInformatId 外网ID
     * @return
     */
    public InterFacePermissionEntity get (String interfaceId,String netInformatId){
        if (StringUtils.isEmpty(interfaceId) || StringUtils.isEmpty(netInformatId)){
            return null;
        }
        List<InterFacePermissionEntity> interFacePermissionEntities = get(netInformatId);
        if (EmptyUtil.isEmpty(interFacePermissionEntities)){
            InterFacePermissionEntity interFacePermissionEntity = findDb(interfaceId, netInformatId);
            if (Objects.nonNull(interFacePermissionEntity)){
                put(interFacePermissionEntity.getNetInformatId(),interFacePermissionEntity);
                return interFacePermissionEntity;
            }
        } else {
            List<InterFacePermissionEntity> collect = interFacePermissionEntities.stream().filter(e -> Objects.equals(interfaceId, e.getInterfaceId())).collect(Collectors.toList());
            if (EmptyUtil.isNotEmpty(collect)){
                return collect.get(0);
            } else {
                InterFacePermissionEntity interFacePermissionEntity = findDb(interfaceId, netInformatId);
                if (Objects.nonNull(interFacePermissionEntity)){
                    put(interFacePermissionEntity.getNetInformatId(),interFacePermissionEntity);
                    return interFacePermissionEntity;
                }
            }
        }
        return null;
    }

    /**
     * 删除全部
     * @param netInformatId 外网ID
     */
    public void removeAll (String netInformatId){
        evict(netInformatId);
    }

    /**
     * 删除单个
     * @param interFacePermissionEntity
     */
    public void remove (InterFacePermissionEntity interFacePermissionEntity){
        List<InterFacePermissionEntity> list = get(interFacePermissionEntity.getNetInformatId());
        if (Objects.nonNull(list)){
            for (int i = 0; i < list.size(); i++) {
                InterFacePermissionEntity permissionEntity = list.get(i);
                if (permissionEntity.getInterfaceId().equals(interFacePermissionEntity.getInterfaceId())){
                    list.remove(i);
                }
            }
            put(interFacePermissionEntity.getNetInformatId(),list);
        }
    }

    /**
     * 查找数据库
     * @param interfaceId 接口ID
     * @param netInformatId 外网ID
     * @return
     */
    public InterFacePermissionEntity findDb (String interfaceId,String netInformatId){
        IInterFacePermissionService iInterFacePermissionService = SpringHelper.getBean(IInterFacePermissionService.class);
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("interfaceId",interfaceId , null, QueryOperSymbolEnum.eq));
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("netInformatId", netInformatId , null, QueryOperSymbolEnum.eq));
        List<InterFacePermissionEntity> entitys =  iInterFacePermissionService.find(queryCriterion);
        if (EmptyUtil.isNotEmpty(entitys)){
            return entitys.get(0);
        }
        return null;
    }

    /**
     * 通过外网ID获取权限列表
     * @param key
     * @return
     */
    @Override
    public List<InterFacePermissionEntity> get(Object key) {
        List<InterFacePermissionEntity> interFacePermissionEntitys = (List<InterFacePermissionEntity>) CacheFactory.getInstance().getValue(_className, key,
                System.currentTimeMillis());
        return interFacePermissionEntitys;
    }

    /**
     * 添加权限
     * @param key 外网ID
     * @param value 权限
     */
    @Override
    public void put(Object key, Object value) {
        if (value instanceof Collection){
            CacheFactory.getInstance().setValue(_className, key, value, false);
        } else {
            List<InterFacePermissionEntity> interFacePermissionEntities = get(key);
            if (EmptyUtil.isNotEmpty(interFacePermissionEntities)){
                interFacePermissionEntities.add((InterFacePermissionEntity) value);
                CacheFactory.getInstance().setValue(_className, key, interFacePermissionEntities, false);
            } else {
                List<InterFacePermissionEntity> list = Lists.newArrayList();
                list.add((InterFacePermissionEntity) value);
                CacheFactory.getInstance().setValue(_className, key, list, false);
            }
        }
    }

    /**
     * 删除权限
     * @param key
     */
    @Override
    public void evict(Object key) {
        CacheFactory.getInstance().remove(_className, key);
    }

    @Override
    public void destroy() {
        CacheFactory.getInstance().clear(_className);
    }

}
