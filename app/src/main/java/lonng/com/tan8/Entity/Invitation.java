package lonng.com.tan8.Entity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Invitation {



    private User sendUser;
    private String headiconUrl;
    private String nickName;
    private String content;
    private List<String> picUrls;
    private List<User> users;
    private String bank;
    private String dzCount;
    private String plCount;
    private List<Comment> comments;
    private String videoUrl;
    private String previewimage;

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

    private int tid;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadiconUrl() {
        return headiconUrl;
    }

    public void setHeadiconUrl(String headiconUrl) {
        this.headiconUrl = headiconUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getDzCount() {
        return dzCount;
    }

    public void setDzCount(String dzCount) {
        this.dzCount = dzCount;
    }

    public String getPlCount() {
        return plCount;
    }

    public void setPlCount(String plCount) {
        this.plCount = plCount;
    }





}
