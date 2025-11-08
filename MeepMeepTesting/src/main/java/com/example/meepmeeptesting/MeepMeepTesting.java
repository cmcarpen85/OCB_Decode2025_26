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
        public double startOri = 0;

        //SM = Spike Mark
        public double shoot1X = 0;
        public double shoot1Y = 50.09;
        public double pickFarSMX = 18.4;
        public double pickFarSMY = 50.09;
        public double intakeDriveFarX = 21.5; //39.9076
        public double shoot2X = 0;
        public double shoot2Y = 50.09;
        public double pickMidSMX = 18.4;
        public double pickMidSMY = 73.25;
        public double intakeDriveX = 27.2; //45.5875
        public double shoot3X = 0;
        public double shoot3Y = 50.09;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = 64;
    }

    public static Params PARAMS = new Params();

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.shoot2X, PARAMS.shoot2Y, Math.toRadians(PARAMS.startOri)))
                .splineToConstantHeading(new Vector2d(PARAMS.pickMidSMX, PARAMS.pickMidSMY), Math.toRadians(PARAMS.startOri))
                .lineToXConstantHeading(PARAMS.pickMidSMX + PARAMS.intakeDriveX, new TranslationalVelConstraint(20), new ProfileAccelConstraint(-30, 30))
 .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}