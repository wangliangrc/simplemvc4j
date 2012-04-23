package com.sina.weibosdk.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.sina.weibosdk.Util;
import com.sina.weibosdk.exception.WeiboParseException;

@SuppressWarnings("unchecked")
public class HotTopicList extends ResponseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5673698967918777174L;

	private List<HotTopic> hotTopicList = new ArrayList<HotTopicList.HotTopic>();
	
	private String as_of;
	
	public HotTopicList() {}
	
	public HotTopicList(String json) throws WeiboParseException {
		parse(json);
	}

	@Override
	protected ResponseBean parse(String json) throws WeiboParseException {
		try {
			JSONObject obj = new JSONObject(json);
			as_of = obj.optString("as_of");
			String trendStr = obj.optString("trends");
			if(!TextUtils.isEmpty(trendStr)) {
				JSONObject trends = new JSONObject(trendStr);
				Iterator<String> it = trends.keys();
				while(it.hasNext()) {
					String key = it.next();
					String trendArrStr = trends.optString(key);
					JSONArray trendJsonArr = new JSONArray(trendArrStr);
					for(int i = 0; i < trendJsonArr.length(); i++) {
						HotTopic temp = new HotTopic(trendJsonArr.optString(i));
						temp.setTime(key);
						hotTopicList.add(temp);
					}
				}
			}
        } catch (JSONException e) {
        	Util.loge(e.getMessage(), e);
            throw new WeiboParseException(e);
        }
        return this;
	}

	public List<HotTopic> getHotTopicList() {
		return hotTopicList;
	}

	public void setHotTopicList(List<HotTopic> hotTopicList) {
		this.hotTopicList = hotTopicList;
	}

	public String getAs_of() {
		return as_of;
	}

	public void setAs_of(String as_of) {
		this.as_of = as_of;
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        HotTopicList other = (HotTopicList) obj;

        if (!(as_of == null ? other.getAs_of() == null : as_of
                .equals(other.getAs_of()))) {
            return false;
        }
        if (!hotTopicList.equals(other.getHotTopicList())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hotTopicList.hashCode();
        result = prime * result + ((as_of == null) ? 0 : as_of.hashCode());
        return result;
    }
	

	public class HotTopic extends ResponseBean {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2932802775225843367L;

		private String time;
		private String name;
		private String query;
		private String amount;
		private String delta;
		
		public HotTopic() {}
		
		public HotTopic(String json) throws WeiboParseException {
			parse(json);
		}

		@Override
		protected ResponseBean parse(String json) throws WeiboParseException {
			try {
	            JSONObject obj = new JSONObject(json);
	            this.name = obj.optString("name");
	            this.query = obj.optString("query");
	            this.amount = obj.optString("amount");
	            this.delta = obj.optString("delta");
			} catch (JSONException e) {
				Util.loge(e.getMessage(), e);
	            throw new WeiboParseException(PARSE_ERROR, e);
	        }
			return this;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getQuery() {
			return query;
		}

		public void setQuery(String query) {
			this.query = query;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getDelta() {
			return delta;
		}

		public void setDelta(String delta) {
			this.delta = delta;
		}
		
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((name == null) ? 0 : name.hashCode());
	        return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        HotTopic other = (HotTopic) obj;
	        if (name == null) {
	            if (other.name != null)
	                return false;
	        } else if (!name.equals(other.name))
	            return false;
	        return true;
	    }
		
		
	}
	
}
