package com.badou.platform.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.platform.PlatformConst;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.vo.ResourceVersionVO;
import com.badou.platform.web.form.PlatformResourceForm;

/**
 * 云中心资源表service接口
 * @ClassName IPlatformResoutceService
 * @Description TODO(这里用一句话描述这个类的作用)
 * @version 1.0.0
 */
public interface IPlatformResourceService extends IBaseSpringService<PlatformResourceEntity, Serializable> {

	/**
	 * 根据资源id查找相应实体
	 * @param moduleId
	 * @author chenjiabo
	 * @return 云中心资源实体
	 */
	PlatformResourceEntity findByResourceId(String resourceId, Integer type);

	/**
	 * 根据模型id获取所有的版本列表
	 * @param resourceId
	 * @param type
	 * @author chenjiabao
	 * @return 云中心资源form表单分页集合对象
	 * @throws IOException if has error(直接往外抛)
	 */
	LigeruiListVO<PlatformResourceForm> getAllVersionList(String resourceId, Integer type) throws IOException;
	 
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:15:48
	 * @todo 根据模型id和选择的版本id想云中心发送请求，下载相应的版本文件
	 * @param versionId
	 * @param resourceId
	 * @param versionCode
	 * @param version1
	 * @param version2
	 * @param version3
	 * @param type
	 * @param operType
	 * @param updateType
	 * @return 资源id
	 * @throws IOException if has error(直接往外抛)
	 */
	String updateResource(String versionId, String resourceId, String versionCode, 
			String version1, String version2, String version3, Integer type, Integer operType, Integer updateType) throws IOException;
	
	/**
	 * 创建模型
	 * @param versionId
	 * @param platformResourceId
	 * @param versionCode
	 * @param version1
	 * @param version2
	 * @param version3
	 * @param type
	 * @param operType
	 * @param updateType
	 * @return 资源id
	 * @throws IOException if has error(直接往外抛)
	 */
	public String createResource(String versionId ,String platformResourceId ,String versionCode, 
			String version1, String version2, String version3, Integer type, Integer operType, Integer updateType) throws IOException; 

	
	/**
	 * 根据资源id获取版本数据
	 * @Description (TODO描述这个方法的作用)
	 * @author wjw
	 * @Date 2018年3月1日 下午2:36:42
	 * @Updator bduser18
	 * @UpdateDate 2018年3月1日 下午2:36:42
	 * @UpdateDesc (更新内容描述)
	 * @param resourceId
	 * @param type
	 * @return 版本json数据
	 * @throws Exception if has error(直接往外抛)
	 */
	public String getVersionData(String resourceId, Integer type) throws Exception;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:34:11
	 * @todo 上传模型数据到云中心
	 * @param resourceId
	 * @param type
	 * @param versionVo
	 * @param remark
	 * @param isNew
	 * @return 结果相关数据
	 * @throws Exception if has error(直接往外抛)
	 */
	Map<String,Object> createUploadData(String resourceId, Integer type, ResourceVersionVO versionVo,String remark,Integer isNew) throws Exception;

	/**
	 * 判断用户选择的版本文件本地是否已存在
	 * @param resourceId  资源id
	 * @param type 资源类型
	 * @param versionCode 版本编码
	 * @param version1 版本号1
	 * @param version2 版本号2
	 * @param version3 版本号3
	 * @author chenjiabao
	 * @date 2018年3月2日上午10:31:35
	 * @return 是否存在，若存在则返回对应的资源id，不存在则返回null
	 * @throws Exception if has error(直接往外抛)
	 */
	String isExistLocal(String resourceId, Integer type, String versionCode, String version1,
			String version2, String version3) throws Exception;
	
	/**
	 * 判断远程的资源在本地是否存在
	 * @param platformIds
	 * @param type
	 * @return 是否存在，若存在则返回对应的资源id集合，不存在则返回空集合
	 * @throws Exception if has error(直接往外抛)
	 */
	List<String> isExistLocal(List<String> resourceIds, Integer type) throws Exception;
	
	/**
	 * 查询存在于本地，但不存在与云中心的模型
	 * @return 需要上传的模型，列表元素结构：{"id": String, "is_new": Integer} 
	 * 	       id为模型id，is_new是否为初次上传，1是，0否。
	 * @author wujunliang
	 */
	List<Map<String, Object>> findNoExistCenterModule();
	
	/**
	 * @param versionResourceId
	 * @param remark
	 * @return 上传文件参数
	 */
	Map<String, String> getUploadFileParams(String versionResourceId);
	
	/**
	 * 上传模型的文件到云中心
	 * @param oper
	 * @param resourceId
	 * @param dataMap
	 */
	void submitModuleFielToCenter(ResourceOperEntity oper,String resourceId,Map<String,String> dataMap);

	/**
	 * 根据类型查询所有的资源
	 * @param resourceType 资源类型
	 * <ul>
	 * 	<li>模型资源：0，常量：{@link PlatformConst#RESOUTCE_TYPE_MODULE}</li>
	 * 	<li>插件资源：1，常量：{@link PlatformConst#RESOUTCE_TYPE_PLUGIN}</li>
	 * </uL>
	 * 
	 * @return 云中心资源实体类集合
	 */
	List<PlatformResourceEntity> queryAllResource(Integer resourceType);

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:22:20
	 * @todo 排除不需要更新的模型
	 * @param list
	 * @param data
	 * @return 返回的JSONArray中的元素格式与data参数的格式相同，JSONArray中的元素多了一个resourceId
	 *         的属性，为资源的id。假如是模型，就是md_module表的id
	 */
	JSONArray findUpdatResource(List<PlatformResourceEntity> list, String data);
}
