package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.actions.IntakeAction;
import org.firstinspires.ftc.teamcode.actions.PrepShootAction;
import org.firstinspires.ftc.teamcode.actions.ShootAction;
import org.firstinspires.ftc.teamcode.enums.IntakeActionType;
import org.firstinspires.ftc.teamcode.enums.PrepShootActionType;
import org.firstinspires.ftc.teamcode.enums.ShootaActionType;

import Modules.OCBHWM;

public class RedFarAuto extends LinearOpMode {


    Pose2d initialPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    MecanumDrive drive;

    Vector2d scoreVec = new Vector2d(14, 21);

    public static class Params {
        public double startX = 0;
        public double startY = 0;
        public double startOri = -90;

        //SM = Spike Mark
        public double pickMidSMX = 0;
        public double pickMidSMY = 0;
        public double intakeDriveX = 0;
        public double intakeDriveY = 0;
        public double shoot1X = 0;
        public double shoot1Y = 0;
        public double pickCloseSMX = 0;
        public double pickCloseSMY = 0;
        public double shoot2X = 0;
        public double shoot2Y = 0;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = 0;
    }

    public static Params PARAMS = new Params();

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(-90));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(-90));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(-90));
    Pose2d shootPos2 = new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(-90));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(-90));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();

        drive = new MecanumDrive(hardwareMap, initialPos);

        TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(PARAMS.startOri))
                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX);

        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(new Pose2d(PARAMS.pickMidSMX + PARAMS.intakeDriveX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri)))
                .lineToXConstantHeading(PARAMS.pickMidSMX)
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(PARAMS.startOri));


        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(PARAMS.startOri))
                .lineToXConstantHeading(PARAMS.pickCloseSMX + PARAMS.intakeDriveX);

        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(new Pose2d(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, PARAMS.pickCloseSMY, Math.toRadians(PARAMS.startOri)))
                .lineToXConstantHeading(PARAMS.pickCloseSMX)
                .splineToConstantHeading(new Vector2d(PARAMS.shoot2X, PARAMS.shoot2Y), Math.toRadians(PARAMS.startOri));


        TrajectoryActionBuilder LeaveLaunchZone = drive.actionBuilder(shootPos2)
                .strafeToConstantHeading(new Vector2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY));

        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(
                            //Shoot preload
                            new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT),
                            new ShootAction(ShootaActionType.SHOOT),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick mid spike mark
                            new ParallelAction(
                                    PickMidSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    new IntakeAction(IntakeActionType.INTAKE_HOLD)
                            ),

                            //Prep mid spike mark shoot
                            new ParallelAction(
                                    DriveToShoot1.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT)
                            ),

                            //Shoot1
                            new ShootAction(ShootaActionType.SHOOT),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick close spike mark
                            new ParallelAction(
                                    PickCloseSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN),
                                    new IntakeAction(IntakeActionType.INTAKE_HOLD)
                            ),

                            //Prep close spike mark shoot
                            new ParallelAction(
                                    DriveToShoot2.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT)
                            ),

                            //Shoot2
                            new ShootAction(ShootaActionType.SHOOT),
                            new ShootAction(ShootaActionType.STOP),

                            //Leave launch zone!
                            LeaveLaunchZone.build()
                    )
            );
        }
    }
}