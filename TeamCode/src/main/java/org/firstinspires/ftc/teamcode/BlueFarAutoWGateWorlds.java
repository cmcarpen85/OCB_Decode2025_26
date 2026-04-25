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
public class BlueFarAutoWGateWorlds extends LinearOpMode {


    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    public static class Params {
        public double Color = 1.0; // blue = 1 red = -1
        public double startX = -64.1575;
        public double startY = 16.499;
        public double startOri = 0;

        //SM = Spike Mark
        //SM = Spike Mark
        public double pickMidSMX = -13.5;
        public double pickMidSMY = 31.8* Color;
        public double pickMidSMOri = 80* Color;
        public double intakeDriveMidX = 0;
        public double intakeDriveMidY = 16.1;
        public double openGateX = -7;
        public double openGateY = 55.3* Color;
        public double openGateOri = 82.4* Color;
        public double shoot1X = -55.5;
        public double shoot1Y = 15.5* Color;
        public double shoot1Ori = 90* Color;
        public double pickCloseSMX = -34.1;
        public double pickCloseSMY = 32.6* Color;
        public double intakeDriveCloseX = 0;
        public double intakeDriveCloseY = 22.2* Color;
        public double pickCorner1X = -64;
        public double pickCorner1Y = 62* Color;
        public double pickCornerRounded1X = -25;
        public double pickCornerRounded1Y = 48.5* Color;
        public double pickCornerRounded1Ori = 90* Color;
        public double pickCornerRounded2X = -33.5;
        public double pickCornerRounded2Y = 58.9* Color;
        public double pickCornerRounded2Ori = 144.7* Color;
        public double pickCornerRounded3X = -53.6;
        public double pickCornerRounded3Y = 58.9* Color;
        public double pickCornerRounded3Ori = 144.7* Color;
        public double endAutoX = -61;
        public double endAutoY = 31.5* Color;
    }

    public static Params PARAMS = new Params();

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(90* PARAMS.Color));
    Pose2d shootPos = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90* PARAMS.Color));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(90* PARAMS.Color));
    //Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(0));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        HeadingTracker.setPinpointStart(-64.1575, 16.499);

        drive = new MecanumDrive(hardwareMap, initialPos);

        TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(startPos)
                .splineToSplineHeading(new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.pickMidSMOri)), Math.toRadians(PARAMS.pickMidSMOri))
                .lineToYConstantHeading(PARAMS.pickMidSMY + Math.signum(PARAMS.pickMidSMY) * PARAMS.intakeDriveMidY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30))
                .splineToSplineHeading(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(PARAMS.openGateOri)), Math.toRadians(PARAMS.openGateOri));

        TrajectoryActionBuilder Shoot1 = drive.actionBuilder(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(PARAMS.openGateOri)))
                .setTangent(Math.toRadians(-90* PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-180* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(shootPos)
                .setTangent(Math.toRadians(0* PARAMS.Color))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveCloseY), PARAMS.pickCloseSMX);

        TrajectoryActionBuilder Shoot2 = drive.actionBuilder(new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveMidY, Math.toRadians(90* PARAMS.Color)))
                .setTangent(Math.toRadians(-90* PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-180* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder PickRoundedCorner = drive.actionBuilder(shootPos)
                .setTangent(Math.toRadians(60* PARAMS.Color))
                .splineTo(new Vector2d(PARAMS.pickCornerRounded1X, PARAMS.pickCornerRounded1Y), Math.toRadians(90* PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerRounded2X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded2Ori)), Math.toRadians(180* PARAMS.Color))
                .lineToXConstantHeading(PARAMS.pickCornerRounded3X);

        TrajectoryActionBuilder Shoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerRounded3X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded3Ori)))
                .setTangent(Math.toRadians(-90* PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder PickCorner1 = drive.actionBuilder(shootPos)
                .setTangent(Math.toRadians(90* PARAMS.Color))
                .splineTo(new Vector2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y), 90* PARAMS.Color);

        TrajectoryActionBuilder Shoot4 = drive.actionBuilder(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y, Math.toRadians(90* PARAMS.Color)))
                .setTangent(Math.toRadians(-90* PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder PickCorner2 = drive.actionBuilder(shootPos)
                .setTangent(Math.toRadians(90* PARAMS.Color))
                .splineTo(new Vector2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y), Math.toRadians(90* PARAMS.Color));

        TrajectoryActionBuilder Shoot5 = drive.actionBuilder(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y, Math.toRadians(90* PARAMS.Color)))
                .setTangent(Math.toRadians(-90* PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));


        waitForStart();
        while (opModeIsActive()) {
            OCBHWM.turretServo.update();
            Actions.runBlocking(
                    new ParallelAction(new UpdateAction(UpdateActionType.UPDATE, "blue"),
                            new SequentialAction(
                                    //Shoot preload
                                    new PrepShootAction(PrepShootActionType.PREP_STARTING_SHOT, 1500, PARAMS.Color),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick mid spike mark
                                    new ParallelAction(
                                            PickMidSpikeMark.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 100, PARAMS.Color)
                                    ),

                                    //Prep mid spike mark shoot
                                    new ParallelAction(
                                            Shoot1.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 100, PARAMS.Color)
                                    ),

                                    //Shoot1
//                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),

                                    //Pick close
                                    new ParallelAction(
                                            PickCloseSpikeMark.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
//                                    new SleepAction(1),
                                    //Prep close Shoot
                                    new ParallelAction(
                                            new SequentialAction(
                                                    new SleepAction(0.25),
                                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                                            ),
                                            Shoot2.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 100, PARAMS.Color)
                                    ),
                                    //Shoot close SM
                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick rounded corner
                                    new ParallelAction(
                                            PickRoundedCorner.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
                                    new IntakeAction(IntakeActionType.INTAKE_REST),
                                    //Prep rounded corner Shoot
                                    new ParallelAction(
                                            Shoot3.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, PARAMS.Color)
                                    ),
                                    //Shoot rounded corner Shoot
                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick Corner 1
                                    new ParallelAction(
                                            PickCorner1.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),

                                    new IntakeAction(IntakeActionType.INTAKE_REST),
                                    //Prep corner 1 Shoot
                                    new ParallelAction(
                                            Shoot4.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, PARAMS.Color)
                                    ),
                                    //Shoot corner 1 Shoot
//                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick Corner 2
                                    new ParallelAction(
                                            PickCorner2.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),

                                    new IntakeAction(IntakeActionType.INTAKE_REST),
                                    //Prep corner 2 Shoot
                                    new ParallelAction(
                                            Shoot5.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, PARAMS.Color)
                                    ),
                                    //Shoot corner 2 Shoot
//                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600)
//                                    new ShootAction(ShootaActionType.STOP),
                            )
                    ));
            sleep(30000);
        }
    }
}