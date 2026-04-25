package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.enums.IntakeActionType;

import Modules.Intake;
import Modules.Transfer;

public class IntakeWaitAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final IntakeActionType actionType;
    private long duration = -1;
    private long BlockTimeDuration = -1;
    private long timeout = 0;

    private long startTime = 0;
    private long BlockTimeStart = 0;


    public IntakeWaitAction(IntakeActionType actionType) {
        this.actionType = actionType;
        this.duration = 100;
    }

    public IntakeWaitAction(IntakeActionType actionType, long milliseconds) {
        this.actionType = actionType;
        this.duration = milliseconds;
    }

    private void initialize() {
        this.startTime = System.currentTimeMillis();
        this.BlockTimeDuration = 1000;
        switch (this.actionType) {
            case INTAKE_IN:
                Intake.autoIntakeIn();
                Transfer.transferIn();
                Transfer.clawClose();
                break;
            case INTAKE_HOLD:
                Intake.intakeHold();
                Transfer.clawClose();
                break;
            case INTAKE_OUT:
                Intake.intakeOut();
                break;
            case INTAKE_REST:
                Transfer.clawClose();
                Transfer.transferHold();
                Intake.intakeRest();
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
            return false;
        }
        if (this.BlockTimeDuration != -1 && System.currentTimeMillis() - this.startTime >= this.BlockTimeDuration) {
            return false;
        }

        return true;
    }

    public boolean didFail() {
        return isFailed;
    }
}
