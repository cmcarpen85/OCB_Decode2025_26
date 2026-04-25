package Modules;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.util.LUT;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.BlueFarAutoWorlds;
import org.firstinspires.ftc.teamcode.PinpointLocalizer;
import org.opencv.core.Mat;

import java.util.function.ToDoubleBiFunction;

@Config
public class HeadingTracker {

    public double turretOri;
    public static double robotStartX = 0;
    public static double robotStartY = 0;
    public static double robotStartOri = 0;
    public static double redGoalX = 70.16;//63.84
    public static double redGoalY = -57.8;//-58.8
    public static double blueGoalX = 70.16;//63.84
    public static double blueGoalY = 57.8;//-58.8

    public static double turretShiftDistance = 2.215;// in Inches

    public static double turretShiftX = 0;
    public static double turretShiftY = 0;
    public static double distanceToGoal = 0;
    public static double visionAimOffset = 0;
    public static double aimOffset = 0;
    public static double aimOffsetGAIN = 0.018;
    public static double manualAimOffset = 0;
    public static double limelightOffset = 0;
    public static double hDAimOffset = 0;
    public static double hDPowerOffset = 0;
    public static double headingDiff = 0;
    public static boolean FlywheelEnable = false;

    static LUT<Double, Double> XaimOffset = new LUT<Double, Double>() {{
        //Close Zone
        add(0.0, 3.0);
        add(5.0, 3.0);
        add(10.0, 3.0);
        add(15.0, 3.5);
        add(20.0, 4.0);
        add(25.0, 4.5);
        add(30.0, 5.0);
        add(35.0, 6.0);
        add(40.0, 6.25);
        add(45.0, 6.5);
        add(50.0, 6.5);
        add(55.0, 6.75);
        add(60.0, 7.0);
        add(65.0, 7.5);
        add(70.0, 7.75);
        add(75.0, 8.0);
        add(80.0, 0.0);

    }};

    public static double getAimOffset(double Xdistance) {
        return XaimOffset.getClosest(Xdistance);
    }


    public static void initializePinPoint(double startX, double startY, double startOri) {
        OCBHWM.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH, startX, startY, AngleUnit.DEGREES, startOri));
    }

    public static void setPinPointXY(double X, double Y) {
        OCBHWM.pinPoint.setPosX(X, DistanceUnit.INCH);
        OCBHWM.pinPoint.setPosY(Y, DistanceUnit.INCH);
    }

    public static void headingTrackingBlue(boolean enableFlywheel) {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(blueGoalX - (currentPos.getX(DistanceUnit.INCH) + robotStartX));
        double YDistance = Math.abs(blueGoalY - (currentPos.getY(DistanceUnit.INCH) + robotStartY));
        setTurretShiftOffsets();
        double robotDistance = Math.sqrt(Math.pow(XDistance + turretShiftX, 2) + Math.pow(YDistance + turretShiftY, 2));
        HeadingTracker.distanceToGoal = robotDistance;
        double angleToGoal = Math.asin((YDistance + turretShiftY) / robotDistance) * 180 / Math.PI;
//         HeadingTracker.headingDiff = headingDifferenceBase(angleToGoal);
//        setLaunchOffsets(headingDiff);
        double currentXAimOffset = HeadingTracker.getAimOffset(XDistance + turretShiftX);
        Shoota.gyroAdjustTurret(angleToGoal + manualAimOffset + currentXAimOffset);
        if (enableFlywheel) {
            Shoota.setSpeed(Shoota.getSpeeds(robotDistance));
        }
        if (robotDistance > Constants.FARSHOTDISTANCE) {
            Hood.setToAngle(Constants.FARSHOTHOODSERVO);
            Transfer.TransferShootPower = Constants.FARSHOTTRANSFERPOWER;
        } else {
            Transfer.TransferShootPower = 1;
            Hood.setToAngle(Shoota.gethoodAngle(robotDistance));
        }
    }

    public static void headingTrackingRed(boolean enableFlywheel) {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(redGoalX - (currentPos.getX(DistanceUnit.INCH) + robotStartX));
        double YDistance = Math.abs(redGoalY - (currentPos.getY(DistanceUnit.INCH) + robotStartY));
        setTurretShiftOffsets();
        double robotDistance = Math.sqrt(Math.pow(XDistance + turretShiftX, 2) + Math.pow(YDistance + turretShiftY, 2));
        HeadingTracker.distanceToGoal = robotDistance;
        double angleToGoal = -1 * Math.asin((YDistance + turretShiftY) / robotDistance) * 180 / Math.PI;
        double currentXAimOffset = HeadingTracker.getAimOffset(XDistance + turretShiftX);
        Shoota.gyroAdjustTurret(angleToGoal + manualAimOffset + -1 * currentXAimOffset);
        if (enableFlywheel) {
            Shoota.setSpeed(Shoota.getSpeeds(robotDistance));
        }
        if (robotDistance > Constants.FARSHOTDISTANCE) {
            Hood.setToAngle(Constants.FARSHOTHOODSERVO);
            Transfer.TransferShootPower = Constants.FARSHOTTRANSFERPOWER;
        } else {
            Transfer.TransferShootPower = 1;
            Hood.setToAngle(Shoota.gethoodAngle(robotDistance));
        }
    }

    public static void headingTrackingBlueAuto(boolean enableFlywheel) {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(blueGoalX - (currentPos.getX(DistanceUnit.INCH) + robotStartX));
        double YDistance = Math.abs(blueGoalY - (currentPos.getY(DistanceUnit.INCH) + robotStartY));
        setTurretShiftOffsets();
        double robotDistance = Math.sqrt(Math.pow(XDistance + turretShiftX, 2) + Math.pow(YDistance + turretShiftY, 2));
        HeadingTracker.distanceToGoal = robotDistance;
        double angleToGoal = Math.asin(YDistance / robotDistance) * 180 / Math.PI;
        double currentXAimOffset = HeadingTracker.getAimOffset(XDistance + turretShiftX);
        Shoota.gyroAdjustTurret(angleToGoal + manualAimOffset + currentXAimOffset);
        if (enableFlywheel) {
//        Shoota.setSpeed(Shoota.getSpeeds(robotDistance)+hDPowerOffset);
            Shoota.setSpeed(Shoota.getSpeeds(robotDistance));
        }
        if (robotDistance > Constants.FARSHOTDISTANCE) {
            Hood.setToAngle(Constants.FARSHOTHOODSERVO);
            Transfer.TransferShootPower = Constants.FARSHOTTRANSFERPOWER;
        } else {
            Transfer.TransferShootPower = 1;
            Hood.setToAngle(Shoota.gethoodAngle(robotDistance));
        }
    }

    public static void setPinpointStart(double X, double Y) {
        robotStartX = X;
        robotStartY = Y;
    }

    public static double gyroDifference() {
        double pinPointHeading = OCBHWM.pinPoint.getHeading(AngleUnit.DEGREES);
        double turretHeading = OCBHWM.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        return turretHeading - pinPointHeading;
//        if (Math.abs(pinPointHeading) < 90 && Math.abs(turretHeading) < 90) {
//            return pinPointHeading - turretHeading;
//        } else if (Math.abs(pinPointHeading) >= 90) {
//            double opAngle = -1 * (180 - Math.abs(pinPointHeading) * Math.signum(pinPointHeading));
//            if ((180 - Math.abs(turretHeading)) * (Math.signum(turretHeading)) >= opAngle) {
//                return pinPointHeading - turretHeading;
//            } else {
//                return (180 - Math.abs(turretHeading)) * Math.signum(turretHeading) - (180 - Math.abs(pinPointHeading)) * Math.signum(pinPointHeading);
//            }
//        } else {
//            return (pinPointHeading - turretHeading);
//        }
    }

    public static double headingDifferenceTurret(double desiredHeading) {
        double turretHeading = OCBHWM.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        if (Math.abs(desiredHeading) < 90 && Math.abs(turretHeading) < 90) {
            return desiredHeading - turretHeading;
        } else if (Math.abs(desiredHeading) >= 90) {
            double opAngle = -1 * (180 - Math.abs(desiredHeading) * Math.signum(desiredHeading));
            if ((180 - Math.abs(turretHeading)) * (Math.signum(turretHeading)) >= opAngle) {
                return desiredHeading - turretHeading;
            } else {
                return (180 - Math.abs(turretHeading)) * Math.signum(turretHeading) - (180 - Math.abs(desiredHeading)) * Math.signum(desiredHeading);
            }
        } else {
            return (desiredHeading - turretHeading);
        }
    }

    public static double headingDifferenceBase(double desiredHeading) {
        double BaseHeading = OCBHWM.pinPoint.getHeading(AngleUnit.DEGREES);
        double desiredHeadingPlus180 = desiredHeading - 180;

        if (Math.signum(desiredHeading) == Math.signum(BaseHeading)) {
            return desiredHeading - BaseHeading;
        } else if (Math.signum(desiredHeading) != Math.signum(BaseHeading) && Math.abs(BaseHeading) <= Math.abs(desiredHeadingPlus180)) {
            return desiredHeading - BaseHeading;
        } else {
            return 360 - Math.abs(desiredHeading - BaseHeading);
        }
    }

    public static void setLaunchOffsets(double headingDifference) {
        hDPowerOffset = (Math.abs(headingDifference) * Constants.MAXHDPOWER) / 180;
        hDAimOffset = Math.abs(90 - Math.abs(headingDifference)) * Math.signum(headingDifference) * Constants.MAXHDAIM / 90;
    }

    public static void setTurretShiftOffsets() {
        double pinPointHeading = OCBHWM.pinPoint.getHeading(AngleUnit.RADIANS);
        turretShiftY = -1 * Math.sin(pinPointHeading) * turretShiftDistance;
        turretShiftX = -1 * Math.cos(pinPointHeading) * turretShiftDistance;
    }

}
