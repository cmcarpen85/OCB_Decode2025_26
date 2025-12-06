package Modules;

import com.arcrobotics.ftclib.util.Timing;

import java.util.Timer;

public class Intake {

   public static Timing.Timer blockTime;

    public static void intakeIn() {
        OCBHWM.intakeM.setPower(Constants.INTAKEPOWER);
    }

    public static void autoIntakeIn() {
        OCBHWM.intakeM.setPower(Constants.INTAKEPOWER);
    }

    public static void intakeRest() {
        OCBHWM.intakeM.setPower(0);
    }

    public static void intakeOut(){
        OCBHWM.intakeM.setPower(-Constants.INTAKEPOWER);
    }

    public static void intakeHold() {
        OCBHWM.intakeM.setPower(Constants.INTAKEHOLD);
    }

    public static boolean ballPresentInIntake() {
        return OCBHWM.artifactInIntake.getState();
    }

    public static void setIntakeLight (boolean On) {
        if (On) {
            OCBHWM.indLight.setPosition(0.722);
        } else {
            OCBHWM.indLight.setPosition(0);
        }
    }

    public static boolean intakeFull() {
        boolean Blocked = OCBHWM.artifactInIntake.getState();
        if (Blocked && !blockTime.isTimerOn()) {
            blockTime.start();
        } else if (blockTime.elapsedTime() >= Constants.PASSTIME && Blocked) {
            return true;
        } else if (!Blocked) {
            blockTime.pause();
            return false;
        }
        return false;
    }
}
