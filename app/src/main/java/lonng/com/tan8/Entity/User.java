package lonng.com.tan8.Entity;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class User {

    private String userId;
    private String userNickname;
    //头像
    private String headiconUrl;
    //发帖数
    private int sendInvatationCount;
    //精华帖子数
    private int jhInvatationCount;

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    //获取赞 数
    private int upCount;


    public Bitmap getHeadiconBitmap() {
        return headiconBitmap;
    }

    public void setHeadiconBitmap(Bitmap headiconBitmap) {
        this.headiconBitmap = headiconBitmap;
    }

    private Bitmap headiconBitmap;


    public String getHeadiconUrl() {
        return headiconUrl;
    }

    public void setHeadiconUrl(String headiconUrl) {
        this.headiconUrl = headiconUrl;
    }

    public int getSendInvatationCount() {
        return sendInvatationCount;
    }

    public void setSendInvatationCount(int sendInvatationCount) {
        this.sendInvatationCount = sendInvatationCount;
    }

    public int getJhInvatationCount() {
        return jhInvatationCount;
    }

    public void setJhInvatationCount(int jhInvatationCount) {
        this.jhInvatationCount = jhInvatationCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
