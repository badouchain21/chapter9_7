package com.bdc.base.service.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.badou.brms.base.support.spring.IBaseSpringService;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.system.org.dao.IUserCommonInfoDao;
import com.badou.brms.system.org.service.IUserService;
import com.badou.brms.system.org.vo.User;
import com.badou.brms.system.org.vo.UserCommInfo;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.designer.module.design.service.IModuleDesignService;
import com.bdc.assetdefine.dao.IAssetDefinedDAO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.common.BaseAssetConst;
import com.bdc.base.dao.IBaseAssetDAO;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.base.vo.BaseAssetIndexAttentionVO;
import com.bdc.base.vo.BaseAssetPublicityAttentionVO;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.blockchainconfig.service.IBlockchainConfigService;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.EmptyUtil;
import com.bdc.common.utils.HttpUtils;
import com.bdc.common.utils.SecurityUtil;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import com.bdc.zeromq.ZeroMqClient;
import com.bdc.zeromq.ZeroMqConst;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.brms.dictionary.DictionarySelect;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.designer.module.design.ModuleCacheContainer;
import com.badou.designer.module.design.ModuleConstants;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.badou.designer.module.design.model.MdModuleEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;


/**
 * interface of  BaseAssetService
 *
 * @author lps
 *
 */
@Transactional
@Service
@Slf4j
public class BaseAssetServiceImpl implements IBaseAssetService {
	@Autowired
	private SecurityUtil securityUtil;
	@Value("${base_path}")
	private String baseUrl;
	@Value("${monitor_path}")
	private String monitorPath;
	@Autowired
	private IAssetDefinedService assetDefinedService;
	@Autowired
	private IAssetDefinedDAO assetDefinedDAO;
	@Autowired
	private IBlockchainConfigService blockchainConfigService;
	@Autowired
	private IUploadAssetLogService uploadAssetLogService;
	@Autowired
	private IModuleDesignService moduleDesignService;
	@Autowired
	private BaseUploadAssetFactory baseUploadAssetFactory;
	/**
	 * 24小时以内sql
	 */
	private final String IN_24_HOURS_SQL = "interval 24 hour";
	/**
	 * 一个月内sql
	 */
	private final String IN_1_MONTH_SQL = "interval 1 month";
	/**
	 * year
	 */
	private final String YEAR = "Year";
	/**
	 * month
	 */
	private final String MONTH = "Month";

	@Override
	public List<Object> fillAssetDataWithJumpUrl(List<Object> objects, boolean isBaseAssetEntity,boolean displayTxAttr) throws Exception {
		Map<String,AssetDefinedEntity> assetDefinedEntityMap = new HashMap<>();
		JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(objects));
		jsonArray.stream().map(e -> ((JSONObject) e).getString("assetCode")).distinct().forEach(e -> {
			try {
				assetDefinedEntityMap.put(e,assetDefinedService.findByAssetCode(e));
			} catch (Exception exception){exception.printStackTrace();}
		});
		for (Object o : jsonArray) {
			JSONObject assetData = (JSONObject) JSONObject.toJSON(o);
			String orgHash = assetData.getString("hash");
			String assetCode = assetData.getString("assetCode");
			AssetDefinedEntity assetDefinedEntity = assetDefinedEntityMap.get(assetCode);
			if (StringUtils.isNotEmpty(orgHash)){
				if (displayTxAttr){
					String userName = LogonCertificateHolder.getLogonCertificate().getUserName();
					String assetId = isBaseAssetEntity ? assetData.getString("id") : assetData.getString("assetId");
					String orignalSign =   userName + SecurityUtil.COMM_SEPERTOR + new Date().getTime() + SecurityUtil.COMM_SEPERTOR + assetId;
					String sign = securityUtil.signAuth(orignalSign);
					String orignal = SecurityUtil.str2HexStr(orignalSign);
					String signParam = securityUtil.generateSignParam(sign, orignal);
					String jumpUrl = baseUrl + "/client/blockinfo/" + assetDefinedEntity.getBlockchainConfig().getChannelName() + "/transaction?searchStr=" + orgHash+"&signParam="+signParam ;
					assetData.put("jumpUrl", jumpUrl);
				}else {
					String jumpUrl = baseUrl + "/client/blockinfo/" + assetDefinedEntity.getBlockchainConfig().getChannelName() + "/transaction?searchStr=" + orgHash;
					assetData.put("jumpUrl", jumpUrl);
				}
			} else {
				assetData.put("hash", "");
			}

			Object time = assetData.get("uploadTime");
			if (Objects.nonNull(time)){
				String uploadTime = "";
				if (time instanceof String){
					if(((String) time).length() > 19){
						uploadTime = LocalDateTime.parse(time.toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					}else{
						uploadTime = (String) time;
					}
				} else {
					uploadTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(time.toString())), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				}
				assetData.put("uploadTime",uploadTime);
			}
		}
		return jsonArray;
	}


	@Override
	public BaseAssetIndexAttentionVO getBaseAssetIndexAttention() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Set<String> assetDbTables =getAssetDbTables(null);
		BaseAssetIndexAttentionVO vo = new BaseAssetIndexAttentionVO();
		if(assetDbTables.size()==0)
			return null;
		vo.setUnUnploadAssetCount(statAssetCount(assetDbTables,UploadStatusEnum.UNUPLOAD.getValue(),null));
		vo.setUploadedAssetCount(statAssetCount(assetDbTables,UploadStatusEnum.UPLOADED.getValue(),null));
		vo.setUploadingAssetCount(statAssetCount(assetDbTables,UploadStatusEnum.UPLOADING.getValue(),null));
		vo.setUploadedAssetIn24Hours(statAssetCount(assetDbTables,UploadStatusEnum.UPLOADED.getValue(),LocalDateTime.now().minusDays(1).format(dateTimeFormatter)));
		vo.setUploadedAssetInOneMonth(statAssetCount(assetDbTables,UploadStatusEnum.UPLOADED.getValue(),LocalDateTime.now().minusMonths(1).format(dateTimeFormatter)));
		return vo;
	}

	/**
	 * 获取资产表集合
	 * @return
	 */
	private Set<String> getAssetDbTables(String assetCode){
		Set<String> assetDbTables = new HashSet<String>();
		List<AssetDefinedEntity> assetDefineds = null;
		if(StringUtils.isNotBlank(assetCode)){
			QueryCriterion query = new QueryCriterion();
			query.addParam(new QueryHibernatePlaceholderParam("asset_code",assetCode,null,QueryOperSymbolEnum.eq));
			assetDefineds=assetDefinedService.find(query);
		}else{
			assetDefineds=assetDefinedService.listAll();
		}
		assetDefineds.forEach((assetDefined)->{
			MdModuleEntity mdEntity =ModuleCacheContainer.getInstance().getByCode(assetDefined.getAssetMdCode());
			if(mdEntity!=null){
				assetDbTables.add(mdEntity.getDbTable());
			}
		});
		return assetDbTables;
	}
	/**
	 * 根据上链状态与时间统计所有资产
	 * @return
	 */
	private int statAssetCount(Set<String> assetDbTables,Integer uploadStatus,String interval){
		String userId = LogonCertificateHolder.getLogonCertificate().getUserId();
		StringBuffer sqlSb= new StringBuffer();
		sqlSb.append("select sum(a) from (");
		assetDbTables.forEach((db)->{
			sqlSb.append(" select count(*) a from ");
			sqlSb.append(db);
			sqlSb.append(" where upload_status=");
			sqlSb.append(uploadStatus);
			if(StringUtils.isNotBlank(interval)){
				sqlSb.append(" and Upload_time >= '");
				sqlSb.append(interval);
				sqlSb.append("'");
			}
			if (!BdLoginUtil.isSa()){
				sqlSb.append(" and CREATOR = '"+userId+"'");
			}
			sqlSb.append(" union all ");
		});
		String sqlString =sqlSb.substring(0, sqlSb.lastIndexOf("union all"));
		StringBuffer sqlFinalSb = new StringBuffer();
		sqlFinalSb.append(sqlString);
		sqlFinalSb.append(") as tb ");
		log.info("get asset sql :"+sqlFinalSb.toString());
		int sum = 0;
		try {
			 sum = assetDefinedDAO.countAssetBySql(sqlFinalSb.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
		return sum;
	}

	@Override
	public List getAssetsByYear(@NonNull String startYear, @NonNull String endYear,String assetsCode) {
		Set<String> assetDbTables=getAssetDbTables(assetsCode);
		if(assetDbTables.size()==0)
			return null;
		List list = assetDefinedDAO.statAssetBySql(genAssetStatSql(YEAR,startYear,endYear,assetDbTables,assetsCode,null));
		// 填充过的列表，对没有数据的年份进行填充空值
		List listFilled = new ArrayList();
		int year = Integer.valueOf(startYear);
		for (Iterator iterator = list.iterator(); iterator.hasNext();year++) {
			Object[] object = (Object[]) iterator.next();
			Object returnYear = object[0];
			// 若返回的年份大于当前年份，说明当前年份没有数据，进行填充
			while(Integer.parseInt(returnYear.toString()) > year) {
				Object[] fillData = {new Integer(year), BigInteger.valueOf(0)};
				listFilled.add(fillData);
				year++;
			}
			// 当年份相等时，将查询到的数据添加到列表
			listFilled.add(object);
			// 例如：当查询2020年的数据，但2020年没有数据，最新的数据只有2019年时，进行的填充
			while(!iterator.hasNext() && year > Integer.parseInt(returnYear.toString())) {
				Object[] fillData = {new Integer(year), BigInteger.valueOf(0)};
				listFilled.add(fillData);
				year--;
			}
		}
		return listFilled;
	}

	@Override
	public List getAssetsByMonth(@NonNull String year, String assetCode) {
		StringBuffer otherParmSqlSB = new StringBuffer();
		otherParmSqlSB.append(" and year(create_time) =");
		otherParmSqlSB.append(year);
		Set<String> assetDbTables = getAssetDbTables(assetCode);
		if(assetDbTables.size()==0)
			return null;
		//1-12 月
		List list = assetDefinedDAO.statAssetBySql(genAssetStatSql(MONTH,"1","12",assetDbTables,assetCode,otherParmSqlSB.toString()));
		return list;
	}
	@Override
	public List getAssetsByType(@NonNull String year) {
		Set<String> assetDbTables =getAssetDbTables(null);
		if(assetDbTables.size()==0)
			return null;
		List list = assetDefinedDAO.statAssetBySql(genGetAssetByTypeSql(assetDbTables,year));
		return list;
	}

	private String genAssetStatSql(String unit,String start,String end,Set<String> assetDbTables,String assetsCode,String otherParmSql){
		String userId = LogonCertificateHolder.getLogonCertificate().getUserId();
		StringBuffer assetStatSqlBuffer = new StringBuffer();
		assetStatSqlBuffer.append(" SELECT yearTime,sum(a) FROM ( ");
		assetDbTables.forEach((db)->{
			assetStatSqlBuffer.append(" SELECT ");
			assetStatSqlBuffer.append(unit);
			assetStatSqlBuffer.append( "(Upload_time) as yearTime,count(*) a from ");
			assetStatSqlBuffer.append(db);
			assetStatSqlBuffer.append(" where upload_status=");
			assetStatSqlBuffer.append(UploadStatusEnum.UPLOADED.getValue());
			assetStatSqlBuffer.append(" and ");
			assetStatSqlBuffer.append(unit);
			assetStatSqlBuffer.append("(Upload_time) >= ");
			assetStatSqlBuffer.append(start);
			assetStatSqlBuffer.append(" and ");
			assetStatSqlBuffer.append(unit);
			assetStatSqlBuffer.append("(Upload_time) <= ");
			assetStatSqlBuffer.append(end);
			if(StringUtils.isNotBlank(assetsCode)){
				assetStatSqlBuffer.append(" and  asset_code = ");
				assetStatSqlBuffer.append(assetsCode);
			}
			if(StringUtils.isNotBlank(otherParmSql)){
				assetStatSqlBuffer.append(otherParmSql);
			}
			assetStatSqlBuffer.append(" and CREATOR = '"+userId+"'");
			assetStatSqlBuffer.append(" GROUP BY yearTime ");
			assetStatSqlBuffer.append(" union all ");
		});

		String sqlString =assetStatSqlBuffer.substring(0, assetStatSqlBuffer.lastIndexOf("union all"));
		StringBuffer sqlFinalSb = new StringBuffer();
		sqlFinalSb.append(sqlString);
		sqlFinalSb.append(") as tb GROUP BY yearTime order by yearTime");

		log.info("get asset sql :"+sqlFinalSb.toString());
		return sqlFinalSb.toString();
	}

	/**
	 * 拼装按资产类型统计SQL
	 * @param assetDbTables
	 * @param year
	 * @return
	 */
	private String genGetAssetByTypeSql(Set<String> assetDbTables,String year){
		StringBuffer assetStatSqlBuffer = new StringBuffer();
		assetStatSqlBuffer.append(" SELECT	asset_name,	a from (");
		assetDbTables.forEach((db)->{
			assetStatSqlBuffer.append("SELECT  asset_name, count(*) a FROM ");
			assetStatSqlBuffer.append(db);
			assetStatSqlBuffer.append(" where upload_status=");
			assetStatSqlBuffer.append(UploadStatusEnum.UPLOADED.getValue());
			assetStatSqlBuffer.append(" and year(upload_time) = ");
			assetStatSqlBuffer.append(year);
			assetStatSqlBuffer.append(" group by asset_name ");
			assetStatSqlBuffer.append(" union all ");
		});

		String sqlString =assetStatSqlBuffer.substring(0, assetStatSqlBuffer.lastIndexOf("union all"));
		StringBuffer sqlFinalSb = new StringBuffer();
		sqlFinalSb.append(sqlString);
		sqlFinalSb.append(") as tb GROUP BY asset_name ");
		return sqlFinalSb.toString();
	}
	@Override
	public JSONObject getCZTField(String id,String assetCode) throws Exception{
		JSONObject czf = new JSONObject();;
		JSONObject json = new JSONObject();
		IAssetDefinedService assetDefinedService = InstanceFactory.getInstance(IAssetDefinedService.class);
		AssetDefinedEntity ad = assetDefinedService.findByAssetCode(assetCode);
		MdModuleEntity md = ModuleCacheContainer.getInstance().getByCode(ad.getAssetMdCode());
		if(md==null){
			return null;
		}
		JSONObject dicObj = new JSONObject();
		JSONArray array = new JSONArray();
		//拼接中文
		Set<MdFieldEntity> fields = md.getFields();
		Boolean isExistSuper = false;
		PropertyDescriptor[] assetpds = Introspector.getBeanInfo(BaseAssetEntity.class).getPropertyDescriptors();
		PropertyDescriptor[] basepds = Introspector.getBeanInfo(AppBaseEntity.class).getPropertyDescriptors();
		for(MdFieldEntity field : fields){
			isExistSuper=false;
			for(PropertyDescriptor pd :assetpds ){
				if(pd.getName().equals(field.getEntityName())){
					isExistSuper=true;
					break;
				}
			}
			if(isExistSuper)
				continue;
			for(PropertyDescriptor pd :basepds ){
				if(pd.getName().equals(field.getEntityName())){
					isExistSuper=true;
					break;
				}
			}
			if(isExistSuper)
				continue;
			json = new JSONObject();
			JSONObject detailObj = new JSONObject();
			detailObj.put("displayName", field.getForm().getDisplayName());
			if(field.getForm().getDataSource()!=null){
				if(ModuleConstants.DATA_SOURCE_DIC.equals(field.getForm().getDataSource())){
					detailObj.put("dic", field.getForm().getDicName());
					if(!dicObj.containsKey(field.getForm().getDicName())){
//						dicObj.put(field.getForm().getDicName(), JSONArray.parse(DictionaryLib.getSelectJsonByCode(field.getForm().getDicName())));
						dicObj.put(field.getForm().getDicName(), getDicItem(field.getForm().getDicName()));
					}
				}
			}
			json.put(field.getEntityName(), detailObj);
			array.add(json);
		}
		czf.put("jsonArray", array);
		czf.put("dicObj", dicObj);
		return czf;
	}


	private JSONArray getDicItem(String dicCode) {
		List<DictionarySelect> dicItemList = DictionaryLib.getDictionarySelectByCode(dicCode);
		JSONArray arr = new JSONArray();
		for (DictionarySelect item : dicItemList) {
			JSONObject o = new JSONObject();
			o.put("id", item.getId());
			o.put("text", item.getText());
			arr.add(o);
		}
		return arr;
	}

	@Override
	public Object getFieldValue(String keyType, BaseAssetEntity baseAssetEntity) {
		try {
			AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(baseAssetEntity.getAssetCode());
			Class<? extends BaseAssetEntity> aClass = baseAssetEntity.getClass();
			Field assetCode = null;
			switch (keyType) {
				case BaseAssetConst.ASSET_KEY_TYPE_ID:
					assetCode = aClass.getDeclaredField(assetDefinedEntity.getAssetKey());
					break;
				case BaseAssetConst.ASSET_KEY_TYPE_NAME:
					assetCode = aClass.getDeclaredField(assetDefinedEntity.getAssetNameField());
					break;
			}
			if (Objects.isNull(assetCode)){
				return null;
			}
			assetCode.setAccessible(true);
			Object value = assetCode.get(baseAssetEntity);
			return value;
		} catch (Exception e){
			log.info(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public BaseAssetPublicityAttentionVO getBaseAssetPublicityAttention() {
		BaseAssetPublicityAttentionVO attentionVO = new BaseAssetPublicityAttentionVO();
		//24小时上链资产数量
		Integer count = uploadAssetLogService.getReportDataByTime(LocalDateTime.now().minusDays(1),null);
		attentionVO.setAssetAmount24hours(count);

		Integer assetCount = uploadAssetLogService.getReportDataByStatus(UploadStatusEnum.UPLOADED);
		//资产总数
		attentionVO.setAssetAmount(assetCount);

		//资产类型总数
		int assetDefinedCount = assetDefinedService.listAll().size();
		attentionVO.setAssetTypeAmount(assetDefinedCount);
		return attentionVO;
	}

	/**
	 * 获取资产表集合并且和对应主键字段
	 * @return
	 */
	private JSONArray getAssetDBTables(){
		JSONArray jsonArray = new JSONArray();
		List<AssetDefinedEntity> assetDefinedEntities = assetDefinedService.listAll();
		assetDefinedEntities.stream().collect(Collectors.groupingBy(e -> e.getAssetMdCode())).forEach((k,v) -> {
			AssetDefinedEntity assetDefinedEntity = v.get(0);
			MdModuleEntity mdEntity =ModuleCacheContainer.getInstance().getByCode(assetDefinedEntity.getAssetMdCode());
			if(mdEntity!=null){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("table",mdEntity.getDbTable());
				jsonObject.put(BaseAssetConst.ASSET_KEY_TYPE_NAME,getColumnField(assetDefinedEntity));
				jsonArray.add(jsonObject);
			}
		});
		return jsonArray;
	}

	/**
	 * 获取数据库字段
	 * @param assetDefinedEntity
	 * @return
	 */
	private String getColumnField (AssetDefinedEntity assetDefinedEntity){
		for (UploadTypeEnum value : UploadTypeEnum.values()) {
			Class<? extends BaseAssetEntity> aClass = value.getAClass();
			try {
				Field declaredField = aClass.getDeclaredField(assetDefinedEntity.getAssetNameField());
				if (Objects.nonNull(declaredField)){
					Column annotation = declaredField.getAnnotation(Column.class);
					return annotation.name();
				}
			} catch (Exception e){
				log.error(e.getMessage());
			}
		}
		return null;
	}

	@Override
	public Integer getBlockHeight() {
		Map params = new HashMap();
		int count = 0;
		try {
			List<BlockchainConfigEntity> blockchainConfigEntities = blockchainConfigService.listAll();
			List<String> collect = blockchainConfigEntities.stream()
					.filter(e -> e.getStatus().intValue() == SystemBoolean.NO.getKey().intValue())
					.map(e -> e.getChannelName()).distinct()
					.collect(Collectors.toList());
			for (String channelName : collect) {
				params.put("userId",LogonCertificateHolder.getLogonCertificate().getUserId());
				params.put("channelId",channelName);
				String url = monitorPath + BaseAssetConst.BLOCK_HEIGHT_URL;
				String result = HttpUtils.post(url, params);
				JSONObject jsonObject = JSONObject.parseObject(result);
				if (Objects.nonNull(jsonObject) && jsonObject.containsKey("return_data")){
					Object returnData = jsonObject.get("return_data");
					int blockHeight = ((JSONObject) returnData).getIntValue("blockHeight");
					count += blockHeight;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return count;
	}

	@Override
	public String getAssetCode(String assetCode) {
		try {
			UploadTypeEnum assetType = UploadTypeEnum.getUploadTypeEnumByCode(assetCode);
			if (Objects.nonNull(assetType)){
				return assetType.getCode();
			} else {
				AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(assetCode);
				String assetMdCode = assetDefinedEntity.getAssetMdCode();
				QueryCriterion queryCriterion = new QueryCriterion();
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("base_class", "com.badou.designer.module.design.base.BaseModuleEntity", null, QueryOperSymbolEnum.like));
				queryCriterion.addParam(new QueryHibernatePlaceholderParam("code", assetMdCode, null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
				List<MdModuleEntity> mds = moduleDesignService.find(queryCriterion);
				if (EmptyUtil.isEmpty(mds)){
					return null;
				}
				String dbTable = mds.get(0).getDbTable();
				return baseUploadAssetFactory.getTableMap().get(dbTable);
			}
		} catch (Exception e){
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public BaseAssetEntity getByAssetId(String assetCode,@NotNull Object assetKeyId) {
		try {
			AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(assetCode);
			IBaseSpringService<? extends BaseAssetEntity, Serializable> businessService = baseUploadAssetFactory.getBusinessService(getAssetCode(assetCode));
			QueryCriterion queryCriterion = new QueryCriterion();
			queryCriterion.addParam(new QueryHibernatePlaceholderParam(assetDefinedEntity.getAssetKey(),assetKeyId,null,QueryOperSymbolEnum.eq));
			List<? extends BaseAssetEntity> baseAssetEntities = businessService.find(queryCriterion);
			if (EmptyUtil.isNotEmpty(baseAssetEntities)){
				return baseAssetEntities.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Autowired
	private IBaseAssetDAO baseAssetDAO;
	public Boolean existByAssetKeyVal(@NonNull AssetDefinedEntity assetDefinedEntity,  @NonNull String keyVal) throws Exception{
		MdModuleEntity mdModuleEntity = ModuleCacheContainer.getInstance().getByCode(assetDefinedEntity.getAssetMdCode());
		String code = baseUploadAssetFactory.getTableMap().get(mdModuleEntity.getDbTable());
		if(code==null)
			return false;

		Object object = baseAssetDAO.existByAssetKeyVal(UploadTypeEnum.getUploadTypeEnumByCode(code).aClass.getName(),assetDefinedEntity.getAssetKey(),keyVal);
		return object!=null;
	}

	/**
	 * 将输入String类型的资产ID转成对应的数据类型
	 * @param assetId
	 * @param assetDefinedEntity
	 * @return
	 */
	@Override
	public Object converAssetId (String assetId, AssetDefinedEntity assetDefinedEntity) throws Exception {
		try {
			String code = getAssetCode(assetDefinedEntity.getAssetCode());
			UploadTypeEnum type = UploadTypeEnum.getUploadTypeEnumByCode(code);
			Class<? extends BaseAssetEntity> clazz = type.getAClass();
			Field declaredField = clazz.getDeclaredField(assetDefinedEntity.getAssetKey());
			declaredField.setAccessible(true);
			Type genericType = declaredField.getGenericType();
			String typeName = genericType.getTypeName();
			if (Objects.equals(typeName,Integer.class.getTypeName())){
				return Integer.parseInt(assetId);
			}else if (Objects.equals(typeName,Long.class.getTypeName())){
				return Long.parseLong(assetId);
			}else {
				return assetId;
			}
		} catch (Exception e){
			throw e;
		}
	}

	@Override
	public void assetUpload(@NotNull BaseAssetEntity baseAssetEntity) {
		try {
			UploadTypeEnum type = UploadTypeEnum.getUploadTypeEnumByCode(getAssetCode(baseAssetEntity.getAssetCode()));
			IBaseUploadAssetService uploadService = baseUploadAssetFactory.getUploadService(type.getCode());
			AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(baseAssetEntity.getAssetCode());
			Integer status = assetDefinedEntity.getBlockchainConfig().getStatus();
			setLogon(baseAssetEntity);
			if (status.intValue() == SystemBoolean.NO.getKey().intValue()){
				uploadService.updateStatus(type, baseAssetEntity,UploadStatusEnum.UPLOADING);
				ZeroMqClient.sendLocalhost(ZeroMqConst.BDC_PORT,baseAssetEntity.getId(), type.getCode());
			} else {
				//创建上链日志
				UploadAssetLogEntity simpleUploadAssetLog = uploadAssetLogService.createSimpleUploadAssetLog(baseAssetEntity);
				//更新状态
				uploadService.updateStatus(type,baseAssetEntity,UploadStatusEnum.FAILD);
				simpleUploadAssetLog.setUploadStatus(UploadStatusEnum.FAILD.getValue());
				simpleUploadAssetLog.setLog("区块链应用配置已禁用");
				simpleUploadAssetLog.setUploadTime(new Date());
				uploadAssetLogService.update(simpleUploadAssetLog);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} finally {
			LogonCertificateHolder.clear();
		}

	}

	/**
	 *
	 * 描述：设置当前用户
	 *
	 * @author linxiaoqing
	 * @date 2019年9月6日
	 * @param entity
	 */
	private Boolean setLogon(AppBaseEntity entity) {
		// 设置当前用户
		String userId = entity.getCreator();
		if (com.badou.tools.common.util.StringUtils.isEmpty(userId))
			return false;
		User user = InstanceFactory.getInstance(IUserService.class).getById(
				userId);
		if (user == null)
			return false;
		UserCommInfo userComminfo = InstanceFactory.getInstance(
				IUserCommonInfoDao.class).getById(user.getUserInfoId());
		if (userComminfo == null)
			return false;
		LogonCertificate logon = new LogonCertificate();
		logon.setUserId(userId);
		logon.setUserName(userComminfo.getName());
		logon.setLoginId(userComminfo.getLogonId());
		LogonCertificateHolder.setLogonCertificate(logon);
		return true;
	}

	@Override
	public void assetReUpload (List<String> assetIds,@NotNull String assetCode){
		UploadTypeEnum type = UploadTypeEnum.getUploadTypeEnumByCode(assetCode);
		IBaseUploadAssetService uploadService = baseUploadAssetFactory.getUploadService(type.getCode());
		for (String assetId : assetIds) {
			BaseAssetEntity baseAssetEntity = uploadService.get(type, assetId);
			assetUpload(baseAssetEntity);
		}
	}

	@Override
	public int statAssetCount(UploadStatusEnum uploadStatusEnum) {
		Set<String> assetDbTables =getAssetDbTables(null);
		if(assetDbTables.size()==0)
			return 0;
		int count = statAssetCount(assetDbTables, UploadStatusEnum.UNUPLOAD.getValue(), null);
		return count;
	}

}
