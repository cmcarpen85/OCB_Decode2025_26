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
        public double startX = -64.1575;
        public double startY = 16.499;
        public double startOri = 0;

        //SM = Spike Mark
        public double pickCloseSMX = -36.6;
        public double pickCloseSMY = 37.1;
        public double intakeDriveX = 0;
        public double intakeDriveY = 21;
        public double shoot1X = -55.6;
        public double shoot1Y = 12;
        public double shoot1Ori = 64.3;
        public double secretTunnel1X = -40.6;
        public double secretTunnel1Y = 47.4;
        public double secretTunnel1Ori = 64.3;
        public double pickSecretTunnelX = -32.8;
        public double pickSecretTunnelY = 60.9;
        public double pickSecretTunnelOri = 22.5;
        public double shoot2X = -62;
        public double shoot2Y = 21.4;
        public double shoot2ori = 90;
        public double pickCornerX = -63.2;
        public double pickCornerY = 57.5;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = 0;
    }

    public static Params PARAMS = new Params();
    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(0));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori));
    Pose2d secretTunnelPos = new Pose2d(PARAMS.pickSecretTunnelX, PARAMS.pickSecretTunnelY, Math.toRadians(PARAMS.pickSecretTunnelOri));
    Pose2d shootPos2 = new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.shoot2ori));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(0));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        HeadingTracker.setPinPointXY(-64.1575, 16.499);
        drive = new MecanumDrive(hardwareMap, startPos);

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(startPos)
                .splineTo(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(90))
                .lineToYConstantHeading(PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY)*PARAMS.intakeDriveY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(pickCloseSM)
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)),PARAMS.shoot1Ori);

        TrajectoryActionBuilder DriveToSecretTunnel = drive.actionBuilder(shootPos1)
                .splineToSplineHeading(new Pose2d(PARAMS.pickSecretTunnelX,PARAMS.pickSecretTunnelY,Math.toRadians(PARAMS.pickSecretTunnelOri)),Math.toRadians(PARAMS.pickSecretTunnelOri));

        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(secretTunnelPos)
                .splineToLinearHeading(new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.shoot2ori)),PARAMS.shoot2ori);

        waitForStart();
        while (opModeIsActive()) {
            OCBHWM.turretServo.update();
            Actions.runBlocking(
                    new ParallelAction(new UpdateAction(UpdateActionType.UPDATE,"blue"),
                    new SequentialAction(
                            //Shoot preload
                            new PrepShootAction(PrepShootActionType.PREP_SHOOT, 1500),
                            new ShootAction(ShootaActionType.SHOOT, 1000),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick close spike mark
                            new ParallelAction(
                                    PickCloseSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    new PrepShootAction(PrepShootActionType.PREP_SHOOT, 100)
                            ),

                            //Prep close spike mark shoot
                            new ParallelAction(
//                                    DriveToShootCloseSPKM.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_SHOOT,100)
                            ),

                            //Shoot2
                            new ShootAction(ShootaActionType.SHOOTFAR, 2000),
                            new ShootAction(ShootaActionType.STOP),
                            new IntakeAction(IntakeActionType.INTAKE_IN),

                            //Pick Corner Corner 1
                            new ParallelAction(
//                                    PickCornerRounded.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new SleepAction(1),
                            //Prep Corner Corner Shoot
                            new ParallelAction(
                                    new SequentialAction(
                                            new SleepAction(0.25),
                                            new IntakeAction(IntakeActionType.INTAKE_REST)
                                    ),
//                                    DriveToShootRounded.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2000),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick Corner Corner 2
                            new ParallelAction(
//                                    PickCornerScrape.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new SleepAction(1),
                            new IntakeAction(IntakeActionType.INTAKE_REST),
                            //Prep Corner Corner Shoot
                            new ParallelAction(
//                                    DriveToShootScrape.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2000),
                            new ShootAction(ShootaActionType.STOP),

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