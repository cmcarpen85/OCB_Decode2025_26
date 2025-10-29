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
    public static Servo indLight;
    public static CRServo gateServo;
    public static CRServo gateServo2;
    public static CRServo kickerServo;

    //Sensors/Gains
    //public static Limelight3A limelight;

    public static AnalogInput turretFeedback;

    public static AnalogInput hoodFeedback;
    public static AnalogInput transferClear;

    public static double kP = 20;
    public static double kV = 0.7;
    public static RevIMU imu;
    public HardwareMap hardwareMap;


    public static void hwinit(HardwareMap hardwareMap) {

        imu = new RevIMU(hardwareMap, "imu");
        leftFront = new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_435);
        rightFront = new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_435);
        leftBack = new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_435);
        rightBack = new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_435);
        m_robotDrive = new MecanumDrive(OCBHWM.leftFront, OCBHWM.rightFront, OCBHWM.leftBack, OCBHWM.rightBack);

        leftEncoder = new MotorEx(hardwareMap, "leftFront");
        rightEncoder = new MotorEx(hardwareMap, "rightFront");
        centerEncoder = new MotorEx(hardwareMap, "leftBack");

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
        flywheelR.setInverted(true);


        transferM = hardwareMap.get(DcMotor.class, "transferM");
        transferM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transferM.setPower(0);
        transferM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeM = hardwareMap.get(DcMotor.class, "intakeM");
        intakeM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeM.setPower(0);
        intakeM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        turretServo = hardwareMap.get(Servo.class, "turretServo");
        turretFeedback = hardwareMap.get(AnalogInput.class, "turretFeedback");

        indLight = hardwareMap.get(Servo.class, "indLight");

        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
        hoodFeedback = hardwareMap.get(AnalogInput.class, "hoodFeedback");
        transferClear = hardwareMap.get(AnalogInput.class, "transferClear");

        gateServo = hardwareMap.get(CRServo.class, "gateServo");
        gateServo.setDirection(DcMotorSimple.Direction.REVERSE);
        gateServo2 = hardwareMap.get(CRServo.class, "gateServo2");


        kickerServo = hardwareMap.get(CRServo.class, "kickerServo");
        kickerServo.setDirection(DcMotorSimple.Direction.REVERSE);

        //limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }
}
