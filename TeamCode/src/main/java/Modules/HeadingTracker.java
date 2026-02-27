package Modules;

import com.arcrobotics.ftclib.geometry.Pose2d;

import org.opencv.core.Mat;

public class HeadingTracker {
    public double robotOri;
    public double turretOri;
    public double robotX;
    public double robotY;
    public static double redGoalX = 0;
    public static double redGoalY = 0;
    public static double blueGoalX = 0;
    public static double blueGoalY = 0;

    public void initialize(double startX, double startY, double startOri) {
        robotX = startX;
        robotY = startY;
        robotOri = startOri;
    }

    public static void drivetrainDistanceBlue() {
        Pose2d currentPos = OCBHWM.m_odometry.getPose();
        double XDistance = Math.abs(blueGoalX - currentPos.getX());
        double YDistance = Math.abs(blueGoalY - currentPos.getY());
        double robotDistance = Math.sqrt(Math.pow(XDistance,2) + Math.pow(YDistance,2));
        double angleToGoal = Math.asin(YDistance/robotDistance);
//        currentPos.getHeading() = angleToGoal;
    }


}
