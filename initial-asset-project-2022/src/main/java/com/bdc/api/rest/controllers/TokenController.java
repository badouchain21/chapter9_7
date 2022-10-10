package com.bdc.api.rest.controllers;


import com.badou.brms.base.support.BaseAction;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.security.ssl.token.TokenWarehouse;
import com.badou.security.ssl.token.pojo.Token;
import com.badou.security.ssl.token.service.ITokenService;
import com.badou.security.ssl.token.utils.EncryptAndDecodeUtil;
import com.bdc.api.ApiConst;
import com.bdc.api.intermanage.netinformation.model.NetInformationEntity;
import com.bdc.api.intermanage.netinformation.service.INetInformationService;
import com.bdc.api.rest.ReturnCodeEnum;
import com.bdc.api.rest.container.TokenContainer;
import com.bdc.api.rest.vo.ApiReturnVO;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 描述：Token
 *
 * @author linxiaoqing
 * @date 2019年8月15日
 */
@RestController
@RequestMapping(value = "/api/v1")
public class TokenController extends BaseAction {

    private boolean synchronizeOnSession = false;

    @Autowired
    private ITokenService tokenService;

    @Autowired
    private INetInformationService netInfoService;

    @RequestMapping(value="/getToken")
    public ApiReturnVO getToken() throws GeneralSecurityException {
        ApiReturnVO apiReturnVO = new ApiReturnVO();
        Random rand = new Random();
        JSONObject obj = new JSONObject();
        StringBuilder encryptToken = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String host = this.request.getRemoteAddr();
        String appID = this.request.getParameter("APP_ID");
        String appKey = this.request.getParameter("APP_KEY");
        if(StringUtils.isBlank(appID) ||StringUtils.isBlank(appKey))
        {
        	apiReturnVO.setReturnCode(ApiReturnVO.FAIL);
            apiReturnVO.setReturnMsg("APP_ID或APP_KEY不可为空");
            return apiReturnVO;
        }
        QueryCriterion queryCriterion2 = new QueryCriterion();
        queryCriterion2.addParam(new QueryHibernatePlaceholderParam("app_Id", appID, null, QueryOperSymbolEnum.eq));
        queryCriterion2.addParam(new QueryHibernatePlaceholderParam("appKey", appKey, null, QueryOperSymbolEnum.eq));
        List<NetInformationEntity> netInformationEntitys =  netInfoService.find(queryCriterion2);
        if(netInformationEntitys== null || netInformationEntitys.size()==0)
        {
            apiReturnVO.setReturnCode(ApiReturnVO.FAIL);
            apiReturnVO.setReturnMsg("应用不存在");
            return apiReturnVO;
        }
         if(Integer.parseInt(DictionaryLib.getItemValueByItemCode(ApiConst.IS_CALL_INTERFACE, "1"))==netInformationEntitys.get(0).getIsCallInterface().intValue())
        {
            apiReturnVO.setReturnCode(ApiReturnVO.FAIL);
            apiReturnVO.setReturnMsg("应用没有权限");
            return apiReturnVO;
        }
        Token token2 = new Token();
        try {
            encryptToken.append(host + "&" + appID + "&" + appKey + "&" + sdf.format(DateUtils.addDays(new Date(), 1))
                + "&" + rand.nextInt());
        } catch (Exception e) {
            apiReturnVO.setReturnCode(ApiReturnVO.FAIL);
            apiReturnVO.setReturnMsg("402加密失败");
            return apiReturnVO;
        }
        com.alibaba.fastjson.JSONObject userInfo = new com.alibaba.fastjson.JSONObject();
        userInfo.put("userId",netInformationEntitys.get(0).getCreator());
        userInfo.put("userName",netInformationEntitys.get(0).getCreatorName());
        if (this.synchronizeOnSession) {
            HttpSession session = this.request.getSession(false);
            if (session != null) {
                Object mutex = WebUtils.getSessionMutex(session);
                synchronized (mutex) {
                    String encryptCode = EncryptAndDecodeUtil.encryptMode(encryptToken.toString());
                    String token = encryptCode.replaceAll("/", "_a").replace("+", "_b").replaceAll("=", "_c");
                    //TokenWarehouse.put(sdf.format(new Date()), token);
                    obj.put("status", "200");
                    obj.put("token", token);
                    apiReturnVO.setReturnCode(ApiReturnVO.SUCCESS);
                    apiReturnVO.setData(token);
                    TokenContainer.getInstance().put(token,userInfo);
                    return apiReturnVO;
                }
            }
        }
        String encryptCode = EncryptAndDecodeUtil.encryptMode(encryptToken.toString());
        String token = encryptCode.replaceAll("/", "_a").replace("+", "_b").replaceAll("=", "_c");
        token = token.replaceAll("\r|\n", "");
       // TokenWarehouse.put(sdf.format(DateUtils.addDays(new Date(), 1)), token);
        apiReturnVO.setReturnCode(ApiReturnVO.SUCCESS);
        apiReturnVO.setData(token);
        try {
            token2.setId(UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", ""));
            token2.setAppId(appID);
            token2.setAppKey(appKey);
            token2.setCreatetime(new Date());
            token2.setEndtime(DateUtils.addDays(new Date(), 1));
            token2.setToken(token);
            userInfo.put("tokenEntity",token2);
            TokenContainer.getInstance().put(token,userInfo);
            this.tokenService.create(token2);
        } catch (Exception e) {
            apiReturnVO.setReturnCode(ApiReturnVO.FAIL);
            apiReturnVO.setReturnMsg("Token保存失败");
            return apiReturnVO;
        }
        return apiReturnVO;
    }

}
