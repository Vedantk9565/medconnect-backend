package model;

public class MedicineWithTime {
    private String medicineName;
    private String timeToTake;

    // Constructor
    public MedicineWithTime(String medicineName, String timeToTake) {
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

    public String getTimeToTake() {
        return timeToTake;
    }

    public void setTimeToTake(String timeToTake) {
        this.timeToTake = timeToTake;
    }
}
