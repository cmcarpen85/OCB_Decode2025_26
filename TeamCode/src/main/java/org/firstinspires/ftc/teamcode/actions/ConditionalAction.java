package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.utils.BoolMethod;

public class ConditionalAction implements Action {

    BoolMethod condition;
    Action actionTrue;
    Action actionFalse;
    boolean initialized = false;
    boolean runTrueAction = false;

    public ConditionalAction(BoolMethod condition, Action actionTrue, Action actionFalse) {
        this.actionTrue = actionTrue;
        this.actionFalse = actionFalse;
        this.condition = condition;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (!this.initialized) {
            // check the condition on the first run
            this.initialized = true;
            this.runTrueAction = condition.execute();
        }

        if (this.runTrueAction) {
            return actionTrue.run(packet);
        }

        return actionFalse.run(packet);
    }
}
