package com.bdc.api.rest.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.badou.brms.attach.model.Attach;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.brms.dictionary.DictionarySelect;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.designer.jdbc.CommonConsts;
import com.badou.designer.jdbc.common.util.HttpUtil;
import com.badou.designer.jdbc.common.web.BaseModuleAction;
import com.badou.designer.jdbc.common.web.form.BaseCommonSaveForm;
import com.badou.designer.jdbc.core.util.UUIDUtils;
import com.badou.designer.jdbc.core.vo.BaseVO;
import com.badou.designer.module.design.ModuleCacheContainer;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;

import com.badou.security.ssl.token.pojo.Token;
import com.badou.security.ssl.token.service.ITokenService;
import com.badou.tools.common.PropertiesConfig;
import com.badou.tools.common.util.ParamUtils;

import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import com.bdc.api.intermanage.interfacelogger.service.IInterFaceLoggerService;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.api.rest.ReturnCodeEnum;
import com.bdc.api.rest.StatusEnum;
import com.bdc.api.rest.container.TokenContainer;
import com.bdc.api.rest.service.IBaseRestService;
import com.bdc.api.rest.vo.ApiReturnVO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.common.CommonConst;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.properties.SecurityProperties;
import com.bdc.common.utils.BdLoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author chenjiabao
 * @date 2019???7???2?????????10:36:55
 * @todo Restful API ??????
 */
@Slf4j
@RestController
@Scope("prototype")
@RequestMapping(value = "/api/v1")
public class BaseRestController extends BaseModuleAction{

	/**
	 * ??????
	 */
	protected MdModuleEntity moduleBean;
	/**
	 * ????????????
	 */
	protected ApiReturnVO returnVO;

	protected static final String REQUEST_PARAMS_PREFIX = "SDdetail_";

	/**
	 * ??????????????????
	 */
	protected static final String DOCUMENT_TYPE="docCode-ori";


	protected BaseCommonSaveForm custForm;

	@Autowired
	protected IBaseRestService baseRestService;
	@Autowired
	private IInterFaceLoggerService interFaceLoggerService;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private INetInformationService netService;
	@Autowired
	private IModuleDesignService moduleDesignService;
	@Autowired
	private IAssetDefinedService assetDefinedService;

	protected List<JSONObject> cusList = null;
	@Autowired
	private IBaseAssetService baseAssetService;
	@Autowired
	private INetInformationService netInformationService;



	public MdModuleEntity getModuleBean() {
		return moduleBean;
	}

	public void setModuleBean(MdModuleEntity moduleBean) {
		this.moduleBean = moduleBean;
	}

	public BaseCommonSaveForm getCustForm() {
		return custForm;
	}

	public void setCustForm(BaseCommonSaveForm custForm) {
		this.custForm = custForm;
	}

	/**
	 * @auth chenjiabao
	 * @date 2019???7???2?????????5:37:59
	 * @todo ?????????moduleBean
	 * @param mdCode
	 */
	public void initModuleBean(String mdCode) {
		MdModuleEntity moduleBean = ModuleCacheContainer.getInstance()
				.getByCode(mdCode);
		baseRestService.initModule(moduleBean);
		this.moduleBean =moduleBean;
	}

	/**
	 * rest ??????get?????? ??????
	 *
	 * @param mdCode
	 *            ?????????code
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
 	@RequestMapping(value = "/{mdCode}", method = RequestMethod.GET , produces = {"application/json;charset=UTF-8"})
	public ApiReturnVO doGet(@PathVariable String mdCode, ModelMap model,HttpServletRequest request) {
		returnVO = new ApiReturnVO();
		try{
			String access_token = request.getParameter("access_token");
			JSONObject userInfo = TokenContainer.getInstance().get(access_token);
			String userId = userInfo.getString("userId");
			// ??????????????????????????????????????????
		    AssetDefinedEntity assetDefined = assetDefinedService.findByAssetCode(mdCode);
            // ????????????
            this.initModuleBean(assetDefined.getAssetMdCode());
			Pagelet pagelet = new Pagelet();
			if(request.getParameter("pageNo")!=null)
				pagelet.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
			if(request.getParameter("perPageSize")!=null)
				pagelet.setPerPageSize(Integer.parseInt(request.getParameter("perPageSize")));
			PaginationTheadLocal.setPagelet(pagelet);
			if (!BdLoginUtil.isSa(userId)){
				QueryCriterion queryCriterion = new QueryCriterion();
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("creator", userId,null,QueryOperSymbolEnum.eq));
				pagelet = baseRestService.findPages(queryCriterion);
			} else {
				pagelet = baseRestService.findPages();
			}
			List<Object> list = pagelet.getDatas();
            if (list != null) {
				returnVO.setReturnCode(ReturnCodeEnum.SUCCESS.getReturnCode());
                returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.toString());
                this.convert(list, CommonConsts.STRATEGY_SINGEL);
                returnVO.setData(new ArrayList<Object>(cusList));
            } else {
				returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
				returnVO.setReturnMsg(ReturnCodeEnum.FAIL.toString());
            }
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}
		return returnVO;
	}

	/**
	 * rest ??????post?????? ??????
	 *
	 * @param mdCode
	 *            ?????????code
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
	@RequestMapping(value = "/{mdCode}", method = RequestMethod.POST , produces = {"application/json;charset=UTF-8"})
	public  ApiReturnVO doPost(@PathVariable String mdCode,HttpServletRequest request) {
		returnVO = new ApiReturnVO();
		try {
            // ??????????????????????????????????????????
            AssetDefinedEntity assetDefined = assetDefinedService.findByAssetCode(mdCode);
            // ????????????
            this.initModuleBean(assetDefined.getAssetMdCode());
			if (custForm == null){
				custForm = new BaseCommonSaveForm();
			}
			setCustFormProperties();
			exeBeforeSave(true, assetDefined,null);
			BaseVO vo = baseRestService.saveBaseVO(moduleBean, null, custForm, null, true);
			custForm.setId(vo.getId());
			String token = ParamUtils.getParameter(request, "access_token");
			exeAfterSave(true,null,vo.getDetailMap());
			LogonCertificateHolder.clear();
            returnVO.setData(JSON.toJSON(vo.getDetailMap()));
			returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.getReturnMsg());
			returnVO.setReturnCode(ReturnCodeEnum.SUCCESS.getReturnCode());
		} catch (Exception e) {
            log.error(e.getMessage(), e);
			e.printStackTrace();
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}
		return returnVO;
	}

	private void exeBeforeSave(Boolean isCreate, AssetDefinedEntity assetDefined,String assetKeyValue) throws Exception {
        if (isCreate) {
			// ?????????????????????
			String assetKeyField = assetDefined.getAssetKey();
			if (Objects.isNull(custForm.getDetails().get(assetKeyField))) {
				throw new Exception("????????????????????????????????????????????????????????????");
			}
			// ?????????????????????
			String assetKey = custForm.getDetails().get(assetKeyField)[0];
			Boolean isExist = baseAssetService.existByAssetKeyVal(assetDefined,assetKey);
//			BaseAssetEntity baseAssetEntity = baseAssetService.getByAssetId(assetDefined.getAssetCode(), assetKey);
			// ????????????????????????
            if (isExist) {
                throw new Exception("????????????????????????????????????????????????");
            }
            // ?????????????????????
            custForm.getDetails().put("uploadStatus", new String[]{UploadStatusEnum.UNUPLOAD.getValue()+""});
			custForm.getDetails().put("isCreate", new String[]{SystemBoolean.NO.getKey().toString()});
			this.custForm.getDetails().put("bdcBaasLogId", new String[]{null});
			this.custForm.getDetails().put("uploadType", new String[]{SystemBoolean.NO.getKey().toString()});
			this.custForm.getDetails().put("hash", new String[]{null});
			custForm.setId(UUIDUtils.uuid64());
           /* // ??????ID???????????????
            custForm.setId(assetKey);*/
        }else{
			// ???????????????????????????
			String[] values = this.custForm.getDetails().get(assetDefined.getAssetKey());
			if (Objects.nonNull(values) && !Objects.equals(values[0],assetKeyValue)){
				throw new Exception(" "+assetDefined.getAssetKey()+" ????????????");
			}
        	this.custForm.getDetails().put("uploadStatus", new String[]{UploadStatusEnum.UNUPLOAD.getValue().toString()});
			this.custForm.getDetails().put("bdcBaasLogId", new String[]{null});
		}
        // ??????????????????
        custForm.getDetails().put("assetName", new String[]{assetDefined.getAssetName()});
		custForm.getDetails().put("assetCode", new String[]{assetDefined.getAssetCode()});
		custForm.getDetails().put("mdcode", new String[]{assetDefined.getAssetMdCode()});

	}

	/**
	 *
	 * ????????????????????????
	 * @author linxiaoqing
	 * @date 2019???8???16???
	 * @throws Exception
	 */
	public void exeAfterSave(Boolean isCreate,Token token,Map<String,Object> map) throws Exception {
		//????????????????????????
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			String key = entry.getKey();

			if (Objects.nonNull(value) && value instanceof Date){
				Date time = (Date)value;
				if (key.contains("time") || key.contains("dateTime")){
					//?????????????????????
					String timeStr = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).format(dateTimeFormatter);
					entry.setValue(timeStr);
				} else if (key.contains("date")){
					//?????????????????????
					String dateStr = LocalDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault()).toLocalDate().format(dateFormatter);
					entry.setValue(dateStr);
				}
			}
		}
	}

	/**
	 * rest ??????put?????? ??????
	 *
	 * @param mdCode
	 * @param id
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
	@RequestMapping(value = "/{mdCode}/{id}", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public ApiReturnVO doPut(@PathVariable String mdCode, @PathVariable String id,HttpServletRequest request) {
		returnVO = new ApiReturnVO();
		try {
            // ??????????????????????????????????????????
            AssetDefinedEntity assetDefined = assetDefinedService.findByAssetCode(mdCode);
            // ????????????
            this.initModuleBean(assetDefined.getAssetMdCode());
			BaseVO vo = null;
			vo = baseRestService.getVObyAssetKey(assetDefined,id,this.moduleBean,null);
			if (Objects.isNull(vo)) {
				throw new Exception("????????????,????????????????????????????????????");
			}
			if (custForm == null){
				custForm = new BaseCommonSaveForm();
			}
			setCustFormProperties();
			custForm.setId(vo.getId());
			custForm.getDetails().put("id", new String[]{vo.getId()});
			exeBeforeSave(false, assetDefined,id);
			custForm.setFormToEntityProperties(vo , true);
			vo = baseRestService.saveBaseVO(moduleBean, null, custForm,vo,false);
            exeAfterSave(false,null,vo.getDetailMap());
			LogonCertificateHolder.clear();
            returnVO.setData(JSON.toJSON(vo.getDetailMap()));
			returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.getReturnMsg());
			returnVO.setReturnCode(ReturnCodeEnum.SUCCESS.getReturnCode());
		}
		catch(Exception e)
		{
            log.error(e.getMessage(), e);
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}
		return returnVO;
	}

	/**
	 * rest ??????delete?????? ??????
	 *
	 * @param mdCode
	 * @param id
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
	@RequestMapping(value = "/{mdCode}/{id}", method = RequestMethod.DELETE, produces = {"application/json;charset=UTF-8"})
	public ApiReturnVO doDelete(@PathVariable String mdCode,
			@PathVariable String id,HttpServletRequest request) {
		returnVO = new ApiReturnVO();
		try {
            // ??????????????????????????????????????????
            AssetDefinedEntity assetDefined = assetDefinedService.findByAssetCode(mdCode);
            // ????????????
            this.initModuleBean(assetDefined.getAssetMdCode());
			if (custForm == null){
				custForm = new BaseCommonSaveForm();
			}
			String msg = baseRestService.delete(moduleBean, id);
			if(StringUtils.isNotBlank(msg)){
				returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+msg);
				returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
			}else{
				returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.getReturnMsg());
				returnVO.setReturnCode(ReturnCodeEnum.SUCCESS.getReturnCode());
			}
		}catch(Exception e){
            log.error(e.getMessage(), e);
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}
		return returnVO;
	}

	/**
	 * rest ?????????id???get?????? ??????id??????????????????
	 *
	 * @param mdCode
	 * @param id
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
	@RequestMapping(value = "/{mdCode}/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public ApiReturnVO doGetWithId(@PathVariable String mdCode,
			@PathVariable String id, ModelMap model,HttpServletRequest request) {
		returnVO = new ApiReturnVO();
		try{
			String access_token = request.getParameter("access_token");
			JSONObject userInfo = TokenContainer.getInstance().get(access_token);
			String userId = userInfo.getString("userId");
			// ??????????????????????????????????????????
            AssetDefinedEntity assetDefined = assetDefinedService.findByAssetCode(mdCode);
            // ????????????
            this.initModuleBean(assetDefined.getAssetMdCode());
            BaseVO vo = baseRestService.getVObyAssetKey(assetDefined,id,this.moduleBean,userId);
            if (vo != null) {
                JSONObject result = this.getListForm(vo);
                returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.toString());
                returnVO.setReturnCode(ApiReturnVO.SUCCESS);
                returnVO.setData(result);
            } else {
                returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
                returnVO.setReturnMsg(ReturnCodeEnum.FAIL.toString());
            }
		}catch(Exception e){
            log.error(e.getMessage(), e);
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}
		return returnVO;
	}

	/**
	 * rest ??????accessToken
	 *
	 * @param mdCode
	 * @param id
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
	@RequestMapping(value = "/oauth2/token", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	public ApiReturnVO token() {
		returnVO = new ApiReturnVO();
		try{
			String getToken = PropertiesConfig.getPropValue("token.url");
			Map<String,String> map = new HashMap<String,String>();
			String appId = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("app_id");
			String appKey = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getParameter("app_key");
			map.put("APP_ID", appId);
			map.put("APP_KEY", appKey);


			net.sf.json.JSONObject jsonResult=HttpUtil.httpPost(getToken, map, false);
			if(jsonResult != null && jsonResult.getBoolean("hasOk")){
				String message =jsonResult.get("message").toString();
				returnVO.setData(message);
				returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.getReturnMsg());
				returnVO.setReturnCode(ReturnCodeEnum.SUCCESS.getReturnCode());
			}else{
				returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg());
				returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
			}
		}catch(Exception e){
            log.error(e.getMessage(), e);
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}

		return returnVO;
	}

	/**
	 * @throws Exception
	 * @auth chenjiabao
	 * @date 2019???7???2?????????7:36:13
	 * @todo ?????????????????????
	 */
	protected void setCustFormProperties() throws Exception {
		Map<String, String[]> reqParams = new HashMap<String, String[]>();
		Map<String, String[]> srcParams = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getParameterMap();
		String[] values = null;
		for (String key : srcParams.keySet()) {
			values = srcParams.get(key);
			if (key.startsWith(REQUEST_PARAMS_PREFIX)) {
				reqParams.put(key.substring(REQUEST_PARAMS_PREFIX.length()),
						values);
			} else {
				reqParams.put(key, values);
			}
		}
		if (reqParams.containsKey("id")) {
			this.custForm.setId(reqParams.get("id")[0]);
		}
		this.custForm.setDetails(reqParams);
		// ????????????
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		dealAttach(request);

	}


	/**
	 * @auth chenjiabao
	 * @date 2019???7???2?????????5:39:27
	 * @todo ??????????????????
	 * @param list
	 * @param strategy
	 * @throws Exception if has error(???????????????)
	 */
	protected void convert(List<Object> list, Integer strategy) throws Exception{
		cusList = new ArrayList<JSONObject>();
		if(list != null && !list.isEmpty()){
			for(Object t : list){
				cusList.add(convert((BaseVO)t,strategy));
			}
		}
	}

	/**
	 * @auth chenjiabao
	 * @date 2019???7???2?????????5:39:27
	 * @todo ????????????????????????
	 * @param list
	 * @param strategy
	 * @throws Exception if has error(???????????????)
	 * @return ????????????json????????????
	 */
	protected JSONObject convert(BaseVO t, Integer strategy) throws Exception{
		return super.getListForm((BaseVO)t);
	}

	/**
	 * ???request????????????(?????????)
	 * @param request
	 * @throws Exception
	 */
	private void dealAttach(HttpServletRequest request) throws Exception {
		custForm.getAttachMap().clear(); // ?????????????????????(???????????????)
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
		List<Attach> attachList = null;
		// ????????????
		if (multipartResolver.isMultipart(request)) { // spring
			MultipartHttpServletRequest multi = new StandardMultipartHttpServletRequest(request, false);
			 attachList = null;
			for (Entry<String, MultipartFile> me : multi.getFileMap().entrySet()) {
				attachList = custForm.getAttachMap().get(me.getKey());
				if (attachList == null) {
					attachList = new ArrayList<>();
					Attach attach = getAttach(me);
					if(!isVaildFile(attach)){
						throw new Exception("????????????????????????????????????");
					}
					attachList.add(getAttach(me));
					custForm.getAttachMap().put(me.getKey(), attachList);
				} else {
					attachList.add(getAttach(me));
				}
			}

		}
	}

	/**
	 * ??????attach(??????????????????)
	 * @return
	 * @throws Exception
	 */
	private Attach getAttach(Entry<String, MultipartFile> me) throws Exception{
		MultipartFile file = me.getValue();
		String fileName = file.getOriginalFilename();
		byte[] content = file.getBytes();
		Attach attach = new Attach();
		LogonCertificate logon = LogonCertificateHolder.getLogonCertificate();
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			attach.setExtendName(suffix);
		}
		attach.setName(fileName);
		attach.setFileContent(content);
		attach.setFileSize(content.length);
		attach.setGenPersonId(logon.getUserId());
		attach.setGenPersonName(logon.getLoginId());
		return attach;
	}

	/**
	 * ???????????????
	 * @param attach
	 * @return
	 */
	private boolean isVaildFile(Attach attach){
		SecurityProperties sp = InstanceFactory.getInstance(SecurityProperties.class);
		String fileWhiteList = sp.getFileWhiteList();
		String[] list = fileWhiteList.split(";");
		for(String t : list){
			if(("."+t).equals(attach.getExtendName()))
			{
				return true;
			}
		}
		List<DictionarySelect> selects = DictionaryLib.getDictionarySelectByCode(CommonConst.DIC_FILE_WHITE_LIST);
		for(DictionarySelect select : selects){
			if(("."+select.getId()).equals((attach.getExtendName()))){
				return true;
			}
		}
		return false;
	}
	/**
	 * rest ??????accessToken
	 *
	 * @param mdCode
	 * @param id
	 * @param model
	 * @return ApiReturnVO
	 */
	@ResponseBody
	@RequestMapping(value = "/cz/getCZTField", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public ApiReturnVO getCZTField() {
		returnVO = new ApiReturnVO();
		try{
			String id = ParamUtils.getParameter(request, "id");
			String assetCode = ParamUtils.getParameter(request, "assetCode");

			JSONObject json = baseAssetService.getCZTField(id,assetCode);
			returnVO.setData(json);
			returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.getReturnMsg());
			returnVO.setReturnCode(ReturnCodeEnum.SUCCESS.getReturnCode());
		}catch(Exception e){
            log.error(e.getMessage(), e);
			returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+e.getMessage());
			returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
		}

		return returnVO;
	}
}
