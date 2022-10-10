package com.bdc.zeromq;

public class ZeroMqConst {
	/**
	 * tcp
	 */
	public static final String TCP = "tcp";
	/*
	 * 127.0.0.1
	 */
	public static final String LOCALHOST = "127.0.0.1";
	/**
	 * 服务端zeromq绑定最小的开始端口
	 */
	public static final int START_PORT = 9100;

	/**
	 * 上链专用端口(项目启动后可能会变，防止端口被占用)
	 */
	public static Integer BDC_PORT = 9111;

	/**
	 * 服务端可以动态创建zeromq的最大数量
	 */
	public static final int MAX_PORT_NUM = 100;

	/**
	 * 用于清除zeroMq线程 -99
	 */
	public static final int KILL_ZEROMQ_THREAD = -99;

}
