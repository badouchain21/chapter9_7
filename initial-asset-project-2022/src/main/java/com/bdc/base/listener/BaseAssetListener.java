package com.bdc.base.listener;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import com.bdc.apple.model.AppleEntity;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.base.service.impl.BaseUploadAssetServiceImpl;
import com.bdc.common.annotate.UploadAssetTypeHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.base.support.spring.IBaseSpringService;

import javax.persistence.Table;


/**
 * 通过注解 初始化资产服务Map对象
 * @author lps
 *
 */
@Component
public class BaseAssetListener implements ApplicationListener<ContextRefreshedEvent> {
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(UploadAssetTypeHandler.class);
        BaseUploadAssetFactory baseUploadAssetFactory = event.getApplicationContext().getBean(BaseUploadAssetFactory.class);
        beans.forEach((name, bean) -> {
        	UploadAssetTypeHandler typeHandler = bean.getClass().getAnnotation(UploadAssetTypeHandler.class);
        	if(bean.getClass().getSuperclass().equals(BaseUploadAssetServiceImpl.class)){
        		baseUploadAssetFactory.putUploadService(typeHandler.value().code, (IBaseUploadAssetService)bean);
        	}else{
            	baseUploadAssetFactory.putBusinessService(typeHandler.value().code, (IBaseSpringService<? extends BaseAssetEntity, Serializable>)bean);
				Class modelClass =this.getActualTypeArgument(bean.getClass());
				if(modelClass!=null){
					ClassLoader loader =modelClass.getClassLoader();
					try {
						Class tableClass=loader.loadClass(Table.class.getName());
						Annotation table = modelClass.getDeclaredAnnotation(tableClass);
						if(table!=null)
							baseUploadAssetFactory.putTableMap(table.getClass().getMethod("name").invoke(table).toString(),typeHandler.value().code);
					} catch (Exception e) {
						//continue;
					}

				}
			}
        });
    }
	/*
	 * 获取泛型类Class对象，不是泛型类则返回null
	 */
	private  Class<?> getActualTypeArgument(Class<?> clazz) {
		Class<?> entitiClass = null;
		Type genericSuperclass = clazz.getSuperclass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length > 0) {
				entitiClass = (Class<?>) actualTypeArguments[0];
			}
		}

		return entitiClass;
	}

	public static void main(String[] args) {
		Class<?> modelClass = AppleEntity.class;
		if (modelClass != null) {
			ClassLoader loader = modelClass.getClassLoader();
			// Class src = loader.loadClass("com.cj.Annotation.AnnotationParsing");
			try {
				Class tableClass = loader.loadClass(Table.class.getName());
				Annotation table = modelClass.getDeclaredAnnotation(tableClass);
				System.out.println(table.getClass().getMethod("name").invoke(table).toString());
			} catch (Exception e) {
				e.printStackTrace();
				//continue;
			}
		}
	}

}
