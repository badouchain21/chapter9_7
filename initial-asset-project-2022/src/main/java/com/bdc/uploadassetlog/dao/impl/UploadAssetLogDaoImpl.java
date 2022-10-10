package com.bdc.uploadassetlog.dao.impl;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.badou.brms.base.support.page.PaginationTheadLocal;
import com.badou.brms.base.support.page.model.Pagelet;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.common.UploadStatusEnum;
import com.bdc.common.utils.BdLoginUtil;
import com.bdc.common.utils.EmptyUtil;
import com.bdc.uploadassetlog.dao.IUploadAssetLogDao;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 描述：资产上链日志 数据库实现
 * @author zhongzhilong
 * @date 2020-11-02 11:28:30
 */
@Repository
public class UploadAssetLogDaoImpl extends BaseHibernateDAO<UploadAssetLogEntity, Serializable> implements IUploadAssetLogDao {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public int getUploadAssetSize(@NotNull String assetCodeVal, @NotNull String assetKeyVal){
        String sql="select count(*) from bdc_upload_asset_log where ASSET_KEY=? and ASSET_CODE =? and UPLOAD_STATUS<>3 ";
        Query query = this.getSession().createSQLQuery(sql);
        query.setString(0,assetKeyVal).setString(1,assetCodeVal);
        return Integer.parseInt(query.uniqueResult().toString());
    }

    @Override
    public List<UploadAssetLogEntity> getByTime(@NotNull LocalDateTime startTime, LocalDateTime endTime, List<String> assetCodes) {
        String userId = LogonCertificateHolder.getLogonCertificate().getUserId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM bdc_upload_asset_log WHERE UPLOAD_STATUS = 2 AND UPLOAD_TIME >= '"+startTime.format(dateTimeFormatter)+"' ");
        if (Objects.nonNull(endTime)){
            sql.append(" AND UPLOAD_TIME <= '"+endTime.format(dateTimeFormatter)+"' ");
        }
        if (EmptyUtil.isNotEmpty(assetCodes)){
            sql.append(" AND ASSET_CODE IN (:assetCodes) ");
        }
        if (!BdLoginUtil.isSa(userId)){
            sql.append(" AND CREATOR = '"+userId+"' ");
        }
        sql.append("ORDER BY UPLOAD_TIME ASC");
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString());
        if (EmptyUtil.isNotEmpty(assetCodes)){
            sqlQuery.setParameterList("assetCodes",assetCodes);
        }
        sqlQuery.addEntity(UploadAssetLogEntity.class);
        List list = sqlQuery.list();
        return list;
    }

    @Override
    public Pagelet findByAssetId(String assetId, LocalDateTime time, Pagelet pagelet) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select ASSET_CODE AS assetCode,ASSET_TYPE_NAME AS assetTypeName,HASH AS hash,UPLOAD_TIME AS uploadTime,ASSET_NAME_FIELD AS assetName,ASSET_ID AS assetId,ID AS id FROM bdc_upload_asset_log ");
        stringBuilder.append(" WHERE UPLOAD_STATUS = 2 ");
        stringBuilder.append(" AND ASSET_ID = '"+assetId+"' ");
        if (Objects.nonNull(time)){
            stringBuilder.append(" AND UPLOAD_TIME >= '"+time.format(dateTimeFormatter)+"' ");
        }
        stringBuilder.append("ORDER BY UPLOAD_TIME DESC");

        Query query = this.getSession().createSQLQuery(stringBuilder.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (pagelet.isUsePage()) {
            List list = query.list();
            Integer totalCount = list == null ? 0 : list.size();
            pagelet.setTotalCount(totalCount);
            final int firstResult = pagelet.getStartOfPage().intValue();
            final int maxResults = pagelet.getPerPageSize().intValue();
            if(maxResults>totalCount) {
                pagelet.setDatas(list);
            }else {
                query.setFirstResult(firstResult).setMaxResults(maxResults);
                pagelet.setDatas(query.list());
            }
        } else {
            List list = query.list();
            Integer totalCount = list == null ? 0 : list.size();
            pagelet.setTotalCount(totalCount);
            pagelet.setDatas(list);
        }
        return pagelet;
    }

    @Override
    public Pagelet findByKeyword(String keyword, LocalDateTime time, Pagelet pagelet) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select a.ASSET_CODE AS assetCode,a.ASSET_KEY AS assetKey,a.ASSET_ID AS assetId,a.ASSET_NAME_FIELD AS assetName,a.ASSET_TYPE_NAME AS assetTypeName,a.HASH AS hash,a.UPLOAD_TIME AS uploadTime ");
        stringBuilder.append("from bdc_upload_asset_log a ");
        stringBuilder.append("INNER JOIN (select ASSET_ID,MAX(UPLOAD_TIME) AS UPLOAD_TIME from bdc_upload_asset_log where UPLOAD_STATUS = 2 AND HASH IS NOT NULL ");
        if (StringUtils.isNotEmpty(keyword)){
            stringBuilder.append("AND (ASSET_TYPE_NAME LIKE '%"+keyword+"%' OR ASSET_NAME_FIELD LIKE '%"+keyword+"%') ");
        }
        stringBuilder.append("GROUP BY ASSET_ID ORDER BY UPLOAD_TIME DESC ");
        if (pagelet.isUsePage()){
            Integer perPageSize = pagelet.getPerPageSize();
            Integer pageNo = pagelet.getPageNo();
            stringBuilder.append("LIMIT " + (pageNo - 1)*perPageSize + "," + perPageSize);
        }
        stringBuilder.append(") b ");
        stringBuilder.append("USING (UPLOAD_TIME, asset_id) ");
        if (Objects.nonNull(time)){
            stringBuilder.append("WHERE a.UPLOAD_TIME >= '"+time.format(dateTimeFormatter)+"' ");
        }
        stringBuilder.append("ORDER BY a.UPLOAD_TIME DESC");
        Query query = this.getSession().createSQLQuery(stringBuilder.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (pagelet.isUsePage()) {
            List list = query.list();
            pagelet.setTotalCount(getReportDataByKeyword(keyword));
            pagelet.setDatas(list);
        } else {
            List list = query.list();
            Integer totalCount = list == null ? 0 : list.size();
            pagelet.setTotalCount(totalCount);
            pagelet.setDatas(list);
        }
        return pagelet;
    }

    @Override
    public Integer getReportDataByTime(@NotNull LocalDateTime startTime, LocalDateTime endTime) {
        LogonCertificate logonCertificate = LogonCertificateHolder.getLogonCertificate();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) FROM bdc_upload_asset_log WHERE UPLOAD_STATUS = 2 AND UPLOAD_TIME >= '"+startTime.format(dateTimeFormatter)+"' ");
        if (Objects.nonNull(endTime)){
            sql.append(" AND UPLOAD_TIME <= '"+endTime.format(dateTimeFormatter)+"' ");
        }
        if (Objects.nonNull(logonCertificate) && !BdLoginUtil.isSa(logonCertificate.getUserId())){
            sql.append(" AND CREATOR = '"+logonCertificate.getUserId()+"' ");
        }
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql.toString());
        Integer count = Integer.parseInt(sqlQuery.uniqueResult().toString());
        return count;
    }

    @Override
    public Integer getReportDataByKeyword(String keyword) {
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) from (select count(*) from bdc_upload_asset_log where UPLOAD_STATUS = 2 AND HASH IS NOT NULL ");
        if (StringUtils.isNotEmpty(keyword)){
            countSql.append("AND (ASSET_TYPE_NAME LIKE '%"+keyword+"%' OR ASSET_NAME_FIELD LIKE '%"+keyword+"%') ");
        }
        countSql.append("GROUP BY ASSET_ID) AS a");
        int count = Integer.parseInt(this.getSession().createSQLQuery(countSql.toString()).uniqueResult().toString());
        return count;
    }

    @Override
    public List getReportData(LocalDateTime startTime, LocalDateTime endTime,List<String> assetCodes,String method) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("select "+method+",count(ASSET_ID) assetCount from ");
        sqlBuild.append("(select "+method+"(UPLOAD_TIME) "+method+",ASSET_ID from bdc_upload_asset_log where UPLOAD_STATUS = 2 ");
        sqlBuild.append("AND UPLOAD_TIME >= '"+startTime.format(dateTimeFormatter)+"' ");
        sqlBuild.append("AND UPLOAD_TIME <= '"+endTime.format(dateTimeFormatter)+"' ");
        if (EmptyUtil.isNotEmpty(assetCodes)){
            sqlBuild.append("AND ASSET_CODE in (:assetCodes) ");
        }
        String userId = LogonCertificateHolder.getLogonCertificate().getUserId();
        if (!BdLoginUtil.isSa(userId)){
            sqlBuild.append("AND CREATOR = '"+userId+"' ");
        }
        sqlBuild.append("group by "+method+"(UPLOAD_TIME),ASSET_ID) as a group by " + method);
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sqlBuild.toString());
        if (EmptyUtil.isNotEmpty(assetCodes)){
            sqlQuery.setParameterList("assetCodes",assetCodes);
        }
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = sqlQuery.list();
        return list;
    }

    @Override
    public List getReportDataByType(int year,List<String> assetCodes) {
        LocalDateTime startTime = LocalDateTime.of(LocalDate.ofYearDay(year, LocalDate.MIN.getDayOfYear()), LocalTime.MIN).with(TemporalAdjusters.firstDayOfYear());
        LocalDateTime endTime = LocalDateTime.of(LocalDate.ofYearDay(year, LocalDate.MIN.getDayOfYear()), LocalTime.MAX).with(TemporalAdjusters.lastDayOfYear());
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("select ASSET_TYPE_NAME as assetName,count(ASSET_ID) assetCount from ");
        sqlBuild.append("(select ASSET_TYPE_NAME,ASSET_ID from bdc_upload_asset_log where UPLOAD_STATUS = 2 ");
        sqlBuild.append("AND UPLOAD_TIME >= '"+startTime.format(dateTimeFormatter)+"' ");
        sqlBuild.append("AND UPLOAD_TIME <= '"+endTime.format(dateTimeFormatter)+"' ");
        if (EmptyUtil.isNotEmpty(assetCodes)){
            sqlBuild.append("AND ASSET_CODE in (:assetCodes) ");
        }
        String userId = LogonCertificateHolder.getLogonCertificate().getUserId();
        if (!BdLoginUtil.isSa(userId)){
            sqlBuild.append("AND CREATOR = '"+userId+"' ");
        }
        sqlBuild.append("group by ASSET_TYPE_NAME,ASSET_ID) as a group by ASSET_TYPE_NAME");
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sqlBuild.toString());
        if (EmptyUtil.isNotEmpty(assetCodes)){
            sqlQuery.setParameterList("assetCodes",assetCodes);
        }
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = sqlQuery.list();
        return list;
    }

    @Override
    public int getReportDataByStatus(UploadStatusEnum uploadStatusEnum) {
        String sql = "select COUNT(*) from bdc_upload_asset_log where UPLOAD_STATUS = " + uploadStatusEnum.getValue();
        String userId = LogonCertificateHolder.getLogonCertificate().getUserId();
        if (!BdLoginUtil.isSa(userId)){
            sql += " AND CREATOR = '"+userId+"'";
        }
        BigInteger count = (BigInteger) this.getSession().createSQLQuery(sql).uniqueResult();
        return count.intValue();
    }

}
