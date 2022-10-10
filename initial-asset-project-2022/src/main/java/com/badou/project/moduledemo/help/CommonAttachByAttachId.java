package com.badou.project.moduledemo.help;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.badou.brms.attach.AttachFactory;
import com.badou.brms.attach.model.Attach;
import com.badou.brms.attach.service.IAttachService;
import com.badou.brms.util.InstanceFactory;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午11:17:46
 * @todo 通用根据附件id操作附件类
 */
public class CommonAttachByAttachId {
	@Autowired
	private static IAttachService attachService;

	@Autowired
	private static AttachFactory attachFactory;
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午5:50:46
	 * @todo 通过AttachId查询的Attach
	 * @param attachId
	 * @return 附件对象id
	 * @throws Exception if has error(直接往外抛)
	 */
	public String findTempIdByattachId(String attachId) throws Exception {
		String tempId = "";
		InputStream is = null;
		try {
			IAttachService attachService = (IAttachService) InstanceFactory
					.getInstance(IAttachService.class);
			AttachFactory attachFactory = (AttachFactory) InstanceFactory
					.getInstance(AttachFactory.class);
			// 把图片找出来并放进缓存中
			Attach attach = attachService.getById(attachId);
			if(attach!=null){
			is = attachFactory.downloadFile(attachId);
				//该方法是因为name有后缀名，故而需要将name去除后缀名
				//无后缀名时请使用ComAttach
				String fileNameWithSuffix = attach.getName();
				//String fileName = fileNameWithSuffix.substring(0,fileNameWithSuffix.lastIndexOf("."));
				tempId = AttachImgCaches.storeImg(attachId,
						new AttachImgCaches.AttachImg(attachId,null, is.available(), is));
			}
		} finally {
			if (is != null){
				is.close();
			} 
		}
		return tempId;
	}
}
