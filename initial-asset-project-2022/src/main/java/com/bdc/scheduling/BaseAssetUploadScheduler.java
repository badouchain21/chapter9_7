package com.bdc.scheduling;

import com.badou.brms.dictionary.DictionaryLib;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.common.UploadTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 描述：资产定时器
 *
 * @author lps
 */
@Slf4j
@Component
@Order(value=300)
public class BaseAssetUploadScheduler extends Scheduler {
	@Autowired
	private BaseUploadAssetFactory baseUploadAssetFactory;
	@Autowired
	private IBaseAssetService baseAssetService;

	@Override
	protected void processTask() {
		// 遍历资产类型，设置定时器
		for (UploadTypeEnum type : UploadTypeEnum.values()) {
			// 获取资产对应的上链服务
			IBaseUploadAssetService uploadService = baseUploadAssetFactory
					.getUploadService(type.getCode());
			if (uploadService == null) {
				log.error("uploadService NULL:" + type.getCode());
				continue;
			}
			// 定时资产上链
			log.info("开始为：" + type.name() + "资产上链！");
			List<BaseAssetEntity> list = uploadService.findUnUpload(type);
			for (BaseAssetEntity entity : list) {
				baseAssetService.assetUpload(entity);
			}
		}
	}

	@Override
	protected String getCron() {
		return DictionaryLib.getItemValueByItemCode("SCHEDULER_CONFIG","ASSET_UPLOAD_CRON");
	}

}
