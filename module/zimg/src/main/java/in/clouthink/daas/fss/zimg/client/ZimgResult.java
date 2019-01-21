package in.clouthink.daas.fss.zimg.client;

public class ZimgResult {

    private boolean ret;

    private ZimgInfo info;

    private ZimgError error;

    public ZimgResult() {
    }

    public ZimgResult(boolean ret, ZimgError error) {
        this.ret = ret;
        this.error = error;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public ZimgError getError() {
        return error;
    }

    public void setError(ZimgError error) {
        this.error = error;
    }

    public ZimgInfo getInfo() {
        return info;
    }

    public void setInfo(ZimgInfo info) {
        this.info = info;
    }
}
