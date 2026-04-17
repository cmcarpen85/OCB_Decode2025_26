package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
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

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
//new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(0))

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(0)))
                .splineTo(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(90))
                .lineToYConstantHeading(PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY)*PARAMS.intakeDriveY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(64.3)),64.3)
//                .splineTo(new Vector2d(PARAMS.secretTunnel1X,PARAMS.secretTunnel1Y),Math.toRadians(PARAMS.secretTunnel1Ori))
                .splineToSplineHeading(new Pose2d(PARAMS.pickSecretTunnelX,PARAMS.pickSecretTunnelY,Math.toRadians(PARAMS.pickSecretTunnelOri)),Math.toRadians(PARAMS.pickSecretTunnelOri))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.shoot2ori)),PARAMS.shoot2ori)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}