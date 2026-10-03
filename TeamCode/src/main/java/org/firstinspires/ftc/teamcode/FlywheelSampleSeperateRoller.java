package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

import Modules.Constants;

/**
 * A sample opmode for a flywheel with two motors
 * that are linked mechanically.
 */

@TeleOp
public class FlywheelSampleSeperateRoller extends LinearOpMode {
    public static double MotorPower1 = 0.4;
    public static double MotorPower2 = 0.4;
    private GamepadEx toolOp;

    // this is our flywheel motor group
    private Motor flywheelL;
    private Motor flywheelR;
    private MotorGroup flywheel;

    private Servo hoodServo;
    private Servo turretServo;
    private AnalogInput turretFeedback;
    private AnalogInput hoodFeedback;

    private CRServo gateServo;
    private CRServo gateServo2;

    public static double kP = 10;
    public static double kV = 0.7;

    @Override
    public void runOpMode() throws InterruptedException {
        toolOp = new GamepadEx(gamepad1);
        // this creates a group of two 6k RPM goBILDA motors
        // the 'flywheel_left' motor in the configuration will be set
        // as the leader for the group
        flywheelR = new Motor(hardwareMap, "flywheelR", Motor.GoBILDA.BARE);
                flywheelL = new Motor(hardwareMap, "flywheelL", Motor.GoBILDA.BARE);


//        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
//        turretServo = hardwareMap.get(Servo.class, "turretServo");
//        turretFeedback = hardwareMap.get(AnalogInput.class,"turretFeedback");
//        hoodFeedback = hardwareMap.get(AnalogInput.class, "hoodFeedback");
//
//        gateServo = hardwareMap.get(CRServo.class, "gateServo");
//        gateServo2 = hardwareMap.get(CRServo.class, "gateServo2");

        flywheelL.setRunMode(Motor.RunMode.VelocityControl);
        flywheelL.setVeloCoefficients(kP, 0, 0);
        flywheelL.setFeedforwardCoefficients(0, kV);
        flywheelL.setInverted(false);

        flywheelR.setRunMode(Motor.RunMode.VelocityControl);
        flywheelR.setVeloCoefficients(kP, 0, 0);
        flywheelR.setFeedforwardCoefficients(0, kV);
        flywheelR.setInverted(false);

//        hoodServo.setPosition(0.05);
//        turretServo.setPosition(0.5);
//        turretServo.setPower(0);

        // this is not required for this example
        // here, we are setting the bulk caching mode to manual so all hardware reads
        // for the motors can be read in one hardware call.
        // we do this in order to decrease our loop time
        List<LynxModule> hubs = hardwareMap.getAll(LynxModule.class);
        hubs.forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));

        waitForStart();

        while (!isStopRequested() && opModeIsActive()) {
            // This clears the cache for the hardware
            // Refer to https://gm0.org/en/latest/docs/software/control-system-internals.html#bulk-reads
            // for more information on bulk reads.
            hubs.forEach(LynxModule::clearBulkCache);

            if (gamepad1.right_trigger > 0.4) {
                flywheelL.set(MotorPower1);
                flywheelR.set(MotorPower2);
            } else {
                flywheelL.stopMotor();
                flywheelR.stopMotor();
            }


            if (gamepad1.dpad_left && MotorPower1 < 1) {
                MotorPower1 = MotorPower1 + .0001;
            } else if (gamepad1.dpad_right && MotorPower1 > 0) {
                MotorPower1 = MotorPower1 - .0001;
            }


            if (gamepad1.dpad_up && MotorPower2 < 1) {
                MotorPower2 = MotorPower2 + .0001;
            } else if (gamepad1.dpad_down && MotorPower2 > 0) {
                MotorPower2 = MotorPower2 - .0001;
            }
//            if (gamepad1.dpad_up) {
//                hoodServo.setPosition(hoodServo.getPosition() + 0.0003);
//            } else if (gamepad1.dpad_down) {
//                hoodServo.setPosition(hoodServo.getPosition() - 0.0003);
//            }

//            if (gamepad1.left_bumper) {
////                turretServo.setPower(1);
//                turretServo.setPosition(turretServo.getPosition() + 0.001);
//            } else if (gamepad1.right_bumper) {
////                turretServo.setPower(-1);
//                turretServo.setPosition(turretServo.getPosition() - 0.001);
//            }
//            } else {
//                turretServo.setPower(0);
//            }

//            if (gamepad1.a) {
//                gateServo.setDirection(DcMotorSimple.Direction.FORWARD);
//                gateServo2.setDirection(DcMotorSimple.Direction.REVERSE);
//                gateServo.setPower(1);
//                gateServo2.setPower(1);
//            } else if (gamepad1.y) {
//                gateServo.setDirection(DcMotorSimple.Direction.REVERSE);
//                gateServo2.setDirection(DcMotorSimple.Direction.FORWARD);
//                gateServo.setPower(1);
//                gateServo2.setPower(1);
//            } else {
//                gateServo.setPower(0);
//                gateServo2.setPower(0);
//            }

            // we can obtain a list of velocities with each item in the list
            // representing the motor passed in as an input to the constructor.
            // so, our flywheel_left is index 0 and flywheel_right is index 1
            telemetry.addData("Left Flywheel Velocity", flywheelL.getCorrectedVelocity());
            telemetry.addData("Right Flywheel Velocity", flywheelR.getCorrectedVelocity());
            telemetry.addData("motorPower L", MotorPower1);
            telemetry.addData("motorPower Rw", MotorPower2);
//            telemetry.addData("turret pos", turretServo.getPosition());
//            telemetry.addData("Turret Voltage", turretFeedback.getVoltage());
//            telemetry.addData("Hood Voltage", hoodFeedback.getVoltage());

            telemetry.update();
            toolOp.readButtons();
        }
    }

}