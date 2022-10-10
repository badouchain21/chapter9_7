package com.bdc.rabbitmq.handler;

import com.alibaba.fastjson.JSON;
import com.badou.core.runtime.thread.local.LogonCertificate;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.bdc.api.intermanage.interfacelogger.model.InterFaceLoggerEntity;
import com.bdc.api.intermanage.interfacelogger.service.IInterFaceLoggerService;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Message;

/**
 * @author lipeishan@badousoft.com
 * @version 1.0.0
 * @ClassName ReceiveHandler.java
 * @Description  HANDLE INTERCELOG CREATE OR UPDATE
 * @createTime 2020年12月07日 16:48:00
 */
public class ReceiveHandler implements MessageListener {

    private IInterFaceLoggerService interFaceLoggerService;

    public ReceiveHandler(IInterFaceLoggerService interFaceLoggerService) {
        this.interFaceLoggerService = interFaceLoggerService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            InterFaceLoggerEntity loggerEntity= JSON.parseObject(messageBody, InterFaceLoggerEntity.class);
            if(loggerEntity==null)
                return;
            LogonCertificate logon = new LogonCertificate();
            logon.setUserId(loggerEntity.getCreator());
            logon.setUserName(loggerEntity.getCreatorName());
            LogonCertificateHolder.setLogonCertificate(logon);
            interFaceLoggerService.create(loggerEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LogonCertificateHolder.clear();
        }
    }

}
