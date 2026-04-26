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
import Modules.Shoota;

@Config
@Autonomous
public class BlueFarAutoWorlds extends LinearOpMode {


    public static class Params {
        public double Color = 1.0; // blue = 1 red = -1
        public double startX = -64.1575;
        public double startY = 15.420;
        public double startOri = 0;

        //SM = Spike Mark
        //SM = Spike Mark
        public double pickMidSMX = -13.5;
        public double pickMidSMY = 31.8 * Color;
        public double pickMidSMOri = 80 * Color;
        public double intakeDriveMidX = 0;
        public double intakeDriveMidY = 16.1;
        public double openGateX = -7;
        public double openGateY = 55.3 * Color;
        public double openGateOri = 82.4 * Color;
        public double shoot1X = -55.5;
        public double shoot1Y = 15.5 * Color;
        public double shoot1Ori = 90 * Color;
        public double pickCloseSMX = -36.1;
        public double pickCloseSMY = 32.6 * Color;
        public double intakeDriveCloseX = 0;
        public double intakeDriveCloseY = 18.2 * Color;
        public double pickCorner1X = -64;
        public double pickCorner1Y = 52 * Color;
        public double pickCorner2X = -64;
        public double pickCorner2Y = 58 * Color;
        public double pickCornerRounded1X = -25;
        public double pickCornerRounded1Y = 48.5 * Color;
        public double pickCornerRounded1Ori = 90 * Color;
        public double pickCornerRounded2X = -33.5;
        public double pickCornerRounded2Y = 54.9 * Color;
        public double pickCornerRounded2Ori = 144.7 * Color;
        public double pickCornerRounded3X = -50.6;
        public double pickCornerRounded3XRedo = -53.6;
        public double pickCornerRounded3Y = 58.9 * Color;
        public double pickCornerRounded3Ori = 144.7 * Color;
        public double pickSecretTunnelX = -32.8;
        public double pickSecretTunnelY = 55.9 * Color;
        public double pickSecretTunnelOri = 22.5 * Color;
        public double endAutoX = -61;
        public double endAutoY = 31.5 * Color;
    }

    public static Params PARAMS = new Params();
    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(90 * PARAMS.Color));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori));
    Pose2d secretTunnelPos = new Pose2d(PARAMS.pickSecretTunnelX, PARAMS.pickSecretTunnelY, Math.toRadians(PARAMS.pickSecretTunnelOri));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.endAutoX, PARAMS.endAutoY, Math.toRadians(0));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        HeadingTracker.setPinpointStart(-64.1575, 15.420);
        drive = new MecanumDrive(hardwareMap, startPos);

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(startPos)
                .splineTo(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(90 * PARAMS.Color))
                .lineToYConstantHeading(PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveCloseY);

        TrajectoryActionBuilder DriveToShootClosepick = drive.actionBuilder(pickCloseSM)
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90)), Math.toRadians(-90 * PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 40));

        TrajectoryActionBuilder DriveToSecretTunnel = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(90 * PARAMS.Color))
//                .splineTo(new Vector2d(PARAMS.secretTunnel1X,PARAMS.secretTunnel1Y),Math.toRadians(PARAMS.secretTunnel1Ori))
                .splineToSplineHeading(new Pose2d(PARAMS.pickSecretTunnelX, PARAMS.pickSecretTunnelY, Math.toRadians(PARAMS.pickSecretTunnelOri)), Math.toRadians(PARAMS.pickSecretTunnelOri));

        TrajectoryActionBuilder DriveToShootSecretTunnel = drive.actionBuilder(secretTunnelPos)
                .setTangent(Math.toRadians(PARAMS.pickSecretTunnelOri - 180))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90 * PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 40));

        TrajectoryActionBuilder PickCorner1 = drive.actionBuilder(shootPos1)
                .splineToSplineHeading(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y - 10* PARAMS.Color, Math.toRadians(110 * PARAMS.Color)), Math.toRadians(90 * PARAMS.Color))
                .lineToYConstantHeading(PARAMS.pickCorner1Y);

        TrajectoryActionBuilder DriveToShoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y, Math.toRadians(90 * PARAMS.Color)))
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90 * PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color));

        TrajectoryActionBuilder DriveToShootCorner1 = drive.actionBuilder(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y, Math.toRadians(90 * PARAMS.Color)))
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90 * PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color));

        TrajectoryActionBuilder PickCorner2 = drive.actionBuilder(shootPos1)
                .splineToSplineHeading(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner2Y - 15* PARAMS.Color, Math.toRadians(110 * PARAMS.Color)), Math.toRadians(90 * PARAMS.Color))
                .lineToYConstantHeading(PARAMS.pickCorner2Y);

        TrajectoryActionBuilder DriveToShootCorner2 = drive.actionBuilder(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y, Math.toRadians(90 * PARAMS.Color)))
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90 * PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color));

        TrajectoryActionBuilder EndAuto = drive.actionBuilder(shootPos1)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y), Math.toRadians(90 * PARAMS.Color));

        TrajectoryActionBuilder PickRoundedCorner = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(0 * PARAMS.Color))
//                .splineTo(new Vector2d(PARAMS.pickCornerRounded1X, PARAMS.pickCornerRounded1Y), Math.toRadians(90* PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerRounded2X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded2Ori)), Math.toRadians(179 * PARAMS.Color))
                .lineToXConstantHeading(PARAMS.pickCornerRounded3X);

        TrajectoryActionBuilder ShootRoundedCorner = drive.actionBuilder(new Pose2d(PARAMS.pickCornerRounded3X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded3Ori)))
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90 * PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder PickRoundedCorner2 = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(0 * PARAMS.Color))
//                .splineTo(new Vector2d(PARAMS.pickCornerRounded1X, PARAMS.pickCornerRounded1Y), Math.toRadians(90* PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerRounded2X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded2Ori)), Math.toRadians(179 * PARAMS.Color))
                .lineToXConstantHeading(PARAMS.pickCornerRounded3XRedo);

        TrajectoryActionBuilder ShootRoundedCorner2 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerRounded3X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded3Ori)))
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90 * PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        waitForStart();
        Shoota.checkLimelight();
        Shoota.LimelightOffsetBlue();

        while (opModeIsActive()) {
            OCBHWM.turretServo.update();
            Actions.runBlocking(
                    new ParallelAction(new UpdateAction(UpdateActionType.UPDATE, "blue"),
                            new SequentialAction(
                                    //Shoot preload
                                    new PrepShootAction(PrepShootActionType.PREP_STARTING_SHOT, 1500, PARAMS.Color),
                                    new ShootAction(ShootaActionType.SHOOT, 750),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick close spike mark
                                    new ParallelAction(
                                            PickCloseSpikeMark.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 100, PARAMS.Color)
                                    ),

                                    //Prep close spike mark shoot
                                    new ParallelAction(
                                            DriveToShootClosepick.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 100, PARAMS.Color)
                                    ),

                                    //Shoot2
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),

                                    //Pick Rounded corner 1
                                    new ParallelAction(
                                            PickRoundedCorner.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
//                                    new SleepAction(1),
                                    //Prep Rounded Corner Shoot
                                    new ParallelAction(
                                            new SequentialAction(
                                                    new SleepAction(0.5),
                                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                                            ),
                                            ShootRoundedCorner.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, PARAMS.Color)
                                    ),
                                    //Shoot Rounded Corner  Shoot
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),


                                    //Pick Corner 1
                                    new ParallelAction(
                                            PickCorner1.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
//                                    new SleepAction(1),
                                    //Prep Corner Corner Shoot
                                    new ParallelAction(
                                            new SequentialAction(
                                                    new SleepAction(0.5),
                                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                                            ),
                                            DriveToShootCorner1.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, PARAMS.Color)
                                    ),

                                    //Shoot Corner 1 Shoot
                                    new ShootAction(ShootaActionType.SHOOT, 600),

                                    //Pick Rounded 2
                                    new ParallelAction(
//                                            DriveToSecretTunnel.build(),
                                            PickRoundedCorner2.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
//                                    new SleepAction(0.5),
                                    //Prep Rounded 2 Shoot
                                    new ParallelAction(
                                            new SequentialAction(
                                                    new SleepAction(0.5),
                                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                                            ),
                                            ShootRoundedCorner2.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 100, PARAMS.Color)
                                    ),
                                    //Shoot Rounded 2 Shoot
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick Corner 2
                                    new ParallelAction(
                                            PickCorner2.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
//                                    new SleepAction(1),
                                    //Prep Corner Corner Shoot
                                    new ParallelAction(
                                            new SequentialAction(
                                                    new SleepAction(0.5),
                                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                                            ),
                                            DriveToShootCorner2.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, PARAMS.Color)
                                    ),
                                    //Shoot Corner Corner Shoot
                                    new ShootAction(ShootaActionType.SHOOT, 600),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    EndAuto.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                            )
                    ));
            sleep(30000);
        }
    }
}