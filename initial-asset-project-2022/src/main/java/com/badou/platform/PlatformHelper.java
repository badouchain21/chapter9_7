package com.badou.platform;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.badou.platform.vo.ApiReturnVO;
import com.badou.project.common.util.ProjectPropertiesLoader;
import com.badou.tools.common.util.HttpInvoker;

/**
 * 云中心相关帮助类
 * @author cjb
 *
 */
public class PlatformHelper {

	public static final String REQUEST_URL = ProjectPropertiesLoader.getPlatformUrl()+"api/";
	
	/**
	 * 用于在请求云中心接口时拼装参数
	 * @param params
	 * @return 拼装结果
	 * @author chenjiabao
	 * @date 下午3:35:48
	 */
	public static String assemblyParam(Map<String , Object> params){
		StringBuffer postEntity = new StringBuffer();
		//固定拼接上项目id，从配置文件中读取
		postEntity.append("projectId=" + ProjectPropertiesLoader.getProjectId());
		postEntity.append("&token=" + ProjectPropertiesLoader.getToken());
		postEntity.append("&userId=" + ProjectPropertiesLoader.getUserId());
		//判断参数是否为空，若不为空则进行拼装
		if(params != null && !params.isEmpty()){
			//使用Lambda方式循环拼接参数
			params.forEach((k , v) -> {
					postEntity.append("&" + k + "=" + v) ;	
			} );
		}
		return postEntity.toString();
	}
	
	/**
	 * 用于在请求云中心接口时拼装参数
	 * @param params
	 * @return 拼装结果
	 * @author chenjiabao
	 * @date 下午3:35:48
	 */
	public static Map<String , String>  assemblyDefaultParam(Map<String,String> postMap){
		if(postMap == null){
			postMap = new HashMap<String, String>();
		}
 		postMap.put("projectId", ProjectPropertiesLoader.getProjectId());
		postMap.put("token", ProjectPropertiesLoader.getToken());
		postMap.put("userId", ProjectPropertiesLoader.getUserId());
		return postMap;
	}
	
	/**
	 * 用于在请求云中心接口时拼装参数
	 * @param params
	 * @return 拼装结果
	 * @author chenjiabao
	 * @date 下午3:35:48
	 */
	public static String assemblyParam(String tempPostEntity){
		StringBuffer postEntity = new StringBuffer();
		//固定拼接上项目id，从配置文件中读取
		postEntity.append("projectId=" + ProjectPropertiesLoader.getProjectId());
		postEntity.append("&token=" + ProjectPropertiesLoader.getToken());
		postEntity.append("&userId=" + ProjectPropertiesLoader.getUserId());
		postEntity.append(tempPostEntity);
		return postEntity.toString();
	}
	
	

	
	/**
	 * GET请求
	 * @param getUrl
	 * @throws IOException if has error(直接往外抛)
	 * @return 请求返回结果
	 */
	public static ApiReturnVO httpGet(String getUrl) throws IOException { 
		ApiReturnVO vo = new ApiReturnVO();
		if(getUrl.contains("?")){
			getUrl = getUrl+"&"+assemblyParam("");
		}else{
			getUrl = getUrl+"?"+assemblyParam("");
		}
		String returnBean = HttpInvoker.httpGet(REQUEST_URL+getUrl);
		//返回结果
		JSONObject jsonObject = JSONObject.fromObject(returnBean);
		vo = (ApiReturnVO) JSONObject.toBean(jsonObject, ApiReturnVO.class);
		return vo;
	}
	 
	/**
	 * POST请求
	 * @param postUrl
	 * @param postHeaders
	 * @param postEntity
	 * @throws IOException if has error(直接往外抛)
	 * @return API统一封装vo
	 */
	public static ApiReturnVO httpPost(String postUrl,Map<String, String> postHeaders, String postEntity) throws IOException {
		ApiReturnVO vo = new ApiReturnVO();	
		String returnBean =  HttpInvoker.httpPost(REQUEST_URL+postUrl, postHeaders, postEntity);
		//返回结果
		JSONObject jsonObject = JSONObject.fromObject(returnBean);
		vo = (ApiReturnVO) JSONObject.toBean(jsonObject, ApiReturnVO.class);
		return vo;
	}
	
}
