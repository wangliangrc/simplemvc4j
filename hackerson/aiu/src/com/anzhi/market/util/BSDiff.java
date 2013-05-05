package com.anzhi.market.util;

public class BSDiff {

	static {
		System.loadLibrary("bspatch");
	}

	public static void doBspatch(String oldFile, String newFile,
			String patchFile) {
		String[] arrayOfString = new String[3];
		arrayOfString[0] = oldFile;
		arrayOfString[1] = newFile;
		arrayOfString[2] = patchFile;
		bspatch(arrayOfString);
	}

	public static native int bspatch(String[] paramArrayOfString);
}