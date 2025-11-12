package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

import Modules.Constants;
import Modules.Intake;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Transfer;
import Modules.Turret;

@TeleOp
public class RedTeleopSLOW extends LinearOpMode {

    public String ShootaMode = "Manual";

    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();
        GamepadEx driverOp = new GamepadEx(gamepad1);
        GamepadEx OperatorOp = new GamepadEx(gamepad2);
        double ShootaSpeed = .6;



        waitForStart();
        OCBHWM.hoodServo.setPosition(Constants.HOODHOME);
        OCBHWM.turretServo.setPosition(Constants.TURRETHOME);

        while (!isStopRequested()) {
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
                        (-driverOp.getLeftX()*0.6),
                        (-driverOp.getLeftY()*0.6),
                        (-driverOp.getRightX()*0.6),
                        OCBHWM.imu.getRotation2d().getDegrees(),   // gyro value passed in here must be in degrees
                        false
                );

            }

            //Prep Shoota
            if (gamepad2.left_trigger > 0.4 ) {
                Shoota.setSpeed(ShootaSpeed);
            } else if (gamepad2.a) {
                Shoota.setSpeed(Constants.FARSHOTSPEED);
            }else if (gamepad2.x){
                Shoota.setSpeed(Constants.MIDSHOTSPEED);
            }
            else {
                Shoota.stop();
            }
            if (-gamepad2.right_stick_y >= 0.4 && ShootaSpeed < 1) {
                ShootaSpeed = ShootaSpeed + .0001;
            } else if (-gamepad2.right_stick_y <= -0.4 && ShootaSpeed > 0) {
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

            if (gamepad2.right_trigger > 0.4 || gamepad2.left_bumper) {
                Transfer.transferIn();
            } else if (gamepad2.b) {
                Transfer.transferOut();
            }else {
                Transfer.transferHold();
            }


            if (gamepad2.a) {
                OCBHWM.hoodServo.setPosition(Constants.FARSHOTHOODSERVO);
                Turret.setToAngle(-Constants.TELEFARSHOTTURRETANGLE);
                Shoota.setSpeed(Constants.FARSHOTSPEED);
                ShootaSpeed = Constants.FARSHOTSPEED;
            } else if (gamepad2.x) {
                OCBHWM.hoodServo.setPosition(Constants.MIDSHOTHOODSERVO);
                Turret.setToAngle(-Constants.MIDSHOTTURRETANGLE);
                Shoota.setSpeed(Constants.MIDSHOTSPEED);
                ShootaSpeed = Constants.MIDSHOTSPEED;

            } else if (gamepad2.y) {
//                    Turret.setToAngle(Constants.CLOSESHOTTURRETANGLE);
//                    Hood.setToAngle(Constants.CLOSESHOTHOODANGLE);
                Shoota.setSpeed(Constants.CLOSESHOTSPEED);
            }

            if (-gamepad2.left_stick_y >= 0.4 && OCBHWM.hoodServo.getPosition() < Constants.HOODMAXSERVOVALUE) {
                OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() + 0.006);
            } else if (-gamepad2.left_stick_y <= -0.4 && OCBHWM.hoodServo.getPosition() > Constants.HOODMINSERVOVALUE) {
                OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() - 0.006);
            }

            if (gamepad2.dpad_left && OCBHWM.turretServo.getPosition() > Constants.TURRETMINSERVOVALUE) {
                OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() - 0.002);
            } else if (gamepad2.dpad_right && OCBHWM.turretServo.getPosition() < Constants.TURRETMAXSERVOVALUE) {
                OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() + 0.002);
            }

            Shoota.CheckSpeed(ShootaSpeed);

            telemetry.addData("shoota mode", ShootaMode);
            telemetry.addData("Shoota set speed", ShootaSpeed);
            List<Double> velocities = OCBHWM.flywheel.getVelocities();
            telemetry.addData("Left Flywheel Velocity", velocities.get(0));
            telemetry.addData("Right Flywheel Velocity", velocities.get(1));
            telemetry.addData("turret Servo angle",OCBHWM.turretServo.getPosition());
            telemetry.addData("turret current angle", Turret.servoValueToAngle(OCBHWM.turretServo.getPosition()));
            telemetry.addData("turret Feedback voltage", OCBHWM.turretFeedback.getVoltage());
            telemetry.addData("hood Servo angle",OCBHWM.hoodServo.getPosition());
            telemetry.addData("Hood Feedback voltage",OCBHWM.hoodFeedback.getVoltage());
            telemetry.addData("Transfer Sensor voltage",OCBHWM.transferClear.getVoltage());
            telemetry.addData("heading", OCBHWM.imu.getRotation2d());
            telemetry.update();


        }
    }

    public void shooterControl() {
        switch (ShootaMode) {
            case "Manual":
                if (gamepad2.a) {
//                    Turret.setToAngle(Constants.FARSHOTTURRETANGLE);
//                    Hood.setToAngle(Constants.FARSHOTHOODANGLE);
//                    OCBHWM.hoodServo.setPosition(Constants.FARSHOTHOODSERVO);
//                    OCBHWM.turretServo.setPosition(Constants.FARSHOTTURRETSERVO);
//                    Shoota.setSpeed(Constants.FARSHOTSPEED);
                } else if (gamepad2.x) {
//                    Turret.setToAngle(Constants.MIDSHOTTURRETANGLE);
//                    Hood.setToAngle(Constants.MIDSHOTHOODANGLE);
//                    OCBHWM.hoodServo.setPosition(Constants.MIDSHOTHOODSERVO);
//                    OCBHWM.turretServo.setPosition(Constants.MIDSHOTTURRETSERVO);
//                    Shoota.setSpeed(Constants.MIDSHOTHOODANGLE);
                } else if (gamepad2.y) {
//                    Turret.setToAngle(Constants.CLOSESHOTTURRETANGLE);
//                    Hood.setToAngle(Constants.CLOSESHOTHOODANGLE);
//                    Shoota.setSpeed(Constants.CLOSESHOTHOODANGLE);
                }

//                if (gamepad2.dpad_up && OCBHWM.hoodServo.getPosition() < Constants.HOODMAXSERVOVALUE) {
//                    OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() + 0.003);
//                } else if (gamepad2.dpad_down && OCBHWM.hoodServo.getPosition() > Constants.HOODMINSERVOVALUE) {
//                    OCBHWM.hoodServo.setPosition(OCBHWM.hoodServo.getPosition() - 0.003);
//                }
//
//                if (gamepad2.right_stick_x < -0.1 && OCBHWM.turretServo.getPosition() > Constants.TURRETMINSERVOVALUE) {
//                    OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() - 0.001);
//                } else if (gamepad2.right_stick_x > 0.1&& OCBHWM.turretServo.getPosition() < Constants.TURRETMAXSERVOVALUE) {
//                    OCBHWM.turretServo.setPosition(OCBHWM.turretServo.getPosition() + 0.001);
//                }


                break;
            case "Tracking":

                break;

        }
    }

}