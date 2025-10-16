package Modules;

public class Turret {
    public static double angleToServoValue(double angle) {
        return angle * ((Constants.TURRETMAXSERVOVALUE - Constants.TURRETMINSERVOVALUE) / (Constants.TURRETMAXANGLE - Constants.TURRETMINANGLE));
    }

    public static double servoValueToAngle(double servoValue) {
        return servoValue * ((Constants.TURRETMAXANGLE - Constants.TURRETMINANGLE) / (Constants.TURRETMAXSERVOVALUE - Constants.TURRETMINSERVOVALUE));
    }

    public static void setToAngle(double angle) {
        OCBHWM.turretServo.setPosition(angleToServoValue(angle));
    }

    public static void addAngle(double increment) {
        double currentValue= servoValueToAngle(OCBHWM.turretServo.getPosition());
        OCBHWM.turretServo.setPosition(angleToServoValue(currentValue+increment));
    }

    public static void subtractAngle(double increment) {
        double currentValue= servoValueToAngle(OCBHWM.turretServo.getPosition());
        OCBHWM.turretServo.setPosition(angleToServoValue(currentValue+increment));
    }

    public double getCurrentAngle() {
        if (OCBHWM.turretFeedback == null) return 0;
        return (OCBHWM.turretFeedback.getVoltage() / 3.3)*360;
    }

}
