package dfgden.pxart.com.pxart.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Pattern implements Serializable {

    private String id;
    private String imageId;
    private String name;
    private int likesCount;
    private boolean liked;
    private String url;
    private String imageCdnUrl;
    private Author author;
    private List<String> canvases = new ArrayList<String>();
    private List<Boolean> layersVisibility = new ArrayList<Boolean>();
    private int scale;
    private String filterName;
    private boolean isDraft;
    private String creationTime;
    private int commentsCount;
    private List<Comment> comments = new ArrayList<Comment>();
    private String originalImageId;

    public Pattern() {
    }


    public Pattern(String id, String imageId, String name, int likesCount, boolean liked, String url, String imageCdnUrl, Author author, List<String> canvases, List<Boolean> layersVisibility, int scale, String filterName, boolean isDraft, String creationTime, int commentsCount, List<Comment> comments, String originalImageId) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.likesCount = likesCount;
        this.liked = liked;
        this.url = url;
        this.imageCdnUrl = imageCdnUrl;
        this.author = author;
        this.canvases = canvases;
        this.layersVisibility = layersVisibility;
        this.scale = scale;
        this.filterName = filterName;
        this.isDraft = isDraft;
        this.creationTime = creationTime;
        this.commentsCount = commentsCount;
        this.comments = comments;
        this.originalImageId = originalImageId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageCdnUrl() {
        return imageCdnUrl;
    }

    public void setImageCdnUrl(String imageCdnUrl) {
        this.imageCdnUrl = imageCdnUrl;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<String> getCanvases() {
        return canvases;
    }

    public void setCanvases(List<String> canvases) {
        this.canvases = canvases;
    }

    public List<Boolean> getLayersVisibility() {
        return layersVisibility;
    }

    public void setLayersVisibility(List<Boolean> layersVisibility) {
        this.layersVisibility = layersVisibility;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public boolean isIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getOriginalImageId() {
        return originalImageId;
    }

    public void setOriginalImageId(String originalImageId) {
        this.originalImageId = originalImageId;
    }
}
