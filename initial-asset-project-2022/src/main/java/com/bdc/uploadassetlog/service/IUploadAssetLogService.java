package com.bdc.uploadassetlog.service;

import com.alibaba.fastjson.JSONArray;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.brms.base.support.spring.IBaseSpringService;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.vo.BaseAssetIndexAttentionVO;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.UploadTypeEnum;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 描述：资产上链日志 服务接口
 * @author zhongzhilong
 * @date 2020-11-02 11:28:40
 */
public interface IUploadAssetLogService extends IBaseSpringService<UploadAssetLogEntity, Serializable> {

    /**
     * 创建上链日志
     * @param baseAssetEntity
     * @return
     */
    UploadAssetLogEntity createSimpleUploadAssetLog (BaseAssetEntity baseAssetEntity);

    /**
     * 获取导航栏报表数据
     * @return
     */
    BaseAssetIndexAttentionVO getBaseAssetIndexAttention ();

    /*
     * 通过年份获取报表数据
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return
     */
    JSONArray getReportDataByYear(Integer startYear , Integer endYear);

    /**
     * 通过资产类型获取一年的报表数据
     * @param year 年份
     * @return
     */
    JSONArray getReportDataByAssetType (Integer year);
    /**
     * stastic the size of UploadAssetLog
     * @param assetCodeVal
     * @param assetKeyVal
     * @return
     */
    public int getUploadAssetSize(@NotNull String assetCodeVal, @NotNull String assetKeyVal);

    /**
     * 通过资产名称查找同一资产下所有的历史上链信息
     * @param assetId 资产Id
     * @param time 时间
     * @return
     */
    Pagelet findByAssetId (LocalDateTime time,String assetId,Pagelet pagelet);

    /**
     * 根据资产名称或者资产类型名称模糊查询
     * @param keyword 资产类型名称 | 资产名称
     * @param time 上链时间 可选
     * @param pagelet 分页对象
     * @return
     */
    Pagelet findByKeyword (String keyword, LocalDateTime time, Pagelet pagelet);

    /**
     * 获取上链中的日志实体
     * @param maxResultCount 结果最大数量
     * @return
     */
    List<UploadAssetLogEntity> findUploading (Integer maxResultCount);

    /**
     * 通过资产ID获取日志实体
     * @param assetId 资产ID
     * @return
     */
    List<UploadAssetLogEntity> findByAssetId (String assetId,UploadStatusEnum uploadStatusEnum);

    /**
     * 通过时间获取报表数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Integer getReportDataByTime (@NotNull LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 通过上链状态获取报表数据
     * @param uploadStatusEnum 上链状态
     * @return
     */
    int getReportDataByStatus (UploadStatusEnum uploadStatusEnum);

    /**
     * 获取一年内每个月的报表数据
     * @param startYear
     * @param endYear
     * @return
     */
    JSONArray getReportDataByMonth(Integer startYear, Integer endYear);

}
