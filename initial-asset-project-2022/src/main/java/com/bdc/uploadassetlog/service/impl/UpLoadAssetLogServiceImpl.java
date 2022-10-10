package com.bdc.uploadassetlog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.brms.dboperation.query.*;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.core.standard.enums.SystemBoolean;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.common.BaseAssetConst;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.vo.BaseAssetIndexAttentionVO;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.EmptyUtil;
import com.bdc.uploadassetlog.dao.IUploadAssetLogDao;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 描述：资产上链日志 服务实现
 * @author zhongzhilong
 * @date 2020-11-02 11:28:50
 */
@Slf4j
@Service
public class UpLoadAssetLogServiceImpl extends BaseSpringService<UploadAssetLogEntity, Serializable> implements IUploadAssetLogService {

    private IUploadAssetLogDao uploadAssetLogDao;
    @Autowired
    private IAssetDefinedService assetDefinedService;
    @Autowired
    private IBaseAssetService baseAssetService;

    /**
     * sql年份调用函数
     */
    private final String YEAR_METHOD = "year";
    /**
     * sql月份调用函数
     */
    private final String MONTH_METHOD = "month";

    @Autowired
    public void setUploadAssetLogDao(IUploadAssetLogDao uploadAssetLogDao) {
        this.uploadAssetLogDao = uploadAssetLogDao;
        super.setBaseDAO(uploadAssetLogDao);
    }

    @Override
    public UploadAssetLogEntity createSimpleUploadAssetLog(BaseAssetEntity baseAssetEntity) {
        UploadAssetLogEntity logEntity = new UploadAssetLogEntity();

        try {
            Object primarykeyValue = baseAssetService.getFieldValue(BaseAssetConst.ASSET_KEY_TYPE_ID, baseAssetEntity);
            Object assetNameValue = baseAssetService.getFieldValue(BaseAssetConst.ASSET_KEY_TYPE_NAME, baseAssetEntity);
            logEntity.setAssetCode(baseAssetEntity.getAssetCode());
            logEntity.setAssetId(baseAssetEntity.getId());
            logEntity.setAssetKey(primarykeyValue.toString());
            logEntity.setAssetTypeName(baseAssetEntity.getAssetName());
            logEntity.setUploadStatus(UploadStatusEnum.UPLOADING.getValue());
            logEntity.setAssetNameField(assetNameValue.toString());
            this.create(logEntity);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return logEntity;
    }

    @Override
    public BaseAssetIndexAttentionVO getBaseAssetIndexAttention() {
        BaseAssetIndexAttentionVO indexAttentionVO = new BaseAssetIndexAttentionVO();
        indexAttentionVO.setUnUnploadAssetCount(baseAssetService.statAssetCount(UploadStatusEnum.UNUPLOAD));
        indexAttentionVO.setUploadedAssetCount(getReportDataByStatus(UploadStatusEnum.UPLOADED));
        indexAttentionVO.setUploadingAssetCount(getReportDataByStatus(UploadStatusEnum.UPLOADING));
        indexAttentionVO.setUploadedAssetIn24Hours(uploadAssetLogDao.getReportDataByTime(LocalDateTime.now().minusDays(1),null));
        indexAttentionVO.setUploadedAssetInOneMonth(uploadAssetLogDao.getReportDataByTime(LocalDateTime.now().minusMonths(1),null));
        return indexAttentionVO;
    }

    @Override
    public JSONArray getReportDataByAssetType(@NotNull Integer year) {
        JSONArray jsonArray = new JSONArray();
        List reportDataByMonth = uploadAssetLogDao.getReportDataByType(year,listAssetCode());

        reportDataByMonth.forEach(e -> {
            Object[] arr = new Object[2];
            JSONObject json = (JSONObject) JSONObject.toJSON(e);
            arr[0] = json.getString("assetName");
            arr[1] = json.getIntValue("assetCount");
            jsonArray.add(arr);
        });
        return jsonArray;
    }

    @Override
    public int getReportDataByStatus (UploadStatusEnum uploadStatusEnum){
        return uploadAssetLogDao.getReportDataByStatus(uploadStatusEnum);
    }

    @Override
    public JSONArray getReportDataByYear(@NotNull Integer startYear,@NotNull Integer endYear) {
        JSONArray jsonArray = new JSONArray();
        List reportDataByYear = uploadAssetLogDao.getReportData(
                LocalDateTime.of(LocalDate.ofYearDay(startYear, LocalDate.MIN.getDayOfYear()), LocalTime.MIN).with(TemporalAdjusters.firstDayOfYear()),
                LocalDateTime.of(LocalDate.ofYearDay(endYear, LocalDate.MIN.getDayOfYear()), LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear()),
                listAssetCode(),
                YEAR_METHOD);

        //取出数据方便筛选
        Map<Integer,Integer> map = new HashMap<>();
        reportDataByYear.forEach(e -> {
            JSONObject json = (JSONObject) JSONObject.toJSON(e);
            map.put(json.getIntValue("year"),json.getIntValue("assetCount"));
        });

        for (int i = startYear; i <= endYear; i++) {
            int[] arr = new int[2];
            arr[0] = i;
            if (map.containsKey(i)){
                arr[1] = map.get(i);
            } else {
                arr[1] = 0;
            }
            jsonArray.add(arr);
        }
        return jsonArray;
    }

    @Override
    public int getUploadAssetSize(@NotNull String assetCodeVal, @NotNull String assetKeyVal){
        return  uploadAssetLogDao.getUploadAssetSize(assetCodeVal,assetKeyVal);
    }

    /**
     * 获取所有的资产编码
     * @return
     */
    private List<String> listAssetCode (){
        return assetDefinedService.listAll().stream().map(e -> e.getAssetCode()).collect(Collectors.toList());
    }

    @Override
    public Pagelet findByAssetId(LocalDateTime time,String assetId,Pagelet pagelet) {
        try {
            uploadAssetLogDao.findByAssetId(assetId, time,pagelet);
            pagelet.setDatas(baseAssetService.fillAssetDataWithJumpUrl(pagelet.getDatas(), false,false));
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return pagelet;
    }

    @Override
    public Pagelet findByKeyword(String keyword, LocalDateTime time, Pagelet pagelet) {
        try {
            uploadAssetLogDao.findByKeyword(keyword, time, pagelet);
            pagelet.setDatas(baseAssetService.fillAssetDataWithJumpUrl(pagelet.getDatas(), false,false));
        } catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return pagelet;
    }

    @Override
    public List<UploadAssetLogEntity> findUploading(Integer maxResultCount) {
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("UPLOAD_STATUS",UploadStatusEnum.UPLOADING.getValue(),null,QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
        queryCriterion.addOrder(new QueryOrderby(1,"create_time",SortOrderEnum.ase));
        if (Objects.nonNull(maxResultCount)){
            queryCriterion.setMaxResults(maxResultCount);
        }
        List<UploadAssetLogEntity> logEntities = uploadAssetLogDao.find(queryCriterion);
        return logEntities;
    }

    @Override
    public List<UploadAssetLogEntity> findByAssetId (@NotNull String assetId,UploadStatusEnum uploadStatusEnum) {
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("ASSET_ID",assetId,null,QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("UPLOAD_STATUS",uploadStatusEnum.getValue(),null,QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
        queryCriterion.addOrder(new QueryOrderby(1,"create_time",SortOrderEnum.desc));
        List<UploadAssetLogEntity> logEntities = uploadAssetLogDao.find(queryCriterion);
        return EmptyUtil.isNotEmpty(logEntities)?logEntities:null;
    }

    @Override
    public Integer getReportDataByTime(@NotNull LocalDateTime startTime, LocalDateTime endTime) {
        return uploadAssetLogDao.getReportDataByTime(startTime,endTime);
    }

    @Override
    public JSONArray getReportDataByMonth(@NotNull Integer startYear, @NotNull Integer endYear) {
        JSONArray jsonArray = new JSONArray();
        List reportDataByMonth = uploadAssetLogDao.getReportData(
                LocalDateTime.of(LocalDate.ofYearDay(startYear, LocalDate.MIN.getDayOfYear()), LocalTime.MIN).with(TemporalAdjusters.firstDayOfYear()),
                LocalDateTime.of(LocalDate.ofYearDay(endYear, LocalDate.MIN.getDayOfYear()), LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear()),
                listAssetCode(),
                MONTH_METHOD);

        reportDataByMonth.forEach(e -> {
            int[] arr = new int[2];
            JSONObject json = (JSONObject) JSONObject.toJSON(e);
            arr[0] = json.getIntValue(MONTH_METHOD);
            arr[1] = json.getIntValue("assetCount");
            jsonArray.add(arr);
        });
        return jsonArray;
    }

}
