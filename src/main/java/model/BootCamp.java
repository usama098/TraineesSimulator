package model;

import java.util.ArrayList;
import java.util.List;

public class BootCamp extends TrainingCentre implements TrainingCentersInterface {
    private static List<Trainee> bootcampTrainees = new ArrayList<>();

    public BootCamp(int capacity, boolean closed, String course,
                    int months, int lowAttendance) {
        super(capacity, closed, course, months, lowAttendance);
    }

    public void storeTrainees(Trainee trainee) {
        this.bootcampTrainees.add(trainee);
    }

    public List<Trainee> getTraineesFromCenter() {
        List<Trainee> trainees = this.bootcampTrainees;
        return trainees;
    }

    public void increaseMonth() {
        for(int i = 0; i < bootcampTrainees.size(); i++) {
            Trainee trainee = bootcampTrainees.get(i);
            trainee.setMonths(trainee.getMonths() + 1);
        }
    }
}
