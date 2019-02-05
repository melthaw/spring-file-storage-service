package in.clouthink.daas.fss.qiniu.impl;

public class UploadResult {

    private long fsize;
    private String key;
    private String hash;
    private int width;
    private int height;

    public UploadResult() {
    }

    public long getFsize() {
        return fsize;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
