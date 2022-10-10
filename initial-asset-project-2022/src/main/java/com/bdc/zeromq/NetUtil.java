package com.bdc.zeromq;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetUtil {

	/**
	 * true:already in using  false:not using
	 * @param port
	 */
	public static boolean isLoclePortUsing(int port){
		boolean flag = true;
		try {
			flag = isPortUsing("127.0.0.1", port);
		} catch (Exception e) {
		}
		return flag;
	}

	/**
	 * true:already in using  false:not using
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 */
	public static boolean isPortUsing(String host, int port) throws UnknownHostException{
		boolean flag = false;
		InetAddress theAddress = InetAddress.getByName(host);
		Socket socket = null;
		try {
			socket = new Socket(theAddress, port);
			flag = true;
		} catch (IOException e) {

		} finally {
			if (null != socket && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	/**
	 * 初始化端口，防止端口占用
	 * @author ztl
	 * @Date 2018年11月29日 上午10:26:41
	 */
	public static Integer initPort(Integer port) {
		if (null == port) return port;
		while (isLoclePortUsing(port)) {
			port = port + 1;
		}
		return port;
	}

	public static void main(String[] args) {
		int port = 9000;
		System.out.println(port + "端口是否已经使用，结果：" + (isLoclePortUsing(port) ? "是" : "否" ));
	}
}
