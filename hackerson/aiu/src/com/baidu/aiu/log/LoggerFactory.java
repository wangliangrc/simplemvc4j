package com.baidu.aiu.log;

public class LoggerFactory {

	public static Logger getLogger() {
		return new LoggerImpl();
	}
}
