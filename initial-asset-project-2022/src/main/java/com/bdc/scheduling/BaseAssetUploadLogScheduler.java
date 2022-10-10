package com.bdc.scheduling;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.brms.dictionary.DictionaryLib;
import com.badou.brms.system.org.dao.IUserCommonInfoDao;
import com.badou.brms.system.org.service.IUserService;
import com.badou.brms.system.org.vo.User;
import com.badou.brms.system.org.vo.UserCommInfo;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.core.standard.enums.SystemBoolean;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 上链日志定时器
 * 更新一定时间内获取不到结果的日志状态
 * @author zhongzhilong
 * @date 2020-11-21 16:21:30
 */
@Slf4j
@Component
@Order(value=400)
public class BaseAssetUploadLogScheduler extends Scheduler {

    @Autowired
    private IUploadAssetLogService uploadAssetLogService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBaseAssetService baseAssetService;
    @Autowired
    private BaseUploadAssetFactory baseUploadAssetFactory;

    @Override
    protected void processTask() {
        //获取数据字典定义的状态维持最大时间
        String timeStr = DictionaryLib.getItemValueByItemCode("SCHEDULER_CONFIG", "UPLOAD_LOG_UPDATE_TIME");
        //获取数据字典定义的时间格式
        String timeType = DictionaryLib.getItemValueByItemCode("SCHEDULER_CONFIG", "UPLOAD_LOG_UPDATE_TIME_TYPE");
        //获取上链中的日志
        List<UploadAssetLogEntity> uploading = uploadAssetLogService.findUploading(null);
        //获取现在的时间
        LocalDateTime nowTime = LocalDateTime.now();

        for (UploadAssetLogEntity uploadAssetLogEntity : uploading) {
            try {
                //上链状态维持的最大时间
                LocalDateTime maxTime = getMaxTime(uploadAssetLogEntity.getCreateTime(), timeStr, timeType);
                if (Objects.isNull(maxTime))
                    continue;

                if (nowTime.isAfter(maxTime)){
                    setLogon(uploadAssetLogEntity);
                    //判断资产还有没有上链中数据
                    List<UploadAssetLogEntity> uploadAssetLogEntities = uploadAssetLogService.findByAssetId(uploadAssetLogEntity.getAssetId(), UploadStatusEnum.UPLOADING);
                    //如果是最后一条数据
                    if (Objects.nonNull(uploadAssetLogEntities) && uploadAssetLogEntities.size() == SystemBoolean.YES.getKey().intValue()){
                        String assetCode = baseAssetService.getAssetCode(uploadAssetLogEntity.getAssetCode());
                        UploadTypeEnum assetType = UploadTypeEnum.getUploadTypeEnumByCode(assetCode);
                        IBaseUploadAssetService uploadService = baseUploadAssetFactory.getUploadService(assetType.getCode());
                        BaseAssetEntity baseAssetEntity = uploadService.get(assetType, uploadAssetLogEntity.getAssetId());
                        //判断是不是上链中状态
                        if (baseAssetEntity.getUploadStatus().intValue() == UploadStatusEnum.UPLOADING.getValue().intValue()){
                            //更新资产状态
                            uploadService.updateStatus(assetType,baseAssetEntity,UploadStatusEnum.FAILD);
                        }
                    }
                    uploadAssetLogEntity.setLog("获取上链结果超时");
                    uploadAssetLogEntity.setUploadStatus(UploadStatusEnum.FAILD.getValue());
                    uploadAssetLogService.update(uploadAssetLogEntity);
                    LogonCertificateHolder.clear();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
        }
    }

    @Override
    protected String getCron() {
        return DictionaryLib.getItemValueByItemCode("SCHEDULER_CONFIG","UPLOAD_LOG_UPDATE_CRON");
    }

    /**
     * 描述：设置当前用户
     *
     * @author zhongzhilong
     * @date 2020-11-21 17:10
     * @param entity
     */
    private void setLogon(AppBaseEntity entity) {
        String userId = entity.getCreator();
        User user = userService.getById(userId);
        UserCommInfo userComminfo = InstanceFactory.getInstance(
                IUserCommonInfoDao.class).getById(user.getUserInfoId());
        LogonCertificate logon = new LogonCertificate();
        logon.setUserId(userId);
        logon.setUserName(userComminfo.getName());
        logon.setLoginId(userComminfo.getLogonId());
        LogonCertificateHolder.setLogonCertificate(logon);
    }

    /**
     * 获取数据字典配置的最大时间
     * @param timeStr 时间偏移量
     * @param timeType 时间格式 y年 M月 d日 H时 m分 s秒
     * @param nowDate 当前时间
     * @return
     */
    private LocalDateTime getMaxTime (Date nowDate,String timeStr,String timeType){
        //当前时间
        LocalDateTime max = LocalDateTime.ofInstant(nowDate.toInstant(),ZoneId.systemDefault());
        Integer time = Integer.parseInt(timeStr);
        if (StringUtils.isEmpty(timeType)){
            return null;
        }
        switch (timeType) {
            case "y":
                max = max.plusYears(time);
                break;
            case "M":
                max = max.plusMonths(time);
                break;
            case "d":
                max = max.plusDays(time);
                break;
            case "H":
                max = max.plusHours(time);
                break;
            case "m":
                max = max.plusMinutes(time);
                break;
            case "s":
                max = max.plusSeconds(time);
                break;
            default:
                return null;
        }
        return max;
    }

}
