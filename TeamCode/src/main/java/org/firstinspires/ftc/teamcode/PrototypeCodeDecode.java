package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
public class PrototypeCodeDecode extends LinearOpMode {

    // This variable determines whether the following program
    // uses field-centric or robot-centric driving styles. The
    // differences between them can be read here in the docs:
    // https://docs.ftclib.org/ftclib/features/drivebases#control-scheme
    static final boolean FIELD_CENTRIC = true;
    public static Servo TestServo1;
    public static Servo TestServo2;
    public static DcMotor TestMotor1;
    public static DcMotor TestMotor2;
    public static CRServo CRTest1;
    public static CRServo CRTest2;

    public static double MotorPower1 = 0.5;
    public static double MotorPower2 = 0.5;
    public static double ServoPower1 = 0.5;
    public static double ServoPower2 = 0.5;
    public static double CRPower1;
    public static double CRPower2;


    @Override
    public void runOpMode() throws InterruptedException {
        // constructor takes in frontLeft, frontRight, backLeft, backRight motors
        // IN THAT ORDER
//        MecanumDrive drive = new MecanumDrive(true,
//                new Motor(hardwareMap, "leftFront", Motor.GoBILDA.RPM_435),
//                new Motor(hardwareMap, "rightFront", Motor.GoBILDA.RPM_435),
//                new Motor(hardwareMap, "leftBack", Motor.GoBILDA.RPM_435),
//                new Motor(hardwareMap, "rightBack", Motor.GoBILDA.RPM_435)
//        );

        TestServo2 = hardwareMap.get(Servo.class, "TestServo2");
        TestServo1 = hardwareMap.get(Servo.class, "TestServo1");
        TestMotor1 = hardwareMap.get(DcMotor.class, "TestMotor1");
        TestMotor2 = hardwareMap.get(DcMotor.class, "TestMotor2");
        CRTest1 = hardwareMap.get(CRServo.class, "CRTest1");
        CRTest2 = hardwareMap.get(CRServo.class, "CRTest2");

        TestMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        TestMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TestMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        TestMotor1.setTargetPosition(0);
        TestMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        TestMotor1.setPower(0);

        TestMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
        TestMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TestMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        TestMotor2.setTargetPosition(0);
        TestMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        TestMotor2.setPower(0);

        CRTest1.setDirection(CRServo.Direction.FORWARD);
        CRTest2.setDirection(CRServo.Direction.REVERSE);


        // This is the built-in IMU in the REV hub.
        // We're initializing it by its default parameters
        // and name in the config ('imu'). The orientation
        // of the hub is important. Below is a model
        // of the REV Hub and the orientation axes for the IMU.
        //
        //                           | Z axis
        //                           |
        //     (Motor Port Side)     |   / X axis
        //                       ____|__/____
        //          Y axis     / *   | /    /|   (IO Side)
        //          _________ /______|/    //      I2C
        //                   /___________ //     Digital
        //                  |____________|/      Analog
        //
        //                 (Servo Port Side)
        //
        // (unapologetically stolen from the road-runner-quickstart)

//        RevIMU imu = new RevIMU(hardwareMap);
//        imu.init();

        // the extended gamepad object
        GamepadEx driverOp = new GamepadEx(gamepad1);

        waitForStart();

        while (!isStopRequested()) {


            if (gamepad1.left_trigger > 0.4) {
                TestMotor1.setPower(MotorPower1);
                TestMotor2.setPower(MotorPower2);
            } else if (gamepad1.right_trigger > 0.4) {
                TestMotor1.setPower(-MotorPower1);
                TestMotor2.setPower(-MotorPower2);
            } else {
                TestMotor1.setPower(0);
                TestMotor2.setPower(0);
            }


            if (gamepad1.left_bumper) {
                CRTest1.setPower(ServoPower1);
                CRTest2.setPower(ServoPower2);
            } else if (gamepad1.right_bumper) {
                CRTest1.setPower(-ServoPower1);
                CRTest2.setPower(-ServoPower2);
            } else
                CRTest1.setPower(0);
            CRTest2.setPower(0);


        if (gamepad1.dpad_up && MotorPower1 < 1) {
            MotorPower1 = MotorPower1 + .0001;
            MotorPower2 = MotorPower2 + .0001;
        } else if (gamepad1.dpad_down && MotorPower1 > 0) {
            MotorPower1 = MotorPower1 - .0001;
            MotorPower2 = MotorPower2 - .0001;
        }

        if(gamepad1.y && ServoPower1<1){
            ServoPower1=ServoPower1+.0001;
            ServoPower2=ServoPower2+.0001;
        } else if (gamepad1.a && ServoPower1>0) {
            ServoPower1=ServoPower1-.0001;
            ServoPower2=ServoPower2-.0001;
        }


        telemetry.addData("Motor 1 power", MotorPower1);
        telemetry.addData("Motor 2 power", MotorPower2);
        telemetry.addData("Servo 1 power",ServoPower1);
        telemetry.addData("Servo 2 power",ServoPower2);



        telemetry.update();


        // Driving the mecanum base takes 3 joystick parameters: leftX, leftY, rightX.
        // These are related to the left stick x value, left stick y value, and
        // right stick x value respectively. These values are passed in to represent the
        // strafing speed, the forward speed, and the turning speed of the robot frame
        // respectively from [-1, 1].

//            if (!FIELD_CENTRIC) {
//
//                // For a robot centric model, the input of (0,1,0) for (leftX, leftY, rightX)
//                // will move the robot in the direction of its current heading. Every movement
//                // is relative to the frame of the robot itself.
//                //
//                //                 (0,1,0)
//                //                   /
//                //                  /
//                //           ______/_____
//                //          /           /
//                //         /           /
//                //        /___________/
//                //           ____________
//                //          /  (0,0,1)  /
//                //         /     ↻     /
//                //        /___________/
//
//                // optional fourth parameter for squared inputs
//                drive.driveRobotCentric(
//                        driverOp.getLeftX(),
//                        driverOp.getLeftY(),
//                        -driverOp.getRightX(),
//                        false
//                );
//            } else {
//
//                // Below is a model for how field centric will drive when given the inputs
//                // for (leftX, leftY, rightX). As you can see, for (0,1,0), it will travel forward
//                // regardless of the heading. For (1,0,0) it will strafe right (ref to the 0 heading)
//                // regardless of the heading.
//                //
//                //                   heading
//                //                     /
//                //            (0,1,0) /
//                //               |   /
//                //               |  /
//                //            ___|_/_____
//                //          /           /
//                //         /           / ---------- (1,0,0)
//                //        /__________ /
//
//                // optional fifth parameter for squared inputs
//                if (gamepad1.right_trigger >= .4) {
//                    drive.driveFieldCentric(
//                            (-driverOp.getLeftX() * 0.3),
//                            (-driverOp.getLeftY() * 0.3),
//                            (-driverOp.getRightX() * 0.3),
//                            imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
//                            false
//                    );
//
//                } else {
//                    drive.driveFieldCentric(
//                            (-driverOp.getLeftX() * 0.8),
//                            (-driverOp.getLeftY() * 0.8),
//                            (-driverOp.getRightX() * 0.8),
//                            imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
//                            false
//                    );
//
//                }
//
//            }

    }

}
}
