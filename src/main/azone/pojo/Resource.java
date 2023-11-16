package azone.pojo;

public class Resource {//9
    private Integer resource_id;
    private String resourceImg;
    private String resourceName;
    private Double resourceSize;
    private String publishAuthor;
    private Integer downLoadNum;
    private Integer potentUserNum;
    private String resourceUrl;
    private Integer status=0;//status =0 是有效的状态; -1 无效

    public Resource(){}

    public Resource(String resourceImg, String resourceName, Double resourceSize,
                    String publishAuthor, Integer downLoadNum, Integer potentUserNum, String resourceUrl) {
        this.resourceImg = resourceImg;
        this.resourceName = resourceName;
        this.resourceSize = resourceSize;
        this.publishAuthor = publishAuthor;
        this.downLoadNum = downLoadNum;
        this.potentUserNum = potentUserNum;
        this.resourceUrl = resourceUrl;

    }

    public Resource(Integer resource_id){
        this.resource_id = resource_id;
    }

    public Integer getResource_id() {
        return resource_id;
    }

    public void setResource_id(Integer resource_id) {
        this.resource_id = resource_id;
    }

    public String getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(String resourceImg) {
        this.resourceImg = resourceImg;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Double getResourceSize() {
        return resourceSize;
    }

    public void setResourceSize(Double resourceSize) {
        this.resourceSize = resourceSize;
    }

    public String getPublishAuthor() {
        return publishAuthor;
    }

    public void setPublishAuthor(String publishAuthor) {
        this.publishAuthor = publishAuthor;
    }

    public Integer getDownLoadNum() {
        return downLoadNum;
    }

    public void setDownLoadNum(Integer downLoadNum) {
        this.downLoadNum = downLoadNum;
    }

    public Integer getPotentUserNum() {
        return potentUserNum;
    }

    public void setPotentUserNum(Integer potentUserNum) {
        this.potentUserNum = potentUserNum;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(){ this.status = status;}


}
