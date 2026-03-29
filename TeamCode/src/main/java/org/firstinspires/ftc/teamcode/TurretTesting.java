/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.Datalogging.DatalogExample_v01;
import org.firstinspires.ftc.teamcode.Datalogging.Datalogger;

import Modules.OCBHWM;


@TeleOp(name="Basic: Linear OpMode", group="Linear OpMode")
public class TurretTesting extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Datalog datalog;

    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.turretServo.setRtp(false);

        datalog = new Datalog("datalog_turret");

        datalog.opModeStatus.set("INIT");
        datalog.writeLine();

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Status", "Initialized");
        telemetry.addData("Turret Servo Power", OCBHWM.turretServo.getPower());
        telemetry.addData("Turret Rotation", OCBHWM.turretServo.getTotalRotation());
        telemetry.update();



        // Wait for the game to start (driver presses START)
        waitForStart();
        datalog.opModeStatus.set("RUNNING");
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            for (double i = 0; opModeIsActive(); i++)
            {
                datalog.loopCounter.set(i);
                OCBHWM.turretServo.setPower( i/1000);
                OCBHWM.turretServo.update();

                datalog.turretEncoder.set("%.3f",OCBHWM.turretServo.getTotalRotation());
                datalog.turretHeading.set(OCBHWM.imu.getRobotYawPitchRollAngles().getYaw());
                datalog.servoPower.set(OCBHWM.turretServo.getPower());

                // The logged timestamp is taken when writeLine() is called.
                datalog.writeLine();

                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Turret Servo Power", OCBHWM.turretServo.getPower());
                telemetry.addData("Turret Rotation", OCBHWM.turretServo.getTotalRotation());
                telemetry.addData("Turret Heading",  OCBHWM.imu.getRobotYawPitchRollAngles().getYaw());
                telemetry.addData("Base Heading", OCBHWM.pinPoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES));
                telemetry.update();

                sleep(5);
            }
        }
    }

    public static class Datalog
    {
        // The underlying datalogger object - it cares only about an array of loggable fields
        private final Datalogger datalogger;

        // These are all of the fields that we want in the datalog.
        // Note that order here is NOT important. The order is important in the setFields() call below
        public Datalogger.GenericField opModeStatus = new Datalogger.GenericField("OpModeStatus");
        public Datalogger.GenericField loopCounter = new Datalogger.GenericField("Loop Counter");

        public Datalogger.GenericField turretEncoder = new Datalogger.GenericField("Turret Enc.");
        public Datalogger.GenericField servoPower = new Datalogger.GenericField("Turret Pow.");

        public Datalogger.GenericField turretHeading = new Datalogger.GenericField("Turret Head.");


        public Datalog(String name)
        {
            // Build the underlying datalog object
            datalogger = new Datalogger.Builder()

                    // Pass through the filename
                    .setFilename(name)

                    // Request an automatic timestamp field
                    .setAutoTimestamp(Datalogger.AutoTimestamp.DECIMAL_SECONDS)

                    // Tell it about the fields we care to log.
                    // Note that order *IS* important here! The order in which we list
                    // the fields is the order in which they will appear in the log.
                    .setFields(
                            opModeStatus,
                            loopCounter,
                            turretEncoder,
                            servoPower,
                            turretHeading
                    )
                    .build();
        }

        // Tell the datalogger to gather the values of the fields
        // and write a new line in the log.
        public void writeLine()
        {
            datalogger.writeLine();
        }
    }
}
