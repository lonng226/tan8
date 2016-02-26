package lonng.com.tan8.Entity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Invitation {


    private int tid;
    private User sendUser;
    private String content;
    private List<String> picUrls;
    private List<User> upUsers;
    private int bankid;
    private List<Comment> comments;
    private String videoUrl;
    private String previewimage;


    public List<User> getUpUsers() {
        return upUsers;
    }

    public void setUpUsers(List<User> upUsers) {
        this.upUsers = upUsers;
    }



    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getPreviewimage() {
        return previewimage;
    }

    public void setPreviewimage(String previewimage) {
        this.previewimage = previewimage;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }



    public User getSendUser() {
        return sendUser;
    }

    public void setSendUser(User sendUser) {
        this.sendUser = sendUser;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }



    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getBank() {
        return bankid;
    }

    public void setBank(int bank) {
        this.bankid = bank;
    }

    public int getDzCount() {
        if (upUsers== null)
            return 0;
        return upUsers.size();
    }


    public int getPlCount() {
        if (comments == null)
            return 0;
        return comments.size();
    }



}
