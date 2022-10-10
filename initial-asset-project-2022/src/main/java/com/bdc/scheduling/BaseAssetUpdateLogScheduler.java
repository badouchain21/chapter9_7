package com.bdc.scheduling;


import java.util.List;

import com.badou.brms.dictionary.DictionaryLib;
import com.bdc.base.common.BaseAssetConst;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.blockchain.service.IBlockChainService;
import com.bdc.common.utils.EmptyUtil;
import com.bdc.uploadassetlog.model.UploadAssetLogEntity;
import com.bdc.uploadassetlog.service.IUploadAssetLogService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.brms.system.org.dao.IUserCommonInfoDao;
import com.badou.brms.system.org.service.IUserService;
import com.badou.brms.system.org.vo.User;
import com.badou.brms.system.org.vo.UserCommInfo;
import com.badou.brms.util.InstanceFactory;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.tools.common.util.StringUtils;


/**
 * 描述：资产定时器
 *
 * @author lps
 */
@Slf4j
@Component
@Order(value=200)
public class BaseAssetUpdateLogScheduler extends Scheduler {
	@Autowired
	private IBlockChainService blockChainService;

	@Autowired
	private BaseUploadAssetFactory baseUploadAssetFactory;

	@Autowired
	private IUploadAssetLogService uploadAssetLogService;

	@Override
	protected void processTask() {
		List<UploadAssetLogEntity> logEntities = uploadAssetLogService.findUploading(BaseAssetConst.UPLOAD_LOG_UPDATE_COUNT);
		if (EmptyUtil.isEmpty(logEntities)){
			return;
		}
		blockChainService.updateUploadStatus(logEntities);
	}

	@Override
	protected String getCron() {
		return DictionaryLib.getItemValueByItemCode("SCHEDULER_CONFIG","ASSET_UPDATE_LOG_CRON");
	}




	/**
	 *
	 * 描述：设置当前用户
	 *
	 * @author linxiaoqing
	 * @date 2019年9月6日
	 * @param entity
	 */
	private Boolean setLogon(AppBaseEntity entity) {
		// 设置当前用户
		String userId = entity.getCreator();
		if (StringUtils.isEmpty(userId))
			return false;
		User user = InstanceFactory.getInstance(IUserService.class).getById(
				userId);
		if (user == null)
			return false;
		UserCommInfo userComminfo = InstanceFactory.getInstance(
				IUserCommonInfoDao.class).getById(user.getUserInfoId());
		if (userComminfo == null)
			return false;
		LogonCertificate logon = new LogonCertificate();
		logon.setUserId(userId);
		logon.setUserName(userComminfo.getName());
		logon.setLoginId(userComminfo.getLogonId());
		LogonCertificateHolder.setLogonCertificate(logon);
		return true;
	}
}
