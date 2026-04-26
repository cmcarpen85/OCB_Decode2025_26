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
import org.firstinspires.ftc.teamcode.actions.UpdateAction;
import org.firstinspires.ftc.teamcode.enums.IntakeActionType;
import org.firstinspires.ftc.teamcode.enums.PrepShootActionType;
import org.firstinspires.ftc.teamcode.enums.ShootaActionType;
import org.firstinspires.ftc.teamcode.enums.UpdateActionType;

import Modules.HeadingTracker;
import Modules.OCBHWM;


@Config
@Autonomous
public class RedCloseAutoWorlds extends LinearOpMode {

    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    public static class Params {
        public double Color = -1.0; // blue = 1 red = -1
        public double startX = 60.1;
        public double startY = 15.420 * Color;
        public double startOri = 0;

        //SM = Spike Mark
        public double shoot1X = 12.6;
        public double shoot1Y = 18.6 * Color;
        public double pickFarSMX = 9.1;
        public double pickFarSMY = 30.9 * Color;
        public double intakeDriveFarX = 0;
        public double intakeDriveFarY = 15.3 * Color;

        public double pickMidSMX = -14.6;
        public double pickMidSMY = 33 * Color;
        public double pickToGateMidX = -15.1;
        public double pickToGateMidY = 45.9 * Color;
        public double pickToGateMidOri = 66.9 * Color;
        public double openGateX = 2;
        public double openGateY = 54 * Color;
        public double leaveLaunchZoneX = 6.1;
        public double leaveLaunchZoneY = 25.3;
    }

    public static Params PARAMS = new Params();

    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90 * PARAMS.Color));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri));
    Pose2d pickFarSM = new Pose2d(PARAMS.pickFarSMX, PARAMS.pickFarSMY, Math.toRadians(90 * PARAMS.Color));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(PARAMS.startOri));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        HeadingTracker.setPinpointStart(PARAMS.startX,PARAMS.startY);

        drive = new MecanumDrive(hardwareMap, initialPos);


        TrajectoryActionBuilder PickFarSpikeMark = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(180* PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickFarSMX, PARAMS.pickFarSMY, Math.toRadians(90* PARAMS.Color)), Math.toRadians(90* PARAMS.Color), new TranslationalVelConstraint(30), new ProfileAccelConstraint(-30, 60))
                .lineToY(PARAMS.pickFarSMY + PARAMS.intakeDriveFarY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .setTangent(Math.toRadians(180* PARAMS.Color))
                .splineToConstantHeading(new Vector2d(PARAMS.openGateX,PARAMS.openGateY),Math.toRadians(90* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder DriveToShootFarSM = drive.actionBuilder(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(90 * PARAMS.Color)))
                .strafeToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y));


        // TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(shootPos2)
        //         .splineToConstantHeading(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(PARAMS.startOri))
        //         .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        // TrajectoryActionBuilder DriveToShoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickMidSMX + PARAMS.intakeDriveX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri)))
        //                 .splineToConstantHeading(new Vector2d(PARAMS.shoot3X, PARAMS.shoot3Y), Math.toRadians(PARAMS.startOri));

        TrajectoryActionBuilder LeaveLaunchZone = drive.actionBuilder(shootPos1)
                .strafeToConstantHeading(new Vector2d(PARAMS.openGateX, PARAMS.openGateY - 10 * Math.signum(PARAMS.openGateY)), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));


        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(
                    new ParallelAction(new UpdateAction(UpdateActionType.UPDATE, "blue"),
                            new SequentialAction(
                                    //Shoot preload
                                    new PrepShootAction(PrepShootActionType.PREP_CLOSE_STARTING_SHOT, 1500, PARAMS.Color),
                                    new ShootAction(ShootaActionType.SHOOT, 750),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick mid spike mark
                                    new ParallelAction(
                                            PickFarSpikeMark.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN),
                                            new PrepShootAction(PrepShootActionType.PREP_CLOSE_SHOOT, 100, PARAMS.Color)
                                    ),
                                     new IntakeAction(IntakeActionType.INTAKE_REST),
                                    new SleepAction(16),

                                    //Prep mid spike mark shoot
                                    new ParallelAction(
                                            DriveToShootFarSM.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_CLOSE_SHOOT, 100, PARAMS.Color)
                                    ),

                                    //Shoot1
                                    new SleepAction(1),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick close
                                    new ParallelAction(
                                            LeaveLaunchZone.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_REST)
                                    )
                            )
                    )
            );
            sleep(30000);
        }
    }
}