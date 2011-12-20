package com.sina.openapi.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentOpen implements Serializable, Cloneable,
        Comparable<CommentOpen> {
    private static final long serialVersionUID = 5704729047834430064L;

    /**
     * 评论ID
     */
    private String id;
    /**
     * 评论内容
     */
    private String text;
    /**
     * 评论来源
     */
    private String source;
    /**
     * 是否收藏
     */
    private boolean favorited;
    /**
     * 是否被截断
     */
    private boolean truncated;
    /**
     * 评论时间
     */
    private String created_at;
    /**
     * 评论人信息,结构参考user
     */
    private UserShow user;
    /**
     * 评论来源，数据结构跟comment一致
     */
    private CommentOpen reply_comment;
    /**
     * 评论的微博,结构参考status
     */
    private Status status;

    /**
     * 是否为新评论
     */
    private boolean isNew;

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

    public Date getCreated_at() throws ParseException {
        return Utils.parserWeiboDate(created_at);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserShow getUser() {
        return user;
    }

    public void setUser(UserShow user) {
        this.user = user;
    }

    public CommentOpen getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(CommentOpen reply_comment) {
        this.reply_comment = reply_comment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        CommentOpen.srcPattern = srcPattern;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CommentOpen [id=").append(id).append(", text=")
                .append(text).append(", source=").append(source)
                .append(", favorited=").append(favorited)
                .append(", truncated=").append(truncated)
                .append(", created_at=").append(created_at).append(", user=")
                .append(user).append(", reply_comment=").append(reply_comment)
                .append(", status=").append(status).append(", isNew=")
                .append(isNew).append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        CommentOpen o = null;
        try {
            o = (CommentOpen) super.clone();
            o.user = user == null ? null : (UserShow) user.clone();
            o.reply_comment = reply_comment == null ? null
                    : (CommentOpen) reply_comment.clone();
            o.status = status == null ? null : (Status) status.clone();
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
        CommentOpen other = (CommentOpen) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(CommentOpen another) {
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
            }
        }
        return "";
    }

}
