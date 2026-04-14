package Modules;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.opencv.core.Mat;

import java.util.function.ToDoubleBiFunction;
@Config
public class HeadingTracker {
    public double robotOri;
    public double turretOri;
    public double robotX;
    public double robotY;
    public static double redGoalX = 63.84;//70.55
    public static double redGoalY = -58.8;//-57.64
    public static double blueGoalX = 63.84;//70.55
    public static double blueGoalY = 58.8;//57.64
    public static double distanceToGoal = 0;
    public static double aimOffset = 0;
    public static double aimOffsetGAIN = 0.018;
    public static double manualAimOffset = 0;

    public static void initializePinPoint(double startX, double startY, double startOri) {
        OCBHWM.pinPoint.setPosition(new Pose2D(DistanceUnit.INCH,startX, startY, AngleUnit.DEGREES, startOri));
    }

    public static void setPinPointXY(double X, double Y) {
        OCBHWM.pinPoint.setPosX(X, DistanceUnit.INCH);
        OCBHWM.pinPoint.setPosY(Y, DistanceUnit.INCH);
    }

    public static void headingTrackingBlue(boolean enableFlywheel) {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(blueGoalX - currentPos.getX(DistanceUnit.INCH));
        double YDistance = Math.abs(blueGoalY - currentPos.getY(DistanceUnit.INCH));
        double robotDistance = Math.sqrt(Math.pow(XDistance, 2) + Math.pow(YDistance, 2));
        HeadingTracker.distanceToGoal = robotDistance;
        double angleToGoal =  Math.asin(YDistance / robotDistance) * 180 / Math.PI;
//        HeadingTracker.aimOffset = aimOffsetGAIN*Math.max(Math.abs(angleToGoal-currentPos.getHeading(AngleUnit.DEGREES)),90)*Math.signum(angleToGoal-currentPos.getHeading(AngleUnit.DEGREES));
        Shoota.gyroAdjustTurret(angleToGoal+manualAimOffset);
        if (enableFlywheel){
        Shoota.setSpeed(Shoota.getSpeeds(robotDistance));
        }
        if (robotDistance>Constants.FARSHOTDISTANCE){
            Hood.setToAngle(Constants.FARSHOTHOODSERVO);
            Transfer.TransferShootPower=Constants.FARSHOTTRANSFERPOWER;
        } else{
            Transfer.TransferShootPower=1;
            Hood.setToAngle(Shoota.gethoodAngle(robotDistance));
        }
//        return headingDifference(angleToGoal);
    }

    public static void headingTrackingRed(boolean enableFlywheel) {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(redGoalX - currentPos.getX(DistanceUnit.INCH));
        double YDistance = Math.abs(redGoalY - currentPos.getY(DistanceUnit.INCH));
        double robotDistance = Math.sqrt(Math.pow(XDistance, 2) + Math.pow(YDistance, 2));
        HeadingTracker.distanceToGoal = robotDistance;
        double angleToGoal = -1* Math.asin(YDistance / robotDistance) * 180 / Math.PI;
//        HeadingTracker.aimOffset = -aimOffsetGAIN*Math.max(Math.abs(angleToGoal-currentPos.getHeading(AngleUnit.DEGREES)),90)*Math.signum(angleToGoal-currentPos.getHeading(AngleUnit.DEGREES));
        Shoota.gyroAdjustTurret(angleToGoal+manualAimOffset);
        if (enableFlywheel){
            Shoota.setSpeed(Shoota.getSpeeds(robotDistance));
        }
        if (robotDistance>Constants.FARSHOTDISTANCE){
            Hood.setToAngle(Constants.FARSHOTHOODSERVO);
            Transfer.TransferShootPower=Constants.FARSHOTTRANSFERPOWER;
        } else{
            Transfer.TransferShootPower=1;
            Hood.setToAngle(Shoota.gethoodAngle(robotDistance));
        }
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
        double desiredHeadingPlus180 = desiredHeading-180;

        if (Math.signum(desiredHeading) == Math.signum(BaseHeading)){
            return desiredHeading-BaseHeading;
        } else if (Math.signum(desiredHeading) != Math.signum(BaseHeading) && Math.abs(BaseHeading)>=Math.abs(desiredHeadingPlus180) ){
            return desiredHeading - BaseHeading;
        } else if (Math.signum(desiredHeading) != Math.signum(BaseHeading) && Math.abs(BaseHeading)>=Math.abs(desiredHeadingPlus180) ){
            return desiredHeading - BaseHeading;
        }




        return desiredHeading - BaseHeading;

    }
}
