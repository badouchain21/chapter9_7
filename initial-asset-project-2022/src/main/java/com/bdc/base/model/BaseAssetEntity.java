package com.bdc.base.model;

import java.awt.print.Book;
import java.util.Date;

import javax.persistence.*;

import com.badou.brms.util.InstanceFactory;
import com.badou.project.CommonConst;
import com.badou.project.GlobalConsts;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.common.utils.SecurityUtil;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Where;

import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.core.standard.enums.SystemBoolean;

/**
 * 通用资产属性定义model
 * @author lps
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Where(clause = "FLG_DELETED=0")
@Getter
@Setter
public class BaseAssetEntity extends AppBaseEntity{
	/**
	 * 分隔符
	 */
	protected static final String SPLIT_COMMA=",";

	/**
     * hash地址
     */
	@Column(name = "Hash", unique = false, nullable = true, insertable = true, updatable = true)
    protected String hash;
	/**
     * 上链状态
     */
	@Column(name = "Upload_status", unique = false, nullable = true, insertable = true, updatable = true)
    protected Integer uploadStatus=SystemBoolean.NO.getKey();
	/**
     * 登记方式
	 * TODO
     */
	@Column(name = "Upload_type", unique = false, nullable = true, insertable = true, updatable = true)
    protected Integer uploadType=0;

	/**
     * 模型编码
     */
	@Column(name = "mdcode", unique = false, nullable = true, insertable = true, updatable = true)
    protected String mdcode;

	/**
     * 金链平台日志id
     */
	@Column(name = "Bdc_baas_log_id", unique = false, nullable = true, insertable = true, updatable = true)
    protected String bdcBaasLogId;
	/**
     * 登记上链时间
     */
	@Column(name = "Upload_time", unique = false, nullable = true, insertable = true, updatable = true)
    protected Date uploadTime;

	/**
     * 资产建模编码
     */
	@Column(name = "Asset_code", unique = false, nullable = true, insertable = true, updatable = true)
    protected String assetCode;
	/**
     * 资产建模Name
     */
	@Column(name = "Asset_name", unique = false, nullable = true, insertable = true, updatable = true)
    protected String assetName;
	/**
	 * if is createInBlockchain
	 */
	@Column(name = "is_create", unique = false, nullable = true, insertable = true, updatable = true)
	protected Integer isCreate=SystemBoolean.NO.getKey();;


	/**
	 *
	 * 描述：组装上链数据
	 * @author lps
	 * @Date 2020年1月8日 上午10:21:22
	 * @return
	 */
	public String initAsset() {
       StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(this.getAssetName());
		stringBuffer.append(SPLIT_COMMA);

		stringBuffer.append(this.getAssetCode());
		stringBuffer.append(SPLIT_COMMA);

		stringBuffer.append(this.getMdcode());
		stringBuffer.append(SPLIT_COMMA);

		stringBuffer.append(this.getUploadType());
		stringBuffer.append(SPLIT_COMMA);

		return  stringBuffer.toString();
	}
	/**
	 *
	 * 描述：组装update上链数据
	 * @author lps
	 * @Date 2020年1月8日 上午10:21:22
	 * @return
	 */
	public String baseAsset() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(this.getAssetCode());
		stringBuffer.append(SPLIT_COMMA);

		return  stringBuffer.toString();
	}

	/**
	 * 描述：metadata数据
	 *
	 * @return
	 * @author lps
	 * @Date 2020年1月8日 下午2:53:05
	 */
	public String metadata() throws Exception {
		String besnString = JSONObject.toJSONString(this);
		JSONObject jsonObject = JSONObject.parseObject(besnString);
		SecurityUtil security = InstanceFactory.getInstance(SecurityUtil.class);
//		String metadata = security.encryptMetaData(security.generateKey(this.getId()), jsonObject.toString());
		jsonObject.remove("id");
		jsonObject.remove("creator");
		jsonObject.remove("creatorName");
		jsonObject.remove("createTime");
		jsonObject.remove("updator");
		jsonObject.remove("updatorName");
		jsonObject.remove("updateTime");
		jsonObject.remove("flgDeleted");
		jsonObject.remove("assetCode");
		jsonObject.remove("mdcode");
		jsonObject.remove("assetName");
		jsonObject.remove("isCreate");
		jsonObject.remove("hash");
		jsonObject.remove("bdcBaasLogId");
		jsonObject.remove("uploadStatus");
		String metadata = jsonObject.toJSONString().replaceAll("\"","").replaceAll(",","&");
		metadata = security.encryptMetaData(security.generateKey(this.getId()), jsonObject.toJSONString());
		System.out.println("metadata:"+metadata);
		return metadata;
	}
}
