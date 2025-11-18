package Modules;

import com.arcrobotics.ftclib.util.InterpLUT;
import com.arcrobotics.ftclib.util.LUT;
import com.qualcomm.hardware.limelightvision.LLResult;

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


    public static void CheckSpeed(double setShootSpeed) {
        if (OCBHWM.flywheel.getVelocity() > setShootSpeed + 20 || OCBHWM.flywheel.getVelocity() < setShootSpeed - 20) {
            OCBHWM.indLight.setPosition(.33);
        } else if (OCBHWM.flywheel.getVelocity() <= setShootSpeed + 20 && OCBHWM.flywheel.getVelocity() >= setShootSpeed - 20) {
            OCBHWM.indLight.setPosition(.5);
        } else {
            OCBHWM.indLight.setPosition(.33);
        }
    }

    public static boolean cameraAdjustTurret() {
        LLResult result = OCBHWM.limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                if (result.getTx() > Constants.TURRETANGLETOLERANCE) {
                    Turret.subtractAngle(Math.abs(Math.pow(result.getTx() * 0.08, 2) * 1.1));
                    return false;
                } else if (result.getTx() < -Constants.TURRETANGLETOLERANCE) {
                    Turret.addAngle(Math.abs(Math.pow(result.getTx() * 0.08, 2) * 1.1));
                    return false;
                }
                return true;
            }

        }
        return true;
    }
    public static void cameraSetLaunch() {
        LLResult result = OCBHWM.limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                if (result.getTy() < Constants.FARSHOTTY) {
                   Hood.setToAngle(Constants.FARSHOTHOODSERVO);
                   Shoota.setSpeed(Constants.FARSHOTSPEED);
                } else if (result.getTy() < Constants.CLOSESHOTTY) {
                    Hood.setToAngle(Constants.CLOSESHOTHOODSERVO);
                    Shoota.setSpeed(Constants.CLOSESHOTSPEED);
                } else if (result.getTy() >= Constants.FARSHOTTY && result.getTy() < 0){
                    //TODO DO MATH FOR ADJUSTMENT
                } else if (result.getTy() <= Constants.CLOSESHOTTY && result.getTy() > 0){

                }
            }

        }
    }
}