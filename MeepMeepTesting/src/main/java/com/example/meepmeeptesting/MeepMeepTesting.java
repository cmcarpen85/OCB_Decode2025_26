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
        //SM = Spike Mark
        public double pickMidSMX = -12.1;
        public double pickMidSMY = 31.8;
        public double pickMidSMOri = 80;
        public double intakeDriveMidX = 0;
        public double intakeDriveMidY = 16.1;
        public double openGateX = -7;
        public double openGateY = 55.3;
        public double openGateOri = 82.4;
        public double driveToShootX = -23.6;
        public double driveToShootY = 24.7;
        public double driveToShootOri = 40;
        public double shoot1X = -55.5;
        public double shoot1Y = 15.5;
        public double shoot1Ori = 90;
        public double pickCloseSMX = -36.1;
        public double pickCloseSMY = 32.6;
        public double intakeDriveCloseX = 0;
        public double intakeDriveCloseY = 27.2;
        public double shoot2X = -55.5;
        public double shoot2Y = 15.5;
        public double shoot2Ori = 25.5;
        public double driveToCornerPickX = -34.6;
        public double driveToCornerPickY = 35.2;
        public double driveToCornerPickOri = 115.9;
        public double pickCorner1X = -54;
        public double pickCorner1Y = 62;
        public double pickCorner1Ori = 171.5;
        public double shoot3X = -55.5;
        public double shoot3Y = 15.5;
        public double pickCorner2X = -60.8;
        public double pickCorner2Y = 55.4;
        public double pickCorner2Ori = 109;
        public double shoot4X = -55.5;
        public double shoot4Y = 15.5;
        public double pickCornerRounded1X = -25;
        public double pickCornerRounded1Y = 48.5;
        public double pickCornerRounded1Ori = 90;
        public double pickCornerRounded2X = -33.5;
        public double pickCornerRounded2Y = 58.9;
        public double pickCornerRounded2Ori = 144.7;
        public double pickCornerRounded3X = -50.6;
        public double pickCornerRounded3Y = 58.9;
        public double pickCornerRounded3Ori = 144.7;
        public double endAutoX = -61;
        public double endAutoY = 31.5;
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
                .splineToSplineHeading(new Pose2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.pickMidSMOri)), Math.toRadians(PARAMS.pickMidSMOri))
                .lineToYConstantHeading(PARAMS.pickMidSMY + Math.signum(PARAMS.pickMidSMY) * PARAMS.intakeDriveMidY, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30))
                .splineToLinearHeading(new Pose2d(PARAMS.openGateX, PARAMS.openGateY, Math.toRadians(PARAMS.openGateOri)), Math.toRadians(PARAMS.openGateOri))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-180), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .setTangent(Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY + Math.signum(PARAMS.pickCloseSMY) * PARAMS.intakeDriveMidY), PARAMS.pickCloseSMX)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-180), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .setTangent(Math.toRadians(90))
                .splineTo(new Vector2d(PARAMS.pickCornerRounded1X, PARAMS.pickCornerRounded1Y), Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(PARAMS.pickCornerRounded2X, PARAMS.pickCornerRounded2Y, Math.toRadians(PARAMS.pickCornerRounded2Ori)), Math.toRadians(-180))
                .lineToXConstantHeading(PARAMS.pickCornerRounded3X)
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .setTangent(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(PARAMS.pickCorner1X, PARAMS.pickCorner1Y))
                .setTangent(Math.toRadians(-90))
                .splineToLinearHeading(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(PARAMS.shoot1Ori)), Math.toRadians(-90), new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 60))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}