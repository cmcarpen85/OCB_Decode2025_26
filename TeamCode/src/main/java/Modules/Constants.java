package Modules;

public class Constants {
    //camara
    public static double FARSHOT_TA = 0.3;
    public static double CLOSESHOT_TA = 0.6;
    public static double GOALHEIGHT = 29.5; //inches
    public static double CAMERAHEIGHT = 15.75; //inches
    public static double CAMERAANGLE = 18.989; //degrees

    //HuskyLens
    public static double LEFTBOUND = 0;
    public static double RIGHTBOUND = 0;


    //FlyWheel
    public static double COASTSPEED = 0.3;
    public static double FARSHOTSPEED = 0.642; // 0.635
    public static double FARSHOTSPEEDOPPO = 0.61;
    public static double FARSHOTVEL = 1560;
    public static double MIDSHOTSPEED = 0.475;
    public static double MIDSHOTVEL = 1160;
    public static double CLOSESHOTSPEED = 0.4;
    public static double STARTSHOTSPEED = 0.60;
    public static double AUTOSHOTSPEED = .59;

    //Hood
    public static double HOODHOME = 0.05;
    public static double HOODMINSERVOVALUE = 0.04;
    public static double HOODMAXSERVOVALUE = .6089;
    public static double HOODTARGETTOLERANCE = 1;
    public static double FARSHOTHOODSERVO = 0.608;
    public static double MIDSHOTHOODSERVO = .3594;
    public static double CLOSESHOTHOODSERVO = 0.05;
    public static double STARTSHOTHOODSERVO = 0.608;

    //Turret
    public static double TURRETHOME = 177.5; // 172.8
    public static double TURRETMINANGLE = -80;
    public static double TURRETMAXANGLE = 80;
    public static double TURRETMINSERVOVALUE = 0;
    public static double TURRETMAXSERVOVALUE = 1;
    public static double TURRETANGLETOLERANCE = 0.5; // 1

    public static double TELEFARSHOTTURRETANGLE = -138;//27.9
    public static double TELEFARSHOTTURRETANGLEOPPO = 83;
    public static double MIDSHOTTURRETANGLE = 40.0; //38
    public static double CLOSESHOTTURRETANGLE = 0;
    public static double STARTSHOTTURRETANGLE = 18; //
    public static double AUTOFARSHOTTURRETANGLE = -88; //-65
    public static double REDAUTOTURRETOFFEST = 11;
    public static double TURRETDYNAMIC = 200;

    //Transfer
    public static double TRANSFERPOWER = 0.8; //1
    public static double GATEPOWER = 1;
    public static double KICKERPOWER = 1;

    //Intake
    public static double INTAKEPOWER = 0.8; //1
    public static double AUTOINTAKEPOWER = 1; //1
    public static double INTAKEHOLD = 0.3;
    public static double PASSTIME = 300;

    //Tilt
    public static double TILTHOME = 0.05;
    public static double TILTUP = 0.5;
}
