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
        public double openGateY = -56.6;
        public double intakeDriveX = 27;
        public double intakeDriveY = 0;
        public double shoot1X = 2;
        public double shoot1Y = -8;
        public double pickCloseSMX = 14.5;
        public double pickCloseSMY = -27;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = -24;
        public double pickCornerX = 25;
        public double pickCornerY = -3; //-10
        public double pickCorner2X = 43;
        public double pickCorner2Y = 1;
        public double pickCornerXAlt = 25;
        public double pickCornerYAlt = -10;
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

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(0)))
                .splineToLinearHeading(new Pose2d(PARAMS.pickCornerXAlt, PARAMS.pickCornerYAlt,Math.toRadians(15)), Math.toRadians(15))
                .splineTo(new Vector2d(PARAMS.pickCorner2X, PARAMS.pickCorner2Y), Math.toRadians(0), new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30))
 .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}