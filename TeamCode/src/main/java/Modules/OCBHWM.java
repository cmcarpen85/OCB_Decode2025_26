package Modules;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class OCBHWM {

    public static Rev9AxisImu imu;
    public static Motor leftFront;
    public static Motor rightFront;
    public static Motor rightBack;
    public static Motor leftBack;
    public static Motor shootaL;
    public static Motor shootaR;
    public static Motor transferM;
    public static Motor intakeM;
    public static MotorGroup flyWheel;
    public HardwareMap hardwareMap;

    public static double kP = 20;
    public static double kV = 0.7;

    public static void hwinit(HardwareMap hardwareMap) {

        imu = hardwareMap.get(Rev9AxisImu.class, "imu");
        leftFront = hardwareMap.get(Motor.class, "leftFront");
        rightFront = hardwareMap.get(Motor.class, "rightFront");
        leftBack = hardwareMap.get(Motor.class, "leftBack");
        rightBack = hardwareMap.get(Motor.class, "rightBack");

        flyWheel = new MotorGroup(
                new Motor(hardwareMap, "shootaL", Motor.GoBILDA.BARE),
                new Motor(hardwareMap, "shootaL", Motor.GoBILDA.BARE)
        );

        flyWheel.setRunMode(Motor.RunMode.VelocityControl);
        flyWheel.setVeloCoefficients(kP, 0, 0);
        flyWheel.setFeedforwardCoefficients(0, kV);

        transferM = hardwareMap.get(Motor.class, "transferM");

        intakeM = hardwareMap.get(Motor.class, "transferM");

    }
}
