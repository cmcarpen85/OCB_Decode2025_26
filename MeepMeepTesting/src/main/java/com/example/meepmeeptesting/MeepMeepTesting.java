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
        public double startX = 0;
        public double startY = 0;
        public double startOri = -90;

        //SM = Spike Mark
        public double pickMidSMX = 16.5;
        public double pickMidSMY = -45.75;
        public double openGateX = 40;
        public double openGateY = -57;
        public double intakeDriveX = 27;
        public double intakeDriveY = 0;
        public double shoot1X = 2;
        public double shoot1Y = -8;
        public double pickCloseSMX = 14.5;
        public double pickCloseSMY = -25;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = -24;
        public double pickCornerX = 25;
        public double pickCornerY = -3; //-10
        public double pickCornerXAlt = 38;
        public double pickCornerYAlt = -10;
        public double pickCorner2X = 43;
        public double pickCorner2Y = 1;
        //        pickCornerScrape
        public double pickCornerScrape1X = 22;
        public double pickCornerScrape1Y = -0.25;
        public double pickCornerScrape1ORI = 0;
        public double pickCornerScrape2X = 36;
        public double pickCornerScrape2Y = -0.25;
        public double pickCornerScrape2ORI = 0;
        public double pickCornerScrape3X = 42.5;
        public double pickCornerScrape3Y = -0.25;
        public double pickCornerScrape3ORI = 0;

        //        pickCornerStraight
        public double pickCornerStraight1X = 20;
        public double pickCornerStraight1Y = -11;
        public double pickCornerStraight1ORI = 0;
        public double pickCornerStraight2X = 34;
        public double pickCornerStraight2Y = -5;
        public double pickCornerStraight2ORI = 0;
        public double pickCornerStraight3X = 42;
        public double pickCornerStraight3Y = -5;
        public double pickCornerStraight3ORI = 0;

        //        pickRoundedCorner
        public double pickRoundedCorner1X = 17.5;
        public double pickRoundedCorner1Y = -23;
        public double pickRoundedCorner1ORI = -25;
        public double pickRoundedCorner2X = 43;
        public double pickRoundedCorner2Y = -24;
        public double pickRoundedCorner2ORI = 59;
        public double pickRoundedCorner3X = 43;
        public double pickRoundedCorner3Y = -11;
        public double pickRoundedCorner3ORI = 59;
        public double pickRoundedCorner4X = 36;
        public double pickRoundedCorner4Y = -8;
        public double pickRoundedCorner4ORI = 0;
        public double endAutoX = 30;
        public double endAutoY = 1;
    }

    public static Params PARAMS = new Params();

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
//new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(0))

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(-90)))
//                .splineTo(new Vector2d(PARAMS.pickRoundedCorner1X, PARAMS.pickRoundedCorner1Y), Math.toRadians(PARAMS.pickRoundedCorner1ORI))
//                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerScrape1X, PARAMS.pickCornerScrape1Y), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerScrape2X, PARAMS.pickCornerScrape2Y), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(PARAMS.pickCornerScrape3X, PARAMS.pickCornerScrape3Y), Math.toRadians(0), new TranslationalVelConstraint(15), new ProfileAccelConstraint(-30, 30))
//                .lineToYConstantHeading(PARAMS.pickRoundedCorner3Y)
//                .splineToSplineHeading(new Pose2d(PARAMS.pickRoundedCorner4X, PARAMS.pickRoundedCorner4Y, Math.toRadians(PARAMS.pickRoundedCorner4ORI)), Math.toRadians(180))
                .splineTo(new Vector2d(PARAMS.pickCloseSMX, PARAMS.pickCloseSMY), Math.toRadians(0))
                .lineToXConstantHeading(PARAMS.pickCloseSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(40), new ProfileAccelConstraint(-30, 30))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}