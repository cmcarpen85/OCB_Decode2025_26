package Modules;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class OCBHWM {

    //Motors
    public static Motor leftFront;
    public static Motor rightFront;
    public static Motor rightBack;
    public static Motor leftBack;
    public static Motor flywheelL;
    public static Motor flywheelR;
    public static MotorGroup flywheel;
    public static DcMotor transferM;
    public static DcMotor intakeM;

    //Servos
    public static Servo turretServo;
    public static Servo hoodServo;

    public static double kP = 20;
    public static double kV = 0.7;
    public static Rev9AxisImu imu;
    public HardwareMap hardwareMap;


    public static void hwinit(HardwareMap hardwareMap) {

        imu = hardwareMap.get(Rev9AxisImu.class, "imu");
        leftFront = hardwareMap.get(Motor.class, "leftFront");
        rightFront = hardwareMap.get(Motor.class, "rightFront");
        leftBack = hardwareMap.get(Motor.class, "leftBack");
        rightBack = hardwareMap.get(Motor.class, "rightBack");

        flywheel = new MotorGroup(
                flywheelL = new Motor(hardwareMap, "flywheelL", Motor.GoBILDA.BARE),
                flywheelR = new Motor(hardwareMap, "flywheelR", Motor.GoBILDA.BARE)
        );
        flywheel.setRunMode(Motor.RunMode.VelocityControl);
        flywheel.setVeloCoefficients(kP, 0, 0);
        flywheel.setFeedforwardCoefficients(0, kV);
        flywheelL.setInverted(true);

        transferM = hardwareMap.get(DcMotor.class, "transferM");
        transferM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transferM.setPower(0);
        transferM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeM = hardwareMap.get(DcMotor.class, "intakeM");
        intakeM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeM.setPower(0);
        intakeM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        turretServo = hardwareMap.get(Servo.class, "turretServo");
        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
    }
}
