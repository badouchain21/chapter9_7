package com.bdc.api.rest.service.impl;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.badou.brms.attach.AttachConfig;
import com.badou.brms.attach.AttachFactory;
import com.badou.brms.attach.AttachUtil;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.attach.service.IAttachService;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParamGroup;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.brms.org.service.IEmployeeService;
import com.badou.brms.system.org.vo.User;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.event.error.exception.DateParseException;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.designer.jdbc.common.util.DataBeanSelector;
import com.badou.designer.jdbc.common.util.DataSelector;
import com.badou.designer.jdbc.common.web.form.BaseCommonSaveForm;
import com.badou.designer.jdbc.core.service.impl.BaseModuleService;
import com.badou.designer.jdbc.core.vo.BaseVO;
import com.badou.designer.jdbc.core.vo.FieldVO;
import com.badou.designer.jdbc.core.vo.SimpleVO;
import com.badou.designer.module.database.dao.IModuleDatabaseDao;
import com.badou.designer.module.design.ModuleCacheContainer;
import com.badou.designer.module.design.ModuleConstants;
import com.badou.designer.module.design.base.IModuleEntity;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.model.MdModuleLinkEntity;

import com.badou.security.ssl.token.TokenWarehouse;
import com.badou.security.ssl.token.pojo.Token;
import com.badou.security.ssl.token.service.ITokenService;
import com.badou.security.ssl.token.utils.EncryptAndDecodeUtil;
import com.badou.tools.common.Globals;
import com.badou.tools.common.file.jxlexcel.ExcelImportException;

import com.badou.tools.common.util.convert.BigDecimalConvert;
import com.badou.tools.common.util.convert.DoubleConvert;
import com.badou.tools.common.util.convert.FloatConvert;
import com.badou.tools.common.util.convert.IntegerConvert;
import com.badou.utils.SqlTypeLoader;
import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.interfacedetail.model.InterFaceDetailEntity;
import com.bdc.api.intermanage.interfacedetail.service.IInterFaceDetailService;
import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import com.bdc.api.intermanage.interfacelogger.service.IInterFaceLoggerService;
import com.bdc.api.intermanage.interfacepermission.model.InterFacePermissionEntity;
import com.bdc.api.intermanage.interfacepermission.service.IInterFacePermissionService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.api.rest.StatusEnum;
import com.bdc.api.rest.container.InterfaceDetailContainer;
import com.bdc.api.rest.container.InterfacePermissionContainer;
import com.bdc.api.rest.container.NetInformationContainer;
import com.bdc.api.rest.container.TokenContainer;
import com.bdc.api.rest.dao.IBaseRestDAO;
import com.bdc.api.rest.service.IBaseRestService;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.common.CommonConst;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.FileUtils;
import com.bdc.rabbitmq.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:41:13
 * @todo 基础restservice接口实现类
 */
@Slf4j
@Service

public class BaseRestService extends BaseModuleService implements IBaseRestService {

	@Autowired
	private IBaseRestDAO baseRestDAO;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private IInterFaceDetailService interFaceDetailService;
	@Autowired
	private INetInformationService netInformationService;
	@Autowired
	private IInterFacePermissionService interFacePermissionService;
	@Autowired
	private IInterFaceLoggerService interFaceLoggerService;
	@Autowired
	private IModuleDatabaseDao moduleDatabaseDao;
	@Autowired
	private AttachFactory attachFactory;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	private static final int LOGGER_SUCCESS = 0;

	private static final int LOGGER_FAIL = 1;

//	@Value("${kill 140}")
	private String dgwhgFilePath;

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:43:36
	 * @todo 注入dao
	 * @param baseRestDAO
	 */
	@Autowired
	public void setBaseRestDAO(IBaseRestDAO baseRestDAO) {
		this.baseRestDAO = baseRestDAO;
		setBaseDAO(baseRestDAO);
	}

	@Override
	@SuppressWarnings("unused")
	public Map<String ,Object> authPermission(Token tokenEntity, HttpServletRequest request)
	{
		boolean isSuccess = false;
		Map<String,Object> result = new HashMap<String,Object>();
		String message = null;
      	String appId = tokenEntity.getAppId();
      	String appKey =tokenEntity.getAppKey();
		JSONObject userInfo = TokenContainer.getInstance().get(tokenEntity.getToken());
		this.setLogin(userInfo);

		//返回状态
		int status= StatusEnum.OTHER_EXCEPTION.getStatus();
		int logStatus = LOGGER_FAIL;
		//返回错误信息
		Throwable error = null;
		List<String> ips = null;
		String ipaddress = null;
		Boolean isIp = false;
		String interfaceName = null;
		//日志对象
		InterFaceLoggerEntity loggerEntity = new InterFaceLoggerEntity();
		Date date = new Date();//获得系统时间.
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);//将时间格式转换成符合Timestamp要求的格式.
		Timestamp time1 =Timestamp.valueOf(time);//把时间转换
		loggerEntity.setRequestTime(time1);
		//请求方式
		loggerEntity.setRequestType(request.getMethod());

		//获取外网和接口对象
        String methodName = request.getRequestURI();
        methodName = StringUtils.remove(methodName,request.getContextPath());
		methodName = methodName.replace(CommonConst.REST_API_PREFIX, "");
		// 通过接口进行访问，不进行切割
		String methodNameeByInterface = methodName.toString();
		if(methodName.indexOf("/") != -1){
			methodName = methodName.substring(0,methodName.lastIndexOf("/"));
		}

		//获取接口信息
		InterFaceDetailEntity faceDetailEntity= InterfaceDetailContainer.getInstance().get(methodName,userInfo.getString("userId"));
		if (Objects.isNull(faceDetailEntity)){
			faceDetailEntity = InterfaceDetailContainer.getInstance().get(methodNameeByInterface,userInfo.getString("userId"));
		}

		//获取外网信息
		NetInformationEntity netInformationEntity= NetInformationContainer.getInstance().get(appId,appKey);
		if(netInformationEntity==null){
			//系统不存在
			status = StatusEnum.OUTTER_SYSNULL.getStatus();
			logStatus = LOGGER_FAIL;
		}else if(faceDetailEntity==null){
			//接口不存在
			status = StatusEnum.OUTTER_INTERNULL.getStatus();
			logStatus = LOGGER_FAIL;

		}else if(SystemBoolean.YES.getKey()==netInformationEntity.getIsCallInterface().intValue())
		{
			status = StatusEnum.OUTTER_START.getStatus();
			logStatus = LOGGER_FAIL;

		}else if(SystemBoolean.YES.getKey()==faceDetailEntity.getStatus().intValue()){
			//接口不存在
			status = StatusEnum.OUTTER_START.getStatus();
			logStatus = LOGGER_FAIL;
		}
		else{
			//判断是否存在或者有权限
			InterFacePermissionEntity interFacePermissionEntity = InterfacePermissionContainer.getInstance().get(faceDetailEntity.getId(), netInformationEntity.getId());

			if(Objects.isNull(interFacePermissionEntity)
					|| interFacePermissionEntity.getInterfacePermission().intValue() == SystemBoolean.YES.getKey().intValue()){
				//没有权限
				status = StatusEnum.OUTTER_AUTHORITY.getStatus();
				logStatus = LOGGER_FAIL;
			}else{
				if(faceDetailEntity.getStatus().intValue()!=0 && netInformationEntity.getIsCallInterface().intValue()!=0){
				 	status = StatusEnum.OUTTER_START.getStatus();
				 	logStatus = LOGGER_FAIL;
				}else{
					//接口已经开启
					//接口正常
					status = StatusEnum.NORMAL.getStatus();
					logStatus = LOGGER_SUCCESS;
				}
			}
		}
		//写入日志
		loggerEntity.setInterfacename(methodName);
		loggerEntity.setAppId(appId);
		loggerEntity.setAppkey(appKey);
		loggerEntity.setIsSuccess(logStatus);
		loggerEntity.setToken(tokenEntity.getToken());
		loggerEntity.setSourceUrl(request.getRequestURL().toString());
        loggerEntity.setRequestParam(getRequestParams(request).toJSONString());
		loggerEntity.setToken(tokenEntity.getToken());
		loggerEntity.setCreator(userInfo.getString("userId"));
		loggerEntity.setCreatorName(userInfo.getString("userName"));

		if(logStatus==LOGGER_FAIL){
			loggerEntity.setFailreason(StatusEnum.getMessageByStatus(status).getMessage());
			Date enddate = new Date();//获得系统时间.
			String enddtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(enddate);//将时间格式转换成符合Timestamp要求的格式.
			Timestamp enddtime1 =Timestamp.valueOf(enddtime);//把时间转换
			loggerEntity.setEndTime(enddtime1);
	        //耗时
	        loggerEntity.setLateTime((new Date().getTime()-loggerEntity.getRequestTime().getTime())+"ms");
//			interFaceLoggerService.create(loggerEntity);
			String logger = JSON.toJSONString(loggerEntity);
			rabbitTemplate.convertAndSend(logger);
		}else if(logStatus==LOGGER_SUCCESS){
			//如果发生错误，直接拦截，返回错误信息
			//如果没有发生错误，不拦截
			//结束时间
			Date date2 = new Date();//获得系统时间.
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date2);//将时间格式转换成符合Timestamp要求的格式.
			time1 =Timestamp.valueOf(time);//把时间转换
//			interFaceLoggerService.create(loggerEntity);
			isSuccess = true;
		}


		request.setAttribute("requestDate", date.getTime());
		request.setAttribute("loggerEntity", loggerEntity);
		result.put("isSuccess", isSuccess);
		result.put("message", StatusEnum.getMessageByStatus(status).getMessage());
//		result.put("logId", loggerEntity.getId());
		return result;
	}

	/**
	 *
	 * 描述：解析请求参数，组装成JSON格式
	 * @author linxiaoqing
	 * @date 2019年10月25日
	 * @param request
	 * @return
	 */
	private JSONArray getRequestParams(HttpServletRequest request) {
        JSONArray requestParams = new JSONArray();
        try {
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                JSONObject requestParam = new JSONObject();
                requestParam.put("name", entry.getKey());
                requestParam.put("value", URLDecoder.decode(entry.getValue()[0], "UTF-8"));
                requestParams.add(requestParam);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return requestParams;
	}
	@Autowired
	private DruidDataSource dataSource;

	@Override
	public BaseVO saveBaseVO( MdModuleEntity moduleBean, Map<String, String> defaultFieldValue,BaseCommonSaveForm form,BaseVO baseVO, Boolean isCreate ) throws Exception {
		StringBuilder error = new StringBuilder();
		String message = "";
		int type =2;
		if(dataSource.getDriverClassName().contains("mysql")){
			type =1;
		}
//		int type = moduleDatabaseDao.find(moduleBean.getDataSourceId()).getType();

		Set<MdFieldEntity> fieldsSet = new HashSet<>();
		fieldsSet.addAll(moduleBean.getFields());
		//判断关系
		for(MdModuleLinkEntity link : moduleBean.getLinkSet()){
			if (ModuleConstants.ONE_TO_ONE.equals(link.getType())) {
				MdModuleEntity mdModule = ModuleCacheContainer.getInstance().get(link.getRelatedModuleId());
				fieldsSet.addAll(mdModule.getFields());
			}
		}
		// 获取模型基类，判断是否允许更新
		IModuleEntity moduleBase =loadBaseInstance(moduleBean);
		Map<String,FieldVO> dic = new HashMap<String, FieldVO>();
		Map<String, MdFieldEntity> map = genByModuleInRest(fieldsSet,dic);
		Map<Integer, MdFieldEntity> fields = new HashMap<Integer, MdFieldEntity>();
		Map<String, Object> defaultFields = new HashMap<String, Object>();
		Map<Integer,String> displayNames = new HashMap<Integer,String>();
		StringBuffer fieldsNames = new StringBuffer();
		Map<String,String[]> details = form.getDetails();

		generateDisplayName(dic ,fields, displayNames , details ,fieldsNames);
		//获取标题行对应字段
		fields = getDetailInRest(map, displayNames);
		//获取默认字段值
		Map<String,Object> defaultDetail = validateDefaultFieldValue(defaultFieldValue,moduleBean);
		//是否包含所有非空字段
		message += this.hasAllNotNullFieldInRest(fieldsSet, fieldsNames.toString());

		if(StringUtils.isNotBlank(message)){
			throw new Exception(message);
		}

		SimpleVO simple = new SimpleVO();
		if(baseVO != null) {
		    simple.setId(baseVO.getId());
		} else {
	        simple.setId(form.getId());
	        simple.getDetail().put("id", form.getId());
		}
		simple.setModule(moduleBean);
		simple.setTableName(moduleBean.getDbTable());
		simple.getDetail().putAll(defaultDetail);
		// 把表单数据设置进simple,有错误信息时抛出异常
		List<Attach> attachList = setSimpleVo(error, type, fieldsSet, dic, fields, details, simple);
		// 唯一校验、是否可以更新校验
		validSimple(error, moduleBase, fields, simple);
		// 上传附件
		myUploadAttach(fieldsSet, form, baseVO);
		// 把附件相关字段赋值给simple
		if(baseVO!=null)
			simple.getDetail().putAll(baseVO.getDetailMap());

		if(StringUtils.isNotBlank(error.toString())){
			throw new Exception(error.toString());
		}
		saveSimpleVo( simple, isCreate);
		IAttachService attachService = InstanceFactory.getInstance(IAttachService.class);
		attachList.forEach((item)->{
			item.setResourceId(simple.getId());
			attachService.create(item);
		});


		return 	genreateReturnVO( simple, baseVO);
	}

	/**
	 * 导入的字段集是否包含所有非空字段
	 * @param baseFields 实体类下所有字段集
	 * @param fieldsName 导入标题栏对应字段集
	 * @return 结果信息
	 * @throws ExcelImportException
	 */

	public String hasAllNotNullFieldInRest(Set<MdFieldEntity> baseFields,String fieldsName) throws ExcelImportException{
		String message = "";
		for(MdFieldEntity baseField : baseFields){
			//跳过主键
			if("id".equals(baseField.getForm().getEntityName().toLowerCase())){
				continue;
			}
			//该字段为必填字段
			if(baseField.getNullable()!= 1 && StringUtils.isBlank(baseField.getDefaultValue())){
				//导入的字段集不包含非空字段
				if(!(fieldsName.contains(baseField.getEntityName()) || fieldsName.contains(baseField.getFieldName()))){
					message+=baseField.getForm().getEntityName()+"不可为空;";
				}
			}

		}
		return message;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午7:37:21
	 * @todo 获取列名对应的字段集,判断标题字段是否存在数据库表结构中
	 * @param map
	 * @param displayNames
	 * @return 对应的结果集对象
	 * @throws ExcelImportException if has error(直接往外抛)
	 */
	public Map<Integer, MdFieldEntity> getDetailInRest(Map<String, MdFieldEntity> map,Map<Integer,String> displayNames) throws ExcelImportException{
		if(displayNames.size() >0){
			Map<Integer, MdFieldEntity> field = new HashMap<Integer, MdFieldEntity>();
			int i=0;
			for(Integer key : displayNames.keySet()){
				String displayName = displayNames.get(key);
				if("access_token".equals(displayName)){
					continue;
				}
				if(map.containsKey(displayName)){
					field.put(i,map.get(displayName));
				}else{
					throw new ExcelImportException("数据库中没有字段：'"+displayName+"'");
				}
				i++;
			}
			return field;
		}
		return null;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:45:15
	 * @todo 根据模型生成相关数据
	 * @param fields
	 * @param dic
	 * @return 结果集信息
	 * @throws Exception if has error(直接往外抛)
	 */
	protected Map<String, MdFieldEntity> genByModuleInRest(Set<MdFieldEntity> fields, Map<String, FieldVO> dic) throws Exception{
		Map<String, MdFieldEntity> map = new HashMap<String, MdFieldEntity>();

		for(MdFieldEntity field : fields){
			map.put(field.getForm().getEntityName(), field);
			if(field.getForm().getDataSource()!=null){
				FieldVO vo = new FieldVO();
				if(StringUtils.isNotBlank(field.getForm().getValueFieldID())){
					if(field.getEntityName().equals(field.getForm().getValueFieldID())){
						vo.setId(field);
					}else {
						for(MdFieldEntity f : fields){
							if(f.getEntityName().equals(field.getForm().getValueFieldID())){
								vo.setId(f);
							}
						}
					}
				}else{
					vo.setId(field);
				}
				if(field.getEntityName().equals(field.getForm().getValueFieldText())){
					vo.setText(field);
				}else {
					for(MdFieldEntity f : fields){
						if(f.getEntityName().equals(field.getForm().getValueFieldText())){
							vo.setText(f);
						}
					}
				}
				if(ModuleConstants.DATA_SOURCE_DIC.equals(field.getForm().getDataSource())){
					vo.setDetails(DictionaryLib.getDictionarySelectByCode(field.getForm().getDicName()));
				}else if(ModuleConstants.DATA_SOURCE_CUSTOM.equals(field.getForm().getDataSource()) && StringUtils.isNotBlank(field.getForm().getDataBean())){
					try {
						Class<?> c = Class.forName(field.getForm().getDataBean());
						DataBeanSelector bean = (DataBeanSelector)c.newInstance();
						vo.setDetails(bean.getSelector());
					} catch (Exception e) {
						logger.error("error",e);
					}
				}
				if(!vo.getDetails().isEmpty()){
					dic.put(field.getEntityName(), vo);
				}
			}
		}
		return map;
	}

	@Override
	public JsonReturnBean validToken(String token) throws Exception {
	    JsonReturnBean returnBean = new JsonReturnBean();
	    try {
	        String encryptCode;
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	        encryptCode = new String(EncryptAndDecodeUtil.decryptMode(token));

	        // 判断是否在session里
	        // token 有没有过期 默认一天
	        Date date = sdf.parse(encryptCode.split("&")[3]);
	        if (date.getTime() < new Date().getTime()) {
	            returnBean.setHasOk(false);
	            returnBean.setMessage("token失效");
				TokenContainer.getInstance().evict(token);
	            return returnBean;
	        }
			JSONObject userInfo = TokenContainer.getInstance().get(token);
	        if (userInfo!=null)
	            returnBean.setHasOk(true);
	        else {
	            returnBean.setHasOk(false);
	            returnBean.setMessage("无法匹配token仓库的token!");
				TokenContainer.getInstance().evict(token);
	        }
	    } catch(Exception e) {
            logger.error("error",e);
            returnBean.setHasOk(false);
            returnBean.setMessage("无法匹配token仓库的token!");
			TokenContainer.getInstance().evict(token);
	    }
	    return returnBean;
	}

	@Override
	public BaseVO getVObyAssetKey(AssetDefinedEntity assetDefinedEntity,String assetKeyValue,MdModuleEntity moduleEntity,String userId) throws Exception {
		QueryCriterion queryCriterion = new QueryCriterion();
		queryCriterion.addParam(new QueryHibernatePlaceholderParam(getFieldNameByMdEntityName(moduleEntity,assetDefinedEntity.getAssetKey()),assetKeyValue,null,QueryOperSymbolEnum.eq));
		if (StringUtils.isNotEmpty(userId) && !BdLoginUtil.isSa(userId)){
			queryCriterion.addParam(new QueryHibernatePlaceholderParam("creator", userId,null,QueryOperSymbolEnum.eq));
		}
		List<BaseVO> results= this.find(queryCriterion);
		return results==null||results.size()==0?null:results.get(0);
	}

	/**
	 * 把表单数据设置进simple,有错误信息时抛出异常
	 */
	private List<Attach> setSimpleVo(StringBuilder error, int type, Set<MdFieldEntity> fieldsSet,
			Map<String, FieldVO> dic, Map<Integer, MdFieldEntity> fields, Map<String, String[]> details,
			SimpleVO simple) throws Exception {

		String content =null;
	    List<Attach> attachList = new ArrayList<Attach>();
		int j=0;
		for(String key : details.keySet()) {
			content=null;
			if("access_token".equals(key)||key.contains("-ori")){
				continue;
			}
			MdFieldEntity field = fields.get(j);
			content = details.get(field.getEntityName())!=null &&details.get(field.getEntityName()).length>0&& StringUtils.isNotBlank(details.get(field.getEntityName())[0])? details.get(field.getEntityName())[0].trim():null;
			//非数据库字段不保存
			if(SystemBoolean.NO.getKey().equals(field.getIsDbField()) && !"whgAttachList".equals(field.getEntityName())){
				j++;
				continue;
			}

			if(ModuleConstants.COMMON_NO.equals(field.getNullable()) && StringUtils.isBlank(content)){
				error.append(field.getEntityName()+"不能为空");
				throw new Exception(error.toString());
			}


			if(StringUtils.isNotBlank(content)){
				if ((field.getLength() != null) && (field.getLength().intValue() < content.length())) {
					error.append(field.getEntityName()+"超出长度");
					throw new Exception(error.toString());
				}
				if(dic.containsKey(field.getEntityName())){
					FieldVO vo = dic.get(field.getEntityName());
					if(vo.getText()!=null){
						simple.addDetail(vo.getText(), content.trim());
					}
					String value = vo.getValue(content);
					if(StringUtils.isBlank(value)){
					    log.info(field.getForm().getEntityName()+"的数据在系统内,匹配不到对应的数据字典值!");
						//error.append(field.getForm().getEntityName()+"的数据在系统内不存在，已设为空值");
						//throw new Exception(error.toString());
						simple.addDetail(field, convert(type,field.getDisplayType(),content));
					}else{
						simple.addDetail(vo.getId(), value.trim());
					}
				}else if(StringUtils.isNotBlank(field.getForm().getDataBean())){
					Class<?> c = Class.forName(field.getForm().getDataBean());
					DataSelector bean = (DataSelector)c.newInstance();
					JSONObject obj = bean.getData(content);
					simple.addDetail(field, content);
					for(MdFieldEntity f : fieldsSet){
						if(obj.containsKey(f.getFieldName())){
							simple.addDetail(f, obj.get(f.getFieldName()));
						}
					}
				}
				/*else if(StringUtils.isNotBlank(field.getForm().getEditType()) &&(("imgSingle".equals(field.getForm().getEditType()))||"attach".equals(field.getForm().getEditType()))){
					File file = FileUtils.downloadFromUrl(dgwhgFilePath+content, AttachConfig.getInstance().getAttachSavePath());
					if(file==null) continue;
					Attach attach = attachFactory.uploadFile(AttachUtil.getAttach(file));
					simple.addDetail(field, convert(type,field.getDisplayType(),attach.getId()));
				}
				else if("whgAttachList".equals(field.getForm().getEntityName())){
					String[] attachs = content.split(",");
					for(String attachContent : attachs){
						File file = FileUtils.downloadFromUrl(dgwhgFilePath+attachContent, AttachConfig.getInstance().getAttachSavePath());
						if(file==null) continue;
						Attach attach = attachFactory.uploadFile(AttachUtil.getAttach(file));
						attachList.add(attach);
					}
				}*/
				else{
					try {
						Object convert = convert(type, field.getDisplayType(), content);
						if(Objects.nonNull(convert)){
							simple.addDetail(field, convert);
						}else{
							error.append(field.getForm().getDisplayName()+"请输入正确的类型"+field.getDisplayType());
							throw new Exception(error.toString());
						}
					} catch (NumberFormatException e) {
						error.append(field.getForm().getDisplayName()+"不是数字");
						throw new Exception(error.toString());
					} catch (DateParseException e) {
						error.append(field.getForm().getDisplayName()+"不是日期");
						throw new Exception(error.toString());
					}
				}
			}else{
				simple.addDetail(field, null);
			}

			j++;
		}
		return attachList;
	}

	/**
	 * 唯一校验、是否可以更新校验
	 */
	private void validSimple(StringBuilder error, IModuleEntity moduleBase, Map<Integer, MdFieldEntity> fields,
			SimpleVO simple) throws Exception {
		String id = simple.getId();
		if(StringUtils.isBlank(id)){
			if(simple.getUniqueMap()!=null&&simple.getUniqueMap().size()>0){
				baseDAO.initModule(simple.getModule());
				id = getByUnique(simple.getUniqueMap());
			}
		}else{
			id = simple.getId();
		}
		if(StringUtils.isNotBlank(id)){
			simple.setId(id);
			simple.getDetail().put("id", id);
			simple.initLinks();
			if (moduleBase != null) {
				if(!moduleBase.canUpdate(simple)){
					error.append(fields.get(0).getForm()+"该表单已提交,不允许修改!");
					throw new Exception(error.toString());
				}
			}

		}else{
			simple.initLinks();
			simple.initFields();
		}
	}

	private void myUploadAttach(Set<MdFieldEntity> fieldsSet, BaseCommonSaveForm form, BaseVO baseVO) throws Exception {
		List<Attach> attachList = null;
		for (MdFieldEntity field : fieldsSet) {
			String editType = StringUtils.isNotBlank(field.getForm().getEditType())
					? field.getForm().getEditType() : "";
			switch (editType) {
			case "imgSingle":
			case "attach":
				if(form.getDetails().containsKey("deleteAttach")){
					String[] deleteAttach = form.getDetails().get("deleteAttach");
					if(deleteAttach!=null && deleteAttach.length>0 && StringUtils.isNotBlank(deleteAttach[0])){
						deleteAttach(JSONObject.parseObject(deleteAttach[0]), baseVO, field, true);
					}
				}
				attachList = form.getAttachMap().get(field.getEntityName());
				uploadAttach(attachList, baseVO, field, true);
				break;
			case "img":
			case "attachMulti":
				if(form.getDetails().containsKey("deleteAttach")){
					String[] deleteAttach = form.getDetails().get("deleteAttach");
					if(deleteAttach!=null && deleteAttach.length>0 && StringUtils.isNotBlank(deleteAttach[0])){
						deleteAttach(JSONObject.parseObject(deleteAttach[0]), baseVO, field, false);
					}
				}
				attachList = form.getAttachMap().get(field.getEntityName());
				uploadAttach(attachList, baseVO, field, false);
				break;
			}
		}
	}

	/**
	 * 上传附件
	 * @param attachList
	 * @param o
	 * @param field
	 * @param single
	 * @throws Exception
	 */
	private void uploadAttach(List<Attach> attachList, BaseVO o,
			MdFieldEntity field, boolean single) throws Exception {
		if (attachList == null || attachList.size() == 0)
			return;

		if (single) {
			String attachId = (String) o.getFieldValueByEntityName(field.getForm().getValueFieldID());
			if (StringUtils.isNotBlank(attachId))
				attachFactory.deleteAttach(attachId);
			Attach attach = attachList.get(0);
			attach.setFileType(field.getFileType());
			attachFactory.uploadFile(attach);
			if(StringUtils.isNotBlank(field.getForm().getValueFieldID())){
				o.setFieldValue(field.getForm().getValueFieldID(), attach.getId());
			}else{
				o.setFieldValue(field.getEntityName(), attach.getId());
			}
			if(StringUtils.isNotBlank(field.getForm().getValueFieldText())){
				o.setFieldValue(field.getForm().getValueFieldText(), attach.getName());
			}
		} else {
			o.getDetailMap().remove(field.getFieldName().toLowerCase());
			for (Attach attach : attachList) {
				attach.setResourceId(o.getId());
				attach.setFileType(field.getFileType());
				attachFactory.uploadFile(attach);
			}
		}
	}

	private void deleteAttach(JSONObject deleteAttach, BaseVO o, MdFieldEntity field, boolean single) throws Exception {
		if (deleteAttach == null) return;
		String attachId = deleteAttach.getString(field.getEntityName());
		if (StringUtils.isBlank(attachId)) return;

		if (single) {
			o.setFieldValue(field.getForm().getValueFieldID(), null);
			o.setFieldValue(field.getForm().getValueFieldText(), null);
			attachFactory.deleteAttach(attachId);
		} else {
			String[] idArr = attachId.split(Globals.SEPARATOR_COMMA);
			for(String id : idArr)
				attachFactory.deleteAttach(id);
		}
	}


	/**
	 * 构建displayName
	 * @param dic
	 * @param fields
	 * @param displayNames
	 * @param details
	 * @param fieldsNames
	 */
	public void generateDisplayName(Map<String,FieldVO> dic ,Map<Integer, MdFieldEntity> fields,Map<Integer,String> displayNames ,Map<String,String[]> details ,StringBuffer fieldsNames){
		int i = 0;
		for(String key : details.keySet()){
			if("access_token".equals(key)||key.contains("-ori")){
				continue;
			}
			String name = key;
			if(dic.containsKey(name)){
				if(dic.get(name).getId()!=null){
					fieldsNames.append(dic.get(name).getId().getFieldName()+",");
				}
				if(dic.get(name).getText()!=null){
					fieldsNames.append(dic.get(name).getText().getFieldName()+",");
				}
			}else{
				fieldsNames.append(name+",");
			}
			displayNames.put(i, key);
			i++;
		}
	}

	/**
	 * 保存simpleVO 创建/更新
	 * @param simple
	 * @param id
	 */
	public void saveSimpleVo(SimpleVO simple, Boolean isCreate){
		if(isCreate){
            baseDAO.create(simple);
		}else{
            baseDAO.update(simple);
		}
	}


	/**
	 * 组装返回vo
	 * @param simple
	 * @param id
	 * @param baseVO
	 */
	 public BaseVO genreateReturnVO(SimpleVO simple,BaseVO baseVO){
		 BaseVO vo = new BaseVO(simple.getModule());
		 vo.setId(simple.getId());
		 if(baseVO == null){
				baseVO = new BaseVO();
		 }
		 baseVO.getDetailMap().putAll(simple.getDetail());
		 vo.setDetailMap(baseVO.getDetailMap());
		 return vo;
	 }

	/**
	 * get fieldname by entityname
	 * @param moduleEntity
	 * @param entityName
	 * @return
	 */
	private String getFieldNameByMdEntityName(MdModuleEntity moduleEntity,String entityName){
		List<MdFieldEntity> mdFieldEntities =moduleEntity.getFields().stream().filter((item)->item.getEntityName().equals(entityName)).collect(Collectors.toList());
		return mdFieldEntities==null||mdFieldEntities.size()==0?null:mdFieldEntities.get(0).getFieldName();
	}

	public void setLogin(JSONObject userInfo){
		LogonCertificate logon = new LogonCertificate(null, userInfo.getString("userId"), userInfo.getString("userName"), "", "");
		LogonCertificateHolder.setLogonCertificate(logon);
	}
	@Autowired
	private SqlTypeLoader sqlTypeLoader;

	@Override
	public Object convert(int type, String displayType, String content) throws Exception {
		String dbType = "";
		if (type == 1) {
			dbType = "mysql";
		} else if (type == 2) {
			dbType = "oracle";
		} else if (type == 3) {
			dbType = "sqlserver";
		}

		String temp = sqlTypeLoader.getType(dbType, displayType).trim();
		switch(temp.hashCode()) {
			case -2056817302:
				if (temp.equals("java.lang.Integer")) {
					try {
						return IntegerConvert.convert(content);
					} catch (Exception var14) {
						throw new NumberFormatException();
					}
				}
				break;
			case -1405464277:
				if (temp.equals("java.math.BigDecimal")) {
					try {
						return BigDecimalConvert.convert(content);
					} catch (Exception var13) {
						throw new NumberFormatException();
					}
				}
				break;
			case -1374008726:
				if (temp.equals("byte[]")) {
					return content.getBytes();
				}
				break;
			case -527879800:
				if (temp.equals("java.lang.Float")) {
					try {
						return FloatConvert.convert(content);
					} catch (Exception var12) {
						throw new NumberFormatException();
					}
				}
				break;
			case 65575278:
				if (temp.equals("java.util.Date")) {
					try {
						new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf;
						String[] vs;
						if (content.contains("-")) {
							vs = content.split("-");
							sdf = new SimpleDateFormat("yyyy-MM-dd");
							return sdf.parse(content);
						}

						if (content.contains("/")) {
							vs = content.split("/");
							sdf = new SimpleDateFormat("yyyy/MM/dd");
							return sdf.parse(content);
						}

						 sdf = new SimpleDateFormat("yyyyMMdd");
						return sdf.parse(content);
					} catch (Exception var11) {
						throw new DateParseException(temp);
					}
				}
				break;
			case 761287205:
				if (temp.equals("java.lang.Double")) {
					try {
						return DoubleConvert.convert(content);
					} catch (Exception var10) {
						throw new NumberFormatException();
					}
				}
				break;
			case 1195259493:
				if (temp.equals("java.lang.String")) {
					return content;
				}
		}

		return content;
	}
}
