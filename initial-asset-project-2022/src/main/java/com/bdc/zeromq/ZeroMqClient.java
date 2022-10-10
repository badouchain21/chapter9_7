package com.bdc.zeromq;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import com.bdc.common.UploadTypeEnum;
import jodd.util.concurrent.ThreadFactoryBuilder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import org.zeromq.ZMQ;

/**
 * 发送zeroMq请求
 * @author ztl
 * @Date 2018年10月23日 上午10:58:02
 * @version 1.0.0
 */
public class ZeroMqClient {

	protected static Logger logger = Logger.getLogger(ZeroMqServer.class);
	  /**
     * MAX_THREAD_POOL_SIZE
     */
    private final static int MAX_THREAD_POOL_SIZE = 100;
    /**
     * CORE_THREAD_POOL_SIZE
     */
    private final static int CORE_THREAD_POOL_SIZE = 10;
    /**
     * 线程名规范
     */
    private static ThreadFactory scheduledThreadFactory =   new ThreadFactoryBuilder().setNameFormat("zeromq-%d").get();
    /**
     * 线程池
     */
    private static ExecutorService scheduledThreadPool = new ThreadPoolExecutor(CORE_THREAD_POOL_SIZE,MAX_THREAD_POOL_SIZE, 0L, NANOSECONDS,
            new LinkedBlockingQueue<Runnable>(),scheduledThreadFactory);
	/**
	 * 使用默认地址(本地)和默认端口发送mq
	 * @author ztl
	 * @Date 2018年10月23日 上午10:53:56
	 * @param data
	 * @param type
	 */
	public static void sendLocalhostDefault(String data, UploadTypeEnum type) {
		send(ZeroMqConst.LOCALHOST, ZeroMqConst.START_PORT, data, type);
	}

	/**
	 * 发送mq给本地
	 * @author ztl
	 * @Date 2018年10月23日 上午10:53:56
	 * @param port
	 * @param data
	 * @param assetCode
	 */
	public static void sendLocalhost(Integer port, String data, String assetCode) {
		send(ZeroMqConst.LOCALHOST, port, data, assetCode);
	}

	/**
	 * 发送mq
	 * @author ztl
	 * @Date 2018年10月23日 上午10:53:11
	 * @param ip
	 * @param port
	 * @param data
	 * @param type
	 */
	public static void send(String ip, Integer port, String data, Object type) {
		scheduledThreadPool.execute(new Runnable() {
			public void run() {
				ZMQ.Context context = null;
				ZMQ.Socket socket = null;
				try {
					context = ZMQ.context(1); // 创建一个I/O线程的上下文
					socket = context.socket(ZMQ.REQ); // 创建一个request类型的socket，这里可以将其简单的理解为客户端，用于向response端发送数据
					socket.connect(ZeroMqConst.TCP + "://" + ip + ":" + port); // 与response端建立连接
					JSONObject json = new JSONObject();
					json.put("data", data);
					json.put("type", type);
					String request = json.toJSONString();
					socket.send(request.getBytes()); // 向reponse端发送数据
					//byte[] responseByte = socket.recv(); // 接收response发送回来的数据
					// 正在request/response模型中，send之后必须要recv之后才能继续send，这可能是为了保证整个request/response的流程走完
					//logger.info("ZeroMqClient receive : " + new String(responseByte));
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				} finally {
                    if (socket != null) {
                        socket.close();
                    }
				    if (context != null) {
	                    context.term();
				    }
				}
			}
		});
	}

	/**
	 * 杀死指定端口的线程
	 * @author ztl
	 * @Date 2018年10月23日 上午11:36:13
	 * @param ip
	 * @param port
	 */
	public static void killZeromqThread(Integer port) {
		send(ZeroMqConst.LOCALHOST, port, null, ZeroMqConst.KILL_ZEROMQ_THREAD);
	}
}
