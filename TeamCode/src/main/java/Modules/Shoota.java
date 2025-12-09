package Modules;

import com.arcrobotics.ftclib.util.LUT;
import com.qualcomm.hardware.limelightvision.LLResult;

public class Shoota {
    public static double PrevTurretAng;
    public static double DesiredTurretAng;
    public static double PosError = 0;
    public static double currentTurretTolerance = 1;
    public static boolean NotInPos = true;
    public static boolean Force = false;
    public static double cameraSign = 0;

    static LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        add(40.0, 0.4200);
        add(42.5, 0.4200);
        add(45.0, 0.4200);
        add(47.5, 0.4200);
        add(50.0, 0.4200);
        add(52.5, 0.4420);
        add(55.0, 0.4640);
        add(57.5, 0.4690);
        add(60.0, 0.4740);
        add(62.5, 0.4800);
        add(65.0, 0.4855);
        add(67.5, 0.4900);
        add(70.0, 0.5040);
        add(72.5, 0.5130);
        add(75.0, 0.5224);
        add(77.5, 0.5290);
        add(80.0, 0.5325);
        add(82.5, 0.5350);
        add(85.0, 0.5400);
        //far shots
        add(126.0,.635);
        add(129.0,.638);
        add(132.0,.640);
        add(136.0,.643);


    }};

    public static double getSpeeds(double distance) {
        return speeds.getClosest(distance);
    }

    static LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        add(40.0, 0.050);
        add(42.5, 0.098);
        add(45.0, 0.141);
        add(47.5, 0.171);
        add(50.0, 0.193);
        add(52.5, 0.248);
        add(55.0, 0.296);
        add(57.5, 0.305);
        add(60.0, 0.318);
        add(62.5, 0.368);
        add(65.0, 0.431);
        add(67.5, 0.480);
        add(70.0, 0.521);
        add(72.5, 0.525);
        add(75.0, 0.530);
        add(77.5, 0.539);
        add(80.0, 0.546);
        add(82.5, 0.550);
        add(85.0, 0.555);
        //far shots
        add(126.0,.608);
        add(129.0,.608);
        add(132.0,.608);
        add(136.0,.608);

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

    //LimeLight
    public static double distanceToGoal(double Ty) {
        double result = (Constants.GOALHEIGHT - Constants.CAMERAHEIGHT) / Math.tan(Math.toRadians(Constants.CAMERAANGLE + Ty));
        return result * 1.21007 - 7.833307;
    }

    public static double farDistanceToGoal(double Ty,double Ta) {
        double Tyresult = (Constants.GOALHEIGHT - Constants.CAMERAHEIGHT) / Math.tan(Math.toRadians(Constants.CAMERAANGLE + Ty));
        double distance = (Tyresult * 1.21007 - 7.833307)*0.92 + Math.sqrt(Ta)*0.85;
        return distance;
    }

    public static double turretTolerance(double distance) {
        currentTurretTolerance = Constants.TURRETDYNAMIC / distance;
        return currentTurretTolerance;
    }

    public static void cameraAdjustTurret() {
        LLResult result = OCBHWM.limelight.getLatestResult();
        double TurretError = OCBHWM.turretServo.getTargetRotation() - OCBHWM.turretServo.getTotalRotation();
        if (result != null) {
            if (result.isValid()) {
                if (TurretError - result.getTx() > Constants.TURRETANGLETOLERANCE) {
                    Turret.addAngle(Math.abs(TurretError - result.getTx()));
                } else if (TurretError - result.getTx() < -Constants.TURRETANGLETOLERANCE) {
                    Turret.subtractAngle(Math.abs(TurretError - result.getTx()));
                }
            }
        }
    }

    public static void cameraSetLaunch() {
        LLResult result = OCBHWM.limelight.getLatestResult();
        if (result != null) {
            if (result.isValid()) {
                if (result.getTa() < Constants.CLOSESHOT_TA) {
                    double farDistance = farDistanceToGoal(result.getTy(),result.getTa());
                    Shoota.setSpeed(Shoota.getSpeeds(farDistance));
                    Hood.setToAngle(Shoota.gethoodAngle(farDistance));
//                    Hood.setToAngle(Constants.FARSHOTHOODSERVO);
//                    Shoota.setSpeed(Constants.FARSHOTSPEED);
                    Shoota.currentTurretTolerance = 2;
                } else if (result.getTa() >= Constants.CLOSESHOT_TA) {
                    double distance = Shoota.distanceToGoal(result.getTy());
                    Shoota.setSpeed(Shoota.getSpeeds(distance));
                    Hood.setToAngle(Shoota.gethoodAngle(distance));
                    currentTurretTolerance = turretTolerance(distance);
                }
                NotInPos = result.getTx() >= currentTurretTolerance || result.getTx() <= -currentTurretTolerance;
            }
        }else {
            Hood.setToAngle(Constants.FARSHOTHOODSERVO);
            Shoota.setSpeed(Constants.FARSHOTSPEED);
        }
    }
}