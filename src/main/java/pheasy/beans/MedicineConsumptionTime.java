package pheasy.beans;

import pheasy.utils.TimeConstants;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayeshathila
 * on 12/06/17.
 */

public class MedicineConsumptionTime {

    private String medicineName;

    // Time to consume the medicine at seconds from 12 A.M Previous night
    private List<Long> timeToConsume;

    public MedicineConsumptionTime(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public List<Long> getTimeToConsume() {
        return timeToConsume;
    }

    public void setTimeToConsume(List<Long> timeToConsume) {
        this.timeToConsume = timeToConsume;
    }

    public MedicineConsumptionTime addCosumption(Long epochTime) {
        if (CollectionUtils.isEmpty(timeToConsume)) {
            this.timeToConsume = new ArrayList<>();
        }

        this.timeToConsume.add(epochTime % TimeConstants.SECS_IN_DAY);
        return this;
    }
}
