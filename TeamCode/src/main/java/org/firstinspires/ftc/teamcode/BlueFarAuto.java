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
        public double startOri = 0;

        //SM = Spike Mark
        public double pickMidSMX = 15;
        public double pickMidSMY = -47;
        public double intakeDriveX = 27;
        public double intakeDriveY = 0;
        public double shoot1X = 0;
        public double shoot1Y = -2;
        public double pickCloseSMX = 15;
        public double pickCloseSMY = -27;
        public double shoot2X = 0;
        public double shoot2Y = -2;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = -24;
    }

    public static Params PARAMS = new Params();

    Pose2d startPos = new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri));
    Pose2d pickMidSM = new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos1 = new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.startOri));
    Pose2d pickCloseSM = new Pose2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY, Math.toRadians(PARAMS.startOri));
    Pose2d shootPos2 = new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.startOri));
    Pose2d leavelaunchZone = new Pose2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY, Math.toRadians(PARAMS.startOri));


    @Override
    public void runOpMode() {
        OCBHWM.hwinit(hardwareMap);
        OCBHWM.imu.init();

        drive = new MecanumDrive(hardwareMap, initialPos);

        TrajectoryActionBuilder PickMidSpikeMark = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(PARAMS.startOri))
                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX,new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot1 = drive.actionBuilder(new Pose2d(PARAMS.pickMidSMX + PARAMS.intakeDriveX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri)))
                .lineToXConstantHeading(PARAMS.pickMidSMX)
                .splineToConstantHeading(new Vector2d(PARAMS.startX, PARAMS.startY), Math.toRadians(90));


        TrajectoryActionBuilder PickCloseSpikeMark = drive.actionBuilder(shootPos1)
                .setTangent(Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(PARAMS.startOri))
                .lineToXConstantHeading(PARAMS.pickCloseSMX + PARAMS.intakeDriveX,new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30));

        TrajectoryActionBuilder DriveToShoot2 = drive.actionBuilder(new Pose2d(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, PARAMS.pickCloseSMY, Math.toRadians(PARAMS.startOri)))
                .lineToXConstantHeading(PARAMS.pickCloseSMX)
                .splineToConstantHeading(new Vector2d(PARAMS.startX, PARAMS.startY), Math.toRadians(90));


        TrajectoryActionBuilder LeaveLaunchZone = drive.actionBuilder(shootPos2)
                .strafeToConstantHeading(new Vector2d(PARAMS.leaveLaunchZoneX, PARAMS.leaveLaunchZoneY));

        waitForStart();
        while (opModeIsActive()) {
            Actions.runBlocking(new SequentialAction(
                            //Shoot preload
                   new PrepShootAction(PrepShootActionType.PREP_STARTING_SHOT,-1.0),
                    new SleepAction(1.5),
                    new ShootAction(ShootaActionType.SHOOT),
                    new ShootAction(ShootaActionType.STOP),

                    //Pick mid spike mark
                    new ParallelAction(
                            PickMidSpikeMark.build(),
                            new IntakeAction(IntakeActionType.INTAKE_IN)
                    ),

                            //Prep mid spike mark shoot
                            new ParallelAction(
                                    DriveToShoot1.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT,-1.0)
                            ),

                            //Shoot1
                            new ShootAction(ShootaActionType.SHOOT),
                            new ShootAction(ShootaActionType.STOP),

                            //Pick close spike mark
                            new ParallelAction(
                                    PickCloseSpikeMark.build(),
                                    new IntakeAction(IntakeActionType.INTAKE_IN)
                            ),

                            //Prep close spike mark shoot
                            new ParallelAction(
                                    DriveToShoot2.build(),
                                    new PrepShootAction(PrepShootActionType.PREP_FAR_SHOOT,-1.0)
                            ),

                            //Shoot2
                            new ShootAction(ShootaActionType.SHOOT),
                            new ShootAction(ShootaActionType.STOP),

                            //Leave launch zone!
                            LeaveLaunchZone.build()
                    )
            );
            sleep(30000);
        }
    }
}