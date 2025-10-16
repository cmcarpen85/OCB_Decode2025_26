package Modules;

import com.arcrobotics.ftclib.util.InterpLUT;
import com.arcrobotics.ftclib.util.LUT;

public class Shoota {

    LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        add(5.0, 1.0);
        add(4.0, 0.9);
        add(3.0, 0.75);
        add(2.0, 0.5);
        add(1.0, 0.2);
    }};

    LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        add(5.0, 1.0);
        add(4.0, 0.9);
        add(3.0, 0.75);
        add(2.0, 0.5);
        add(1.0, 0.2);
    }};


    //Flywheel
    public static void setSpeed(double speed) {
        OCBHWM.flywheel.set(speed);
    }

    public static void stop() {
        OCBHWM.flywheel.stopMotor();
    }

    public static void coast() {
        OCBHWM.flywheel.set(Constants.COASTSPEED);
    }
    public static void cameraAdjustTurret() {
//Error = desiredPos - actualPos
//        if (Error  0) {
    }

}
