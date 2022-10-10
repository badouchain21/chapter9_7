package com.bdc.zeromq;

import java.util.Date;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.bdc.blockchain.service.IBlockChainService;
import com.bdc.common.UploadTypeEnum;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * mq监听器
 */
@Component("mqMessageListener")
public class MQMessageListener implements MessageListener {

	protected static Logger logger = Logger.getLogger(MQMessageListener.class);

	@Autowired
	private IBlockChainService blockChainService;

	@Override
	public void onMessage(Message msg) {
		MapMessage message = (MapMessage) msg;
		try {
			String data = message.getString("data");
			Object type = message.getObject("type");
			this.dealMq(data,(UploadTypeEnum) type);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	public void dealMq(String data, UploadTypeEnum type) throws Exception {
		int tag = RandomUtils.nextInt(100000);
		System.out.println("Random Number(" + tag + ")======================================== MQ START ========================================");
		blockChainService.uploadAsset(type, data);
		System.out.println("MQ time: " + new Date());
		System.out.println("MQ data: " + data);
		System.out.println("MQ type: " + type);
		System.out.println("Random Number(" + tag + ")======================================== MQ END ========================================");
	}
}
