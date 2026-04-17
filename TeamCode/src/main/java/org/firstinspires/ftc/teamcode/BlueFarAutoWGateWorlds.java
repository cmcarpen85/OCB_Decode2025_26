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
public class BlueFarAutoWGateWorlds extends LinearOpMode {


    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    public static class Params {
        public double startX = -64.1575;
        public double startY = 16.499;
        public double startOri = 0;

        //SM = Spike Mark
        public double pickMidSMX = -12.1;
        public double pickMidSMY = 31.8;
        public double intakeDriveMidX = 0;
        public double intakeDriveMidY = 16.1;
        public double openGateX = -7;
        public double openGateY = 55.3;
        public double openGateOri = 82.4;
        public double driveToShootX = -23.6;
        public double driveToShootY = 24.7;
        public double driveToShootOri = 40;
        public double shoot1X = -53.7;
        public double shoot1Y = 12;
        public double shoot1Ori = 59.6;
        public double pickCloseSMX = -36.1;
        public double pickCloseSMY = 32.6;
        public double intakeDriveCloseX = 0;
        public double intakeDriveCloseY = 27.2;
        public double shoot2X = -55.1;
        public double shoot2Y = 15.4;
        public double shoot2Ori = 25.5;
        public double driveToCornerPickX = -34.6;
        public double driveToCornerPickY = 35.2;
        public double driveToCornerPickOri = 115.9;
        public double pickCorner1X = -54;
        public double pickCorner1Y = 62;
        public double pickCorner1YOri = 171.5;
        public double shoot3X = -59;
        public double shoot3Y = 19.1;
        public double pickCorner2X = -60.8;
        public double pickCorner2Y = 55.4;
        public double pickCorner2YOri = 109;
        public double shoot4X = -61.2;
        public double shoot4Y = 22.1;
        public double endAutoX = -61;
        public double endAutoY = 31.5;
    }

    public static Params PARAMS = new Params();

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(0));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(0));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(0));
    //Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(0));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);


        drive = new MecanumDrive(hardwareMap, initialPos);

        TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(startPos)
                .splineTo(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(-15))
                .splineToLinearHeading(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(0)), Math.toRadians(-15), new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));
//                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(0)))
//                .lineToXConstantHeading(PARAMS.pickMidSMX)
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(-85));
//                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(0))
//                .lineToXConstantHeading(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(30), new ProfileAccelConstraint(-30, 30));

//        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(new Pose2d(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, PARAMS.pickCloseSMY, Math.toRadians(0)))
////                .lineToXConstantHeading(PARAMS.pickCloseSMX)
//                .setTangent(Math.toRadians(180))
//                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

//        TrajectoryActionBuilder DriveToShoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerX, PARAMS.pickCornerY, Math.toRadians(0)))
////                .lineToXConstantHeading(PARAMS.pickCloseSMX)
//                .setTangent(Math.toRadians(180))
//                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));
//
//        TrajectoryActionBuilder DriveToShoot4 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerX, PARAMS.pickCornerY, Math.toRadians(0)))
////                .lineToXConstantHeading(PARAMS.pickCloseSMX)
//                .setTangent(Math.toRadians(180))
//                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));
//
//        TrajectoryActionBuilder PickCorner2 = drive.actionBuilder(shootPos1)
//                .splineToLinearHeading(new Pose2d(PARAMS.pickCornerX, PARAMS.pickCornerY,Math.toRadians(15)), Math.toRadians(15))
//                .splineTo(new Vector2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y), Math.toRadians(0), new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));
//
//        TrajectoryActionBuilder PickCorner3 = drive.actionBuilder(shootPos1)
//                .splineTo(new Vector2d(PARAMS.pickCornerX, PARAMS.pickCornerY), Math.toRadians(15))
//                .splineTo(new Vector2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y), Math.toRadians(0));

        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(
                            //Shoot preload
                            new PrepShootAction(PrepShootActionType.PREP_STARTING_SHOT,1750, -1.0),
                            new ShootAction(ShootaActionType.SHOOTSTART, 2500),


                            //Pick mid spike mark
                            new ParallelAction(
                                    new ShootAction(ShootaActionType.STOP,200),
                                    PickMidSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2000, -1.0)
                            ),

                            //Prep mid spike mark shoot
                            new ParallelAction(
                                    DriveToShoot1.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),

                            //Shoot1
                            new ShootAction(ShootaActionType.SHOOTFAR, 2000),

                            //Pick close spike mark
                            new ParallelAction(
                                    new ShootAction(ShootaActionType.STOP,200),
                                    PickCloseSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            //Prep close spike mark shoot
                            new ParallelAction(
                                    new SequentialAction(
                                            new SleepAction(.5),
                                            new IntakeAction(IntakeActionType.INTAKE_REST)
                                    ),
//                                    DriveToShoot2.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),

                            //Shoot2
                            new ShootAction(ShootaActionType.SHOOTFAR, 2000),

                            //Pick Corner Corner 1
                            new ParallelAction(
                                    new ShootAction(ShootaActionType.STOP,200),
//                                    PickCornerRounded.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new SleepAction(0.5),
                            new SequentialAction(
                                           new SleepAction(0),
                                    new IntakeAction(IntakeActionType.INTAKE_REST)
                            ),
                            //Prep Corner Corner Shoot
                            new ParallelAction(

                                 //   DriveToShoot3.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2000),

                            //Pick Corner Corner 2
                            new ParallelAction(
                                    new ShootAction(ShootaActionType.STOP,200),
                                   // PickCorner2.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new IntakeAction(IntakeActionType.INTAKE_REST),

                            //Prep Corner Corner Shoot
                            new ParallelAction(
//                                    EndDrive.build()
//                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2500, -1.0)
                            )
//                            //Shoot Corner Corner Shoot
//                            new ShootAction(ShootaActionType.SHOOTFAR, 2800),
//                            new ShootAction(ShootaActionType.STOP),
//
//
//                            //Pick Corner Corner 3
//                            new ParallelAction(
//                                    PickCorner3.build(),
//                                    new IntakeAction(IntakeActionType.INTAKE_IN)
//                            )
                    )
            );
            sleep(30000);
        }
    }
}