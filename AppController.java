import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class AppController {
    Systems system;
    Feature feature;
    AdminUser admin;
    CaloriesCalulator caloriesCalulator;

    AppController(Systems system, AdminUser admin, CaloriesCalulator caloriesCalulator) {
        this.system = system;
        this.admin = admin;
        this.caloriesCalulator = caloriesCalulator;
    }

    public void changeCalculatorHour(String hour) {
        this.caloriesCalulator.changeHour(convertStringToDouble(hour)); // Controller-View: calling method from
                                                                        // calolries calculator
        ;
    }

    public void changeCalculatorRep(String rep) {
        this.caloriesCalulator.changeRep(convertStringToInt(rep));// Controller-View: calling method from calolries
                                                                  // calculator
    }

    public void changeCalculatorExercise(ExerciseType exerciseType) {// Controller-View: calling method from calolries
                                                                     // calculator
        this.caloriesCalulator.changeExcerciseType(exerciseType);
    }

    public void changeCalculatorWeight() {
        this.caloriesCalulator.setWeight(this.system.getCurrentUser().getWeight().doubleValue());// Controller-View:
                                                                                                 // calling method from
                                                                                                 // calolries calculator
    }

    public void reformatHistory() {
        this.system.getCurrentUser().reformatHistory();// Controller-View: calling method from calolries calculator
    }

    public void addupdateDailyLog(DailyLog dailyLog, ExerciseType exercisetype, String rep) {
        Exercise exercise;
        if (system.getCurrentUser().getCaloriesCalculation() == CalculateExcerciseCalories.DURATION_OF_EXCERCISE) {
            double repetition = convertStringToDouble(rep);
            exercise = new Exercise(new SimpleDoubleProperty(repetition), new SimpleIntegerProperty(0), exercisetype);
        } else {
            int repetition = convertStringToInt(rep);
            exercise = new Exercise(new SimpleDoubleProperty(0), new SimpleIntegerProperty(repetition), exercisetype);
        } // Controller: Logic
        dailyLog.setFeature(exercise);// Controller-View: calling method from DailyLog
        reformatHistory();
    }

    public void deleteDailyLog(DailyLog daily) {
        this.system.getCurrentUser().getFitnessHistory().getHistory().remove(daily);// Controller-Model
        reformatHistory();
    }

    public void addExercise(int day, int month, int year,
            ExerciseType exerciseType, SimpleIntegerProperty repetitions) {
        DailyLog dailyLog = system.getCurrentUser().getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog == null) {
            dailyLog = new DailyLog(day, month, year);
        } // Controller: Logic

        SimpleDoubleProperty hours = new SimpleDoubleProperty(0); // Set to 0 as we're using repetitions
        SimpleIntegerProperty count = repetitions;
        Exercise exercise = new Exercise(hours, count, exerciseType);
        dailyLog.addFeature(exercise);// Controller-Model
        system.getCurrentUser().getFitnessHistory().addOrUpdateDailyLog(dailyLog);// Controller-Model
        system.getCurrentUser().calculateCalories(dailyLog);// Controller-Model
        system.getCurrentUser().calculateImprovement(dailyLog, system.getCurrentUser().getGoal());// Controller-Model
    }

    public void addExercise(int day, int month, int year,
            ExerciseType exerciseType, SimpleDoubleProperty hours) {
        DailyLog dailyLog = system.getCurrentUser().getFitnessHistory().getDailyLog(day, month, year);
        if (dailyLog == null) {
            dailyLog = new DailyLog(day, month, year);
        }
        SimpleIntegerProperty count = new SimpleIntegerProperty(0);
        Exercise exercise = new Exercise(hours, count, exerciseType);
        dailyLog.addFeature(exercise);

        system.getCurrentUser().getFitnessHistory().addOrUpdateDailyLog(dailyLog);// Controller-Model

        system.getCurrentUser().calculateCalories(dailyLog);// Controller-Model

        system.getCurrentUser().calculateImprovement(dailyLog, system.getCurrentUser().getGoal());// Controller-Model

    }

    public void addExercise(DailyLog dailyLog, ExerciseType exerciseType, SimpleIntegerProperty repetitions) {
        SimpleDoubleProperty hours = new SimpleDoubleProperty(0); // Set to 0 as we're using repetitions
        SimpleIntegerProperty count = repetitions;
        Exercise exercise = new Exercise(hours, count, exerciseType);
        dailyLog.addFeature(exercise);// Controller-Model
        system.getCurrentUser().getFitnessHistory().addOrUpdateDailyLog(dailyLog);// Controller-Model
        system.getCurrentUser().calculateCalories(dailyLog);// Controller-Model
        system.getCurrentUser().calculateImprovement(dailyLog, system.getCurrentUser().getGoal());// Controller-Model
    }

    public void addExercise(DailyLog dailyLog, ExerciseType exerciseType, SimpleDoubleProperty hours) {
        SimpleIntegerProperty count = new SimpleIntegerProperty(0);
        Exercise exercise = new Exercise(hours, count, exerciseType);
        dailyLog.addFeature(exercise);// Controller-Model
        system.getCurrentUser().getFitnessHistory().addOrUpdateDailyLog(dailyLog);// Controller-Model
        system.getCurrentUser().calculateCalories(dailyLog);// Controller-Model
        system.getCurrentUser().calculateImprovement(dailyLog, system.getCurrentUser().getGoal());// Controller-Model
    }

    public void createAccount(String username, String password, String email,
            String weight, String height, Goal goal, CalculateExcerciseCalories caloriesCalculation) {

        system.addAccount(username, password, email, convertStringToDouble(weight),
                convertStringToDouble(height), goal, caloriesCalculation);
    }// Controller-Model

    public void logIn(String username, String password) {
        SimpleBooleanProperty log = new SimpleBooleanProperty(system.logIn(username, password));// Controller-Model
        if (!log.getValue()) {
            throw new Error("Cant logIn");
        }
    }

    public void logIn(int id, String password) {
        SimpleBooleanProperty log = new SimpleBooleanProperty(system.logIn(id, password));// Controller-Model
        if (!log.getValue()) {
            throw new Error("Cant logIn");
        }
    }

    public void addSleep(String day, String month, String year, String hours) {
        DailyLog daily = system.getCurrentUser().getFitnessHistory().getDailyLog(
                convertStringToInt(day), convertStringToInt(month), convertStringToInt(year));
        if (daily == null) {
            daily = new DailyLog(convertStringToInt(day), convertStringToInt(month), convertStringToInt(year));
        }
        Double hour = Double.parseDouble(hours);
        daily.addFeature(new SimpleDoubleProperty(hour));// Controller-Model
    }

    public void addCalories(String cal, DailyLog daily) {
        daily.addCalories(Double.parseDouble(cal));// Controller-Model
    }

    public void burnCalories(String cal, DailyLog daily) {
        daily.burnCalories(Double.parseDouble(cal));// Controller-Model
    }

    public void addOrUpdateDailyLog(DailyLog dailyLog) {
        if (system.getCurrentUser().getFitnessHistory().getDailyLog(dailyLog).getDate().equals(dailyLog.getDate())) {
            return;
        }
        system.getCurrentUser().getFitnessHistory().addOrUpdateDailyLog(dailyLog);// Controller-Model
    }

    public void removeAccout(String idString) {
        int id = convertStringToInt(idString);
        system.removeAccount(id);
    }// Controller-Model

    public int convertStringToInt(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        if ("-".equals(s)) {
            return 0;
        }
        return Integer.parseInt(s); // Convert string into integer
    }

    public double convertStringToDouble(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        if ("-".equals(s)) {
            return 0;
        }
        return Double.parseDouble(s); // Convert string into double
    }

}
