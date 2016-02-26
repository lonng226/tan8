package lonng.com.tan8.Entity;

/**
 * Created by Administrator on 2015/12/28.
 */
public class Bankuai {

    private int id;
    private String bname;
    private String content;

    private String category;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
