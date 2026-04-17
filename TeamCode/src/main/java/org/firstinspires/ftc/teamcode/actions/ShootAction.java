package org.firstinspires.ftc.teamcode.actions;

import static org.firstinspires.ftc.teamcode.enums.ShootaActionType.STOP;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.enums.ShootaActionType;

import Modules.Constants;
import Modules.HeadingTracker;
import Modules.Intake;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Transfer;

public class ShootAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final ShootaActionType actionType;
    private long duration = -1;
    private long timeout = 0;

    private long startTime = 0;
    private long emptyTime = -1;
    private long shootTime = -1;
    private boolean countingEmpty = false;

    private double shootSpeed = 0;


    public ShootAction(ShootaActionType actionType) {
        this.actionType = actionType;
        this.duration = 3000;
        this.shootTime = 1000;
    }

    public ShootAction(ShootaActionType actionType, long milliseconds) {
        this.actionType = actionType;
        this.duration = milliseconds;
        this.shootTime = 1000;
    }

    public ShootAction(ShootaActionType actionType, long milliseconds, double speed) {
        this.actionType = actionType;
        this.duration = milliseconds;
        this.shootTime = 1000;
        this.shootSpeed = speed;
        shootSpeed = speed;

    }

    private void initialize() {
        this.startTime = System.currentTimeMillis();
        OCBHWM.turretServo.setRtp(true);
        switch (this.actionType) {
            case SHOOT:
                HeadingTracker.FlywheelEnable = true ;
                Transfer.clawOpen();
                Transfer.transferShoot();
                Intake.intakeIn();
                break;

            case STOP:
                HeadingTracker.FlywheelEnable = false;
                Transfer.clawHold();
                Transfer.transferHold();
                Intake.intakeRest();
                Shoota.coast();
                break;
        }


        countingEmpty = false;
        initialized = true;
    }

    private void startEmptyTime() {
        this.emptyTime = System.currentTimeMillis();
        countingEmpty = true;
    }

//    private void end() {
//        Intake.intakeRest();
//    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            initialize();
        }// else if (countingEmpty && this.emptyTime > 0 && System.currentTimeMillis() >= this.emptyTime + this.shootTime) {
//            OCBHWM.turretServo.setRtp(false);
//            OCBHWM.turretServo.setPower(0);
//            return false;
//        } else if (this.actionType == STOP) {
//            OCBHWM.turretServo.setRtp(false);
//            OCBHWM.turretServo.setPower(0);
//            Shoota.stop();
//            return false;
//        }
//        if (OCBHWM.transferClear.getVoltage() <= 0.26) {
//            this.countingEmpty = false;
//        } else if (OCBHWM.transferClear.getVoltage() > 0.26 && !countingEmpty) {
//            startEmptyTime();
//        }
        if (this.duration != -1 && System.currentTimeMillis() - this.startTime >= this.duration) {
            Transfer.transferHold();
            Intake.intakeRest();
            Transfer.clawHold();
            return false;
        }

        return true;
    }

    public boolean didFail() {
        return isFailed;
    }
}


