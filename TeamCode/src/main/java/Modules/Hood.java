package Modules;

import com.sun.tools.javac.code.Attribute;

public class Hood {


    public static void setToAngle(double angle) {
        OCBHWM.hoodServo.setPosition(angle);
    }

    public static void addAngle(double increment) {
        double currentValue = OCBHWM.hoodServo.getPosition();
        if (currentValue + increment > Constants.HOODMAXSERVOVALUE) {
            OCBHWM.hoodServo.setPosition(Constants.HOODMAXSERVOVALUE);
        } else {
            OCBHWM.hoodServo.setPosition(currentValue + increment);
        }

    }

    public static void subtractAngle(double increment) {
        double currentValue = OCBHWM.hoodServo.getPosition();
        if (currentValue - increment < Constants.HOODMINSERVOVALUE) {
            OCBHWM.hoodServo.setPosition(Constants.HOODMINSERVOVALUE);
        } else {
            OCBHWM.hoodServo.setPosition(currentValue - increment);
        }
    }

    public double getCurrentAngle() {
        if (OCBHWM.hoodFeedback == null) return 0;
        return (OCBHWM.hoodFeedback.getVoltage() / 3.3)*360;
    }

    public boolean hoodAngelOk(double desiredAngle) {
        double Error = getCurrentAngle() - desiredAngle;
        if(Error > (Constants.HOODTARGETTOLERANCE)){
            return false;

        } else {
            return true;
        }
    }
}
