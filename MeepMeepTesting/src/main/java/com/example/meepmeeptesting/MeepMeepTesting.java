package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
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
        public double pickMidSMX = 15;
        public double pickMidSMY = 51;
        public double intakeDriveX = 21;
        public double intakeDriveY = 0;
        public double shoot1X = 0;
        public double shoot1Y = 2;
        public double pickCloseSMX = 15;
        public double pickCloseSMY = 27;
        public double shoot2X = 0;
        public double shoot2Y = 2;
        public double leaveLaunchZoneX = 0;
        public double leaveLaunchZoneY = 24;
    }

    public static Params PARAMS = new Params();

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.pickMidSMX + PARAMS.intakeDriveX, PARAMS.pickMidSMY, Math.toRadians(PARAMS.startOri)))
                .lineToXConstantHeading(PARAMS.pickMidSMX)
                .splineToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y), Math.toRadians(-90))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}