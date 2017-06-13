package pheasy.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Function;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.MapUtils;
import pheasy.utils.PheasyCollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jayeshathila
 * on 12/06/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prescription {

    public static final Function<PrescriptionUserRightMapping, String> TO_USERNAME = PrescriptionUserRightMapping::getUsername;

    @Id
    private String id;

    // This is kept unique as it will be easy to understand apis by name rather by id
    @Field(PrescriptionMapping.PRESCRIPTION_TITLE)
    private String prescriptionTitle;

    @Field(PrescriptionMapping.ASSOCIATED_USERNAME)
    private String associatedUsername;

    @Field(PrescriptionMapping.MEDICINE_CONSUMPTION_TIME)
    private List<MedicineConsumptionTime> medicineConsumptionTime;

    @Field(PrescriptionMapping.USER__RIGHTS)
    private List<PrescriptionUserRightMapping> userRightMappings;

    @Transient
    private transient Map<String, PrescriptionUserRightMapping> usernameVsRights;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public List<MedicineConsumptionTime> getMedicineConsumptionTime() {
        return medicineConsumptionTime;
    }

    public void setMedicineConsumptionTime(List<MedicineConsumptionTime> medicineConsumptionTime) {
        this.medicineConsumptionTime = medicineConsumptionTime;
    }

    public void addMedicineConsumptionTime(MedicineConsumptionTime medicineConsumptionTime) {
        if (CollectionUtils.isEmpty(this.medicineConsumptionTime)) {
            this.medicineConsumptionTime = new ArrayList<>();
        }
        this.medicineConsumptionTime.add(medicineConsumptionTime);
    }

    public List<PrescriptionUserRightMapping> getUserRightMappings() {
        return userRightMappings;
    }

    public void setUserRightMappings(List<PrescriptionUserRightMapping> userRightMappings) {
        this.userRightMappings = userRightMappings;
    }

    public Map<String, PrescriptionUserRightMapping> getUsernameVsRights() {
        if (!CollectionUtils.isEmpty(userRightMappings)) {
            if (CollectionUtils.isEmpty(usernameVsRights)) {
                usernameVsRights = PheasyCollectionUtils.transformMap(userRightMappings, TO_USERNAME);
            }
        }

        return usernameVsRights;
    }


    public void addToUsernameVsRights(String username, PrescriptionUserRightMapping mapping) {
        if (CollectionUtils.isEmpty(userRightMappings)) {
            this.userRightMappings = new ArrayList<>();
        }

        if (MapUtils.isEmpty(usernameVsRights)) {
            this.usernameVsRights = PheasyCollectionUtils.transformMap(userRightMappings, TO_USERNAME);
        }

        // Entry already present
        if (usernameVsRights.get(username) != null) {
            return;
        }

        this.userRightMappings.add(mapping);
        this.usernameVsRights.put(username, mapping);
    }

    public boolean isUserAccessibleToDetails(String userName) {
        if (associatedUsername.equals(userName)) {
            return true;
        }

        if (!CollectionUtils.isEmpty(userRightMappings)) {
            if (CollectionUtils.isEmpty(usernameVsRights)) {
                usernameVsRights = PheasyCollectionUtils.transformMap(userRightMappings, TO_USERNAME);
            }
        }

        if (CollectionUtils.isEmpty(usernameVsRights)) {
            return false;
        }

        PrescriptionUserRightMapping mapping = usernameVsRights.get(userName);

        return mapping != null && mapping.getState() == ApprovalState.SUCCESSFUL;
    }


    public String getPrescriptionTitle() {
        return prescriptionTitle;
    }

    public void setPrescriptionTitle(String prescriptionTitle) {
        this.prescriptionTitle = prescriptionTitle;
    }

    public interface PrescriptionMapping {
        String ASSOCIATED_USERNAME = "asctUname";
        String MEDICINE_CONSUMPTION_TIME = "medConsmTim";
        String USER__RIGHTS = "usrRts";
        String PRESCRIPTION_TITLE = "prscTtl";
    }
}