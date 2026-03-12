package Modules;

import com.arcrobotics.ftclib.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.opencv.core.Mat;

import java.util.function.ToDoubleBiFunction;

public class HeadingTracker {
    public double robotOri;
    public double turretOri;
    public double robotX;
    public double robotY;
    public static double redGoalX = 70.55;
    public static double redGoalY = -61;
    public static double blueGoalX = 70.55;
    public static double blueGoalY = 61;

    public void initialize(double startX, double startY, double startOri) {
        robotX = startX;
        robotY = startY;
        robotOri = startOri;
    }

    // TODO add * -1 to one of the two
    public static void headingTrackingBlue() {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(blueGoalX - currentPos.getX(DistanceUnit.INCH));
        double YDistance = Math.abs(blueGoalY - currentPos.getY(DistanceUnit.INCH));
        double robotDistance = Math.sqrt(Math.pow(XDistance, 2) + Math.pow(YDistance, 2));
        double angleToGoal = -1* Math.asin(YDistance / robotDistance) * 180 / Math.PI;
        Shoota.gyroAdjustTurret(angleToGoal);
//        return headingDifference(angleToGoal);
    }

    public static void headingTrackingRed() {
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(redGoalX - currentPos.getX(DistanceUnit.INCH));
        double YDistance = Math.abs(redGoalY - currentPos.getY(DistanceUnit.INCH));
        double robotDistance = Math.sqrt(Math.pow(XDistance, 2) + Math.pow(YDistance, 2));
        double angleToGoal = Math.asin(YDistance / robotDistance) * 180 / Math.PI;
        Shoota.gyroAdjustTurret(angleToGoal);
//        return headingDifference(angleToGoal);
    }

    public static double gyroDifference() {
        double pinPointHeading = OCBHWM.pinPoint.getHeading(AngleUnit.DEGREES);
        double turretHeading = OCBHWM.imu.getRobotYawPitchRollAngles().getYaw();
        if (Math.abs(pinPointHeading) < 90 && Math.abs(turretHeading) < 90) {
            return pinPointHeading - turretHeading;
        } else if (Math.abs(pinPointHeading) >= 90) {
            double opAngle = -1 * (180 - Math.abs(pinPointHeading) * Math.signum(pinPointHeading));
            if ((180 - Math.abs(turretHeading)) * (Math.signum(turretHeading)) >= opAngle) {
                return pinPointHeading - turretHeading;
            } else {
                return (180 - Math.abs(turretHeading)) * Math.signum(turretHeading) - (180 - Math.abs(pinPointHeading)) * Math.signum(pinPointHeading);
            }
        } else {
            return (pinPointHeading - turretHeading);
        }
    }

    public static double headingDifference(double desiredHeading) {
        double turretHeading = OCBHWM.imu.getRobotYawPitchRollAngles().getYaw();
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
}
