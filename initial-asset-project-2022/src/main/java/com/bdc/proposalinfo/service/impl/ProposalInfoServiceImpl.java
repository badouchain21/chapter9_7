package com.bdc.proposalinfo.service.impl;

import com.badou.brms.base.support.hibernate.IBaseHibernateDAO;
import com.badou.brms.base.support.spring.BaseSpringService;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.annotate.UploadAssetTypeHandler;
import com.bdc.proposalinfo.dao.IProposalInfoDao;
import com.bdc.proposalinfo.model.ProposalInfo;
import com.bdc.proposalinfo.service.IProposalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 *	提案信息管理service实现
 * @author zhongzhilong
 * @date 2020-11-13 15:34
 */
@Service
@UploadAssetTypeHandler(UploadTypeEnum.PROPOSAL_INFO)
public class ProposalInfoServiceImpl extends BaseSpringService<ProposalInfo, Serializable> implements IProposalInfoService {

    private IProposalInfoDao proposalInfoDao;

    @Autowired
    public void setProposalInfoDao(IProposalInfoDao proposalInfoDao) {
        this.proposalInfoDao = proposalInfoDao;
        super.setBaseDAO(proposalInfoDao);
    }

}
