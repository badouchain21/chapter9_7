package com.bdc.api.rest.controllers;

import com.alibaba.fastjson.JSONObject;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.api.rest.ReturnCodeEnum;
import com.bdc.api.rest.container.TokenContainer;
import com.bdc.api.rest.vo.ApiReturnVO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/blockchain")
public class BlockChainController {

    @Value("${base_path}")
    private String baseUrl;

    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IAssetDefinedService assetDefinedService;
    @Autowired
    private IBaseAssetService baseAssetService;

    /**
     * 获取区块链浏览器URL
     * @param assetCode 资产编码
     * @param assetKeyId 资产主键ID
     * @param request
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/url/{assetCode}/{assetKeyId}", produces = {"application/json;charset=UTF-8"})
    public ApiReturnVO getBlockChainBrowserUrl (@PathVariable String assetCode,
                                                @PathVariable String assetKeyId,
                                                HttpServletRequest request
    ){
        ApiReturnVO returnVO = new ApiReturnVO();
        try{
            String access_token = request.getParameter("access_token");
            JSONObject userInfo = TokenContainer.getInstance().get(access_token);
            String userId = userInfo.getString("userId");
            //设置登录用户
            BdLoginUtil.setLogin(userId);
            if (StringUtils.isEmpty(assetCode)){
                throw new Exception("资产编码不能为空");
            }
            // 根据资产编码获取资产建模定义
            AssetDefinedEntity assetDefined = assetDefinedService.findByAssetCode(assetCode);
            //转换主键ID值
            Object assetId = baseAssetService.converAssetId(assetKeyId, assetDefined);
            //获取资产实体
            BaseAssetEntity baseAssetEntity = baseAssetService.getByAssetId(assetCode,assetId);
            if (baseAssetEntity != null) {
                if (baseAssetEntity.getUploadStatus().intValue() == UploadStatusEnum.UPLOADED.getValue().intValue()){
                    if(StringUtils.isEmpty(baseAssetEntity.getHash()))
                        throw new Exception("交易Hash为空");
                    String orignalSign =   baseAssetEntity.getCreatorName() + SecurityUtil.COMM_SEPERTOR + new Date().getTime() + SecurityUtil.COMM_SEPERTOR + baseAssetEntity.getId();
                    String sign = securityUtil.signAuth(orignalSign);
                    String orignal = SecurityUtil.str2HexStr(orignalSign);
                    String signParam = securityUtil.generateSignParam(sign, orignal);
                    String url = baseUrl + "/client/blockinfo/" + assetDefined.getBlockchainConfig().getChannelName() + "/transaction?searchStr=" + baseAssetEntity.getHash() +"&signParam="+signParam ;
                    returnVO.setReturnMsg(ReturnCodeEnum.SUCCESS.toString());
                    returnVO.setReturnCode(ApiReturnVO.SUCCESS);
                    returnVO.setData(url);
                } else {
                    throw new Exception("资产未上链");
                }
            } else {
                throw new Exception("找不到资产");
            }
            return returnVO;
        }catch(Exception e){
            log.error(e.getMessage());
            returnVO.setReturnMsg(ReturnCodeEnum.FAIL.getReturnMsg()+" : "+e.getMessage());
            returnVO.setReturnCode(ReturnCodeEnum.FAIL.getReturnCode());
            return returnVO;
        } finally {
            LogonCertificateHolder.clear();
        }
    }

}
