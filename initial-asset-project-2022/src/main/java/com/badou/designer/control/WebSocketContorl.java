package com.badou.designer.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.badou.brms.util.InstanceFactory;
import com.badou.designer.control.model.ContorlInstructionEntity;
import com.badou.designer.control.service.IContorlInstructionService;
import com.badou.tools.common.Globals;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:32:02
 * @todo websocket控制台
 */
@ServerEndpoint(value="/websocket/panel")
public class WebSocketContorl {

    protected static Logger logger = Logger.getLogger(WebSocketContorl.class);

	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketContorl> webSocketSet = new CopyOnWriteArraySet<WebSocketContorl>();

    //中文匹配正则
    private String reg = "[\\u4e00-\\u9fa5\\w]+" ;
    
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param code 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String code, Session session) {
        System.out.println("来自客户端的消息:" + code);
        IContorlInstructionService instructionService = InstanceFactory.getInstance(IContorlInstructionService.class);
        //操作对象
        String params = "";
        String instructionStr = "";
        ContorlInstructionEntity instruction = null ;
        //判断指令是否全中文，如果是。说明是语音输入,否则是页面上传输的code
        if(code.matches(reg)){
        	List<ContorlInstructionEntity> list = instructionService.listAll() ;
        	//循环指令库中所有指令。查找出适配的指令
        	for(ContorlInstructionEntity entity : list){
        		//这个地方暂时使用indexOf的方式来判断。后续应该优化
        		if(code.indexOf(entity.getName()) != -1){
        			//去除指令关键词。剩余的词汇作为操作对象
        			params = code.replaceFirst(entity.getName(), "") ;
        			instruction = entity ;
        			break ;
        		}	
        	}
        }else{
        	if(code.indexOf(Globals.SEPARATOR_COMMA) != -1){
            	instructionStr = code.substring(0 , code.indexOf(Globals.SEPARATOR_COMMA)) ;
            	instruction = instructionService.findByCode(instructionStr);
            	params = code.substring(code.indexOf(Globals.SEPARATOR_COMMA) + 1 , code.length());
            }else{
            	instruction = instructionService.findByCode(code);
            }
        }
        if(instruction != null) {
        	//群发消息
            for(WebSocketContorl item: webSocketSet){
                try {
                	if(!item.session.getId().equals(session.getId())){
                		if(StringUtils.isNotBlank(params)){
                			item.sendMessage(instruction.getFunctionName() + "," + params);
                		} else {
                			item.sendMessage(instruction.getFunctionName());
                		}
                	}
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        logger.error(error);
    }

    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午3:25:19
     * @todo 发送消息
     * @remark 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message 需要发送的消息
     */
    private void sendMessage(String message) {
        try {
			this.session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午3:29:45
     * @todo 获取客户端列表
     * @return List<ClientVo> 返回客户端集合
     */
    public static List<ClientVo> clientList(){
    	List<ClientVo> list = new ArrayList<ClientVo>();
    	for(WebSocketContorl item: webSocketSet){
    		ClientVo vo = new ClientVo();
    		vo.setSessionId(item.session.getId());
    	}
    	return list ;
    }

    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午3:30:45
     * @todo 获取在线总数
     * @return int
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午3:31:07
     * @todo 在线数量增加1
     * @param
     */
    public static synchronized void addOnlineCount() {
    	WebSocketContorl.onlineCount++;
    }

    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午3:32:59
     * @todo 在线数量减1
     */
    public static synchronized void subOnlineCount() {
    	WebSocketContorl.onlineCount--;
    }
}
