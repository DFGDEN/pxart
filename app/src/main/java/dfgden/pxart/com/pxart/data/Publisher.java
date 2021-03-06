package dfgden.pxart.com.pxart.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Publisher implements Serializable {

    private String id;
    private String name;
    private String avatarImageId;
    private String avatarCdnUrl;
    private String registeredTime;
    private Boolean isFollowed;
    private int followersCount;
    private int followingCount;
    private ArrayList<Follower> followers = new ArrayList<>();
    private ArrayList<Following> followings = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarImageId() {
        return avatarImageId;
    }

    public void setAvatarImageId(String avatarImageId) {
        this.avatarImageId = avatarImageId;
    }

    public String getAvatarCdnUrl() {
        return avatarCdnUrl;
    }

    public void setAvatarCdnUrl(String avatarCdnUrl) {
        this.avatarCdnUrl = avatarCdnUrl;
    }

    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registeredTime) {
        this.registeredTime = registeredTime;
    }

    public boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Follower> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<Following> followings) {
        this.followings = followings;
    }
}
