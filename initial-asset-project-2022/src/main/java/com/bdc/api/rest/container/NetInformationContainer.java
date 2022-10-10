package com.bdc.api.rest.container;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.tools.common.cache.CacheFactory;
import com.badou.tools.common.cache.CacheManager;
import com.badou.tools.common.util.SpringHelper;
import com.badou.tools.common.util.StringUtils;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.common.utils.EmptyUtil;
import org.apache.commons.io.Charsets;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;

/**
 * 外网信息缓存
 *
 * @author zhongzhilong
 * @date 2020-12-3 11:19
 */
public class NetInformationContainer implements CacheManager {

    private static final long serialVersionUID = -3404743818266854947L;

    private String _className = this.getClass().getName();

    /**
     * 实例对象
     */
    private final static NetInformationContainer _instance = new NetInformationContainer();

    /**
     * 获取实例
     * @return
     */
    public static NetInformationContainer getInstance (){
        return _instance;
    }

    /**
     * 初始化缓存
     */
    public void init (){
        INetInformationService netInformationService = SpringHelper.getBean(INetInformationService.class);
        netInformationService.listAll().forEach(e -> {
            String appId = e.getAppId();
            String appkey = e.getAppkey();
            CacheFactory.getInstance().setValue(_className, getKey(appId,appkey), e, false);
        });
    }

    /**
     * 通过APPID和APPKEY获取外网信息
     * @param appId
     * @param appKey
     * @return
     */
    public NetInformationEntity get (String appId,String appKey){
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appKey)){
            return null;
        }
        String key = getKey(appId,appKey);
        NetInformationEntity netInformationEntity = get(key);
        if (Objects.isNull(netInformationEntity)){
            INetInformationService netInformationService = SpringHelper.getBean(INetInformationService.class);
            QueryCriterion queryCriterion = new QueryCriterion();
            queryCriterion.addParam(new QueryHibernatePlaceholderParam("appId", appId, null, QueryOperSymbolEnum.eq));
            queryCriterion.addParam(new QueryHibernatePlaceholderParam("appKey", appKey, null, QueryOperSymbolEnum.eq));
            List<NetInformationEntity> netInformationEntitys =  netInformationService.find(queryCriterion);
            if (EmptyUtil.isNotEmpty(netInformationEntitys)){
                netInformationEntity = netInformationEntitys.get(0);
                put(netInformationEntity);
            }
        }
        return netInformationEntity;
    }

    /**
     * 添加外网信息
     * @param netInformationEntity
     */
    public void put (NetInformationEntity netInformationEntity){
        String key = getKey(netInformationEntity.getAppId(),netInformationEntity.getAppkey());
        put(key,netInformationEntity);
    }

    /**
     * 获取Key
     * @param appId
     * @param appKey
     * @return
     */
    private String getKey (String appId,String appKey){
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appKey)){
            return "";
        }
        return DigestUtils.md5DigestAsHex((appId + appKey).getBytes(Charsets.UTF_8));
    }

    /**
     * 删除外网信息
     * @param netInformationEntity
     */
    public void remove (NetInformationEntity netInformationEntity){
        String key = getKey(netInformationEntity.getAppId(), netInformationEntity.getAppkey());
        evict(key);
    }

    @Override
    public NetInformationEntity get(Object key) {
        NetInformationEntity value = (NetInformationEntity) CacheFactory.getInstance().getValue(_className, key,
                System.currentTimeMillis());
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        if (value instanceof NetInformationEntity){
            CacheFactory.getInstance().setValue(_className, key, value, false);
        }
    }

    @Override
    public void evict(Object key) {
        CacheFactory.getInstance().remove(_className, key);
    }

    @Override
    public void destroy() {
        CacheFactory.getInstance().clear(_className);
    }
}
