package org.firstinspires.ftc.teamcode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.bylazar.panels.Panels;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

import Modules.Constants;
import Modules.Intake;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Transfer;
import Modules.Turret;

@TeleOp
public class ShootaTuning extends LinearOpMode {

    public String ShootaMode = "Manual";

    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx OperatorOp = new GamepadEx(gamepad2);
        double ShootaSpeed = .6;
        double ShootaDesiredVelocity = 0;
        boolean Tracking = false;

        OCBHWM.limelight.start();
        OCBHWM.limelight.pipelineSwitch(0);

        waitForStart();
        OCBHWM.hoodServo.setPosition(Constants.HOODHOME);
        OCBHWM.turretServo.setRtp(true);
//        OCBHWM.turretServo.setTargetRotation(Constants.TURRETHOME);

        while (!isStopRequested()) {
            OCBHWM.turretServo.update();
            if (gamepad2.back){
                OCBHWM.turretServo.setRtp(false);
            } else if (gamepad2.start){
                OCBHWM.turretServo.setRtp(true);
            }

            if (gamepad1.dpad_left){
                OCBHWM.limelight.pipelineSwitch(0);

            }else if (gamepad1.dpad_right){
                  OCBHWM.limelight.pipelineSwitch(2);
            }

            if (gamepad1.back) {
                OCBHWM.imu.reset();
            }

//            if (gamepad2.dpad_down) {
//                ShootaMode = "Manual";
//            } else if (gamepad2.dpad_up) {
//                ShootaMode = "Tracking";
//            }

//          Slow Mode
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
                        (-driverOp.getRightX() * 0.8),
                        OCBHWM.imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                        false
                );
            }

            //Prep Shoota
            if (gamepad2.left_trigger > 0.4) {
                Shoota.setSpeed(ShootaSpeed);
            } else if (gamepad2.a) {
                Shoota.setSpeed(Constants.FARSHOTSPEED);
            } else if (gamepad2.x) {
                Shoota.setSpeed(Constants.MIDSHOTSPEED);
            } else {
                Shoota.stop();
            }

            if (gamepad2.dpad_up && ShootaSpeed < 1) {
                ShootaSpeed = ShootaSpeed + .0001;
            } else if (gamepad2.dpad_down && ShootaSpeed > 0) {
                ShootaSpeed = ShootaSpeed - .0001;
            }

            // Intake & Transfer
            if (gamepad2.left_bumper) {
                Intake.intakeIn();
            } else if (gamepad2.b) {
                Intake.intakeOut();
            } else {
                Intake.intakeRest();
            }

            //Gate & Kicker (Shoot)
            if (gamepad2.right_trigger > 0.4) {
                Transfer.gateForward();
                Transfer.kickerForward();
            } else if (gamepad2.b) {

                Transfer.gateReverse();
                Transfer.kickerReverse();
            } else {
                Transfer.gateRest();
                Transfer.kickerRest();
            }

            //Transfer Belts
            if (gamepad2.right_trigger > 0.4 || gamepad2.left_bumper) {
                Transfer.transferIn();
            } else if (gamepad2.b) {
                Transfer.transferOut();
            } else {
                Transfer.transferHold();
            }

            if (gamepad2.a) {
                OCBHWM.hoodServo.setPosition(Constants.FARSHOTHOODSERVO);
                Turret.setToAngle(Constants.TELEFARSHOTTURRETANGLE);
                Shoota.setSpeed(Constants.FARSHOTSPEED);
                ShootaSpeed = Constants.FARSHOTSPEED;
                ShootaDesiredVelocity = Constants.FARSHOTVEL;
            } else if (gamepad2.x) {
                Turret.setToAngle(90);
            } else if (gamepad2.y) {
                Turret.setToAngle(0);
            } else if (gamepad2.b) {
                Turret.setToAngle(-90);
            } else if (gamepad2.right_bumper) {
                Shoota.cameraAdjustTurret();
//                Shoota.cameraSetLaunch(ShootaSpeed);
            }

            if (-gamepad2.left_stick_y >= 0.4 && OCBHWM.hoodServo.getPosition() < Constants.HOODMAXSERVOVALUE) {
                OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() + 0.001);
            } else if (-gamepad2.left_stick_y <= -0.4 && OCBHWM.hoodServo.getPosition() > Constants.HOODMINSERVOVALUE) {
                OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() - 0.001);
            }

//            if (gamepad2.dpad_left && OCBHWM.turretServo.getPosition() > Constants.TURRETMINSERVOVALUE) {
//                OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() - 0.002);
//            } else if (gamepad2.dpad_right && OCBHWM.turretServo.getPosition() < Constants.TURRETMAXSERVOVALUE) {
//                OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() + 0.002);
            if (gamepad2.right_stick_x >= 0.4) {
                Turret.addAngle(gamepad2.right_stick_y * 2);
            } else if (gamepad2.right_stick_x <= -0.4) {
                Turret.subtractAngle(Math.abs(gamepad2.right_stick_y) * 2);
            }

            Shoota.CheckSpeed(ShootaDesiredVelocity);

            LLResult result = OCBHWM.limelight.getLatestResult();
            if (result != null) {
                if (result.isValid()) {
                    telemetry.addData("Tx", result.getTx());
                    telemetry.addData("Ty", result.getTy());
                    telemetry.addData("Ta", result.getTa());
                    telemetry.addData("Distance", Shoota.distanceToGoal(result.getTy()));
                    telemetry.addData("far Distance Calc", Shoota.farDistanceToGoal(result.getTy(), result.getTa()));
                }
            }
            telemetry.addData("turret currently tracking", Shoota.NotInPos);
//            telemetry.addData("turret Pos Error",Shoota.PosError);
//            telemetry.addData("turret Desired Angle",Shoota.DesiredTurretAng);
//            telemetry.addData("turret Feedback Angle",Turret.FeedbacktoAngle());
//            telemetry.addData("turret Servo angle",OCBHWM.turretServo.getPosition());
//            telemetry.addData("turret current angle", Turret.servoValueToAngle(OCBHWM.turretServo.getPosition()));

//            telemetry.addData("shoota mode", ShootaMode);
            telemetry.addData("Shoota set speed", ShootaSpeed);
            List<Double> velocities = OCBHWM.flywheel.getVelocities();
            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
//            telemetry.addData("turret Feedback voltage", OCBHWM.turretFeedback.getVoltage());
            telemetry.addData("hood Servo angle", OCBHWM.hoodServo.getPosition());
            telemetry.addData("Turret Power", OCBHWM.turretServo.getPower());
            telemetry.addData("Turret Error", OCBHWM.turretServo.getTargetRotation() - OCBHWM.turretServo.getTotalRotation());
            telemetry.addData("turretAngle", OCBHWM.turretServo.getTargetRotation());
//            telemetry.addData("Hood Feedback voltage",OCBHWM.hoodFeedback.getVoltage());
//           telemetry.addData("Transfer Sensor voltage",OCBHWM.transferClear.getVoltage());
//            telemetry.addData("heading", OCBHWM.imu.getRotation2d());
            telemetry.update();
        }
    }
}