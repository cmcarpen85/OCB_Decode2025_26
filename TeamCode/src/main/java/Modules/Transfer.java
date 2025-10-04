package Modules;

public class Transfer {

    public static void transferIn(){
        OCBHWM.transferM.setPower(Constants.TRANSFERPOWER);
    }

    public static void transferHold(){
        OCBHWM.transferM.setPower(0);
    }

    public static void transferOut(){
        OCBHWM.transferM.setPower(-Constants.TRANSFERPOWER);
    }

    //Gate
    public static void gateForward(){
        OCBHWM.gateServo.setPower(Constants.GATEPOWER);
    }
    public static void gateReverse() {
        OCBHWM.gateServo.setPower(-Constants.GATEPOWER);
    }
    public static void gateRest() {
        OCBHWM.gateServo.setPower(0);
    }

    //Kicker
    public static void kickerForward(){
        OCBHWM.gateServo.setPower(Constants.KICKERPOWER);
    }
    public static void kickerReverse(){
        OCBHWM.gateServo.setPower(-Constants.KICKERPOWER);
    }
    public static void kickerRest() {
        OCBHWM.kickerServo.setPower(0);
    }
}
