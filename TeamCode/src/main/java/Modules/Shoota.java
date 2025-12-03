package Modules;

import com.arcrobotics.ftclib.util.LUT;
import com.qualcomm.hardware.limelightvision.LLResult;

public class Shoota {
    public static double PrevTurretAng;
    public static double DesiredTurretAng;
    public static double PosError = 0;
    public static double currentTurretTolerance = 1;
    public static boolean InPos = true;
    public static boolean Force = false;



    static LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        add(40.0, 0.42);
        add(45.0, 0.42);
        add(50.0, 0.42);
        add(55.0, 0.464);
        add(60.0, 0.474);
        add(65.0, 0.4855);
        add(70.0, 0.504);
        add(75.0, 0.5224);
        add(80.0, 0.5325);

    }};

    public static double getSpeeds(double distance) {
        return speeds.getClosest(distance);
    }

    static LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        add(40.0, 0.05 );
        add(45.0, 0.171);
        add(50.0, 0.193);
        add(55.0, 0.296);
        add(60.0, 0.318);
        add(65.0, 0.431);
        add(70.0, 0.521);
        add(75.0, 0.5);
        add(80.0, 0.546);
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

    public static void turretTolerance(double distance){
      currentTurretTolerance = Constants.TURRETDYNAMIC/distance;
    }

    public static boolean cameraAdjustTurret() {
        LLResult result = OCBHWM.limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                if (result.getTx() > Constants.TURRETANGLETOLERANCE) {
                    Turret.subtractAngle(Math.abs(Math.pow(result.getTx() * 0.08, 2)));
                } else if (result.getTx() < -Constants.TURRETANGLETOLERANCE) {
                    Turret.addAngle(Math.abs(Math.pow(result.getTx() * 0.08, 2)));
                }
                return result.getTx()>= currentTurretTolerance || result.getTx()<= -currentTurretTolerance;
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
                    Shoota.currentTurretTolerance = 1;
                } else if (result.getTa() < Constants.CLOSESHOT_TA) {
                    //TODO more accurate far shot distance calculation
                } else if (result.getTa() >= Constants.CLOSESHOT_TA) {
                    double distance = Shoota.distanceToGoal(result.getTy());
                    Shoota.setSpeed(Shoota.getSpeeds(distance));
                    Hood.setToAngle(Shoota.gethoodAngle(distance));
                    turretTolerance(distance);
                }
            }
        }
    }
}