package com.badou.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:03:14
 * @todo 复制工具类
 */
public class CopyHandle  {  
	
	/**
	 * log4j日志记录
	 */
	protected static Logger logger = Logger.getLogger(CopyHandle.class);
	
    private static CopyHandle instance = null;  
      
    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午4:51:24
     * @todo 无参构造 
     */
    private CopyHandle(){}  
    
    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午4:51:47
     * @todo 获取实例
     * @return 实例对象
     */
    public static CopyHandle getInstance(){  
        if(null == instance){  
            instance = new CopyHandle();  
        }  
        return instance;  
    }  
      
    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午4:52:10
     * @todo 拷贝对象
     * @param object
     * @return 复制的对象
     */
    public Object copy(Object object) {       
        Object objectCopy = null;  
        try {  
            if(null == object){
            	return null;  
            } 
            Class<?> classType = object.getClass();             
            Field[] fields = classType.getDeclaredFields();  
              
            objectCopy = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});  
              
            Field field = null;  
            String suffixMethodName;  
            Method getMethod;
            Method setMethod;  
            Object value = null;  
            for (int i = 0; i < fields.length; i++) {  
                try {
					field = fields[i];  
					if(field.getName().equals("serialVersionUID") || field.getName().equals("updator") || field.getName().equals("creator")){
						continue;
					}
					suffixMethodName = field.getName().substring(0, 1).toUpperCase() + (field.getName().length()>1?field.getName().substring(1):"");  
					      
					getMethod = classType.getMethod("get" + suffixMethodName, new Class[] {});    
					setMethod = classType.getMethod("set" + suffixMethodName, new Class[] { field.getType() });  
     
					value = getMethod.invoke(object, new Object[] {});  
					
					if(null == value){  
					    if(field.getType().getName().equalsIgnoreCase("java.lang.String")){  
					        setMethod.invoke(objectCopy, new Object[] { "" });  
					    }  
					} else if(field.getType().getName().equalsIgnoreCase("java.util.Set")){  
							continue;
					}else{
						 setMethod.invoke(objectCopy, new Object[] { value });  
					}
				} catch (Exception e) {
					logger.error(e);
				}  
            }  
        } catch (NoSuchMethodException e) {  
            logger.error("CopyHandle.copy(): NoSuchMethodException: "+ e);
        } catch (Exception e) {  
            logger.error("CopyHandle.copy(): Exception: "+ e);
        }
        return objectCopy;
    }
    
    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午4:52:35
     * @todo 拷贝对象
     * @param object 源对象
     * @param objectCopy 目标对象
     * @return 目标对象
     * @throws NoSuchMethodException if has error(直接往外抛)
     */
    public Object copy(Object object,Object objectCopy) throws NoSuchMethodException {       
      //  Object objectCopy = null;  
        try {  
            if(null == object){
            	return null;  
            }
              
            Class<?> classType = object.getClass();             
            Field[] fields = classType.getDeclaredFields();  
              
          //  objectCopy = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});  
              
            Field field = null;  
            String suffixMethodName;  
            Method getMethod ; 
            Method setMethod;  
            Object value = null;  
            for (int i = 0; i < fields.length; i++) {  
                try {
					field = fields[i];  
					if(field.getName().equals("serialVersionUID") || field.getName().equals("updator") || field.getName().equals("creator")){
						continue;
					}
					suffixMethodName = field.getName().substring(0, 1).toUpperCase() + (field.getName().length()>1?field.getName().substring(1):"");  
					      
					getMethod = classType.getMethod("get" + suffixMethodName, new Class[] {});    
					setMethod = classType.getMethod("set" + suffixMethodName, new Class[] { field.getType() });  
     
					value = getMethod.invoke(object, new Object[] {});  
					
					if(null == value){  
					    if(field.getType().getName().equalsIgnoreCase("java.lang.String")){  
					        setMethod.invoke(objectCopy, new Object[] { "" });  
					    }  
					} else if(field.getType().getName().equalsIgnoreCase("java.util.Set")){  
							continue;
					}else{
						 setMethod.invoke(objectCopy, new Object[] { value });  
					}
				} catch (Exception e) {
					logger.error(e);
				}  
            }  
        } catch (Exception e) {  
            logger.error("CopyHandle.copy(): Exception: "+ e);
        }
        return objectCopy;
    }  
  
}  