package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;

import java.util.Objects;

import Modules.Constants;
import Modules.Hood;
import Modules.Intake;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Transfer;
import Modules.Turret;

@TeleOp
public class OCBTeleop extends LinearOpMode {

    public String ShootaMode = "Manual";

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

            if (gamepad2.dpad_down) {
                ShootaMode = "Manual";
            } else if (gamepad2.dpad_up) {
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
            if (gamepad2.left_trigger > 0.4) {
                Intake.intakeIn();
                Transfer.transferIn();
            } else if (gamepad2.right_trigger > 0.4) {
                Intake.intakeOut();
                Transfer.transferOut();
            } else {
                Intake.intakeRest();
                Transfer.transferHold();
            }

            //Gate & Kicker
            if (gamepad2.right_bumper) {
                Transfer.gateForward();
                Transfer.kickerForward();
            } else if (gamepad2.b) {
                Transfer.gateReverse();
                Transfer.kickerReverse();
            } else {
                Transfer.gateRest();
                Transfer.kickerRest();
            }


        }
    }

    public void shooterControl() {
        switch (ShootaMode) {
            case "Manual":
                if (gamepad2.x) {
                    Turret.setToAngle(Constants.FARSHOTTURRETANGLE);
                    Hood.setToAngle(Constants.FARSHOTHOODANGLE);
                    Shoota.setSpeed(Constants.FARSHOTSPEED);
                } else if (gamepad2.y) {
                    Turret.setToAngle(Constants.MIDSHOTTURRETANGLE);
                    Hood.setToAngle(Constants.MIDSHOTHOODANGLE);
                    Shoota.setSpeed(Constants.MIDSHOTHOODANGLE);
                } else if (gamepad2.a) {
                    Turret.setToAngle(Constants.CLOSESHOTTURRETANGLE);
                    Hood.setToAngle(Constants.CLOSESHOTHOODANGLE);
                    Shoota.setSpeed(Constants.CLOSESHOTHOODANGLE);
                }
                break;
            case "Tracking":

                break;

        }
    }

}