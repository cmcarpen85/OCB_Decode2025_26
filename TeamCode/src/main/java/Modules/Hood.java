package Modules;

public class Hood {
    public static double angleToServoValue(double angle) {
        return angle * ((Constants.HOODMAXSERVOVALUE - Constants.HOODMINSERVOVALUE) / (Constants.HOODMAXANGLE - Constants.HOODMINANGLE));
    }

    public static double servoValueToAngle(double servoValue) {
        return servoValue * ((Constants.HOODMAXANGLE - Constants.HOODMINANGLE) / (Constants.HOODMAXSERVOVALUE - Constants.HOODMINSERVOVALUE));
    }

    public static void setToAngle(double angle) {
        OCBHWM.hoodServo.setPosition(angleToServoValue(angle));
    }

    public static void addAngle(double increment) {
        double currentValue = servoValueToAngle(OCBHWM.hoodServo.getPosition());
        if (currentValue + increment > Constants.HOODMAXANGLE) {
            OCBHWM.hoodServo.setPosition(Constants.HOODMAXSERVOVALUE);
        } else {
            OCBHWM.hoodServo.setPosition(angleToServoValue(currentValue + increment));
        }

    }

    public static void subtractAngle(double increment) {
        double currentValue = servoValueToAngle(OCBHWM.hoodServo.getPosition());
        if (currentValue - increment < Constants.HOODMINANGLE) {
            OCBHWM.hoodServo.setPosition(Constants.HOODMINSERVOVALUE);
        } else {
            OCBHWM.hoodServo.setPosition(angleToServoValue(currentValue - increment));
        }
    }
}
