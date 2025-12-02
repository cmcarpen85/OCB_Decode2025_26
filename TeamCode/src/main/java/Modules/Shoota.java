package Modules;

import com.arcrobotics.ftclib.util.LUT;
import com.qualcomm.hardware.limelightvision.LLResult;

public class Shoota {
    public static double PrevTurretAng;
    public static double DesiredTurretAng;
    public static double PosError = 0;
    public static boolean InPos = true;
    public static boolean Force = false;



    static LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        add(40.0, 0.38 );
        add(45.0, 0.40);
        add(50.0, 0.42);
        add(55.0, 0.435);
        add(60.0, 0.445);
        add(65.0, 0.455);
        add(70.0, 0.475);
        add(75.0, 0.48);
        add(80.0, 0.5);

    }};

    public static double getSpeeds(double distance) {
        return speeds.getClosest(distance);
    }

    static LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        add(40.0, 0.05 );
        add(45.0, 0.07);
        add(50.0, 0.14);
        add(55.0, 0.21);
        add(60.0, 0.28);
        add(65.0, 0.35);
        add(70.0, 0.42);
        add(75.0, 0.49);
        add(80.0, 0.608);
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
                if (result.getTa() < Constants.FARSHOT_TA) {
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