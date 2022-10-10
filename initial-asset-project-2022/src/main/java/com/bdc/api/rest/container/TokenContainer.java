package com.bdc.api.rest.container;

import com.alibaba.fastjson.JSONObject;
import com.badou.tools.common.cache.CacheFactory;
import com.badou.tools.common.cache.CacheManager;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：缓存用户生成的token
 *
 * @author zhongzhilong
 * @date 2020-11-07 14:34
 */
public class TokenContainer implements CacheManager {

    private static final long serialVersionUID = -3404793818266854947L;

    private String _className = this.getClass().getName();

    private final static TokenContainer _instance = new TokenContainer();

    public static TokenContainer getInstance() {
        return _instance;
    }

    @Override
    public JSONObject get(Object key) {
        JSONObject jsonObject = (JSONObject) CacheFactory.getInstance().getValue(_className, key,
                System.currentTimeMillis());
        return jsonObject;
    }

    @Override
    public void put(Object key, Object value) {
        if (value instanceof JSONObject){
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
