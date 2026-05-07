package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

import Modules.Constants;
import Modules.HeadingTracker;
import Modules.Intake;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Transfer;
import Modules.Turret;

@TeleOp
public class BlueOCBTeleop extends LinearOpMode {

    public String ShootaMode = "Manual";

    @Override
    public void runOpMode() {
        if (!OCBHWM.Initialized) {
            HeadingTracker.setPinpointStart(-64.1575, 15.420);
        }
        OCBHWM.hwinit(hardwareMap);
//        Intake.InitalizeTimer();
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx OperatorOp = new GamepadEx(gamepad2);
        double ShootaSpeed = .6;
        double ShootaDesiredVelocity = 0;
        boolean Tracking = false;
        HeadingTracker.manualAimOffset = 0;
        boolean recovery = false;

//        OCBHWM.limelight.start();
//        OCBHWM.limelight.pipelineSwitch(0);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        OCBHWM.hoodServo.setPosition(Constants.HOODHOME);
        OCBHWM.turretServo.setRtp(true);
//        Shoota.checkLimelight();
//        if(HeadingTracker.limelightOffset != 0){
//        Shoota.LimelightOffsetBlue();
//        }

        while (!isStopRequested()) {
            driverOp.readButtons();
            OperatorOp.readButtons();

            OCBHWM.turretServo.update();
//            Shoota.checkLimelight();
            OCBHWM.pinPoint.update();

            if (gamepad1.back) {
                OCBHWM.pinPoint.setHeading(0,AngleUnit.DEGREES);
            }


//          Slow Mode
            if (gamepad1.right_trigger >= .4) {
                OCBHWM.m_robotDrive.driveFieldCentric(
                        (-driverOp.getLeftX() * 0.4),
                        (-driverOp.getLeftY() * 0.4),
                        (-driverOp.getRightX() * 0.4),
                        OCBHWM.pinPoint.getHeading(AngleUnit.DEGREES) - 90,
//                        OCBHWM.imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                        false
                );

            } else {
                OCBHWM.m_robotDrive.driveFieldCentric(
                        (-driverOp.getLeftX() * 0.9),
                        (-driverOp.getLeftY() * 0.9),
                        (-driverOp.getRightX() * 0.8),
                        OCBHWM.pinPoint.getHeading(AngleUnit.DEGREES) - 90,
//                        OCBHWM.imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                        false
                );
            }

            //Prep Shoota
            if (gamepad2.right_bumper) {
                Shoota.setSpeed(ShootaSpeed);
            }

            // Intake & Transfer
            if (gamepad2.left_bumper) {
                Intake.intakeIn();
            } else if (gamepad2.b) {
                Intake.intakeOut();
            } else if (gamepad2.right_trigger > 0.4 && Intake.intakeFull()) {
                Intake.intakeIn();
            } else {
                Intake.intakeRest();
            }

            //Gate & Kicker (Shoot)
            if (gamepad2.right_trigger > 0.4) {
                Transfer.clawOpen();
            } else if (gamepad2.left_bumper) {
                Transfer.clawClose();
            } else {
                Transfer.clawHold();
            }

            //Transfer Belts
            if (gamepad2.right_trigger >= 0.4) {
                Transfer.transferShoot();
            } else if (gamepad2.left_bumper) {
                Transfer.transferIn();
            } else if (gamepad2.b) {
                Transfer.transferOut();
            } else {
                Transfer.transferHold();
            }

            //Turret Angles
            if (gamepad2.a) {
                OCBHWM.hoodServo.setPosition(Constants.FARSHOTHOODSERVO);
//                Turret.setToAngle(Constants.TELEFARSHOTTURRETANGLE);
                Shoota.setSpeed(Constants.FARSHOTSPEED);
                ShootaSpeed = Constants.FARSHOTSPEED;
                ShootaDesiredVelocity = Constants.FARSHOTVEL;
            } else if (gamepad2.left_trigger > 0.4) {
//                Shoota.cameraAdjustTurret("blue");
//                Shoota.cameraSetLaunch(ShootaSpeed);
                HeadingTracker.headingTrackingBlue(true);
            } else {
//            Shoota.cameraAdjustTurret("blue");
                HeadingTracker.headingTrackingBlue(false);
                Shoota.coast();
            }

            //Manual Turret Control
            if (gamepad2.right_stick_x >= 0.4) {
                Turret.subtractAngle(Math.abs(gamepad2.right_stick_y * 4));
            } else if (gamepad2.right_stick_x <= -0.4) {
                Turret.addAngle(Math.abs(gamepad2.right_stick_y * 4));
            }

            //manual aim adjust
            if (OperatorOp.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                HeadingTracker.manualAimOffset = HeadingTracker.manualAimOffset + 0.5;
            } else if (OperatorOp.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                HeadingTracker.manualAimOffset = HeadingTracker.manualAimOffset - 0.5;
            } else if (OperatorOp.wasJustPressed(GamepadKeys.Button.Y)) {
                HeadingTracker.manualAimOffset = HeadingTracker.manualAimOffset - 4.0;
            } else if (OperatorOp.wasJustPressed(GamepadKeys.Button.X)) {
                HeadingTracker.manualAimOffset = HeadingTracker.manualAimOffset + 4.0;
            }

            if (gamepad2.left_trigger >= 0.4) {
                Shoota.CheckSpeed(Shoota.CurrentShootPower);
            } else if (gamepad2.left_bumper) {
                if (Intake.intakeFull()) {
                    Intake.setIntakeLight(true);
                } else {
                    Intake.setIntakeLight(false);
                }
            }

//            //Worst Case Scenario
            if (gamepad2.back && gamepad2.right_stick_button && recovery){
                OCBHWM.turretServo.setRtp(true);
                recovery = false;
            }
            else if (gamepad2.back) {
                OCBHWM.turretServo.setRtp(false);
                recovery = true;

            }

            if (recovery){
            OCBHWM.turretServo.setPower(-1 * gamepad2.right_stick_y);
            }


//            if (gamepad2.right_stick_button) {
//                Shoota.cameraSetPinPoint();
//            }

//            double robotYaw = OCBHWM.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)+180;
//            OCBHWM.limelight.updateRobotOrientation(robotYaw);

//            LLResult result = OCBHWM.limelight.getLatestResult();
//            if (result != null) {
//                if (result.isValid()) {
//                    Pose3D botpose_mt2 = result.getBotpose_MT2();
//                    if (botpose_mt2 != null) {
//                        telemetry.addData("botpose X", botpose_mt2.getPosition().x);
//                        telemetry.addData("botpose Y", botpose_mt2.getPosition().y);
//                    }
//                    telemetry.addData("Tx", result.getTx());
////                    telemetry.addData("Ty", result.getTy());
////                    telemetry.addData("Ta", result.getTa());
////                    telemetry.addData("Distance", Shoota.distanceToGoal(result.getTy()));
////                    telemetry.addData("far Distance Calc", Shoota.farDistanceToGoal(result.getTy(),result.getTa()));
//                }
//            }


////            telemetry.addData("turret currently tracking", Shoota.NotInPos);
//            telemetry.addData("turretAngle", OCBHWM.turretServo.getTargetRotation());
//            telemetry.addData("turretCurrent Rotation", OCBHWM.turretServo.getTotalRotation());
//            telemetry.addData("turret Pos Error",Shoota.PosError);
//            telemetry.addData("turret Desired Angle",Shoota.DesiredTurretAng);
//            telemetry.addData("turret Feedback Angle",Turret.FeedbacktoAngle());
//            telemetry.addData("turret Servo angle",OCBHWM.turretServo.getPosition());
//            telemetry.addData("turret current angle", OCBHWM.turretServo.getCurrentAngle());

//            telemetry.addData("shoota mode", ShootaMode);
//            telemetry.addData("Shoota set speed", ShootaSpeed);
//            List<Double> velocities = OCBHWM.flywheel.getVelocities();
//            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
//            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
//            telemetry.addData("turret Feedback voltage", OCBHWM.turretFeedback.getVoltage());
//            telemetry.addData("hood Servo angle", OCBHWM.hoodServo.getPosition());
//            telemetry.addData("Turret Power", OCBHWM.turretServo.getPower());
//            telemetry.addData("Turret Error", Turret.getTurretError());
            telemetry.addData("aim offset", HeadingTracker.manualAimOffset);
//            telemetry.addData("xPos", OCBHWM.pinPoint.getPosX(DistanceUnit.INCH));
//            telemetry.addData("yPos", OCBHWM.pinPoint.getPosY(DistanceUnit.INCH));
//            telemetry.addData("turret shift x",HeadingTracker.turretShiftX);
//            telemetry.addData("turret shift y",HeadingTracker.turretShiftY);
//            telemetry.addData("limelight offset", HeadingTracker.limelightOffset);
//            telemetry.addData("start offset X", HeadingTracker.robotStartX);
//            telemetry.addData("start offset Y", HeadingTracker.robotStartY);

//            telemetry.addData("turret heading vel", OCBHWM.imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate);
//            telemetry.addData("base heading vel", OCBHWM.pinPoint.getHeading(UnnormalizedAngleUnit.DEGREES));
            telemetry.update();
        }
    }
}