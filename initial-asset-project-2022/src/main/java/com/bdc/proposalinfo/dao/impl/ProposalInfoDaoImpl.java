package com.bdc.proposalinfo.dao.impl;

import com.badou.brms.base.support.hibernate.BaseHibernateDAO;
import com.bdc.proposalinfo.dao.IProposalInfoDao;
import com.bdc.proposalinfo.model.ProposalInfo;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 *	提案信息管理dao实现
 * @author zhongzhilong
 * @date 2020-11-13 15:34
 */
@Repository
public class ProposalInfoDaoImpl extends BaseHibernateDAO<ProposalInfo, Serializable> implements IProposalInfoDao {
}
