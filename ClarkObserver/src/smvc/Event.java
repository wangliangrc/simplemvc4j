package smvc;

public class Event {
    public static final String DEFAULT_ID = "Event_DEFAULT_ID";

    private final String mId;
    private final Object mMsg;

    Event(String id, Object msg) {
        super();
        this.mId = id.intern();
        this.mMsg = msg;
    }

    Event(Event another) {
        mId = another.mId;
        mMsg = another.mMsg;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mId == null) ? 0 : mId.hashCode());
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
        Event other = (Event) obj;
        if (mId == null) {
            if (other.mId != null)
                return false;
        } else if (!mId.equals(other.mId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Event [id=" + mId + ", message=" + mMsg + "]";
    }

    public String getId() {
        return mId;
    }

    public Object getMessage() {
        return mMsg;
    }

}
