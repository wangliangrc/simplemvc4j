package com.sina.openapi.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Status implements Serializable, Cloneable, Comparable<Status> {
    private static final long serialVersionUID = 4900943169696807050L;
    /**
     * 创建时间
     */
    private String created_at;
    /**
     * 微博id
     */
    private String id;
    /**
     * 微博信息内容
     */
    private String text;
    /**
     * 微博来源
     */
    private String source;
    /**
     * 是否已收藏
     */
    private boolean favorited;
    /**
     * 是否被截断
     */
    private boolean truncated;
    /**
     * 回复ID
     */
    private String in_reply_to_status_id;
    /**
     * 回复人UID
     */
    private String in_reply_to_user_id;
    /**
     * 回复人昵称
     */
    private String in_reply_to_screen_name;

    private String mid;

    /**
     * 作者信息
     */
    private UserShow user;

    // private String annotations;

    /**
     * 缩略图链接地址
     */
    private String thumbnail_pic;
    /**
     * 中型图片链接地址
     */
    private String bmiddle_pic;
    /**
     * 原始图片链接地址
     */
    private String original_pic;
    /**
     * 转发的博文，内容为status，如果不是转发，则没有此字段
     */
    private Status retweeted_status;

    // /**
    // * 元数据
    // */
    // private TypeBase[] type;

    /**
     * 评论数
     */
    private int commentCount;

    /**
     * 转发数
     */
    private int repostCount;

    /**
     * 是否为新微博
     */
    private boolean isNew;
    
    private GEO geo;

    /**
     * @return the geo
     */
    public GEO getGeo() {
        return geo;
    }

    /**
     * @param geo the geo to set
     */
    public void setGeo(GEO geo) {
        this.geo = geo;
    }

    public Date getCreated_at() throws ParseException {
        return Utils.parserWeiboDate(created_at);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public UserShow getUser() {
        return user;
    }

    public void setUser(UserShow user) {
        this.user = user;
    }

    // public String getAnnotations() {
    // return annotations;
    // }
    //
    // public void setAnnotations(String annotations) {
    // this.annotations = annotations;
    // }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public Status getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Status retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getRepostCount() {
        return repostCount;
    }

    public void setRepostCount(int repostCount) {
        this.repostCount = repostCount;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public static Pattern getSrcPattern() {
        return srcPattern;
    }

    public static void setSrcPattern(Pattern srcPattern) {
        Status.srcPattern = srcPattern;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Status [created_at=").append(created_at)
                .append(", id=").append(id).append(", text=").append(text)
                .append(", source=").append(source).append(", favorited=")
                .append(favorited).append(", truncated=").append(truncated)
                .append(", in_reply_to_status_id=")
                .append(in_reply_to_status_id).append(", in_reply_to_user_id=")
                .append(in_reply_to_user_id)
                .append(", in_reply_to_screen_name=")
                .append(in_reply_to_screen_name).append(", mid=").append(mid)
                .append(", user=").append(user).append(", thumbnail_pic=")
                .append(thumbnail_pic).append(", bmiddle_pic=")
                .append(bmiddle_pic).append(", original_pic=")
                .append(original_pic).append(", retweeted_status=")
                .append(retweeted_status).append(", commentCount=")
                .append(commentCount).append(", repostCount=")
                .append(repostCount).append(", isNew=").append(isNew)
                .append(", geo=").append(geo)
                .append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        Status o = null;
        try {
            o = (Status) super.clone();
            o.retweeted_status = retweeted_status == null ? null
                    : (Status) retweeted_status.clone();
            o.user = user == null ? null : (UserShow) user.clone();
            o.geo = geo == null ? null : (GEO) geo.clone();
        } catch (CloneNotSupportedException e) {
        }
        return o;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Status other = (Status) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Status another) {
        if (!equals(another)) {
            try {
                return getCreated_at().compareTo(another.getCreated_at()) > 0 ? -1
                        : 1;
            } catch (ParseException e) {
            }
        }
        return 0;
    }

    private static Pattern srcPattern = Pattern.compile(">\\s*(\\S+)\\s*<");

    /**
     * 返回格式化的来源信息
     * 
     * @return
     */
    public String getFormatSourceDesc() {
        if (source != null && source.length() > 0) {
            final Matcher m = srcPattern.matcher(source);
            if (m.find()) {
                return m.group(1);
            } else {
                return source;
            }
        }
        return "";
    }

    /**
     * 是否带有原始图
     * 
     * @return
     */
    public boolean hasOriginalPic() {
        return original_pic != null && original_pic.length() > 0;
    }

    /**
     * 是否带有缩略图
     * 
     * @return
     */
    public boolean hasThumbnailPic() {
        return thumbnail_pic != null && thumbnail_pic.length() > 0;
    }

    /**
     * 是否带有中型尺寸图片
     * 
     * @return
     */
    public boolean hasBmiddlePic() {
        return bmiddle_pic != null && bmiddle_pic.length() > 0;
    }

    /**
     * 是否转发围脖
     * 
     * @return
     */
    public boolean hasRetweetedStatus() {
        return retweeted_status != null;
    }

    /**
     * 是否有转发围脖原始图片
     * 
     * @return
     */
    public boolean hasRetweetedOriginalPic() {
        return retweeted_status != null
                && retweeted_status.original_pic != null
                && retweeted_status.original_pic.length() > 0;
    }

    /**
     * 是否有转发围脖缩略图片
     * 
     * @return
     */
    public boolean hasRetweetedThumbnailPic() {
        return retweeted_status != null
                && retweeted_status.thumbnail_pic != null
                && retweeted_status.thumbnail_pic.length() > 0;
    }

    /**
     * 是否有转发围脖中型尺寸图片
     * 
     * @return
     */
    public boolean hasRetweetedBmiddlePic() {
        return retweeted_status != null && retweeted_status.bmiddle_pic != null
                && retweeted_status.bmiddle_pic.length() > 0;
    }
    
   public class GEO implements Serializable, Cloneable {
        private static final long serialVersionUID =  3940364120829530175L;
        private String type;
        private double [] coordinates;
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public double[] getCoordinates() {
            return coordinates;
        }
        
        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
        
        public String toString () {
            return "GEO [type = " + type + ", coordinates = " + coordinates + "]";
        }
        
        /*@Override
        public int compareTo(Status o) {
            return 0;
        }*/
        
        @Override
        public Object clone() {
            GEO o = null;
            try {
                o = (GEO) super.clone();
            } catch (CloneNotSupportedException e) {
            }
            return o;
        }

        /*@Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
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
            GEO other = (GEO) obj;
            if (id == null) {
                if (other.coordinates != null)
                    return false;
            } else if (!coordinates.equals(other.coordinates))
                return false;
            return true;
        }*/
    }
}