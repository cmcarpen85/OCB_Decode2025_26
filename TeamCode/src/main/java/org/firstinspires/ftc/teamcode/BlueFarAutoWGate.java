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

import Modules.Intake;
import Modules.OCBHWM;

@Config
@Autonomous
public class BlueFarAutoWGate extends LinearOpMode {


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
        public double openGateY = -56.6;
        public double intakeDriveX = 27;
        public double intakeDriveY = 0;
        public double shoot1X = 2;
        public double shoot1Y = -8;
        public double pickCloseSMX = 14.5;
        public double pickCloseSMY = -27;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = -24;
        public double pickCornerX = 35;
        public double pickCornerY = -3; //-10
        public double pickCorner2X = 45;
        public double pickCorner2Y = 1;
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

        TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(startPos)
                .splineTo(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(-15))
                .splineToLinearHeading(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(0)), Math.toRadians(-15), new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));
//                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(0)))
//                .lineToXConstantHeading(PARAMS.pickMidSMX)
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(-85))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(0))
                .lineToXConstantHeading(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(30), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(new Pose2d(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, PARAMS.pickCloseSMY, Math.toRadians(0)))
//                .lineToXConstantHeading(PARAMS.pickCloseSMX)
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder DriveToShoot3 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerX, PARAMS.pickCornerY, Math.toRadians(0)))
//                .lineToXConstantHeading(PARAMS.pickCloseSMX)
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder DriveToShoot4 = drive.actionBuilder(new Pose2d(PARAMS.pickCornerX, PARAMS.pickCornerY, Math.toRadians(0)))
//                .lineToXConstantHeading(PARAMS.pickCloseSMX)
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(90));

        TrajectoryActionBuilder PickCorner1 = drive.actionBuilder(shootPos1)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerX, PARAMS.pickCornerY), Math.toRadians(70))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y), Math.toRadians(70));

        TrajectoryActionBuilder PickCorner2 = drive.actionBuilder(shootPos1)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerX, PARAMS.pickCornerY), Math.toRadians(70))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y), Math.toRadians(70));

        TrajectoryActionBuilder PickCorner3 = drive.actionBuilder(shootPos1)
                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerX, PARAMS.pickCornerY), Math.toRadians(70))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y), Math.toRadians(70));

        TrajectoryActionBuilder OpenGate = drive.actionBuilder(new Pose2d(PARAMS.pickMidSMX + PARAMS.intakeDriveX, PARAMS.pickMidSMY, Math.toRadians(0)))
                .lineToXConstantHeading(PARAMS.pickMidSMX + 8)
                .splineToConstantHeading(new Vector2d(PARAMS.openGateX, PARAMS.openGateY), Math.toRadians(0));


        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(
                            //Shoot preload
                            new PrepShootAction(PrepShootActionType.PREP_STARTING_SHOT, -1.0),
                            new SleepAction(1.75),
                            new ShootAction(ShootaActionType.SHOOTSTART, 2800),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick mid spike mark
                            new ParallelAction(
                                    PickMidSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 1000, -1.0)
                            ),

                            //Prep mid spike mark shoot
                            new ParallelAction(
                                    DriveToShoot1.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2000, -1.0)
                            ),

                            //Shoot1
                            new ShootAction(ShootaActionType.SHOOTFAR, 2800),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick close spike mark
                            new ParallelAction(
                                    PickCloseSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            //Prep close spike mark shoot
                            new ParallelAction(
                                    new SequentialAction(
                                            new SleepAction(1),
                                            new IntakeAction(IntakeActionType.INTAKE_REST)
                                    ),
                                    DriveToShoot2.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2000, -1.0)
                            ),

                            //Shoot2
                            new ShootAction(ShootaActionType.SHOOTFAR, 2800),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick Corner Corner 1
                            new ParallelAction(
                                    PickCorner1.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            //Prep Corner Corner Shoot
                            new ParallelAction(
                                    new SequentialAction(
                                            new SleepAction(1),
                                            new IntakeAction(IntakeActionType.INTAKE_REST)
                                    ),
                                    DriveToShoot3.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2000, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2800),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick Corner Corner 2
                            new ParallelAction(
                                    PickCorner2.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),
                            new IntakeAction(IntakeActionType.INTAKE_REST),
                            //Prep Corner Corner Shoot
                            new ParallelAction(
                                    DriveToShoot4.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT, 2000, -1.0)
                            ),
                            //Shoot Corner Corner Shoot
                            new ShootAction(ShootaActionType.SHOOTFAR, 2800),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick Corner Corner 3
                            new ParallelAction(
                                    PickCorner3.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            )
                    )
            );
            sleep(30000);
        }
    }
}