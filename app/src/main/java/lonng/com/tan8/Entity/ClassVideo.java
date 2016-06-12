package lonng.com.tan8.Entity;

/**
 * Created by Administrator on 2016/3/1.
 */
public class ClassVideo {

    private String type;
    private String videoName;
    private String albumname;
    private String description;
    private String previewimageUrl;
    private String videoUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getPreviewimageUrl() {
        return previewimageUrl;
    }

    public void setPreviewimageUrl(String previewimageUrl) {
        this.previewimageUrl = previewimageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
