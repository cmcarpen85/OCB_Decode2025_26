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
        public double pickMidSMY = -50.75;
        public double openGateX = 38;
        public double openGateY = 54.6;
        public double intakeDriveX = 29;
        public double intakeDriveY = 0;
        public double shoot1X = 3.5;
        public double shoot1Y = -14;
        public double pickCloseSMX = 14.5;
        public double pickCloseSMY = -27.5;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = -24;
        public double pickCornerX = 37.5;
        public double pickCornerY = -10;
        public double pickCorner2X = 40;
        public double pickCorner2Y = -0.5;
    }

    public static Params PARAMS = new Params();

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(PARAMS.startOri)))
                .splineTo(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians (0))
                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30))
 .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}