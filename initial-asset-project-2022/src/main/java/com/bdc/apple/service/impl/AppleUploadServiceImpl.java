package com.bdc.apple.service.impl;


import com.bdc.apple.service.IAppleUploadService;
import com.bdc.base.service.impl.BaseUploadAssetServiceImpl;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.annotate.UploadAssetTypeHandler;
import org.springframework.stereotype.Service;

/**
 * 描述：上链服务
 * @author zhongzhilong
 * @Date 2020年11月04日 上午11:41:12
 */
@Service
@UploadAssetTypeHandler(UploadTypeEnum.APPLE)
public class AppleUploadServiceImpl extends BaseUploadAssetServiceImpl implements IAppleUploadService {

}
