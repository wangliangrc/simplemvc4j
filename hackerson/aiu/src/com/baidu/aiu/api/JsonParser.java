package com.baidu.aiu.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.baidu.aiu.api.model.UpdateVersionModel;

public class JsonParser {
	public static UpdateVersionModel parser(String cfgJsonString) {
		JSONObject cfg;
		UpdateVersionModel model = null;
		try {
			cfg = new JSONObject(cfgJsonString);
			if (cfg.optString("patch") != null) {
				model = new UpdateVersionModel();
				model.setNeedUpdate(true);
				model.setUrlPatch(cfg.optString("patch"));
				model.setUrlConf(cfg.optString("res_conf"));
				model.setTarget(cfg.optString("target"));
				model.setVersion(cfg.optString("ver"));
				return model;
			} else {
				return null;
			}
		} catch (JSONException e) {
			Log.d("baidu", e.getMessage());
		}
		return null;

	}
}
