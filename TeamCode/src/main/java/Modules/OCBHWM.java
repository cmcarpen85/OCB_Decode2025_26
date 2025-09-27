package Modules;

import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class OCBHWM {

    public static Rev9AxisImu imu;
    public static DcMotorEx leftFront;
    public static DcMotorEx rightFront;
    public static DcMotorEx rightBack;
    public static DcMotorEx leftBack;
    public static DcMotorEx shootaL;
    public static DcMotorEx shootaR;
    public static DcMotorEx transferM;
    public static DcMotorEx intakeM;
    public HardwareMap hardwareMap;

    public static void hwinit(HardwareMap hardwareMap) {

        imu = hardwareMap.get(Rev9AxisImu.class, "imu");
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");

        shootaL = hardwareMap.get(DcMotorEx.class, "shootaL");
        shootaR = hardwareMap.get(DcMotorEx.class, "shootaR");

        transferM = hardwareMap.get(DcMotorEx.class, "transferM");

        intakeM = hardwareMap.get(DcMotorEx.class, "transferM");

    }
}
