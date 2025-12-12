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
public class BlueFarAuto extends LinearOpMode {


    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    public static class Params {
        public double startX = 0;
        public double startY = 0;
        public double startOri = -90;

        //SM = Spike Mark
        public double pickMidSMX = 16.5;
        public double pickMidSMY = -45.75;
        public double openGateX = 40;
        public double openGateY = -57;
        public double intakeDriveX = 27;
        public double intakeDriveY = 0;
        public double shoot1X = 2;
        public double shoot1Y = -8;
        public double pickCloseSMX = 14.5;
        public double pickCloseSMY = -25;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = -24;
        public double pickCornerX = 25;
        public double pickCornerY = -3; //-10
        public double pickCornerXAlt = 38;
        public double pickCornerYAlt = -10;
        public double pickCorner2X = 43;
        public double pickCorner2Y = 1;

        //        pickCornerScrape
        public double pickCornerScrape1X = 22;
        public double pickCornerScrape1Y = -2;
        public double pickCornerScrape1ORI = 30;
        public double pickCornerScrape2X = 36;
        public double pickCornerScrape2Y = -2;
        public double pickCornerScrape2ORI = 0;
        public double pickCornerScrape3X = 42.5;
        public double pickCornerScrape3Y = -2;
        public double pickCornerScrape3ORI = 0;

        //        pickCornerStraight
        public double pickCornerStraight1X = 20;
        public double pickCornerStraight1Y = -11;
        public double pickCornerStraight1ORI = 0;
        public double pickCornerStraight2X = 34;
        public double pickCornerStraight2Y = -5;
        public double pickCornerStraight2ORI = 0;
        public double pickCornerStraight3X = 42;
        public double pickCornerStraight3Y = -5;
        public double pickCornerStraight3ORI = 0;

        //        pickRoundedCorner
        public double pickRoundedCorner1X = 17.5;
        public double pickRoundedCorner1Y = -23;
        public double pickRoundedCorner1ORI = -25;
        public double pickRoundedCorner2X = 43;
        public double pickRoundedCorner2Y = -24;
        public double pickRoundedCorner2ORI = 59;
        public double pickRoundedCorner3X = 43;
        public double pickRoundedCorner3Y = -10;
        public double pickRoundedCorner3ORI = 59;
        public double pickRoundedCorner4X = 38;
        public double pickRoundedCorner4Y = -8;
        public double pickRoundedCorner4ORI = 0;
        public double endAutoX = 30;
        public double endAutoY = 1;
    }

    public static Params PARAMS = new Params();

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(0));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(0));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(0));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(0));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();

        drive = new MecanumDrive(hardwareMap, initialPos);

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(startPos)
//                .splineToSplineHeading(new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY,Math.toRadians(-15)), Math.toRadians(-15))
//                .splineToSplineHeading(new Pose2d(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, PARAMS.pickCloseSMY, Math.toRadians(0)), Math.toRadians(-15), new TranslationalVelConstraint(60), new ProfileAccelConstraint(-30, 30));
                .splineTo(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(0))
                .lineToXConstantHeading(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShootCloseSPKM = drive.actionBuilder(new Pose2d(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, PARAMS.pickCloseSMY, Math.toRadians(0)))
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder DriveToShootRounded = drive.actionBuilder(new Pose2d(PARAMS.pickRoundedCorner4X, PARAMS.pickRoundedCorner4Y, Math.toRadians(0)))
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder DriveToShootScrape = drive.actionBuilder(new Pose2d(PARAMS.pickCornerScrape3X, PARAMS.pickCornerScrape3Y, Math.toRadians(0)))
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder PickCornerRounded = drive.actionBuilder(shootPos1)
                .splineTo(new Vector2d(PARAMS.pickRoundedCorner1X, PARAMS.pickRoundedCorner1Y), Math.toRadians(PARAMS.pickRoundedCorner1ORI))
                .splineToSplineHeading(new Pose2d(PARAMS.pickRoundedCorner2X, PARAMS.pickRoundedCorner2Y, Math.toRadians(PARAMS.pickRoundedCorner2ORI)), Math.toRadians(90))
                .lineToYConstantHeading(PARAMS.pickRoundedCorner3Y)
                .splineToSplineHeading(new Pose2d(PARAMS.pickRoundedCorner4X, PARAMS.pickRoundedCorner4Y, Math.toRadians(PARAMS.pickRoundedCorner4ORI)), Math.toRadians(180));

        TrajectoryActionBuilder PickCornerScrape = drive.actionBuilder(shootPos1)
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerScrape1X, PARAMS.pickCornerScrape1Y, Math.toRadians(PARAMS.pickCornerScrape1ORI)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerScrape2X, PARAMS.pickCornerScrape2Y), Math.toRadians(PARAMS.pickCornerScrape2ORI))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerScrape3X, PARAMS.pickCornerScrape3Y, Math.toRadians(0)), Math.toRadians(0));

        TrajectoryActionBuilder PickCornerStraight = drive.actionBuilder(shootPos1)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerStraight1X, PARAMS.pickCornerStraight1Y), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerStraight2X, PARAMS.pickCornerStraight2Y), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerStraight3X, PARAMS.pickCornerStraight3Y), Math.toRadians(0), new TranslationalVelConstraint(15), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder EndDrive = drive.actionBuilder(new Pose2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y, Math.toRadians(0)))
                .splineToConstantHeading(new Vector2d(PARAMS.endAutoX, PARAMS.endAutoY), Math.toRadians(-180));

        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(
                            //Shoot preload
                            new PrepShootAction(PrepShootActionType.PREP_STARTING_SHOT, 1750,-1.0),
                            new ShootAction(ShootaActionType.SHOOTSTART, 2500),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick close spike mark
                            new ParallelAction(
                                    PickCloseSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2000, -1.0)
                            ),

                            //Prep close spike mark shoot
                            new ParallelAction(
                                    DriveToShootCloseSPKM.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),

                            //Shoot2
                            new ShootAction(ShootaActionType.SHOOTFAR, 2500),
                            new ShootAction(ShootaActionType.STOP),
                            new IntakeAction(IntakeActionType.INTAKE_IN),

                            //Pick Corner Corner 1
                            new ParallelAction(
                                    PickCornerRounded.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new SleepAction(1),
                            //Prep Corner Corner Shoot
                            new ParallelAction(
                                    new SequentialAction(
                                            new SleepAction(0.25),
                                            new IntakeAction(IntakeActionType.INTAKE_REST)
                                    ),
                                    DriveToShootRounded.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2500),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick Corner Corner 2
                            new ParallelAction(
                                    PickCornerScrape.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new SleepAction(1),
                            new IntakeAction(IntakeActionType.INTAKE_REST),
                            //Prep Corner Corner Shoot
                            new ParallelAction(
                                    DriveToShootScrape.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2500),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick Corner Corner 3
                            new ParallelAction(
                                    PickCornerStraight.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            )
                    )
            );
            sleep(30000);
        }
    }
}