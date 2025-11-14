package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

import Modules.Constants;
import Modules.Hood;
import Modules.Intake;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Transfer;
import Modules.Turret;

@TeleOp
public class ShootaTuning extends LinearOpMode {

    public String ShootaMode = "Manual";
    public double ShootaSpeed = .6;

    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx OperatorOp = new GamepadEx(gamepad2);


        waitForStart();
        while (!isStopRequested()) {
            if (gamepad1.back) {
                OCBHWM.imu.reset();
            }

            if (gamepad1.start) {
                ShootaMode = "Manual";
            } else if (gamepad2.back) {
                ShootaMode = "Tracking";
            }

            if (gamepad1.right_trigger >= .4) {
                OCBHWM.m_robotDrive.driveFieldCentric(
                        (-driverOp.getLeftX() * 0.4),
                        (-driverOp.getLeftY() * 0.4),
                        (-driverOp.getRightX() * 0.4),
                        OCBHWM.imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                        false
                );

            } else {
                OCBHWM.m_robotDrive.driveFieldCentric(
                        (-driverOp.getLeftX()),
                        (-driverOp.getLeftY()),
                        (-driverOp.getRightX()),
                        OCBHWM.imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                        false
                );

            }

            // Intake & Transfer
            if (gamepad1.left_trigger > 0.4) {
                Shoota.setSpeed(ShootaSpeed);
            } else {
                Shoota.stop();
            }
            if (gamepad1.dpad_left && ShootaSpeed < 1) {
                ShootaSpeed = ShootaSpeed + .0001;
                ShootaSpeed = ShootaSpeed + .0001;
            } else if (gamepad1.dpad_right && ShootaSpeed > 0) {
                ShootaSpeed = ShootaSpeed - .0001;
                ShootaSpeed = ShootaSpeed - .0001;
            }

            if (gamepad1.right_trigger > 0.4) {
                Transfer.gateForward();
                Transfer.transferIn();
                Transfer.kickerForward();
                Intake.intakeIn();
            } else {
                Transfer.gateRest();
                Transfer.kickerRest();
                Transfer.transferHold();
                Intake.intakeRest();
            }

            if (gamepad1.dpad_up) {
                OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() + 0.003);
            } else if (gamepad1.dpad_down) {
                OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() - 0.003);
            }

            if (gamepad1.left_bumper) {
                OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() - 0.005);
            } else if (gamepad1.right_bumper) {
                OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() + 0.005);
            }

            telemetry.addData("Shoota set speed", ShootaSpeed);
            List<Double> velocities = OCBHWM.flywheel.getVelocities();
            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
            telemetry.addData("turret Servo angle",OCBHWM.turretServo.getPosition());
            telemetry.addData("turret Feedback voltage", OCBHWM.turretFeedback.getVoltage());
            telemetry.addData("hood Servo angle",OCBHWM.hoodServo.getPosition());
            telemetry.addData("Hood Feedback voltage",OCBHWM.hoodFeedback.getVoltage());
            telemetry.update();
        }
    }

    public void shooterControl() {
        switch (ShootaMode) {
            case "Manual":
                if (gamepad2.x) {
                    Turret.setToAngle(Constants.AUTOFARSHOTTURRETANGLE);
                    Hood.setToAngle(Constants.FARSHOTHOODSERVO);
                    Shoota.setSpeed(Constants.FARSHOTSPEED);
                } else if (gamepad2.y) {
                    Turret.setToAngle(Constants.MIDSHOTTURRETANGLE);
                    Hood.setToAngle(Constants.MIDSHOTHOODSERVO);
                    Shoota.setSpeed(Constants.MIDSHOTSPEED);
                } else if (gamepad2.a) {
                    Turret.setToAngle(Constants.CLOSESHOTTURRETANGLE);
                    Hood.setToAngle(Constants.CLOSESHOTHOODSERVO);
                    Shoota.setSpeed(Constants.CLOSESHOTSPEED);
                }
                break;
            case "Tracking":

                break;

        }
    }

}