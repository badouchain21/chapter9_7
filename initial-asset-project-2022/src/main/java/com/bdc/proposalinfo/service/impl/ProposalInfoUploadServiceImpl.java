package com.bdc.proposalinfo.service.impl;

import com.bdc.base.service.impl.BaseUploadAssetServiceImpl;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.annotate.UploadAssetTypeHandler;
import com.bdc.proposalinfo.service.IProposalInfoUploadService;
import org.springframework.stereotype.Service;

/**
 * 描述：提案信息上链服务
 *
 * @author zhongzhilong
 * @Date 2020年11月13日 上午15:41:12
 */
@Service
@UploadAssetTypeHandler(UploadTypeEnum.PROPOSAL_INFO)
public class ProposalInfoUploadServiceImpl extends BaseUploadAssetServiceImpl implements IProposalInfoUploadService {
}
