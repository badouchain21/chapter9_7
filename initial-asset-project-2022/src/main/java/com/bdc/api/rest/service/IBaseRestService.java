package com.bdc.api.rest.service;

import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.designer.jdbc.common.web.form.BaseCommonSaveForm;
import com.badou.designer.jdbc.core.service.IBaseModuleService;
import com.badou.designer.jdbc.core.vo.BaseVO;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.security.ssl.token.pojo.Token;
import com.bdc.assetdefine.model.AssetDefinedEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:40:45
 * @todo restservice接口类
 */
public interface IBaseRestService extends IBaseModuleService{

	/**
	 * 确认接口权限
	 * @param tokenEntity
	 * @param request
	 * @return 结果信息集合
	 */
    Map<String, Object> authPermission(Token tokenEntity, HttpServletRequest request);

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:42:47
	 * @todo 保存基础信息
	 * @param moduleBean
	 * @param defaultFieldValue
	 * @param form
	 * @param baseVO
	 * @return 模型基类
	 * @throws Exception if has error(直接往外抛)
	 */
	BaseVO saveBaseVO(MdModuleEntity moduleBean,
			Map<String, String> defaultFieldValue, BaseCommonSaveForm form,BaseVO baseVO, Boolean isCreate)
			throws Exception;

	/**
	 *
	 * 描述：校验token
	 * @author linxiaoqing
	 * @date 2019年8月26日
	 * @param token
	 * @return
	 * @throws Exception
	 */
    JsonReturnBean validToken(String token) throws Exception;

	/**
	 * GET BASEVO BY ASSETEKY WHICH IS FROM ASSETDEFINED
	 * @param assetKey
	 * @return
	 * @throws Exception
	 */
    BaseVO getVObyAssetKey(AssetDefinedEntity assetDefinedEntity, String assetKeyValue,MdModuleEntity moduleEntity,String userId)throws Exception;

}
