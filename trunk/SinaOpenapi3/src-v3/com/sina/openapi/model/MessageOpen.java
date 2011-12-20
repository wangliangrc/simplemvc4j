package com.sina.openapi.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class MessageOpen implements Serializable, Cloneable,
        Comparable<MessageOpen> {

    private static final long serialVersionUID = 3318259436073130327L;

    /** id: 私信ID */
    private String id;

    /** text: 私信内容 */
    private String text;

    /** sender_id：发送人UID */
    private String sender_id;

    /** recipient_id: 接受人UID */
    private String recipient_id;

    /** created_at: 发送时间 */
    private String created_at;

    private int send_type;

    /** sender_screen_name: 发送人昵称 */
    private String sender_screen_name;

    /** recipient_screen_name：接受人昵称 */
    private String recipient_screen_name;

    /** sender: 发送人信息，参考user说明 */
    private UserShow sender;

    /** recipient: 接受人信息，参考user说明 */
    private UserShow recipient;

    private String mid;

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

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public Date getCreated_at() throws ParseException {
        return Utils.parserWeiboDate(created_at);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getSend_type() {
        return send_type;
    }

    public void setSend_type(int send_type) {
        this.send_type = send_type;
    }

    public String getSender_screen_name() {
        return sender_screen_name;
    }

    public void setSender_screen_name(String sender_screen_name) {
        this.sender_screen_name = sender_screen_name;
    }

    public String getRecipient_screen_name() {
        return recipient_screen_name;
    }

    public void setRecipient_screen_name(String recipient_screen_name) {
        this.recipient_screen_name = recipient_screen_name;
    }

    public UserShow getSender() {
        return sender;
    }

    public void setSender(UserShow sender) {
        this.sender = sender;
    }

    public UserShow getRecipient() {
        return recipient;
    }

    public void setRecipient(UserShow recipient) {
        this.recipient = recipient;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MessageOpen [id=").append(id).append(", text=")
                .append(text).append(", sender_id=").append(sender_id)
                .append(", recipient_id=").append(recipient_id)
                .append(", created_at=").append(created_at)
                .append(", send_type=").append(send_type)
                .append(", sender_screen_name=").append(sender_screen_name)
                .append(", recipient_screen_name=")
                .append(recipient_screen_name).append(", sender=")
                .append(sender).append(", recipient=").append(recipient)
                .append(", mid=").append(mid).append("]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        MessageOpen o = null;
        try {
            o = (MessageOpen) super.clone();
            o.sender = sender == null ? null : (UserShow) sender.clone();
            o.recipient = recipient == null ? null : (UserShow) recipient
                    .clone();
        } catch (CloneNotSupportedException e) {
            // ingore
        }
        return o;
    }

    @Override
    public int compareTo(MessageOpen another) {
        if (!equals(another)) {
            try {
                return getCreated_at().compareTo(another.getCreated_at()) > 0 ? 1
                        : -1;
            } catch (Exception e) {
                // ingore
            }
        }
        return 0;
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
        MessageOpen other = (MessageOpen) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
