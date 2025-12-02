package Modules;

import com.arcrobotics.ftclib.kotlin.extensions.util.LUTExtKt;
import com.arcrobotics.ftclib.util.LUT;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.arcrobotics.ftclib.util.InterpLUT;

public class Shoota {
    public static double PrevTurretAng;
    public static double DesiredTurretAng;
    public static double PosError = 0;
    public static boolean InPos = true;
    public static boolean Force = false;



    static LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        add(5.0, 1.0);
        add(4.0, 0.9);
        add(3.0, 0.75);
        add(2.0, 0.5);
        add(1.0, 0.2);

    }};

    public static double getSpeeds(double distance) {
        return speeds.getClosest(distance);
    }

    static LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        add(5.0, 1.0);
        add(4.0, 0.9);
        add(3.0, 0.75);
        add(2.0, 0.5);
        add(1.0, 0.2);
    }};

    public static double gethoodAngle(double distance) {
        return hoodAngle.getClosest(distance);
    }

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

    public static double distanceToGoal(double Ty) {
        double result = (Constants.GOALHEIGHT - Constants.CAMERAHEIGHT) / Math.tan(Math.toRadians(Constants.CAMERAANGLE + Ty));
    return result * 1.21007 - 7.833307;
    }

    public static boolean cameraAdjustTurret() {
        LLResult result = OCBHWM.limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                if (result.getTx() > Constants.TURRETANGLETOLERANCE) {
                    Turret.subtractAngle(Math.abs(Math.pow(result.getTx() * 0.08, 2)));
                    return false;
                } else if (result.getTx() < -Constants.TURRETANGLETOLERANCE) {
                    Turret.addAngle(Math.abs(Math.pow(result.getTx() * 0.08, 2)));
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
                if (result.getTa() < Constants.FARSHOTTY) {
                    Hood.setToAngle(Constants.FARSHOTHOODSERVO);
                    Shoota.setSpeed(Constants.FARSHOTSPEED);
                } else if (result.getTa() < Constants.CLOSESHOT_TA) {
                    //TODO more accurate far shot distance calculation
                } else if (result.getTa() >= Constants.CLOSESHOT_TA) {
                    double distance = Shoota.distanceToGoal(result.getTy());
                    Shoota.setSpeed(Shoota.getSpeeds(distance));
                    Hood.setToAngle(Shoota.gethoodAngle(distance));
                }
            }
        }
    }
}