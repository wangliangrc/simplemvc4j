package com.baidu.aiu.monitor;

import java.util.HashMap;

import com.baidu.aiu.util.Config;
import com.baidu.aiu.util.HttpUtils;
import com.baidu.aiu.util.UpdateUtils;

public class Monitor {

	private final static String AP = "ap";
	private final static String VER = "ver";
	private final static String ERR = "error";

	public static void pushException(String message) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put(AP, "map");
		hashMap.put(VER, Config.VERSION);
		hashMap.put(ERR, message);
		HttpUtils.post("http://" + UpdateUtils.getIp() + ":8001/ht/monitor.php",
				hashMap);
	}
}