package com.bdc.blockchain.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.badou.core.standard.enums.SystemBoolean;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.common.BaseAssetConst;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.blockchain.model.ApiReturnVO;
import com.bdc.blockchain.model.BdcBaasLoggerVO;
import com.bdc.blockchain.model.BlockChainRequest;
import com.bdc.blockchain.service.IBlockChainService;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.utils.SecurityUtil;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bdc4j.BdcService;
import bdc4j.BdcService.Chain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.brms.system.org.dao.IUserCommonInfoDao;
import com.badou.brms.system.org.service.IUserService;
import com.badou.brms.system.org.vo.User;
import com.badou.brms.system.org.vo.UserCommInfo;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.tools.common.util.HttpInvoker;
import com.badou.tools.common.util.StringUtils;


/**
 * 描述：对接区块链服务实现类
 *
 * @author lps
 * @date 2019年9月3日
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class BlockChainServiceImpl implements IBlockChainService {

    /**
     * default create asset function NAME
     */
    private static final String CREATE_FUN_NAME = "createAsset";
    /**
     * default update asset function NAME
     */
    private static final String UPDATE_FUN_NAME = "updateAsset";

    /**
     * 分组数量
     */
    private final Integer GROUP_INDEX = 50;

    @Autowired
    private BaseUploadAssetFactory baseUploadAssetFactory;

    @Autowired
    private IAssetDefinedService assetDefinedService;

    @Autowired
    private IUploadAssetLogService uploadAssetLogService;
    @Autowired
    private IBaseAssetService baseAssetService;

    @Override
    public void uploadAsset(@NonNull UploadTypeEnum assetType, @NonNull String id) {
        Boolean isLogon = false;
        BaseAssetEntity asset = null;
        UploadAssetLogEntity uploadAssetLog = null;
        // 获取资产对应的上链服务
        IBaseUploadAssetService uploadService = baseUploadAssetFactory.getUploadService(assetType.getCode());
        try {
            // 获取资产实体
            asset = uploadService.get(assetType, id);
            // 设置登录信息
            isLogon = setLogon(asset);
            //创建上链日志
            uploadAssetLog = uploadAssetLogService.createSimpleUploadAssetLog(asset);

            AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(asset.getAssetCode());
            // 上链时间
            asset.setUploadTime(new Date());
            String result,transactionJsonStr=null;
            Object primarykeyValue = baseAssetService.getFieldValue(BaseAssetConst.ASSET_KEY_TYPE_ID, asset);

            if(uploadAssetLogService.getUploadAssetSize(asset.getAssetCode(),primarykeyValue.toString())>1){
                transactionJsonStr = genTransactionJSONStr(asset,UPDATE_FUN_NAME);
            }else{
                transactionJsonStr = genTransactionJSONStr(asset,CREATE_FUN_NAME);
            }
            Chain chain = getChain(assetDefinedEntity.getBlockchainConfig());
            result =chain.create("Invoke", transactionJsonStr, true);
            // 获取上链结果
            ApiReturnVO returnBean = JSONObject.parseObject(result, ApiReturnVO.class);
            if (returnBean==null ||(returnBean!=null && !ApiReturnVO.SUCCESS.equals(returnBean.getReturnCode()))) {
                // 上链失败
                throw new Exception(returnBean==null?"FAIL":returnBean.getReturnMsg());
            } else {
                // 异步上链成功，保存数据
                JSONObject returnJson = JSONObject.parseObject(returnBean.getReturnData().toString());
                uploadService.saveLogId(assetType, asset, returnJson.getString("logId"));
                uploadAssetLog.setBdcBaasLogId(returnJson.getString("logId"));
                uploadAssetLog.setUploadTime(new Date());
                uploadAssetLogService.update(uploadAssetLog);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
            // 上链失败
            if (asset != null) {
                uploadService.updateStatus(assetType, asset, UploadStatusEnum.FAILD);
                if (Objects.nonNull(uploadAssetLog)){
                    uploadAssetLog.setLog("区块链应用配置不可用");
                    uploadAssetLog.setUploadStatus(UploadStatusEnum.FAILD.getValue());
                    uploadAssetLog.setUploadTime(new Date());
                    uploadAssetLogService.update(uploadAssetLog);
                }
            }
        } finally {
            if (isLogon) {
                LogonCertificateHolder.clear();
            }
        }
    }

    /**
     * generate the blockchain transaction jsonStr
     * @param asset
     * @return
     */
    private String genTransactionJSONStr(BaseAssetEntity asset,String fcnName) throws Exception {
        BlockChainRequest blockChainRequest = new BlockChainRequest();
        blockChainRequest.setFcn(fcnName);
        StringBuffer stringBuffer = new StringBuffer();
        if(UPDATE_FUN_NAME.equals(fcnName)){
            stringBuffer.append(asset.baseAsset());
        }else{
            stringBuffer.append(asset.initAsset());
        }
        Object primarykeyValue = baseAssetService.getFieldValue(BaseAssetConst.ASSET_KEY_TYPE_ID, asset);

        stringBuffer.append(primarykeyValue);
        stringBuffer.append(",");

        stringBuffer.append(asset.metadata());
        blockChainRequest.setArgs(stringBuffer.toString());
        JSONObject json = new JSONObject();
        String besnString = JSONObject.toJSONString(blockChainRequest);
        JSONObject jsonObject = JSONObject.parseObject(besnString);
        return jsonObject.toString();
    }

    @Override
    public void updateUploadStatus(@NonNull List<UploadAssetLogEntity> logEntities) {
        Map<String, List<UploadAssetLogEntity>> map = logEntities.stream()
                .filter(e -> StringUtils.isNotEmpty(e.getAssetCode()))
                .collect(Collectors.groupingBy(e -> e.getAssetCode()));
        for (Map.Entry<String, List<UploadAssetLogEntity>> entry : map.entrySet()) {
            String assetCode = entry.getKey();
            List<UploadAssetLogEntity> uploadAssetLogEntities = entry.getValue();
            try {
                //获取资产定义
                AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(assetCode);
                //过滤数据
                List<UploadAssetLogEntity> logEntityList = uploadAssetLogEntities.stream().filter(e -> StringUtils.isNotEmpty(e.getBdcBaasLogId())).collect(Collectors.toList());
                List<String> ids = logEntityList.stream()
                        .map(e -> e.getBdcBaasLogId())
                        .collect(Collectors.toList());
                // 获取上链结果
                Map data = getUploadResult(ids,assetDefinedEntity.getBlockchainConfig());
                //更新状态
                updateStatus(assetCode,logEntityList,data);
            } catch (Exception e) {
                log.error(e.getMessage());
                UploadTypeEnum assetType = UploadTypeEnum.getUploadTypeEnumByCode(baseAssetService.getAssetCode(assetCode));
                IBaseUploadAssetService uploadService = baseUploadAssetFactory.getUploadService(assetType.getCode());
                for (UploadAssetLogEntity uploadAssetLogEntity : uploadAssetLogEntities) {
                    setLogon(uploadAssetLogEntity);
                    //更新上链日志
                    uploadAssetLogEntity.setUploadStatus(UploadStatusEnum.FAILD.getValue());
                    uploadAssetLogEntity.setLog("获取上链结果失败:" + e.getMessage());
                    uploadAssetLogService.update(uploadAssetLogEntity);

                    BaseAssetEntity baseAssetEntity = uploadService.get(assetType, uploadAssetLogEntity.getAssetId());
                    uploadService.updateStatus(assetType,baseAssetEntity,UploadStatusEnum.FAILD);
                    LogonCertificateHolder.clear();
                }
            }
        }
    }

    /**
     *
     * 描述：加密
     *
     * @author linxiaoqing
     * @date 2019年9月4日
     * @param key
     * @param metadata
     * @return
     * @throws Exception
     */
    private String encryptMetaData(@NonNull BaseAssetEntity asset) throws Exception {
        SecurityUtil security = InstanceFactory.getInstance(SecurityUtil.class);
        JSONObject json = new JSONObject();
        String metadata = security.encryptMetaData(security.generateKey(asset.getId()), asset.metadata().toString());
        json.put("metadata", metadata);
        return json.toString();
    }

    /**
     *
     * 描述：获取链工具
     *
     * @author linxiaoqing
     * @date 2019年9月4日
     * @return
     */
    private Chain getChain(BlockchainConfigEntity blockchainConfig) {
        String appId = blockchainConfig.getAppId().trim();
        String appKey = blockchainConfig.getAppKey().trim();
        String channelName = blockchainConfig.getChannelName().trim();
        String networkName = blockchainConfig.getNetworkName().trim();
        String tokenUrl = blockchainConfig.getTokenUrl().trim();
        String chainPreUrl = blockchainConfig.getChainPreUrl().trim();
        // 获取服务
        BdcService bdcService = new BdcService(chainPreUrl, tokenUrl, appId, appKey, channelName, networkName);
        // 获取token
        String token = bdcService.getToken();
        return bdcService.getChain(token);
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
        if (StringUtils.isEmpty(userId))
            return false;
        User user = InstanceFactory.getInstance(IUserService.class).getById(userId);
        if (user == null)
            return false;
        UserCommInfo userComminfo = InstanceFactory.getInstance(IUserCommonInfoDao.class).getById(user.getUserInfoId());
        if (userComminfo == null)
            return false;
        LogonCertificate logon = new LogonCertificate();
        logon.setUserId(userId);
        logon.setUserName(userComminfo.getName());
        logon.setLoginId(userComminfo.getLogonId());
        LogonCertificateHolder.setLogonCertificate(logon);
        return true;
    }

    /**
     *
     * 描述：根据ID获取金链上链日志
     *
     * @author linxiaoqing
     * @date 2019年9月3日
     * @param ids
     * @return
     * @throws Exception
     */
    private Map<String, BdcBaasLoggerVO> getUploadResult(List<String> ids,BlockchainConfigEntity blockchainConfig) throws Exception {
        System.out.println("getUploadResult:"+ StringUtils.join(ids.toArray(), ","));
        // 组装URL和参数
        String url = blockchainConfig.getLogUrl() + "?ids=" + StringUtils.join(ids.toArray(), ",");
        String result = HttpInvoker.httpPost(url,null,null);
        // 获取结果
        ApiReturnVO returnBean = JSONObject.parseObject(result, ApiReturnVO.class);
        if (!ApiReturnVO.SUCCESS.equals(returnBean.getReturnCode())) {
            throw new Exception(returnBean.getReturnMsg());
        }
        // 用于存放结果
        Map<String, BdcBaasLoggerVO> map = new HashMap<>();
        // 解析返回数据
        Object returnData = returnBean.getReturnData();
        JSONArray.parseArray(returnData.toString()).forEach(i -> {
            BdcBaasLoggerVO logVo = JSONObject.parseObject(i.toString(), BdcBaasLoggerVO.class);
            map.put(logVo.getId(), logVo);
        });
        return map;
    }

    /**
     * 默认区块高度
     */
    private final static int DEFAULT_BLOCKHEIGHT = 1;

    @Override
    public int getBlockHeight(BlockchainConfigEntity blockchainConfig) {
        String url =                blockchainConfig.getMonitorUrl() + "/getBlockchainHeight?channelName=" + blockchainConfig.getChannelName();
        try {
            String data = HttpInvoker.httpGet(url);
            JSONObject jsonObject = getReturnData(data);
            if (jsonObject == null || !jsonObject.containsKey("blockHeight"))
                return DEFAULT_BLOCKHEIGHT;
            return jsonObject.getIntValue("blockHeight");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return DEFAULT_BLOCKHEIGHT;
        }
    }

    /**
     * 获取返回的数据
     *
     * @param data
     * @return
     */
    private JSONObject getReturnData(String data) {
        if (data != null) {
            JSONObject jsonObject = JSONObject.parseObject(data);
            if (jsonObject == null || jsonObject.get("return_data") == null)
                return null;
            JSONObject dataObject = JSONObject.parseObject(jsonObject.get("return_data").toString());
            if (dataObject != null && jsonObject.get("return_code").equals("SUCCESS")) {
                return dataObject;
            }
        }
        return null;
    }

    /**
     *
     * 描述：按照指定数量分组
     * @author linxiaoqing
     * @date 2019年10月21日
     * @param list
     * @param index
     * @return
     */
    private <T> List<List<T>> groupList(List<T> list, Integer index) {
        int listSize = list.size();
        List<List<T>> newList = new ArrayList<>(listSize/index + 1);
        for (int i = 0; i < list.size(); i += index) {
            if (i + index > listSize) {
                index = listSize - i;
            }
            List<T> tempList = list.subList(i, i + index);
            newList.add(tempList);
        }
        return newList;
    }

    /**
     * 更新上链日志
     * @param assetCode 资产编码
     * @param logEntities 上链日志实体集合
     * @param map 结果
     */
    private void updateStatus (String assetCode,
                               List<UploadAssetLogEntity> logEntities,
                               Map<String,BdcBaasLoggerVO> map
    ) {
        UploadTypeEnum assetType = UploadTypeEnum.getUploadTypeEnumByCode(baseAssetService.getAssetCode(assetCode));
        //获取对应的上链服务
        IBaseUploadAssetService uploadService = baseUploadAssetFactory.getUploadService(assetType.getCode());

        Map<String, List<UploadAssetLogEntity>> logMap = logEntities.stream().collect(Collectors.groupingBy(e -> e.getBdcBaasLogId()));

        for (Map.Entry<String, BdcBaasLoggerVO> entry : map.entrySet()) {
            String key = entry.getKey();
            BdcBaasLoggerVO blocklog = entry.getValue();

            UploadAssetLogEntity uploadAssetLogEntity = null;

            if (Objects.nonNull(logMap) && logMap.containsKey(key)){
                List<UploadAssetLogEntity> uploadAssetLogEntities = logMap.get(key);
                uploadAssetLogEntity = uploadAssetLogEntities.get(0);
                setLogon(uploadAssetLogEntity);
            }

            // 保存交易Hash
            if (blocklog.isUploaded()) {
                if (Objects.nonNull(uploadAssetLogEntity)){
                    //更新日志
                    uploadAssetLogEntity.setHash(blocklog.getTransactionHash());
                    uploadAssetLogEntity.setUploadStatus(UploadStatusEnum.UPLOADED.getValue());
                    uploadAssetLogService.update(uploadAssetLogEntity);

                    String assetId = uploadAssetLogEntity.getAssetId();
                    BaseAssetEntity baseAssetEntity = uploadService.get(assetType, assetId);
                   /* if (UploadStatusEnum.UPLOADING.getValue().intValue() == baseAssetEntity.getUploadStatus().intValue()){
                        baseAssetEntity.setUploadStatus(UploadStatusEnum.UPLOADED.getValue());
                    }*/
                    baseAssetEntity.setUploadStatus(UploadStatusEnum.UPLOADED.getValue());
                    uploadService.saveHash(assetType, baseAssetEntity, blocklog.getTransactionHash());
                }
            } else {
                //更新失败状态
                if (blocklog.isUploadFaild()){
                    if (Objects.nonNull(uploadAssetLogEntity)){
                        //更新日志
                        uploadAssetLogEntity.setLog(blocklog.getFieldLog());
                        uploadAssetLogEntity.setUploadStatus(UploadStatusEnum.FAILD.getValue());
                        uploadAssetLogService.update(uploadAssetLogEntity);

                        String assetId = uploadAssetLogEntity.getAssetId();
                        BaseAssetEntity baseAssetEntity = uploadService.get(assetType, assetId);
                        if (UploadStatusEnum.UPLOADING.getValue().intValue() == baseAssetEntity.getUploadStatus().intValue()){
                            uploadService.updateStatus(assetType, baseAssetEntity, blocklog.getUploadStatus());
                        }
                    }
                }
            }
            LogonCertificateHolder.clear();
        }

    }


}
