package com.bdc.zeromq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.utils.SpringContextUtil;
import org.apache.log4j.Logger;
import org.zeromq.ZMQ;


/**
 * 处理zeroMq请求
 * @author ztl
 * @Date 2018年10月23日 上午10:58:23
 * @version 1.0.0
 */
public class ZeroMqServer {

    protected static Logger logger = Logger.getLogger(ZeroMqServer.class);

    /**
     * 开启服务端
     * <br>tip:发送type等于MQTypeConst.KILL_ZEROMQ_THREAD的数据可以结束线程
     * @author ztl
     * @Date 2018年10月23日 上午10:29:11
     * @param port 端口号 需要在[ZeroMqCons.START_PORT, ZeroMqCons.START_PORT + ZeroMqCons.MAX_PORT_NUM) 范围内
     */
    public static void openZeroMqService(Integer port) {
        if (port == null || port < ZeroMqConst.START_PORT || port >= ZeroMqConst.START_PORT + ZeroMqConst.MAX_PORT_NUM) {
            port = ZeroMqConst.START_PORT;
        }
        final int zeroMqPort = port;
        if (!NetUtil.isLoclePortUsing(zeroMqPort)) { // 端口未被占用则开启
            new Thread(new Runnable() {
                public void run() {
                    ZMQ.Context context = null;
                    ZMQ.Socket socket = null;
                    try {
                        context = ZMQ.context(1); // 这个表示创建用于一个I/O线程的context
                        socket = context.socket(ZMQ.REP); // 创建一个response类型的socket，他可以接收request发送过来的请求，其实可以将其简单的理解为服务端
                        socket.bind(ZeroMqConst.TCP + "://*:" + zeroMqPort); // 绑定端口

                        while (!Thread.currentThread().isInterrupted()) {
                            byte[] requestByte = socket.recv(); // 获取request发送过来的数据
                            logger.info("ZeroMqService receive : " + new String(requestByte));
                            JSONObject json = JSON.parseObject(new String(requestByte));
                            Object type = json.get("type");
                            if ((type instanceof Integer) && ZeroMqConst.KILL_ZEROMQ_THREAD == Integer.parseInt(type.toString())) {
                                String result = "kill zeromq thread, port is " + zeroMqPort;
                                socket.send(result.getBytes()); // 向request端发送数据
                                break;
                            }
                            String result = "success";
                            try {
                                MQMessageListener listener = (MQMessageListener) SpringContextUtil.getBean("mqMessageListener");
                                listener.dealMq(json.getString("data"), UploadTypeEnum.getUploadTypeEnumByCode(type.toString()));
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                                result = e.getMessage();
                                e.printStackTrace();
                            } finally {
                            	if(result!=null)
                            		socket.send(result.getBytes()); // 向request端发送数据
                            }
                            // ，必须要要request端返回数据，没有返回就又recv，将会出错，这里可以理解为强制要求走完整个request/response流程
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        socket.close(); // 先关闭socket
                        context.term(); // 关闭当前的上下文
                    }
                }
            }).start();
        }
    }

}
