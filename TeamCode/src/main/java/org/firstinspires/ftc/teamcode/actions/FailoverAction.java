package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class FailoverAction implements Action {

    FailableAction initialAction;
    Action fallbackAction;

    boolean didFail = false;

    public FailoverAction(FailableAction initialAction, Action fallbackAction) {
        this.initialAction = initialAction;
        this.fallbackAction = fallbackAction;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        // if it hasn't failed, keep running the initial action
        if (!this.didFail) {
            boolean rtrn;
            // run the initial action before checking if it failed
            rtrn = this.initialAction.run(packet);
            this.didFail = this.initialAction.didFail();

            // we don't want to stop if it failed, so return
            // true so that we can run the fallback on the
            // next run
            if (this.didFail) {
                return true;
            }
            return rtrn;
        }

        // at this point, the initial action failed, run the
        // fallback action

        return this.fallbackAction.run(packet);
    }
}
