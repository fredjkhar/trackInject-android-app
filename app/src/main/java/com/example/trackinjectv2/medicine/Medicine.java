package com.example.trackinjectv2.medicine;

import java.io.Serializable;
import java.util.List;

public class Medicine implements Serializable{
        private String id;
        private String name;
        private int nbrOfPillsPerDose;
        private int nbrOfPillsPerPackage;
        private int nbrOfDosesPerDay;
        private boolean notifications;

        private List<Integer> alarmIDs;
        private List<Time> pickedTimes;
        private List<Integer> pickedDays;
        private String notificationText;

        public Medicine() {
        }



    public Medicine(String id, String name, int nbrOfPillsPerDose, int nbrOfPillsPerPackage, int nbrOfDosesPerDay,
                    boolean notifications) {
            this.id = id;
            this.name = name;
            this.nbrOfPillsPerDose = nbrOfPillsPerDose;
            this.nbrOfPillsPerPackage = nbrOfPillsPerPackage;
            this.nbrOfDosesPerDay = nbrOfDosesPerDay;
            this.notifications = notifications;
        }

    public Medicine(String id, String name, int nbrOfPillsPerDose, int nbrOfPillsPerPackage,
                    int nbrOfDosesPerDay, boolean notifications, List<Integer> alarmIDs, List<Time> pickedTimes,
                    List<Integer> pickedDays, String notificationText) {
            this.id = id;
        this.name = name;
        this.nbrOfPillsPerDose = nbrOfPillsPerDose;
        this.nbrOfPillsPerPackage = nbrOfPillsPerPackage;
        this.nbrOfDosesPerDay = nbrOfDosesPerDay;
        this.notifications = notifications;
        this.alarmIDs = alarmIDs;
        this.pickedTimes = pickedTimes;
        this.pickedDays = pickedDays;
        this.notificationText = notificationText;
    }

    public List<Integer> getAlarmIDs() {
        return alarmIDs;
    }

    public void setAlarmIDs(List<Integer> alarmIDs) {
        this.alarmIDs = alarmIDs;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public List<Integer> getPickedDays() {
        return pickedDays;
    }

    public void setPickedDays(List<Integer> pickedDays) {
        this.pickedDays = pickedDays;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }


    public List<Time> getPickedTimes() {
        return pickedTimes;
    }

    public void setPickedTimes(List<Time> pickedTimes) {
        this.pickedTimes = pickedTimes;
    }

    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNbrOfPillsPerDose() {
            return nbrOfPillsPerDose;
        }

        public void setNbrOfPillsPerDose(int nbrOfPillsPerDose) {
            this.nbrOfPillsPerDose = nbrOfPillsPerDose;
        }

        public int getNbrOfPillsPerPackage() {
            return nbrOfPillsPerPackage;
        }

        public void setNbrOfPillsPerPackage(int nbrOfPillsPerPackage) {
            this.nbrOfPillsPerPackage = nbrOfPillsPerPackage;
        }

        public int getNbrOfDosesPerDay() {
            return nbrOfDosesPerDay;
        }

        public void setNbrOfDosesPerDay(int nbrOfDosesPerDay) {
            this.nbrOfDosesPerDay = nbrOfDosesPerDay;
        }
    public String getId() {
        return id;
    }
}
