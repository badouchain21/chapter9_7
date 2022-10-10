package com.bdc.uploadassetlog.dao;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.base.support.page.model.Pagelet;
import com.bdc.common.UploadStatusEnum;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述：资产上链日志 数据库接口
 * @author zhongzhilong
 * @date 2020-11-02 11:28:30
 */
public interface IUploadAssetLogDao extends IBaseHibernateDAO<UploadAssetLogEntity, Serializable> {
    /**
     * stastic the size of UploadAssetLog
     * @param assetCodeVal
     * @param assetKeyVal
     * @return
     */
    public int getUploadAssetSize(@NotNull String assetCodeVal, @NotNull String assetKeyVal);

    /**
     * 根据资产编码获取指定日期的上链日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param assetCodes 资产编码
     * @return
     */
    List<UploadAssetLogEntity> getByTime (@NotNull LocalDateTime startTime, LocalDateTime endTime, List<String> assetCodes);

    /**
     * 通过资产ID查找
     * @param assetId
     * @param time
     * @return
     */
    Pagelet findByAssetId (String assetId, LocalDateTime time, Pagelet pagelet);

    /**
     * 根据资产名称或者资产类型名称模糊查询
     * @param keyword 资产类型名称 | 资产名称
     * @param time 上链时间 可选
     * @param pagelet 分页对象
     * @return
     */
    Pagelet findByKeyword (String keyword, LocalDateTime time, Pagelet pagelet);

    /**
     * 通过时间获取报表数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Integer getReportDataByTime (@NotNull LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 通过关键字获取报表数据
     * @param keyword 资产类型名称 | 资产名称
     * @return
     */
    Integer getReportDataByKeyword(String keyword);

    /**
     * 根据调用的方法类型获取报表数据,如Year根据年获取,month根据月获取
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param assetCodes 资产编码
     * @param method 方法
     * @return
     */
    List getReportData (LocalDateTime startTime, LocalDateTime endTime,List<String> assetCodes,String method);

    /**
     * 根据资产类型获取报表数据
     * @param year
     * @param assetCodes
     * @return
     */
    List getReportDataByType(int year,List<String> assetCodes);

    /**
     * 根据上链状态获取报表数据
     * @param uploadStatusEnum
     * @return
     */
    int getReportDataByStatus (UploadStatusEnum uploadStatusEnum);

}
