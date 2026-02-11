package com.example.yumi.domain.plan.models;
import java.io.Serializable;
import java.util.Date;

public class PlanDay implements Serializable {
    private final Date date;
    private final String dayName;
    private final int dayNumber;
    private final String fullDate;
    private final String dateKey;
    private boolean isSelected;

    public PlanDay(Date date, String dayName, int dayNumber,
                   String fullDate, String dateKey, boolean isSelected) {
        this.date = date;
        this.dayName = dayName;
        this.dayNumber = dayNumber;
        this.fullDate = fullDate;
        this.dateKey = dateKey;
        this.isSelected = isSelected;
    }

    public Date getDate() {
        return date;
    }

    public String getDayName() {
        return dayName;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public String getFullDate() {
        return fullDate;
    }

    public String getDateKey() {
        return dateKey;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}