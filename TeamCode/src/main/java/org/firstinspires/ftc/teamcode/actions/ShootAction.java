package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.enums.ShootaActionType;
import org.firstinspires.ftc.teamcode.enums.TransferActionType;

import Modules.Intake;
import Modules.Transfer;

public class ShootAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final ShootaActionType actionType;
    private long duration = -1;
    private long timeout = 0;

    private long startTime = 0;



    public ShootAction(ShootaActionType actionType) {
        this.actionType = actionType;
        this.duration = 100;
    }

    public ShootAction(ShootaActionType actionType, long milliseconds) {
        this.actionType = actionType;
        this.duration = milliseconds;
    }

    private void initialize() {
        this.startTime = System.currentTimeMillis();

        switch (this.actionType) {
            case SHOOT:
                Transfer.kickerForward();
                Transfer.gateForward();
                Transfer.transferIn();
                Intake.intakeIn();
                break;
            case STOP:
                Transfer.kickerRest();
                Transfer.gateRest();
                Transfer.transferHold();
                Intake.intakeRest();
        }

        initialized = true;
    }

//    private void end() {
//        Intake.intakeRest();
//    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!initialized) {
            initialize();
        }
        if (this.duration != -1 && System.currentTimeMillis() - this.startTime >= this.duration) {
            return true;
        }

        return false;
    }

    public boolean didFail() {
        return isFailed;
    }
}
