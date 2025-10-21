package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.enums.TransferActionType;

import Modules.Intake;
import Modules.Transfer;

public class TransferAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final TransferActionType actionType;
    private long duration = -1;
    private long timeout = 0;

    private long startTime = 0;



    public TransferAction(TransferActionType actionType) {
        this.actionType = actionType;
        this.duration = 100;
    }

    public TransferAction(TransferActionType actionType, long milliseconds) {
        this.actionType = actionType;
        this.duration = milliseconds;
    }

    private void initialize() {
        this.startTime = System.currentTimeMillis();

        switch (this.actionType) {
            case TRANSFER_IN:
                Transfer.transferIn();
                break;

            case TRANSFER_OUT:
                Transfer.transferOut();
                break;

            case TRANSFER_HOLD:
                Transfer.transferHold();
                break;
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
