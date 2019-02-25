package in.clouthink.daas.fss.mongodb.support;

public class FileObjectSaveParam extends in.clouthink.daas.fss.support.FileObjectSaveParam {

    private String provider;

    private String category;

    private String code;

    private String name;

    private String description;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
