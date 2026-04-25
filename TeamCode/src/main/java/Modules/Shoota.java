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
    public static double CurrentShootPower = 0;
    public static double currentTurretTolerance = 1;
    public static boolean NotInPos = true;
    public static boolean Force = false;
    public static double cameraSign = 0;
    public static LLResult result;
    public static double shotHeight = 0;
    public static double distance = 0;

    static LUT<Double, Double> speeds = new LUT<Double, Double>() {{
        //Close Zone
        add(47.0 , 0.3978);
        add(48.0 , 0.401);
        add(52.0 , 0.4028);
        add(56.0 , 0.4074);
        add(61.0 , 0.4142);
        add(65.0 , 0.428);
        add(70.0 , 0.4481);
        add(75.0 , 0.458);
        add(80.0 , 0.468);
        add(85.0 , 0.480);
        add(89.0 , 0.4931);
        add(93.0 , 0.519);
        add(94.0 , 0.5206);
        add(98.0 , 0.539);
        add(101.0, 0.5488);
        add(104.0, 0.5632);
        add(110.0, 0.568);
        add(115.0, 0.5695);
        add(119.0,0.5735);
        // Far Zone
        add(129.0, 0.5706); // 0.59
        add(130.0, 0.581); // 0.59
        add(134.0, 0.585); // 0.598
        add(135.0, 0.588); // 0.59
        add(138.0, 0.599); // 0.608
        add(141.0, 0.599); // 0.59
        add(143.0, 0.605); // 0.608
        add(146.0, 0.610); // 0.6122
        add(148.0, 0.612); // 0.6073
        add(150.0, 0.6122); // 0.6285
        add(154.0, 0.615); // 0.63
        add(156.0, 0.620); // 0.59
        add(158.0, 0.6314); // 0.6314
        add(160.0, 0.6314); // 0.6314
    }};

    public static double getSpeeds(double distance) {
        return speeds.getClosest(distance);
    }

    static LUT<Double, Double> hoodAngle = new LUT<Double, Double>() {{
        //Close Zone
        add(47.0 , 0.2128);
        add(48.0 , 0.417);
        add(52.0 , 0.4233);
        add(56.0 , 0.45);
        add(61.0 , 0.4983);
        add(65.0 , 0.55);
        add(70.0 , 0.5989);
        add(75.0 , 0.631);
        add(80.0 , 0.76);
        add(85.0 , 0.739);
        add(89.0 , 0.7228);
        add(93.0 , 0.76);
        add(94.0 , 0.905);
        add(98.0 , 0.905);
        add(101.0, 0.905);
        add(104.0, 0.905);
        add(110.0, 0.905);
        add(115.0, 0.905);

        // Far Zone
        add(130.0, 0.905); // 0.480
        add(134.0, 0.905); // 0.521
        add(138.0, 0.905); // 0.525
        add(143.0, 0.905); // 0.530
        add(146.0, 0.905); // 0.539
        add(147.0, 0.905); // 0.546
        add(150.0, 0.905); // 0.555
        add(154.0, 0.905); // 0.555
    }};

    public static double gethoodAngle(double distance) {
        return hoodAngle.getClosest(distance);
    }


    //Flywheel
    public static void setSpeed(double speed) {
        OCBHWM.flywheel.set(speed);
        CurrentShootPower = speed;
    }

    public static void stop() {
        OCBHWM.flywheel.stopMotor();
        CurrentShootPower = 0;
    }

    public static void coast() {
        OCBHWM.flywheel.set(Constants.COASTSPEED);
        CurrentShootPower = Constants.COASTSPEED;
    }

    public static void checkLimelight() {
        result = OCBHWM.limelight.getLatestResult();
    }
    public static void LimelightOffsetBlue(){
        checkLimelight();
        if (result != null) {
            if (result.isValid()) {
            double tx = result.getTx();
            HeadingTracker.limelightOffset =  Constants.LimelightBlue - tx ;
            }
        }
    }

    public static void LimelightOffsetRed(){
        checkLimelight();
        if (result != null) {
            if (result.isValid()) {
                double tx = result.getTx();
                HeadingTracker.limelightOffset =  Constants.LimelightRed - tx ;
            }
        }
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
                if (-1 * result.getTx() - TurretError > Constants.TURRETANGLEROUGHTOLERANCE) {
                    Turret.addAngle(Math.abs(-1 * result.getTx() - TurretError));
                } else if (-1 * result.getTx() - TurretError < -Constants.TURRETANGLEROUGHTOLERANCE) {
                    Turret.subtractAngle(Math.abs(-1 * result.getTx() - TurretError));
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
        }
    }

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
        HeadingTracker.setPinPointXY(X+HeadingTracker.turretShiftX, Y+HeadingTracker.turretShiftY);
    }
}