package Modules;

public class Transfer {

    public static double TransferShootPower = 1;

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

    public static void transferShoot(){
        OCBHWM.transferM.setPower(Transfer.TransferShootPower);
    }

    //Gate
    public static void clawOpen(){
        OCBHWM.gateServo.setPosition(Constants.GATECLAWOPEN);
    }

    public static void clawClose() {
        OCBHWM.gateServo.setPosition(Constants.GATECLAWCLOSE);
    }
}
