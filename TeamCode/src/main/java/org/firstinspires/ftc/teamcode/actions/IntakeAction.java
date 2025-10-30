package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.enums.IntakeActionType;
import org.firstinspires.ftc.teamcode.enums.ShootaActionType;

import Modules.Intake;
import Modules.Transfer;

public class IntakeAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final IntakeActionType actionType;
    private long duration = -1;
    private long timeout = 0;

    private long startTime = 0;


    public IntakeAction(IntakeActionType actionType) {
        this.actionType = actionType;
        this.duration = 100;
    }

    public IntakeAction(IntakeActionType actionType, long milliseconds) {
        this.actionType = actionType;
        this.duration = milliseconds;
    }

    private void initialize() {
        this.startTime = System.currentTimeMillis();

        switch (this.actionType) {
            case INTAKE_IN:
                Intake.intakeIn();
                Transfer.transferIn();
                break;
            case INTAKE_HOLD:
                Intake.intakeHold();
                break;
            case INTAKE_OUT:
                Intake.intakeOut();
                break;
            case INTAKE_REST:
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
            return true;
        }

        return false;
    }

    public boolean didFail() {
        return isFailed;
    }
}
