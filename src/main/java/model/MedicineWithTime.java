package model;

import java.util.List;  // Use Java's List class

public class MedicineWithTime {
    private String medicineName;
    private List<String> timeToTake; 
    

    // Constructor
    public MedicineWithTime(String medicineName, List<String> timeToTake) {
        this.medicineName = medicineName;
        this.timeToTake = timeToTake;
    }

    // Getters and Setters
    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public List<String> getTimeToTake() {
        return timeToTake;
    }

    public void setTimeToTake(List<String> timeToTake) {
        this.timeToTake = timeToTake;
    }
}
