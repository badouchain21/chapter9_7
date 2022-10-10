package com.bdc.proposalinfo.model;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.bdc.base.model.BaseAssetEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 *	提案信息
 *
 * @author zhongzhilong
 * @date 2020-11-13 14:47:28
 */
@Getter
@Setter
@javax.persistence.Entity
@Table(name = "bdc_proposal_info")
@Cache(usage = CacheConcurrencyStrategy.NONE) //缓存配置
public class ProposalInfo extends BaseAssetEntity {

    /**
     * 案题
     */
    @Column(name = "title")
    protected String title;

    /**
     * 案题ID
     */
    @Column(name = "title_id")
    protected String titleId;

    /**
     * 提案者
     */
    @Column(name = "user_name")
    protected String userName;

    /**
     * 提案时间
     */
    @Column(name = "proposal_date")
    protected Date proposalDate;

    /**
     * 联系方式
     */
    @Column(name = "mobile_name")
    protected String mobileName;

    /**
     * 所属机构
     */
    @Column(name = "org_name")
    protected String orgName;

    /**
     * 机构ID
     */
    @Column(name = "org_id")
    protected String orgId;

    /**
     * 资料
     */
    @Column(name = "datas")
    protected String datas;

}
