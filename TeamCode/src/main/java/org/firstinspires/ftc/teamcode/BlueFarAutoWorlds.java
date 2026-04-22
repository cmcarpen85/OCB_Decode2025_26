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
public class BlueFarAutoWorlds extends LinearOpMode {


    public static class Params {

        public double Color = 1.0; // blue = 1 red = -1
        public double startX = -64.1575;
        public double startY = 16.499;
        public double startOri = 0;

        //SM = Spike Mark
        public double pickCloseSMX = -38.0;
        public double pickCloseSMY = 37.1* Color;
        public double intakeDriveX = 0;
        public double intakeDriveY = 21;
        public double shoot1X = -55.6;
        public double shoot1Y = 12* Color;
        public double shoot1Ori = 64.3* Color;
        public double secretTunnel1X = -40.6;
        public double secretTunnel1Y = 47.4* Color;
        public double secretTunnel1Ori = 64.3* Color;
        public double pickSecretTunnelX = -32.8;
        public double pickSecretTunnelY = 60.9* Color;
        public double pickSecretTunnelOri = 22.5* Color;
        public double shoot2X = -62;
        public double shoot2Y = 21.4* Color;
        public double shoot2ori = 90* Color;
        public double pickCornerX = -63.2;
        public double pickCornerY = 57.5* Color;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = 0;
    }

    public static Params PARAMS = new Params();
    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori));
    Pose2d secretTunnelPos = new Pose2d(PARAMS.pickSecretTunnelX, PARAMS.pickSecretTunnelY, Math.toRadians(PARAMS.pickSecretTunnelOri));
    Pose2d shootPos2 = new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.shoot2ori));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(0));



    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
//        HeadingTracker.setPinPointXY(-64.1575, 16.499);
        drive = new MecanumDrive(hardwareMap, startPos);
        HeadingTracker.setPinpointStart(-64.1575, 16.499);

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(startPos)
                .splineTo(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(90*PARAMS.Color))
                .lineToYConstantHeading(PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30));


        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(pickCloseSM)
                .setTangent(Math.toRadians(-90*PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90)), Math.toRadians(64.3 - 180), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60));

        TrajectoryActionBuilder DriveToSecretTunnel = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(90*PARAMS.Color))
//                .splineTo(new Vector2d(PARAMS.secretTunnel1X,PARAMS.secretTunnel1Y),Math.toRadians(PARAMS.secretTunnel1Ori))
                .splineToSplineHeading(new Pose2d(PARAMS.pickSecretTunnelX, PARAMS.pickSecretTunnelY, Math.toRadians(PARAMS.pickSecretTunnelOri)), Math.toRadians(PARAMS.pickSecretTunnelOri));

        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(secretTunnelPos)
                .setTangent(Math.toRadians(PARAMS.pickSecretTunnelOri - 180))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.shoot2ori)), Math.toRadians(-150*PARAMS.Color));

        TrajectoryActionBuilder PickCorner1 = drive.actionBuilder(shootPos2)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerX, PARAMS.pickCornerY), Math.toRadians(90*PARAMS.Color));

        TrajectoryActionBuilder DriveToShoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerX, PARAMS.pickCornerY, Math.toRadians(90)))
                .setTangent(Math.toRadians(-90*PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.shoot2ori)), Math.toRadians(-90*PARAMS.Color));

        TrajectoryActionBuilder EndAuto = drive.actionBuilder(shootPos2)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerX, PARAMS.pickCornerY), Math.toRadians(90*PARAMS.Color));

        waitForStart();
        while (opModeIsActive()) {
            OCBHWM.turretServo.update();
            Actions.runBlocking(
                    new ParallelAction(new UpdateAction(UpdateActionType.UPDATE, "blue"),
                            new SequentialAction(
                                    //Shoot preload
                                    new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1500),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick close spike mark
                                    new ParallelAction(
                                            PickCloseSpikeMark.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000)
                                    ),

                                    //Prep close spike mark shoot
                                    new ParallelAction(
                                            DriveToShoot1.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000)
                                    ),

                                    //Shoot2
                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),

                                    //Pick secret tunnel 1
                                    new ParallelAction(
                                            DriveToSecretTunnel.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
                                    new SleepAction(1),
                                    //Prep secret tunnel Shoot
                                    new ParallelAction(
                                            new SequentialAction(
                                                    new SleepAction(0.25),
                                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                                            ),
                                    DriveToShoot2.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 1000, -1.0)
                                    ),
                                    //Shoot secret tunnel Shoot
                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick corner 1
                                    new ParallelAction(
                                    PickCorner1.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    ),
                                    new IntakeAction(IntakeActionType.INTAKE_REST),
                                    //Prep Corner Corner Shoot
                                    new ParallelAction(
                                    DriveToShoot3.build(),
                                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1000, -1.0)
                                    ),
                                    //Shoot Corner Corner Shoot
                                    new SleepAction(.5),
                                    new ShootAction(ShootaActionType.SHOOT, 600),
//                                    new ShootAction(ShootaActionType.STOP),

                                    //Pick Corner Corner 3
                                    new ParallelAction(
//                                    PickCornerStraight.build(),
                                            new IntakeAction(IntakeActionType.INTAKE_IN)
                                    )
                            )
                    ));
            sleep(30000);
        }
    }
}