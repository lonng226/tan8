package lonng.com.tan8.Entity;

/**
 * Created by Administrator on 2015/12/21.
 */
public class Comment {
    private int plID;
    private User plUser;
    private User replyUser;
    private String message;

    public int getPlID() {
        return plID;
    }

    public void setPlID(int plID) {
        this.plID = plID;
    }

    public User getPlUser() {
        return plUser;
    }

    public void setPlUser(User plUser) {
        this.plUser = plUser;
    }

    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
