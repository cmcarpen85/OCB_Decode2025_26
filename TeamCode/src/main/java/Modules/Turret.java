package Modules;

public class Turret {

    public static double angleToServoValue(double angle) {
        double Ratio = ((Constants.TURRETMAXSERVOVALUE - Constants.TURRETMINSERVOVALUE) / (Constants.TURRETMAXANGLE - Constants.TURRETMINANGLE));

        if (angle > 0) {
            return angle * Ratio + 0.5;
        } else if (angle < 0) {
            return angle * Ratio + 0.5;
        }
        return 0.5;
    }

    public static double servoValueToAngle(double servoValue) {
       double Ratio = ((Constants.TURRETMAXANGLE - Constants.TURRETMINANGLE) / (Constants.TURRETMAXSERVOVALUE - Constants.TURRETMINSERVOVALUE));

        if (servoValue > 0.5) {
            return (servoValue - 0.5) * Ratio;
        } else if (servoValue < 0.5) {
            return (servoValue - 0.5) * Ratio;
        }
        return 0;
    }

    public static double FeedbacktoAngle(){
        return (-55.17*(OCBHWM.turretFeedback.getVoltage()-1.6495));
    }

    public static void setToAngle(double angle) {
        OCBHWM.turretServo.setPosition(angleToServoValue(angle));
    }

    public static void addAngle(double increment) {
        double currentValue= servoValueToAngle(OCBHWM.turretServo.getPosition());
        if (increment>2){
            OCBHWM.turretServo.setPosition(angleToServoValue(currentValue + 2));
        }else{
            OCBHWM.turretServo.setPosition(angleToServoValue(currentValue + increment));
        }
    }

    public static void subtractAngle(double increment) {
        double currentValue= servoValueToAngle(OCBHWM.turretServo.getPosition());
        if (increment>2){
            OCBHWM.turretServo.setPosition(angleToServoValue(currentValue - 2));
        }else{
        OCBHWM.turretServo.setPosition(angleToServoValue(currentValue - increment));
        }
    }

    public double getCurrentAngle() {
        if (OCBHWM.turretFeedback == null) return 0;
        return (OCBHWM.turretFeedback.getVoltage() / 3.3)*360;
    }

    public boolean TurretAngelOk(double desiredAngle) {
        double Error = getCurrentAngle() - desiredAngle;
        if(Error > (Constants.TURRETANGLETOLERANCE)){
            return false;

        } else {
            return true;
        }
    }
}
