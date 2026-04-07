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

    public static void transferShoot(double power){
        OCBHWM.transferM.setPower(power);
    }

    //Gate
    public static void gateOpen(){
        OCBHWM.gateServo.setPosition(Constants.GATECLAWOPEN);
    }

    public static void gateClose() {
        OCBHWM.gateServo.setPosition(Constants.GATECLAWCLOSE);
    }
}
