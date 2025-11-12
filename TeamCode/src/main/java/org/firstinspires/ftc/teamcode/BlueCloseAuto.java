package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.actions.IntakeAction;
import org.firstinspires.ftc.teamcode.actions.PrepShootAction;
import org.firstinspires.ftc.teamcode.actions.ShootAction;
import org.firstinspires.ftc.teamcode.enums.IntakeActionType;
import org.firstinspires.ftc.teamcode.enums.PrepShootActionType;
import org.firstinspires.ftc.teamcode.enums.ShootaActionType;

import Modules.OCBHWM;

@Config
@Autonomous
public class BlueCloseAuto extends LinearOpMode {


    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    public static class Params {
        public double startX = 0;
        public double startY = 0;
        public double startOri = 0;

        //SM = Spike Mark
        public double shoot1X = 0;
        public double shoot1Y = 50.09;
        public double pickFarSMX = 18.4;
        public double pickFarSMY = 50.09;
        public double intakeDriveFarX = 21.5; //39.9076
        public double shoot2X = 0;
        public double shoot2Y = 50.09;
        public double pickMidSMX = 18.4;
        public double pickMidSMY = 73.25;
        public double intakeDriveX = 27.2; //45.5875
        public double shoot3X = 0;
        public double shoot3Y = 50.09;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = 64;
    }

    public static Params PARAMS = new Params();

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos2 = new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos3 = new Pose2d(PARAMS.shoot3X, PARAMS.shoot3Y, Math.toRadians(PARAMS.startOri));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri));
    Pose2d pickFarSM = new Pose2d(PARAMS.pickFarSMX, PARAMS.pickFarSMY, Math.toRadians(PARAMS.startOri));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(PARAMS.startOri));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();

        drive = new MecanumDrive(hardwareMap, initialPos);

        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(startPos)
                .lineToYConstantHeading(PARAMS.shoot1Y);

        TrajectoryActionBuilder PickFarSpikeMark = drive.actionBuilder(shootPos1)
                .lineToXConstantHeading(PARAMS.pickFarSMX + PARAMS.intakeDriveFarX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(new Pose2d(PARAMS.pickFarSMX + PARAMS.intakeDriveFarX, PARAMS.pickFarSMY, Math.toRadians(PARAMS.startOri)))
                .lineToXConstantHeading(PARAMS.shoot2X);

        TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(shootPos2)
                .splineToConstantHeading(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(PARAMS.startOri))
                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickMidSMX + PARAMS.intakeDriveX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri)))
                        .splineToConstantHeading(new Vector2d(PARAMS.shoot3X, PARAMS.shoot3Y), Math.toRadians(PARAMS.startOri));

        TrajectoryActionBuilder LeaveLaunchZone = drive.actionBuilder(shootPos3)
                        .lineToYConstantHeading(PARAMS.leaveLaunchZoneY);



        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(


                    )
            );
            sleep(30000);
        }
    }
}