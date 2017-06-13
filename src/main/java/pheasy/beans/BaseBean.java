package pheasy.beans;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by jayeshathila
 * on 12/06/17.
 */

/**
 * This bean need to be extended by every bean which will add basic fields to every bean
 */
public class BaseBean {

    @Field(BaseBeanFieldMapping.createdTime)
    private Long createdTime;

    @Field(BaseBeanFieldMapping.updatedTime)
    private Long updatedTime;

    @Field(BaseBeanFieldMapping.createdByAgentId)
    private String createdById;

    @Field(BaseBeanFieldMapping.updatedByAgentId)
    private String updatedById;

    @Field(BaseBeanFieldMapping.isDeleted)
    private boolean isDeleted;

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static class BaseBeanFieldMapping {
        public static final String createdTime = "createdTime";
        public static final String updatedTime = "updatedTime";
        public static final String createdByAgentId = "cById";
        public static final String updatedByAgentId = "uById";
        public static final String isDeleted = "isDeleted";
        public static final String keyVsCustomFieldValue = "keyVsCustomFieldValue";
        public static final String tags = "tags";
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", createdById='" + createdById + '\'' +
                ", updatedById='" + updatedById + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
