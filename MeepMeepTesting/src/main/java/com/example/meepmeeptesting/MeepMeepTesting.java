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
        public double Color = 1.0; // blue = 1 red = -1
        public double startX = -64.1575;
        public double startY = 15.420;
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
        public double pickSecretTunnelX = -32.8;
        public double pickSecretTunnelY = 60.9 * Color;
        public double pickSecretTunnelOri = 22.5 * Color;
        public double endAutoX = -61;
        public double endAutoY = 31.5* Color;
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
               // Gate auto
//                .splineToSplineHeading(new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.pickMidSMOri)), Math.toRadians(PARAMS.pickMidSMOri))
//                .lineToYConstantHeading(PARAMS.pickMidSMY + Math.signum(PARAMS.pickMidSMY) * PARAMS.intakeDriveMidY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30))
//                .splineToSplineHeading(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(PARAMS.openGateOri)), Math.toRadians(PARAMS.openGateOri))
//                .setTangent(Math.toRadians(-90))
//                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-180), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
//                .setTangent(Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveMidY), PARAMS.pickCloseSMX)
//                .setTangent(Math.toRadians(-90))
//                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-180), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
//                .setTangent(Math.toRadians(50))
//                .splineTo(new Vector2d(PARAMS.pickCornerRounded1X, PARAMS.pickCornerRounded1Y), Math.toRadians(90))
//                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerRounded2X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded2Ori)), Math.toRadians(180))
//                .lineToXConstantHeading(PARAMS.pickCornerRounded3X)
//                .setTangent(Math.toRadians(-90))
//                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
//                .setTangent(Math.toRadians(90))
//                .splineTo(new Vector2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y), Math.toRadians(90))
//                .setTangent(Math.toRadians(-90))
//                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
//                .build());
                //Non Gate
                .splineTo(new Vector2d(PARAMS.pickCloseSMX,  PARAMS.pickCloseSMY), Math.toRadians(90 * PARAMS.Color))
                .lineToYConstantHeading(PARAMS.pickCloseSMY+ Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveCloseY)
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90)), Math.toRadians (-90), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .setTangent(Math.toRadians(90 * PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickSecretTunnelX, PARAMS.pickSecretTunnelY, Math.toRadians(PARAMS.pickSecretTunnelOri)), Math.toRadians(PARAMS.pickSecretTunnelOri))
                .setTangent(Math.toRadians(PARAMS.pickSecretTunnelOri - 180))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90*PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color),new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 40))
                .setTangent(Math.toRadians(0* PARAMS.Color))
//                .splineTo(new Vector2d(PARAMS.pickCornerRounded1X, PARAMS.pickCornerRounded1Y), Math.toRadians(90* PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerRounded2X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded2Ori)), Math.toRadians(175* PARAMS.Color))
                .lineToXConstantHeading(PARAMS.pickCornerRounded3X)
                 .setTangent(Math.toRadians(-90* PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90* PARAMS.Color), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .setTangent(Math.toRadians(90* PARAMS.Color))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y-20, Math.toRadians(110* PARAMS.Color)),Math.toRadians(90*PARAMS.Color))
                .lineToYConstantHeading(PARAMS.pickCorner1Y)
                .setTangent(Math.toRadians(-90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90*PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color))
                .setTangent(Math.toRadians(90 * PARAMS.Color))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(90*PARAMS.Color)), Math.toRadians(-90 * PARAMS.Color))
                .build());




        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}