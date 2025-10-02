package Modules;

public class Intake {

    public static void intakeIn() {
        OCBHWM.intakeM.setPower(Constants.INTAKEPOWER);
    }

    public static void intakeRest() {
        OCBHWM.intakeM.setPower(0);

    }

    public static void intakeOut(){
        OCBHWM.intakeM.setPower(-Constants.INTAKEPOWER);
    }

}
