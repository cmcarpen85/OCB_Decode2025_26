package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTestingClose {
    public static class Params {
        public double startX = 59.9121;
        public double startY = 15.420;
        public double startOri = 0;

        //SM = Spike Mark
        public double shoot1X = 12.6;
        public double shoot1Y = 18.6;
        public double pickFarSMX = 9.1;
        public double pickFarSMY = 30.9;
        public double intakeDriveFarX = 0;
        public double intakeDriveFarY = 17.3;
        public double shoot2X = 11;
        public double shoot2Y = 22.7;
        public double shoot2Ori = 109;
        public double pickMidSMX = -14.6;
        public double pickMidSMY = 33;
        public double pickToGateMidX = -15.1;
        public double pickToGateMidY = 45.9;
        public double pickToGateMidOri = 66.9;
        public double openGateX = 0;
        public double openGateY = 53;
        public double shoot3X = 5.5;
        public double shoot3Y = 15.8;
        public double leaveLaunchZoneX = 6.1;
        public double leaveLaunchZoneY = 25.3;
    }

    public static Params PARAMS = new Params();

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(70, 80, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
//new Pose2d(PARAMS.shoot1X, PARAMS.shoot1Y, Math.toRadians(0))

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(PARAMS.startX, PARAMS.startY, Math.toRadians(0)))
                .setTangent(Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(PARAMS.pickFarSMX, PARAMS.pickFarSMY,Math.toRadians(90)),Math.toRadians(90))
                .lineToY(PARAMS.pickFarSMY+ PARAMS.intakeDriveFarY)
                .strafeToConstantHeading(new Vector2d(PARAMS.openGateX,PARAMS.openGateY))
                .strafeToConstantHeading(new Vector2d(PARAMS.shoot1X, PARAMS.shoot1Y))
                .strafeToConstantHeading(new Vector2d(PARAMS.openGateX,PARAMS.openGateY-10))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}