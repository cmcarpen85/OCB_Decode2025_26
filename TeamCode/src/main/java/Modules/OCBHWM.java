package Modules;

import com.arcrobotics.ftclib.command.OdometrySubsystem;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.arcrobotics.ftclib.kinematics.HolonomicOdometry;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
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

    //drivetrain
    static final double TRACKWIDTH = 11.881;
    static final double WHEEL_DIAMETER = 1.259;    // inches
    static double TICKS_TO_INCHES;
    static final double CENTER_WHEEL_OFFSET = 1.644;
    public static HolonomicOdometry m_robotOdometry;
    public static OdometrySubsystem m_odometry;
    public static MecanumDrive m_robotDrive;
    public static MotorEx leftEncoder, rightEncoder, centerEncoder;

    //Servos
    public static Servo turretServo;
    public static Servo hoodServo;
    public static CRServo gateServo;
    public static CRServo kickerServo;

    //Sensors/Gains
    public static Limelight3A limelight;

    public static AnalogInput turretFeedback;

    public static AnalogInput hoodFeedback;

    public static double kP = 20;
    public static double kV = 0.7;
    public static RevIMU imu;
    public HardwareMap hardwareMap;


    public static void hwinit(HardwareMap hardwareMap) {

        imu = new RevIMU(hardwareMap, "imu");
        leftFront = hardwareMap.get(Motor.class, "leftFront");
        rightFront = hardwareMap.get(Motor.class, "rightFront");
        leftBack = hardwareMap.get(Motor.class, "leftBack");
        rightBack = hardwareMap.get(Motor.class, "rightBack");
        m_robotDrive = new MecanumDrive(OCBHWM.leftFront, OCBHWM.rightFront, OCBHWM.leftBack, OCBHWM.rightBack);

        leftEncoder = new MotorEx(hardwareMap, "rightFront");
        rightEncoder = new MotorEx(hardwareMap, "leftBack");
        centerEncoder = new MotorEx(hardwareMap, "rightBack");

        // calculate multiplier
        TICKS_TO_INCHES = WHEEL_DIAMETER * Math.PI / leftEncoder.getCPR();

        // create our odometry object and subsystem
        m_robotOdometry = new HolonomicOdometry(
                () -> leftEncoder.getCurrentPosition() * TICKS_TO_INCHES,
                () -> rightEncoder.getCurrentPosition() * TICKS_TO_INCHES,
                () -> centerEncoder.getCurrentPosition() * TICKS_TO_INCHES,
                TRACKWIDTH, CENTER_WHEEL_OFFSET
        );
        m_odometry = new OdometrySubsystem(m_robotOdometry);

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
        gateServo = hardwareMap.get(CRServo.class, "gateServo");
        kickerServo = hardwareMap.get(CRServo.class, "kickerServo");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }
}
