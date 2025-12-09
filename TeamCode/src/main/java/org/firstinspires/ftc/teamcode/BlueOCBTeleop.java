package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Modules.Constants;
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
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();
//        Intake.InitalizeTimer();
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

        while (!isStopRequested()) {
            OCBHWM.turretServo.update();
            if (gamepad1.back) {
                OCBHWM.imu.reset();
            }

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
            if (gamepad2.right_bumper) {
                Shoota.setSpeed(ShootaSpeed);
            } else if (gamepad2.a) {
                Shoota.setSpeed(Constants.FARSHOTSPEED);
            } else {
                Shoota.stop();
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
            if (gamepad2.right_trigger >= 0.4 || gamepad2.left_bumper) {
                Transfer.transferIn();
            } else if (gamepad2.b) {
                Transfer.transferOut();
            } else {
                Transfer.transferHold();
            }

            //Turret Angles
            if (gamepad2.a) {
                OCBHWM.hoodServo.setPosition(Constants.FARSHOTHOODSERVO);
                Turret.setToAngle(Constants.TELEFARSHOTTURRETANGLE);
                Shoota.setSpeed(Constants.FARSHOTSPEED);
                ShootaSpeed = Constants.FARSHOTSPEED;
                ShootaDesiredVelocity = Constants.FARSHOTVEL;
            } else if (gamepad2.x) {
                OCBHWM.hoodServo.setPosition(Constants.FARSHOTHOODSERVO);
                Turret.setToAngle(Constants.TELEFARSHOTTURRETANGLEOPPO);
                Shoota.setSpeed(Constants.FARSHOTSPEEDOPPO);
                ShootaSpeed = Constants.FARSHOTSPEEDOPPO;
                ShootaDesiredVelocity = Constants.FARSHOTVEL;
            } else if (gamepad2.y) {
                Turret.setToAngle(0);
            } else if (gamepad2.left_trigger > 0.4) {
                Shoota.cameraAdjustTurret();
                Shoota.cameraSetLaunch(ShootaSpeed);
            }

            //Manual Turret Control
            if (gamepad2.right_stick_x >= 0.4){
                Turret.addAngle(gamepad2.right_stick_y * 0.5);
            } else if (gamepad2.right_stick_x <= -0.4){
                Turret.subtractAngle(Math.abs(gamepad2.right_stick_y * 0.5));
            }
            if (gamepad2.left_trigger >= 0.4) {
                Shoota.CheckSpeed(ShootaDesiredVelocity);
//            } else if (gamepad2.left_bumper) {
//                if (Intake.intakeFull()) {
//                    Intake.setIntakeLight(true);
//                    gamepad2.rumble(100);
//                } else {
//                    Intake.setIntakeLight(false);
//                }
            }

//            LLResult result = OCBHWM.limelight.getLatestResult();
//            if (result != null) {
//                if (result.isValid()) {
//                    telemetry.addData("Tx", result.getTx());
//                    telemetry.addData("Ty", result.getTy());
//                    telemetry.addData("Ta", result.getTa());
//                    telemetry.addData("Distance", Shoota.distanceToGoal(result.getTy()));
//                    telemetry.addData("far Distance Calc", Shoota.farDistanceToGoal(result.getTy(),result.getTa()));
//                }
//            }
            telemetry.addData("turret currently tracking", Shoota.NotInPos);
            telemetry.addData("turretAngle", OCBHWM.turretServo.getTargetRotation());
//            telemetry.addData("turret Pos Error",Shoota.PosError);
//            telemetry.addData("turret Desired Angle",Shoota.DesiredTurretAng);
//            telemetry.addData("turret Feedback Angle",Turret.FeedbacktoAngle());
//            telemetry.addData("turret Servo angle",OCBHWM.turretServo.getPosition());
//            telemetry.addData("turret current angle", Turret.servoValueToAngle(OCBHWM.turretServo.getPosition()));

//            telemetry.addData("shoota mode", ShootaMode);
//            telemetry.addData("Shoota set speed", ShootaSpeed);
//            List<Double> velocities = OCBHWM.flywheel.getVelocities();
//            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
//            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
//            telemetry.addData("turret Feedback voltage", OCBHWM.turretFeedback.getVoltage());
//            telemetry.addData("hood Servo angle", OCBHWM.hoodServo.getPosition());
//            telemetry.addData("Turret Power", OCBHWM.turretServo.getPower());
            telemetry.addData("Turret Error", OCBHWM.turretServo.getTargetRotation() - OCBHWM.turretServo.getTotalRotation());
//            telemetry.addData("Hood Feedback voltage",OCBHWM.hoodFeedback.getVoltage());
//            telemetry.addData("Transfer Sensor voltage",OCBHWM.transferClear.getVoltage());
//            telemetry.addData("heading", OCBHWM.imu.getRotation2d());
            telemetry.update();
        }
    }
}