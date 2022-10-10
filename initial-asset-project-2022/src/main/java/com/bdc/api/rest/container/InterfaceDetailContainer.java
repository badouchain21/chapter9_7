package com.bdc.api.rest.container;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.tools.common.cache.CacheFactory;
import com.badou.tools.common.cache.CacheManager;
import com.badou.tools.common.util.SpringHelper;
import com.badou.tools.common.util.StringUtils;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.common.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Objects;

/**
 * 接口信息缓存
 *
 * @author zhongzhilong
 * @date 2020-12-3 11:31
 */
public class InterfaceDetailContainer implements CacheManager {

    private static final long serialVersionUID = -3404793818266854947L;

    private String _className = this.getClass().getName();

    /**
     * 实例对象
     */
    private final static InterfaceDetailContainer _instance = new InterfaceDetailContainer();

    /**
     * 获取实例
     * @return
     */
    public static InterfaceDetailContainer getInstance (){
        return _instance;
    }

    /**
     * 初始化缓存
     */
    public void init (){
        IInterFaceDetailService interFaceDetailService = SpringHelper.getBean(IInterFaceDetailService.class);
        interFaceDetailService.listAll().forEach(e -> {
            String name = e.getName();
            String creator = e.getCreator();
            CacheFactory.getInstance().setValue(_className, getKey(name,creator), e, false);
        });
    }

    /**
     * 通过接口名和用户ID获取接口信息
     * @param name 接口名
     * @param creator 用户ID
     * @return
     */
    public InterFaceDetailEntity get (String name,String creator){
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(creator)){
            return null;
        }
        String key = getKey(name,creator);
        InterFaceDetailEntity interFaceDetailEntity = get(key);
        if (Objects.isNull(interFaceDetailEntity)){
            IInterFaceDetailService interFaceDetailService = SpringHelper.getBean(IInterFaceDetailService.class);
            QueryCriterion queryCriterion = new QueryCriterion();
            queryCriterion.addParam(new QueryHibernatePlaceholderParam("name", name, null, QueryOperSymbolEnum.eq));
            queryCriterion.addParam(new QueryHibernatePlaceholderParam("creator", creator, null, QueryOperSymbolEnum.eq));
            List<InterFaceDetailEntity> interFaceDetailEntities = interFaceDetailService.find(queryCriterion);
            if (EmptyUtil.isNotEmpty(interFaceDetailEntities)){
                interFaceDetailEntity = interFaceDetailEntities.get(0);
                put(interFaceDetailEntity);
            }
        }
        return interFaceDetailEntity;
    }

    /**
     * 添加接口信息
     * @param interFaceDetailEntity
     */
    public void put (InterFaceDetailEntity interFaceDetailEntity){
        String key = getKey(interFaceDetailEntity.getName(),interFaceDetailEntity.getCreator());
        put(key,interFaceDetailEntity);
    }

    /**
     * 获取key
     * @param name
     * @param userId
     * @return
     */
    private String getKey (String name,String userId){
        return DigestUtils.md5DigestAsHex((name + userId).getBytes(Charsets.UTF_8));
    }

    @Override
    public InterFaceDetailEntity get(Object key) {
        InterFaceDetailEntity interFaceDetailEntity = (InterFaceDetailEntity) CacheFactory.getInstance().getValue(_className, key,
                System.currentTimeMillis());
        return interFaceDetailEntity;
    }

    @Override
    public void put(Object key, Object value) {
        if (value instanceof InterFaceDetailEntity){
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
