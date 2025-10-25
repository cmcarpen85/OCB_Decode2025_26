package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

import Modules.Constants;
import Modules.OCBHWM;
import Modules.Turret;

/**
 * A sample opmode for a flywheel with two motors
 * that are linked mechanically.
 */
@TeleOp
public class FlywheelSample extends LinearOpMode {
    public static double MotorPower1 = 0.5;

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

    public static double kP = 20;
    public static double kV = 0.7;

    @Override
    public void runOpMode() throws InterruptedException {
        toolOp = new GamepadEx(gamepad1);
        // this creates a group of two 6k RPM goBILDA motors
        // the 'flywheel_left' motor in the configuration will be set
        // as the leader for the group
        flywheel = new MotorGroup(
                flywheelR = new Motor(hardwareMap, "flywheelL", Motor.GoBILDA.BARE),
                flywheelL = new Motor(hardwareMap, "flywheelR", Motor.GoBILDA.BARE)
        );

        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
        turretServo = hardwareMap.get(Servo.class, "turretServo");
        turretFeedback = hardwareMap.get(AnalogInput.class,"turretFeedback");
        hoodFeedback = hardwareMap.get(AnalogInput.class, "hoodFeedback");

        gateServo = hardwareMap.get(CRServo.class, "gateServo");
        gateServo2 = hardwareMap.get(CRServo.class, "gateServo2");

        flywheel.setRunMode(Motor.RunMode.VelocityControl);
        flywheel.setVeloCoefficients(kP, 0, 0);
        flywheel.setFeedforwardCoefficients(0, kV);
        flywheelL.setInverted(true);

        hoodServo.setPosition(0.05);
        turretServo.setPosition(0.5);
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
                flywheel.set(MotorPower1);
            } else if (gamepad1.left_trigger > 0.4) {
                flywheel.set(Constants.COASTSPEED);
            } else {
                flywheel.stopMotor();
            }


            if (gamepad1.dpad_left && MotorPower1 < 1) {
                MotorPower1 = MotorPower1 + .0001;
            } else if (gamepad1.dpad_right && MotorPower1 > 0) {
                MotorPower1 = MotorPower1 - .0001;
            }
            if (gamepad1.dpad_up) {
                hoodServo.setPosition(hoodServo.getPosition() + 0.0003);
            } else if (gamepad1.dpad_down) {
                hoodServo.setPosition(hoodServo.getPosition() - 0.0003);
            }

            if (gamepad1.left_bumper) {
//                turretServo.setPower(1);
                turretServo.setPosition(turretServo.getPosition() + 0.001);
            } else if (gamepad1.right_bumper) {
//                turretServo.setPower(-1);
                turretServo.setPosition(turretServo.getPosition() - 0.001);
            }
//            } else {
//                turretServo.setPower(0);
//            }

            if (gamepad1.a) {
                gateServo.setDirection(DcMotorSimple.Direction.FORWARD);
                gateServo2.setDirection(DcMotorSimple.Direction.REVERSE);
                gateServo.setPower(1);
                gateServo2.setPower(1);
            } else if (gamepad1.y) {
                gateServo.setDirection(DcMotorSimple.Direction.REVERSE);
                gateServo2.setDirection(DcMotorSimple.Direction.FORWARD);
                gateServo.setPower(1);
                gateServo2.setPower(1);
            } else {
                gateServo.setPower(0);
                gateServo2.setPower(0);
            }

            // we can obtain a list of velocities with each item in the list
            // representing the motor passed in as an input to the constructor.
            // so, our flywheel_left is index 0 and flywheel_right is index 1
            List<Double> velocities = flywheel.getVelocities();
            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
            telemetry.addData("motorPower", MotorPower1);
            telemetry.addData("turret pos", turretServo.getPosition());
            telemetry.addData("Turret Voltage", turretFeedback.getVoltage());
            telemetry.addData("Hood Voltage", hoodFeedback.getVoltage());

            telemetry.update();
            toolOp.readButtons();
        }
    }

}