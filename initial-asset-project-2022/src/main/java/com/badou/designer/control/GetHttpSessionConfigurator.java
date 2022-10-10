package com.badou.designer.control;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:30:20
 * @todo 获取http请求session配置类
 */
public class GetHttpSessionConfigurator extends Configurator{

	 @Override
	    public void modifyHandshake(ServerEndpointConfig sec,HandshakeRequest request, HandshakeResponse response) {
	        HttpSession httpSession=(HttpSession) request.getHttpSession();
	        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
	    }
}
