package com.badou.platform.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.badou.brms.attach.AttachFactory;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.base.support.vo.LigeruiListVO;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.global.Globals;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.designer.module.design.dao.IModuleDesignDAO;
import com.badou.platform.PlatformConst;
import com.badou.platform.PlatformHelper;
import com.badou.platform.dao.IPlatformResourceDAO;
import com.badou.platform.dao.IResourceOperDAO;
import com.badou.platform.model.PlatformResourceEntity;
import com.badou.platform.model.ResourceOperAttachEntity;
import com.badou.platform.model.ResourceOperEntity;
import com.badou.platform.service.IPlatformResourceService;
import com.badou.platform.service.IResourceOperAttachService;
import com.badou.platform.service.IResourceOperService;
import com.badou.platform.util.UploadModuleZipFactoryThread;
import com.badou.platform.vo.ApiReturnVO;
import com.badou.platform.vo.ResourceVersionVO;
import com.badou.platform.web.form.PlatformResourceForm;
import com.badou.project.common.util.ProjectPropertiesLoader;

/**
 * @author chenjiabao
 * @date 2019???7???2?????????10:17:15
 * @todo ????????????service???????????????
 */
@Service
public class PlatformResourceServiceImpl extends BaseSpringService<PlatformResourceEntity, Serializable> implements IPlatformResourceService {

	@Autowired
	private IPlatformResourceDAO platformResourceDAO;
	
	@Autowired
	private IResourceOperService resourceOperService ;
	
	@Autowired
	private IResourceOperAttachService resourceOperAttachService ;
	
	@Autowired
	private IModuleDesignDAO moduleDesignDAO ;
	
	@Autowired
	private IResourceOperDAO resourceOperDAO ;
	
	@Autowired
	private AttachFactory attachFactory;
	
	/**
	 * @auth chenjiabao
	 * @date 2019???7???2?????????5:33:10
	 * @todo ???????????????dao
	 * @param platformResourceDAO
	 */
	@Autowired
	public void setPlatformResourceDAO(IPlatformResourceDAO platformResourceDAO) {
		this.platformResourceDAO = platformResourceDAO;
		super.setBaseDAO(platformResourceDAO);
	}
	
	@Override
	public PlatformResourceEntity findByResourceId(String resourceId, Integer type) {
		List<PlatformResourceEntity> list = this.platformResourceDAO.findByResourceId(resourceId, type);
		if(list != null && !list.isEmpty() && list.size() > 0){
			return list.get(0);
		}else{
			return null ;
		}
	}

	@Override
	public LigeruiListVO<PlatformResourceForm> getAllVersionList(String resourceId, Integer type) throws IOException {
		//???????????????????????????
		PlatformResourceEntity entity = this.findByResourceId(resourceId, type);
		LigeruiListVO<PlatformResourceForm>  listvo = new LigeruiListVO<PlatformResourceForm>();
		//???????????????????????????????????????????????????????????????vo
		if(entity != null && StringUtils.isNotBlank(entity.getPlatformResourceId())){
			//??????????????????
			String postEntity = assemblyAllVersionParam(entity);
			//????????????
			ApiReturnVO vo = PlatformHelper.httpPost("/project/listVersion.do", null, postEntity.toString());
			JSONObject json = JSONObject.fromObject(vo.getData()) ;
			JSONArray ja = json.getJSONArray("rows");
			List<PlatformResourceForm> list = JSONArray.toList(ja, PlatformResourceForm.class);
			
			// ??????????????????
			setCurrentVersion(list, entity);
			
			listvo.setTotal(Integer.valueOf(json.get("total").toString()));
			listvo.setTRows(list);
		}
		return listvo;
	}
	
	/**
	 * ??????????????????????????????????????????????????? getAllVersionList(String, Integer) ?????????
	 * @param list
	 * @param resourceId
	 */
	private void setCurrentVersion(List<PlatformResourceForm> list, PlatformResourceEntity pr) {
		Optional<String> version1 = Optional.ofNullable(pr.getVersion1());
		Optional<String> version2 = Optional.ofNullable(pr.getVersion2());
		Optional<String> version3 = Optional.ofNullable(pr.getVersion3());
		for (PlatformResourceForm f : list) {
			if (version1.orElse("").equals(f.getVersion1())
					&& version2.orElse("").equals(f.getVersion2())
					&& version3.orElse("").equals(f.getVersion3())) {
				f.setCurrentVersion(true);
				break;
			}
		}
	}

	/**
	 * ????????????????????????????????????
	 * @param entity
	 * @author chenjiabao
	 * @return ????????????
	 */
	private String assemblyAllVersionParam(PlatformResourceEntity entity) {
		Pagelet pagelet = (Pagelet)PaginationTheadLocal.getDefaultPagelet();
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("pageSize", pagelet.getPerPageSize());
		map.put("pageNo", pagelet.getPageNo());
		map.put("resourceId", entity.getPlatformResourceId());
//		map.put("versionCode", entity.getVersionCode());
		map.put("version1", entity.getVersion1());
		map.put("version2", entity.getVersion2());
		map.put("version3", entity.getVersion3());
		map.put("type", entity.getType());
		return PlatformHelper.assemblyParam(map);
	}

	@Override
	public String updateResource(String versionId , String resourceId ,String versionCode, String version1, String version2, String version3 , Integer type , Integer operType, Integer updateType) throws IOException {
		//???????????????????????????
		PlatformResourceEntity entity = this.findByResourceId(resourceId, type);
		
		if (Integer.valueOf(1).equals(updateType)) {
			String err = checkVersion(entity, versionCode, version1, version2, version3);
			if (err != null) {
				throw new RuntimeException(err);
			}
		}
		
		Map<String , Object> map = new HashMap<String , Object>();
		//???????????????????????????????????????????????????????????????vo
		if(entity != null && StringUtils.isNotBlank(entity.getPlatformResourceId())){
			map.put("resourceId", entity.getPlatformResourceId());
			map.put("resourceVersionId", versionId);
			String postEntity = PlatformHelper.assemblyParam(map);
			//????????????
			ApiReturnVO vo = PlatformHelper.httpPost("/project/download.do", null, postEntity.toString());
			ResourceOperEntity resource = new ResourceOperEntity();
			//??????????????????
			if(ApiReturnVO.SUCCESS.equals(vo.getReturnCode())){
				//?????????????????????????????????
				JSONObject json = JSONObject.fromObject(vo.getData());
				resource.setPlatformResourceId(entity.getId());
				resource.setCode(json.getString("code"));
				resource.setName(json.getString("name"));
				resource.setUrl(json.getString("url"));
				resource.setVersionCode(versionCode);
				resource.setVersion1(version1);
				resource.setVersion2(version2);
				resource.setVersion3(version3);
			}else{
				resource.setFailMsg(vo.getReturnMsg());
			}
			resource.setType(type);
			resource.setOperType(operType);
			resource.setUpdateType(updateType);
			resource.setCreateTime(new Date());
			this.resourceOperService.create(resource);
			return resource.getId();
		}else{
			return null;
		}
	}
	
	
	@Override
	public String createResource(String versionId ,String platformResourceId, String versionCode, String version1, String version2, String version3 , Integer type , Integer operType, Integer updateType) throws IOException {
		//???????????????????????????
		PlatformResourceEntity entity =  new PlatformResourceEntity();
		entity.setPlatformResourceId(platformResourceId);
		entity.setType(type);
		entity.setStatus(PlatformConst.DOWNLOAD_STATUS_DOING);
		entity.setVersion1(version1);
		entity.setVersion2(version2);
		entity.setVersion3(version3);
		entity.setVersionCode(versionCode);
		this.create(entity);
		Map<String , Object> map = new HashMap<String , Object>();
		//???????????????????????????????????????????????????????????????vo
		if(entity != null && StringUtils.isNotBlank(entity.getPlatformResourceId())){
			map.put("resourceId", entity.getPlatformResourceId());
			map.put("resourceVersionId", versionId);
			String postEntity = PlatformHelper.assemblyParam(map);
			//????????????
			ApiReturnVO vo = PlatformHelper.httpPost("/project/download.do", null, postEntity.toString());
			ResourceOperEntity resource = new ResourceOperEntity();
			//??????????????????
			if(ApiReturnVO.SUCCESS.equals(vo.getReturnCode())){
				//?????????????????????????????????
				JSONObject json = JSONObject.fromObject(vo.getData());
				resource.setPlatformResourceId(entity.getId());
				resource.setCode(json.getString("code"));
				resource.setName(json.getString("name"));
				resource.setUrl(json.getString("url"));
				resource.setVersionCode(versionCode);
				resource.setVersion1(version1);
				resource.setVersion2(version2);
				resource.setVersion3(version3);
			}else{
				resource.setFailMsg(vo.getReturnMsg());
			}
			resource.setType(type);
			resource.setOperType(operType);
			resource.setUpdateType(updateType);
			resource.setCreateTime(new Date());
			this.resourceOperService.create(resource);
			return resource.getId();
		}else{
			return null;
		}
	}
	
	
	@Override
	public String getVersionData(String resourceId, Integer type) throws Exception {
		String versionJson = null;
		//???????????????id?????????????????????id??????????????????????????????????????????
		String cloudResourceId = null;
		
		PlatformResourceEntity platformResourceEntity = this.findByResourceId(resourceId, type);
		
		if(null == platformResourceEntity){
			return null;
		}
		cloudResourceId = platformResourceEntity.getPlatformResourceId();
		if(StringUtils.isNotBlank(cloudResourceId)){
			//????????????????????????
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("resourceId", cloudResourceId);
			map.put("type", type);
			String postEntity = PlatformHelper.assemblyParam(map);
			ApiReturnVO returnVo = PlatformHelper.httpPost("project/listLatestVersion.do", null, postEntity);//alibaba.fastjson.JSONObject.parseObject(result, ApiReturnVO.class);
			if(ApiReturnVO.SUCCESS.equals(returnVo.getReturnCode())){
				JSONObject returnDataJson = JSONObject.fromObject(returnVo.getData());
				Integer total = returnDataJson.getInt("total");
				if(null!=total && total>0){
					//??????version?????????json??????
					versionJson = jointVersionStr(returnDataJson.getString("rows"),platformResourceEntity);
				}
			}
		}
		return versionJson;
	}
	
	/**
	 * ??????version????????????????????????????????????json
	 * @Description (TODO???????????????????????????)
	 * @author wjw
	 * @Date 2018???3???1??? ??????11:42:49
	 * @Updator bduser18
	 * @UpdateDate 2018???3???1??? ??????11:42:49
	 * @UpdateDesc (??????????????????)
	 * @return ?????????????????????json
	 * @throws Exception if has error(???????????????)
	 */
	private String jointVersionStr(String rows,PlatformResourceEntity platformResourceEntity) throws Exception{
		if(StringUtils.isBlank(rows)){
			return null;
		}
		com.alibaba.fastjson.JSONArray returnArr = new com.alibaba.fastjson.JSONArray();
		com.alibaba.fastjson.JSONArray jsonArr = com.alibaba.fastjson.JSONArray.parseArray(rows);
		for(int i=0;i<jsonArr.size();i++){
			ResourceVersionVO versionVo = jsonArr.getObject(i, ResourceVersionVO.class);
			String versionCode = versionVo.getVersionCode();
			String version1 = versionVo.getVersion1();
			String version2 = versionVo.getVersion2();
			String version3 = versionVo.getVersion3();
			
			JSONObject returnData = new JSONObject();
			returnData.put("id", versionCode+Globals.INFOS_SPLITOR_UL+version1+Globals.INFOS_SPLITOR_UL+version2+Globals.INFOS_SPLITOR_UL+version3);
			returnData.put("text", versionCode);
			if(platformResourceEntity.getVersionCode().equals(versionCode)){
				returnData.put("isSelect", 1);
				if(platformResourceEntity.getVersion1().equals(version1) && platformResourceEntity.getVersion2().equals(version2) && platformResourceEntity.getVersion3().equals(version3)){
					returnData.put("isSameVersion", 1);
				} else {//?????????????????????????????????????????????
					returnData.put("isSameVersion", 0);
					returnData.put("oldVersion", platformResourceEntity.getVersionCode()+ platformResourceEntity.getVersion1() +platformResourceEntity.getVersion2() +platformResourceEntity.getVersion3());
				}
			} else {
				returnData.put("isSelect", 0);
			}
			returnArr.add(returnData);
		}
		return returnArr.toJSONString();
	}

	@Override
	public Map<String,Object> createUploadData(String resourceId, Integer type, ResourceVersionVO versionVo, String remark,Integer isNew) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		PlatformResourceEntity platformResourceEntity = this.findByResourceId(resourceId, type);
		//?????????????????????
		Map<String, Object> map = judgeVersion(versionVo,isNew,platformResourceEntity);
		JsonReturnBean returnBean = (JsonReturnBean)map.get("returnBean");
		returnMap.put("returnBean", returnBean);
		if(!returnBean.isHasOk()){
			return returnMap;
		}
		if(null == platformResourceEntity){
			platformResourceEntity = new PlatformResourceEntity();
		}
		//????????????????????????id????????????
		ResourceVersionVO returnVersionVo = (ResourceVersionVO)map.get("returnVersionVo");
		platformResourceEntity.setPlatformResourceId(returnVersionVo.getResourceId());
	 	platformResourceEntity.setResourceId(resourceId);
		platformResourceEntity.setVersion1(returnVersionVo.getVersion1());
		platformResourceEntity.setVersion2(returnVersionVo.getVersion2());
		platformResourceEntity.setVersion3(returnVersionVo.getVersion3());
		platformResourceEntity.setVersionCode(returnVersionVo.getVersionCode());
		platformResourceEntity.setType(type);
		platformResourceEntity.setStatus(PlatformConst.UPLOAD_STATUS_DOING);
		if(StringUtils.isNotBlank(platformResourceEntity.getId())){
			this.update(platformResourceEntity);
		} else {
			this.create(platformResourceEntity);
		}
		//?????????????????????
		ResourceOperEntity operEntity = new ResourceOperEntity();
		operEntity.setVersion1(returnVersionVo.getVersion1());
		operEntity.setVersion2(returnVersionVo.getVersion2());
		operEntity.setVersion3(returnVersionVo.getVersion3());
		operEntity.setVersionCode(returnVersionVo.getVersionCode());
		operEntity.setCreateTime(new Date());
		operEntity.setStatus(PlatformConst.UPLOAD_STATUS_DOING);
		operEntity.setName(versionVo.getResourceName());
		operEntity.setCode(versionVo.getResourceCode());
		operEntity.setOperType(PlatformConst.OPER_TYPE_UPLOAD);
		operEntity.setType(PlatformConst.RESOUTCE_TYPE_MODULE);
		operEntity.setPlatformResourceId(platformResourceEntity.getId());
		operEntity.setRemark(remark);
 		resourceOperDAO.create(operEntity);
		
		returnMap.put("operEntity", operEntity);
		returnMap.put("returnBean", returnBean);
		returnMap.put("versionResourceId", returnVersionVo.getResourceVersionId());
		return returnMap;
	}
	
	/**
	 * ????????????????????????
	 * @Description (TODO???????????????????????????)
	 * @author wjw
	 * @Date 2018???3???2??? ??????4:38:52
	 * @Updator bduser18
	 * @UpdateDate 2018???3???2??? ??????4:38:52
	 * @UpdateDesc (??????????????????)
	 * @return ??????????????????
	 * @throws Exception if has error(???????????????)
	 */
	private Map<String,Object> judgeVersion(ResourceVersionVO versionVo,Integer isNew,PlatformResourceEntity platformResourceEntity) throws Exception{
		JsonReturnBean returnBean = new JsonReturnBean();
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> judgeFirstSubmitVersionMap = new HashMap<String, Object>();
		judgeFirstSubmitVersionMap.put("resourceCode", versionVo.getResourceCode());
		judgeFirstSubmitVersionMap.put("resourceName", URLEncoder.encode(versionVo.getResourceName(), "utf-8"));
		String postEntity = assemblyParamByVersion(versionVo,judgeFirstSubmitVersionMap);
		ResourceVersionVO returnVersionVo = new ResourceVersionVO();
		String versionResourceId = null;
 		if(null!=isNew && SystemBoolean.YES.getKey().equals(isNew)){//???????????????
 			returnVersionVo.setVersionCode(versionVo.getVersionCode());
			returnVersionVo.setVersion1(versionVo.getVersion1());
			returnVersionVo.setVersion2(versionVo.getVersion2());
			returnVersionVo.setVersion3(versionVo.getVersion3());
			
			ApiReturnVO returnVo = PlatformHelper.httpPost("/project/judgeFirstSubmitVersion.do", null, postEntity.toString());
			if(ApiReturnVO.SUCCESS.equals(returnVo.getReturnCode())){
				JSONObject returnDataJson = JSONObject.fromObject(returnVo.getData());
				Boolean isExists = returnDataJson.getBoolean("isExists");
				if(null!=isExists && isExists.equals(false)){//???????????????????????????????????????id???
					versionResourceId = returnDataJson.getString("versionResourceId");
 					returnVersionVo.setResourceVersionId(versionResourceId);
					returnVersionVo.setResourceId(returnDataJson.getString("resourceId"));
					returnBean.setHasOk(true);
					returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
				} else {
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_FAIL);
					returnBean.setMessage(returnDataJson.getString("msg"));
				}
			} else {
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage(returnVo.getReturnMsg());
			}
		} else {//???????????????????????????,?????????????????????????????????????????????????????????????????????
			//?????????????????????????????????????????????????????????????????????????????????????????????id??????
			if(versionVo.getVersionCode().equals(platformResourceEntity.getVersionCode())){
				postEntity += "&resourceId="+platformResourceEntity.getPlatformResourceId();
			}
		/*	result = HttpInvoker.httpPost("http://192.168.1.171:8080/platform/api/project/judgeLatestVersion.do", null, postEntity.toString());
			
			ApiReturnVO returnVo = com.alibaba.fastjson.JSONObject.parseObject(result, ApiReturnVO.class);*/
			ApiReturnVO returnVo = PlatformHelper.httpPost("/project/judgeLatestVersion.do", null, postEntity.toString());
			if(ApiReturnVO.SUCCESS.equals(returnVo.getReturnCode())){
				//com.alibaba.fastjson.JSONObject returnDataJson = (com.alibaba.fastjson.JSONObject)returnVo.getData();
				JSONObject returnDataJson = JSONObject.fromObject(returnVo.getData());
				Boolean isLatest = returnDataJson.getBoolean("isLatest");
				if(isLatest){//???????????????????????????????????????id???
					versionResourceId = returnDataJson.getString("resourceVersionId");
					returnVersionVo.setResourceId(returnDataJson.getString("resourceId"));
					returnVersionVo.setResourceVersionId(versionResourceId);
					returnVersionVo.setVersion1(returnDataJson.getString("version1"));
					returnVersionVo.setVersion2(returnDataJson.getString("version2"));
					returnVersionVo.setVersion3(returnDataJson.getString("version3"));
					returnVersionVo.setVersionCode(returnDataJson.getString("versionCode"));
 					returnBean.setHasOk(true);
					returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
 					//??????????????????
				} else {
					returnBean.setHasOk(false);
					returnBean.setTip(JsonReturnBean.TIP_FAIL);
					returnBean.setMessage(returnDataJson.getString("msg"));
				}
			} else {
				returnBean.setHasOk(false);
				returnBean.setTip(JsonReturnBean.TIP_FAIL);
				returnBean.setMessage(returnVo.getReturnMsg());
			}
		}
		map.put("returnVersionVo", returnVersionVo);
		map.put("returnBean", returnBean);
		return map;
	}

	/**
	 * ??????????????????
	 * @param entity
	 * @author wjw
	 * @return ????????????
	 */
	private String assemblyParamByVersion(ResourceVersionVO versionVo,Map<String,Object> map) {
		map.put("versionCode", versionVo.getVersionCode());
		map.put("version1", versionVo.getVersion1());
		map.put("version2", versionVo.getVersion2());
		map.put("version3", versionVo.getVersion3());
		map.put("type", versionVo.getType());
		return PlatformHelper.assemblyParam(map);
	}

	@Override
	public String isExistLocal(String resourceId, Integer type, String versionCode,
			String version1, String version2, String version3) throws Exception {
		boolean isExistLocal = false ;
		PlatformResourceEntity entity = this.findByResourceId(resourceId, type);
		if(entity != null){
			ResourceOperEntity resource = this.resourceOperService.findByPlatformResourceId(entity.getId() , versionCode , version1 , version2 , version3);
			if(resource != null){
				ResourceOperAttachEntity att = this.resourceOperAttachService.findByOperId(resource.getId());
				if(att != null && StringUtils.isNotBlank(att.getAttachId())){
					isExistLocal = attachFactory.fileIsExist(att.getAttachId()) ;
					if(isExistLocal){
						return resource.getId();
					}
				}
			}
		}
		return null ;
	}

	@Override
	public List<String> isExistLocal(List<String> resourceIds, Integer type) throws Exception {
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("platformResourceId", resourceIds, null, QueryOperSymbolEnum.in, QueryParam.PARAM_PLACEHOLDER_NAME));
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("type", type, null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
 		queryCriterion.addParam(new QueryHibernatePlaceholderParam("status", PlatformConst.DOWNLOAD_STATUS_SUCCESS, null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
		queryCriterion.setMaxResults(Integer.MAX_VALUE);
		List<PlatformResourceEntity> lp = this.find(queryCriterion);
		List<String> existIds = new ArrayList<String>();
		for(PlatformResourceEntity p : lp){
			existIds.add(p.getPlatformResourceId());
 		}
		return existIds;
	}
	
	/**
	 * @param entity			???????????????????????????????????????????????????
	 * @param versionCode		??????????????????
	 * @param version1			?????????????????????
	 * @param version2			?????????????????????
	 * @param version3			?????????????????????
	 * 
	 * @return	???????????????null????????????????????????????????????
	 */
	private String checkVersion(PlatformResourceEntity entity, String versionCode,
			String version1, String version2, String version3) {
		
		if (!entity.getVersionCode().equals(versionCode)) {
			return "?????????????????????";
		}
		
		if (version1 == null || version2 == null || version3 == null) {
			return "???????????????????????????????????????";
		}

		String oldVersion = entity.getVersion1() + entity.getVersion2() + entity.getVersion3();
		String newVersion = version1 + version2 + version3;
		if (newVersion.compareTo(oldVersion) <= 0) {
			return "??????????????????????????????????????????????????????";
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findNoExistCenterModule() {
		return platformResourceDAO.findNoExistCenterModule();
	}

	@Override
	public Map<String, String> getUploadFileParams(String versionResourceId) {
		Map<String,String> postMap = new HashMap<String, String>();
		postMap.put("resourceVersionId ", versionResourceId);
		postMap.put("projectId", ProjectPropertiesLoader.getProjectId());
		postMap.put("token", ProjectPropertiesLoader.getToken());
		postMap.put("userId", ProjectPropertiesLoader.getUserId());
		return postMap;
	}

	@Override
	public void submitModuleFielToCenter(ResourceOperEntity oper, String moduleId, Map<String, String> dataMap) {
		/*
		 * ????????????????????????????????? UploadModuleZipFactoryThread(oper, moduleId, dataMap).run()
		 * ?????? Hibernate session(org.hibernate.LazyInitializationException) ??????????????????
		 * ??????????????????Service???????????????
		 */
		new UploadModuleZipFactoryThread(oper, moduleId, dataMap).run();
	}

	@Override
	public List<PlatformResourceEntity> queryAllResource(Integer resourceType) {
		if (resourceType == null){
			throw new NullPointerException("resourceType ????????????");
		}
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam("type", resourceType, null, QueryOperSymbolEnum.eq,QueryParam.PARAM_PLACEHOLDER_NAME));
		List<PlatformResourceEntity> list = find(queryCriterion);
		return list;
	}

	/**
	 * ??????????????????????????????
	 * @param list ????????????????????????????????????
	 * @param data ???????????????????????????????????????json????????????????????????
	 *        <pre>
	 *        [{
	 *	        'platformResourceId' : ????????????id, ????????????????????????????????????y_resource_module??????id
	 *	        'code'               : ?????????code
	 *	        'name'               : ???????????????
	 * 	        'versionId'          : ??????ID??????????????????y_uplaod_his??????id,
	 *	        'versionCode'        : ????????????,
	 *	        'version1'           : ?????????1,
	 *	        'version2'           : ?????????2,
	 *	        'version3'           : ?????????3
	 *        }]
	 *        </pre>
	 * 
	 * @return ?????????JSONArray?????????????????????data????????????????????????JSONArray????????????????????????resourceId
	 *         ????????????????????????id???????????????????????????md_module??????id
	 * 
	 * @author wujunliang 2018-11-12
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray findUpdatResource(List<PlatformResourceEntity> list, String data) {
		// ???list??????map?????????????????????
		Map<String, PlatformResourceEntity> localResource = list.stream()
				.collect(Collectors.toMap(
						PlatformResourceEntity::getPlatformResourceId,
						Function.identity()
				));
		
		JSONArray arr = JSONArray.fromObject(data);
		JSONArray result = new JSONArray();
		arr.stream()
				.filter(o -> {
					/*
					 * ?????????????????????????????????????????????????????????????????????????????????
					 * 1. ????????????????????????????????????
					 * 2. ??????????????????????????????????????????????????????
					 */
					JSONObject obj = (JSONObject) o;
					String key = obj.getString("platformResourceId");
					if (localResource.containsKey(key)) {
						PlatformResourceEntity pr = localResource.get(key);
						String localVesion = pr.getVersion1() + pr.getVersion2() + pr.getVersion3();
						String cloudVersion = obj.getString("version1") + obj.getString("version2") + obj.getString("version3");
						return cloudVersion.compareTo(localVesion) > 0;
					}
					return false;
				})
				.forEach(o -> {
					JSONObject obj = (JSONObject) o;
					String key = obj.getString("platformResourceId");
					// ??????resourceId??????
					obj.put("resourceId", localResource.get(key).getResourceId());
					result.add(obj);
				});
		
		return result;
	}
}
