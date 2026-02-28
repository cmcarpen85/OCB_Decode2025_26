package Modules;

import com.arcrobotics.ftclib.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
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
        Pose2D currentPos = OCBHWM.pinPoint.getPosition();
        double XDistance = Math.abs(blueGoalX - currentPos.getX(DistanceUnit.INCH));
        double YDistance = Math.abs(blueGoalY - currentPos.getY(DistanceUnit.INCH));
        double robotDistance = Math.sqrt(Math.pow(XDistance,2) + Math.pow(YDistance,2));
        double angleToGoal = Math.asin(YDistance/robotDistance);
//        currentPos.getHeading() = angleToGoal;
    }

    public static double gyroDifference() {
       return OCBHWM.pinPoint.getHeading(AngleUnit.DEGREES)-OCBHWM.imu.getRobotYawPitchRollAngles().getYaw();
    }

}
