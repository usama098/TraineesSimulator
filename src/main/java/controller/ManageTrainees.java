package controller;

import model.*;

public class ManageTrainees {
    // function that will be used when looping the training centres
    public void manageCentres(TrainingCentre trainingCentre, WaitingList wl,
                              int traineesGoingIntoEachCentre, Trainee t) {
        if(!trainingCentre.isClosed()) {
            System.out.println("old capacity: " + trainingCentre.getCapacity());
            if(wl.getWaitingList().size() > 0 &&
                    traineesGoingIntoEachCentre <= wl.getWaitingList().size()) {
                if(trainingCentre.getCapacity() < traineesGoingIntoEachCentre) {
                    System.out.println("full");
                    // centerFull(trainingCentre, trainee, traineesGoingIntoEachCentre, wl);
                } else {
                    for(int i = 0; i < wl.getWaitingList().size(); i++) {
                        Trainee traineeWaiting = wl.getWaitingList().get(i);

                        if(trainingCentre.getCourse().contains(traineeWaiting.getCourse())) {
                            trainingCentre.storeTrainees(traineeWaiting);
                            wl.deleteWaitingList(traineeWaiting);
                            traineesGoingIntoEachCentre--;
                            trainingCentre.setCapacity(trainingCentre.getCapacity() - 1);

                            if(traineesGoingIntoEachCentre == 0) {
                                break;
                            }
                        }
                    }
                    // if waiting list cannot remove any trainees we start to fill the ones
                    // that we just hired
                    if(traineesGoingIntoEachCentre > 0) {
                        for(int i = 0; i < t.getTrainees().size(); i++) {
                            Trainee trainee = t.getTrainees().get(i);

                            if(trainingCentre.getCourse().contains(trainee.getCourse())) {
                                trainingCentre.storeTrainees(trainee);
                                t.removeNewHired(trainee);
                                traineesGoingIntoEachCentre--;
                                trainingCentre.setCapacity(trainingCentre.getCapacity() - 1);

                                if(traineesGoingIntoEachCentre == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if(trainingCentre.getCapacity() < traineesGoingIntoEachCentre) {
                    System.out.println("full");
                } else {
                    if(traineesGoingIntoEachCentre > 0) {
                        for(int i = 0; i < t.getTrainees().size(); i++) {
                            Trainee trainee = t.getTrainees().get(i);

                            if(trainingCentre.getCourse().contains(t.getCourse())) {
                                // storing into center
                                trainingCentre.storeTrainees(trainee);
                                t.removeNewHired(trainee);
                                traineesGoingIntoEachCentre--;
                                trainingCentre.setCapacity(trainingCentre.getCapacity() - 1);

                                if(traineesGoingIntoEachCentre == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("t.getTrainees().size()" + t.getTrainees().size());
            if(t.getTrainees().size() > 0) {
                for(int i = 0; i < t.getTrainees().size(); i++) {
                    System.out.println("ii"+i);
                    Trainee trainee = t.getTrainees().get(i);

                    wl.storeWaitingList(trainee);
                    t.removeNewHired(trainee);
                }
            }
            System.out.println("trainees " + t.getTrainees().size());
            System.out.println("waiting " + wl.getWaitingList().size());
            System.out.println("new capacity: " + trainingCentre.getCapacity());
        }
    }

    public void manageTrainees(int months) {
        WaitingList wl = new WaitingList();
        GenerateRandomNumber gn = new GenerateRandomNumber();
        Trainee t = new Trainee("any");
        TrainingCentre tc = new TrainingCentre(0,true,"any", 0);

        // starting from 1st month
        for(int i = 1; i <= months; i++) {
            // generating new hires (between 50-100)
            int newHires = gn.generateRandomNumber(50, 101);
            System.out.println("new hires " + newHires);

            // generating random employees based on the newHires number generated
            // and putting them into a list
            Trainee trainee = null;
            for(int j = 0; j < newHires; j++) {
                trainee = t.generateTrainee();
            }
            System.out.println("trainees " + t.getTrainees().size());
            // generating random training centres and putting them into a list
            // first month we can't have a TrainingHub
            int countBootcamps = 0;
            for(int k = 0; k < tc.getTrainingCentres().size(); k++) {
                TrainingCentre trainingCentre = tc.getTrainingCentres().get(k);
                if(trainingCentre instanceof BootCamp) {
                    countBootcamps++;

                    if(trainingCentre.isClosed()) {
                        countBootcamps--;
                    }
                }
            }

            if(i == 1) {
                tc.generateTrainingCentre(0, 2);
            } else {
                if(countBootcamps == 2) {
                    tc.generateTrainingCentre(1, 3);
                } else {
                    tc.generateTrainingCentre(0, 3);
                }
            }

            int traineesGoingIntoEachCentre = gn.generateRandomNumber(0, 51);
            System.out.println("traineesGoingIntoEachCentre " + traineesGoingIntoEachCentre);

            // looping through every training centres
            for(int j = 0; j < tc.getTrainingCentres().size(); j++) {
                TrainingCentre trainingCentre = tc.getTrainingCentres().get(j);

                System.out.println("start centre");
                manageCentres(trainingCentre, wl, traineesGoingIntoEachCentre,
                        trainee);

                int priorityWaitingList = trainingCentre instanceof TrainingHub ?
                        100 - trainingCentre.getCapacity() : 200 -
                        trainingCentre.getCapacity();

                // bootcamps can remain open up to 3 months without closing
                int minimumCapacity = trainingCentre instanceof TrainingHub ? 75 : 175;
                if(trainingCentre.getMonths() > 1 &&
                        trainingCentre.getCapacity() > minimumCapacity &&
                        !(trainingCentre instanceof BootCamp)) {
                    // adding trainees to waiting list with index 0 so we give priority
                    // and removing them from the list of the centre
                    trainingClosed(priorityWaitingList, trainingCentre, wl);
                } else {
                    int minimumCapacityBootcamp = 475;
                    if(trainingCentre instanceof BootCamp && trainingCentre.getMonths() > 3 &&
                    trainingCentre.getCapacity() > minimumCapacityBootcamp) {
                        int priorityWaitingListBootcamp = 500 - trainingCentre.getCapacity();
                        trainingClosed(priorityWaitingListBootcamp, trainingCentre, wl);
                    }
                }
                trainingCentre.setMonths(trainingCentre.getMonths() + 1);
                System.out.println("end centre");
            }
            System.out.println("size training " + tc.getTrainingCentres().size());
        }
    }
    // adding trainees to waiting list with index 0 so we give priority
    // and removing them from the list of the centre
    public void trainingClosed(int priorityWaitingList, TrainingCentre trainingCentre,
                               WaitingList wl) {
        trainingCentre.setClosed(true);
        for(int k = 0; k < priorityWaitingList; k++) {
            Trainee priorityTrainee = trainingCentre.getTrainee();
            wl.getWaitingList().add(0, priorityTrainee);
            trainingCentre.removeTrainees();
        }
    }
}