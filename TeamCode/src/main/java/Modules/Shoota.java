package Modules;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.util.LUT;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import org.firstinspires.ftc.teamcode.RTPAxon;

import java.util.Objects;

@Config
public class Shoota {

    public static class Params {
        public double kVision = 0.5;
    }

    public static Params PARAMS = new Params();
    public static double PrevTurretAng;
    public static double DesiredTurretAng;
    public static double PosError = 0;
    public static double currentTurretTolerance = 1;
    public static boolean NotInPos = true;
    public static boolean Force = false;
    public static double cameraSign = 0;
    public static LLResult result;
    public static double shotHeight = 0;
    public static double distance = 0;

    static LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        add(32.0, 0.400);
        add(34.5, 0.400);//1030
        add(38.0, 0.400);
        add(39.5, 0.400);
        add(42.0, 0.400);
        add(44.5, 0.4220);
        add(48.0, 0.4440);
        add(49.5, 0.4490); //1160
        add(52.0, 0.4540);
        add(54.5, 0.4600);
        add(57.0, 0.4655);
        add(59.5, 0.4700);
        add(62.0, 0.4840);//1240
        add(64.5, 0.4930);
        add(67.0, 0.5024); // 1280
        add(69.5, 0.5090);
        add(72.0, 0.5125);
        add(74.5, 0.5150);
        add(77.0, 0.5200); //vel 1320
//        add(90.0,Constants.FARSHOTSPEED);

        // Far Zone
        add(127.0, 0.598); // 0.521
        add(123.0, 0.59); // 0.525
        add(131.0, 0.608); // 0.530
        add(136.0, 0.608); // 0.539
        add(139.0, 0.6122); // 0.546
        add(140.0, 0.6073); // 0.555
        add(143.0, 0.6285);
        add(151.0, 0.6314);
    }};
    public static double getSpeeds(double distance) {
        return speeds.getClosest(distance);
    }

    static LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        add(32.0, 0.050); // 0.050
        add(34.5, 0.081); // 0.098
        add(38.0, 0.154); // 0.141
        add(39.5, 0.205); // 0.171
        add(42.0, 0.242); // 0.193
        add(44.5, 0.335); // 0.248
        add(48.0, 0.416); // 0.296
        add(49.5, 0.432); // 0.305
        add(52.0, 0.454); // 0.318
        add(54.5, 0.539); // 0.368
        add(57.0, 0.645); // 0.431
        add(59.5, 0.728); // 0.480
        add(62.0, 0.797); // 0.521
        add(64.5, 0.804); // 0.525
        add(67.0, 0.812); // 0.530
        add(69.5, 0.823); // 0.539
        add(72.0, 0.840); // 0.546
        add(74.5, 0.846); // 0.550
        add(77.0, 0.855); // 0.555
//        add(90.0,Constants.FARSHOTHOODSERVO);

        // Far Zone
        add(127.0, 0.905); // 0.480
        add(123.0, 0.905); // 0.521
        add(131.0, 0.905); // 0.525
        add(136.0, 0.905); // 0.530
        add(139.0, 0.905); // 0.539
        add(140.0, 0.905); // 0.546
        add(143.0, 0.905); // 0.555
        add(151.0, 0.905); // 0.555
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

    public static void checkLimelight() {
        result = OCBHWM.limelight.getLatestResult();
    }

    public static void CheckSpeed(double setShootSpeed) {
        double setVelocity = setShootSpeed * 2444.3 + 9;
        if (OCBHWM.flywheel.getVelocity() > setVelocity + 20 || OCBHWM.flywheel.getVelocity() < setVelocity - 20) {
            OCBHWM.indLight.setPosition(.33);
        } else if (OCBHWM.flywheel.getVelocity() <= setVelocity + 20 && OCBHWM.flywheel.getVelocity() >= setVelocity - 20) {
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

    public static double farDistanceToGoal(double Ty, double Ta) {
        double Tyresult = (Constants.GOALHEIGHT - Constants.CAMERAHEIGHT) / Math.tan(Math.toRadians(Constants.CAMERAANGLE + Ty));
        double distance = (Tyresult * 1.21007 - 7.833307) * 0.92 + Math.sqrt(Ta) * 0.85;
        return distance;
    }

    public static double turretTolerance(double distance) {
        currentTurretTolerance = Constants.TURRETDYNAMIC / distance;
        return currentTurretTolerance;
    }

    public static void cameraAdjustTurret(String color) {
        double TargetRotation = OCBHWM.turretServo.getTargetRotation();
        double TurretError = (OCBHWM.turretServo.getTargetRotation() - OCBHWM.turretServo.getTotalRotation());
        Shoota.checkLimelight();
        if (result != null) {
            if (result.isValid()) {
                if (-1*result.getTx() - TurretError > Constants.TURRETANGLEROUGHTOLERANCE) {
                    Turret.addAngle(Math.abs(-1*result.getTx() - TurretError));
                } else if (-1*result.getTx() - TurretError < -Constants.TURRETANGLEROUGHTOLERANCE) {
                    Turret.subtractAngle(Math.abs(-1*result.getTx() - TurretError));
                }
            } else if (!result.isValid()) {
                if (Objects.equals(color, "red")) {
                    HeadingTracker.headingTrackingRed(false);
                } else {
                    HeadingTracker.headingTrackingBlue(false);
                }
            }
        }
    }

    public static void gyroAdjustTurret(double desiredHeading) {
//    double result = -1 * HeadingTracker.gyroDifference();
        double result = desiredHeading - OCBHWM.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double TurretServoError = OCBHWM.turretServo.getTargetRotation() - OCBHWM.turretServo.getTotalRotation();
        if (result - TurretServoError > Constants.TURRETANGLEROUGHTOLERANCE) {
            Turret.addAngle(Math.abs(result - TurretServoError));
        } else if (result - TurretServoError < -Constants.TURRETANGLEROUGHTOLERANCE) {
            Turret.subtractAngle(Math.abs(result - TurretServoError));
        } //else if (result-TurretServoError > Constants.TURRETANGLETOLERANCE) {
//            Turret.addAngle(Constants.TURRETCLOSECONSTANT * Math.abs(result-TurretServoError));
//        } else if (result-TurretServoError < -Constants.TURRETANGLETOLERANCE) {
//            Turret.subtractAngle(Constants.TURRETCLOSECONSTANT * Math.abs(result-TurretServoError));
//        }
    }
// old
//    public static void gyroAdjustTurret(double desiredHeading) {
////    double result = -1 * HeadingTracker.gyroDifference();
//        double result = desiredHeading + OCBHWM.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
//        double TurretError = OCBHWM.turretServo.getTargetRotation() - OCBHWM.turretServo.getTotalRotation();
//        if (TurretError - result > Constants.TURRETANGLEROUGHTOLERANCE) {
//            Turret.addAngle(Math.abs(TurretError - result));
//        } else if (TurretError - result < -Constants.TURRETANGLEROUGHTOLERANCE) {
//            Turret.subtractAngle(Math.abs(TurretError - result));
//        } else if (TurretError - result > Constants.TURRETANGLETOLERANCE) {
//            Turret.addAngle(Constants.TURRETCLOSECONSTANT * Math.abs(TurretError - result));
//        } else if (TurretError - result < -Constants.TURRETANGLETOLERANCE) {
//            Turret.subtractAngle(Constants.TURRETCLOSECONSTANT * Math.abs(TurretError - result));
//        }
//    }

    public static void cameraSetLaunch(double speed) {
        if (result != null) {
            if (result.isValid()) {
                if (result.getTa() < Constants.CLOSESHOT_TA) {
                    Hood.setToAngle(Constants.FARSHOTHOODSERVO);
                    Shoota.setSpeed(speed);
                    Shoota.currentTurretTolerance = 2;
                    CheckSpeed(speed);
                } else if (result.getTa() >= Constants.CLOSESHOT_TA) {
                    double distance = Shoota.distanceToGoal(result.getTy());
                    Shoota.setSpeed(Shoota.getSpeeds(distance));
                    Hood.setToAngle(Shoota.gethoodAngle(distance));
                    currentTurretTolerance = turretTolerance(distance);
                    CheckSpeed(Shoota.getSpeeds(distance));
                }
                NotInPos = result.getTx() >= currentTurretTolerance || result.getTx() <= -currentTurretTolerance;
            } else {
                Hood.setToAngle(Constants.FARSHOTHOODSERVO);
                Shoota.setSpeed(Constants.FARSHOTSPEED);
            }
        }
    }

    //TODO: distance from goal using bot position
    public static double launchRPM() {
        double hoodAngle = Hood.getCurrentAngle();
        double distance = Shoota.distanceToGoal(result.getTy());
        return (60 / (Math.PI * 3.78)) * Math.sqrt((386.0885 * Math.pow(distance, 2)) / 2 * Math.pow(Math.cos(hoodAngle), 2) * distance * Math.tan(hoodAngle) - Constants.SHOTHEIGHT);
    }

    public static void cameraSetPinPoint() {
        double X = result.getBotpose_MT2().getPosition().x;
        double Y = result.getBotpose_MT2().getPosition().y;
        HeadingTracker.setPinPointXY(X, Y);
    }
}