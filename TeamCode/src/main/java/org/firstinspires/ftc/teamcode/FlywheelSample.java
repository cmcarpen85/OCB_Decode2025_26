package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

import Modules.Constants;

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

    public static double kP = 20;
    public static double kV = 0.7;

    @Override
    public void runOpMode() throws InterruptedException {
        toolOp = new GamepadEx(gamepad1);
        // this creates a group of two 6k RPM goBILDA motors
        // the 'flywheel_left' motor in the configuration will be set
        // as the leader for the group
        flywheel = new MotorGroup(
                flywheelL = new Motor(hardwareMap, "flywheelL", Motor.GoBILDA.BARE),
                flywheelR = new Motor(hardwareMap, "flywheelR", Motor.GoBILDA.BARE)
        );

        flywheel.setRunMode(Motor.RunMode.VelocityControl);
        flywheel.setVeloCoefficients(kP, 0, 0);
        flywheel.setFeedforwardCoefficients(0, kV);
        flywheelL.setInverted(true);

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


            if (gamepad1.dpad_up && MotorPower1 < 1) {
                MotorPower1 = MotorPower1 + .0001;
            } else if (gamepad1.dpad_down && MotorPower1 > 0) {
                MotorPower1 = MotorPower1 - .0001;
            }



            // we can obtain a list of velocities with each item in the list
            // representing the motor passed in as an input to the constructor.
            // so, our flywheel_left is index 0 and flywheel_right is index 1
            List<Double> velocities = flywheel.getVelocities();
            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
            telemetry.addData("motorPower", MotorPower1);

            telemetry.update();
            toolOp.readButtons();
        }
    }

}