package controller;

import model.*;

public class ManageTrainees {
    public int calculateTraineesToPlace(Trainee trainee, TrainingCentre trainingCentre,
                                        int traineesGoingIntoEachCentre) {
        int counter = 0;
        for(int i = 0; i < trainee.getTrainees().size(); i++) {
            if(trainingCentre.getCourse().contains(trainee.getCourse())) {
                counter++;
            }
        }
        if(counter >= traineesGoingIntoEachCentre) {
            counter = traineesGoingIntoEachCentre;
        }
        return counter;
    }

    // if the centre is about to get full we full it and put the remaining into a
    // waiting list, basically trainees will fill the capacity of training center
    // the ones placed will be removed by index 0 (so we give priority to the old
    // ones) and the remaining will be added to the waiting list (by index again)
    // so that they can be placed in another location (if there is any)
    public void centerFull(TrainingCentre trainingCentre, Trainee trainee,
                           int traineesGoingIntoEachCentre,
                           WaitingList wl) {
        int traineesToWaiting = traineesGoingIntoEachCentre - trainingCentre.getCapacity();

        if(traineesGoingIntoEachCentre > 0) {
            traineesGoingIntoEachCentre = calculateTraineesToPlace(trainee, trainingCentre,
                    traineesGoingIntoEachCentre);
            // removing trainees by index 0 because they got placed in a training center
            for(int j = 0; j < traineesGoingIntoEachCentre; j++) {
                wl.deleteWaitingList(trainee);
                // adding to list of training center the trainee
                trainingCentre.storeTrainees(trainee.getTrainees().get(0));
            }
        }

        // getting trainees from trainees list and adding them to waiting list
        for(int i = 0; i < traineesToWaiting; i++) {
            wl.storeWaitingList(trainee.getTrainees().get(0));
        }
        System.out.println("old capacity: " + trainingCentre.getCapacity());
        trainingCentre.setCapacity(0);
        System.out.println("new capacity " + trainingCentre.getCapacity());
        System.out.println("traWaiting: " + wl.getWaitingList().size());
    }

    // function that will be used when looping the training centres
    public void manageCentres(TrainingCentre trainingCentre, WaitingList wl,
                              int traineesGoingIntoEachCentre, Trainee trainee, int newHires) {
        if(!trainingCentre.isClosed()) {
            if(wl.getWaitingList().size() > 0
                    && traineesGoingIntoEachCentre <= wl.getWaitingList().size()) {
                System.out.println("old capacity " + trainingCentre.getCapacity());

                // see comments above
                if(trainingCentre.getCapacity() < traineesGoingIntoEachCentre) {
                    centerFull(trainingCentre, trainee, traineesGoingIntoEachCentre, wl);
                } else {
                    traineesGoingIntoEachCentre = calculateTraineesToPlace(trainee, trainingCentre,
                            traineesGoingIntoEachCentre);
                    System.out.println("old capacity: " + trainingCentre.getCapacity());
                    trainingCentre.setCapacity(trainingCentre.getCapacity() -
                            traineesGoingIntoEachCentre);
                    System.out.println("new capacity: " + trainingCentre.getCapacity());
                    System.out.println("traWaiting: " + wl.getWaitingList().size());
                }
                // storing trainees into training center
                for(int i = 0; i < traineesGoingIntoEachCentre; i++) {
                    trainingCentre.storeTrainees(trainee.getTrainees().get(0));
                }

                newHires -= traineesGoingIntoEachCentre;
            } else if(traineesGoingIntoEachCentre > wl.getWaitingList().size()
                    && wl.getWaitingList().size() > 0) {
                System.out.println("old capacity: " + trainingCentre.getCapacity());

                // see comments above
                if(trainingCentre.getCapacity() < traineesGoingIntoEachCentre) {
                    centerFull(trainingCentre, trainee, traineesGoingIntoEachCentre, wl);
                } else {
                    traineesGoingIntoEachCentre = calculateTraineesToPlace(trainee, trainingCentre,
                            traineesGoingIntoEachCentre);
                    trainingCentre.setCapacity(trainingCentre.getCapacity() -
                            traineesGoingIntoEachCentre);

                    // calculating how many trainees we need to remove from waiting
                    int numberTraineesToRemoveFromWaiting = traineesGoingIntoEachCentre -
                            wl.getWaitingList().size();

                    // deleting from waiting list by index (so that we give priority)
                    if(numberTraineesToRemoveFromWaiting > 0) {
                        for(int i = 0; i < numberTraineesToRemoveFromWaiting; i++) {
                            wl.deleteWaitingList(trainee);
                            // adding to list of training center the trainee
                            trainingCentre.storeTrainees(trainee.getTrainees().get(0));
                        }
                    }
                    System.out.println("new capacity: " + trainingCentre.getCapacity());
                    System.out.println("traWaiting: " + wl.getWaitingList().size());

                    // updating new hires so that we can put them into waiting list
                    newHires -= traineesGoingIntoEachCentre - numberTraineesToRemoveFromWaiting;
                }
            } else {
                System.out.println("old capacity: " + trainingCentre.getCapacity());

                // see comments above
                if(trainingCentre.getCapacity() < traineesGoingIntoEachCentre) {
                    centerFull(trainingCentre, trainee, traineesGoingIntoEachCentre, wl);
                } else {
                    traineesGoingIntoEachCentre = calculateTraineesToPlace(trainee, trainingCentre,
                            traineesGoingIntoEachCentre);
                    trainingCentre.setCapacity(trainingCentre.getCapacity() -
                            traineesGoingIntoEachCentre);

                    for(int i = 0; i < traineesGoingIntoEachCentre; i++) {
                        // adding to list of training center the trainee
                        trainingCentre.storeTrainees(trainee.getTrainees().get(0));
                    }
                    System.out.println("new capacity: " + trainingCentre.getCapacity());
                    System.out.println("traWaiting: " + wl.getWaitingList().size());
                    System.out.println("new hires: " + newHires);
                }
                newHires -= traineesGoingIntoEachCentre;
            }
            System.out.println("newHires after: " + newHires);
            // adding to wait list trainees that cannot be placed
            for(int j = 0; j < newHires; j++) {
                wl.storeWaitingList(trainee.getTrainees().get(0));
            }
            System.out.println("waiting list " + wl.getWaitingList().size());
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
            // generating random training centres and putting them into a list
            // first month we can't have a TrainingHub
            int countBootcamps = 0;
            for(int k = 0; k < tc.getTrainingCentres().size(); k++) {
                TrainingCentre trainingCentre = tc.getTrainingCentres().get(k);
                if(trainingCentre instanceof BootCamp) {
                    countBootcamps++;
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

                // bootcamp training centres
                System.out.println("start centre");
                manageCentres(trainingCentre, wl, traineesGoingIntoEachCentre,
                        trainee, newHires);

                int priorityWaitingList = trainingCentre instanceof TrainingHub ?
                        100 - trainingCentre.getCapacity() : 200 -
                        trainingCentre.getCapacity();

                // we first let pass one month and then we check if we should close the training
                // bootcamps can remain open up to 3 months without closing
                int minimumCapacity = trainingCentre instanceof TrainingHub ? 75 : 175;
                if(trainingCentre.getMonths() > 1 &&
                        trainingCentre.getCapacity() > minimumCapacity &&
                        !(trainingCentre instanceof BootCamp)) {
                    trainingCentre.setClosed(true);

                    // adding trainees to waiting list with index 0 so we give priority
                    // and removing them from the list of the centre
                    for(int k = 0; k < priorityWaitingList; k++) {
                        Trainee priorityTrainee = trainingCentre.getTrainee();
                        wl.getWaitingList().add(0, priorityTrainee);
                        trainingCentre.removeTrainees();
                    }
                }
                trainingCentre.setMonths(trainingCentre.getMonths() + 1);
                System.out.println("end centre");
            }
            System.out.println("size training " + tc.getTrainingCentres().size());
        }
    }
}