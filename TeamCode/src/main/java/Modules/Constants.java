package Modules;


import com.acmerobotics.dashboard.config.Config;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
@Config
public class Constants {
    @Sorter(sort = 0)
    //camara
    public static double FARSHOT_TA = 0.3;
    public static double CLOSESHOT_TA = 0.6;
    public static double GOALHEIGHT = 29.5; //inches
    public static double CAMERAHEIGHT = 15.75; //inches
    public static double CAMERAANGLE = 18.989; //degrees
    public static double LimelightBlue = -15.70;
    public static double LimelightRed = 19.15;

    public static double TURRETFEEDBACKNOISE = 2.5; //degrees
    public static double TURRETSYSTEMNOISE = 0.1; // degrees


    @Sorter(sort = 1)
    //HuskyLens
    public static double LEFTBOUND = 0;
    public static double RIGHTBOUND = 0;

    @Sorter(sort = 2)
    //FlyWheel
    public static double MAXHDPOWER = 0.01;
    public static double MAXHDAIM = 1;
    public static double COASTSPEED = 0.4;
    public static double FARSHOTSPEED = 0.5745; // 0.635
    public static double FARSHOTSPEEDOPPO = 0.61;
    public static double FARSHOTVEL = 1560;
    public static double FARSHOTDISTANCE = 114;
    public static double MIDSHOTDISTANCE = 85;
    public static double FARSHOTTRANSFERPOWER = 0.62;
    public static double MIDSHOTSPEED = 0.475;
    public static double MIDSHOTVEL = 1160;
    public static double CLOSESHOTSPEED = 0.4;
    public static double STARTSHOTSPEED = 0.585;
    public static double CLOASESTARTSHOTSPEED = 0.40;
    public static double AUTOSHOTSPEED = .59;
    public static double SHOTHEIGHT = 18.75;

    @Sorter(sort = 3)
    //Hood
    public static double HOODHOME = 0.05;
    public static double HOODMINSERVOVALUE = 0.04;
    public static double HOODMAXSERVOVALUE = .905;
    public static double HOODTARGETTOLERANCE = 1;
    public static double FARSHOTHOODSERVO = 0.905;
    public static double MIDSHOTHOODSERVO = .3594;
    public static double CLOSESHOTHOODSERVO = 0.05;
    public static double STARTSHOTHOODSERVO = 0.905;
    public static double CLOSESTARTSHOTHOODSERVO = 0.417;

    public static double CLOSESHOTTURRETANGLE = 80;


    @Sorter(sort = 4)
    //Turret
    public static double TURRETHOME = 188; // 172.8
    public static double TURRETMINANGLE = -80;
    public static double TURRETMAXANGLE = 80;
    public static double TURRETMINSERVOVALUE = 0;
    public static double TURRETMAXSERVOVALUE = 1;
    public static double TURRETANGLETOLERANCE = 5; // 1
    public static double TURRETANGLEROUGHTOLERANCE = 1;
    public static double TURRETCLOSECONSTANT = 0.5;
    public static double TELEFARSHOTTURRETANGLE = -138;//27.9
    public static double TELEFARSHOTTURRETANGLEOPPO = 83;
    public static double MIDSHOTTURRETANGLE = 40.0; //38

    // auto angles
    public static double STARTSHOTTURRETANGLE = 20.5; //
    public static double AUTOFARSHOTTURRETANGLE = 21; //-65
    public static double REDAUTOTURRETOFFEST = 0.1;
    public static double TURRETDYNAMIC = 200;

    @Sorter(sort = 5)
    //Transfer
    public static double TRANSFERPOWER = 1; //1
    public static double TRANSFERINTAKEPOWER = 0.85;
    public static double GATECLAWOPEN = 0.55;
    public static double GATECLAWCLOSE = 0.62;
    public static double GATECLAWHOLD = 0.605;
    public static double GATEPOWER = 1;
    public static double KICKERPOWER = 1;

    @Sorter(sort = 6)
    //Intake
    public static double INTAKEPOWER = 0.8; //1
    public static double AUTOINTAKEPOWER = 1; //1
    public static double INTAKEHOLD = 0.3;
    public static double PASSTIME = 300;

    @Sorter(sort = 7)
    //Tilt
    public static double TILTHOME = 0.95;
    public static double TILTUP = 0.1;
}
