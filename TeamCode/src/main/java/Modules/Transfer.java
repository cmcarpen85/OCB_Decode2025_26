package Modules;

public class Transfer {

    //Transfer
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
        OCBHWM.gateServo2.setPower(Constants.GATEPOWER);
    }
    public static void gateReverse() {
        OCBHWM.gateServo.setPower(-Constants.GATEPOWER);
        OCBHWM.gateServo2.setPower(-Constants.GATEPOWER);
    }
    public static void gateRest() {
        OCBHWM.gateServo.setPower(0);
        OCBHWM.gateServo2.setPower(0);
    }

    //Kicker
    public static void kickerForward(){
        OCBHWM.kickerServo.setPower(Constants.KICKERPOWER);
    }
    public static void kickerReverse(){
        OCBHWM.kickerServo.setPower(-Constants.KICKERPOWER);
    }
    public static void kickerRest() {
        OCBHWM.kickerServo.setPower(0);
    }
}
