package Controller;

import java.io.File;

public class UserSettings {
    private int dbid;
    private int userId;
    private String fontSize;
    private String fontType;
    private String backgroundColor;
    private String resolution;
    private File avatarImage;
    private byte[] savedAvatar;

    public UserSettings(int id, int userId, String fontSize, String fontType, String backgroundColor, String resolution, byte[] avatar) {
        this.dbid = id;
        this.userId = userId;
        this.fontSize = fontSize;
        this.fontType = fontType;
        this.backgroundColor = backgroundColor;
        this.resolution = resolution;
        this.savedAvatar= avatar;
    }
       
    

    public int getId() {
        return dbid;
    }

    public void setId(int id) {
        this.dbid = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public File getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(File avatarImage) {
        this.avatarImage = avatarImage;
    }

    public byte[] getSavedAvatar() {
        return savedAvatar;
    }

    public void setSavedAvatar(byte[] savedAvatar) {
        this.savedAvatar = savedAvatar;
    }
}

