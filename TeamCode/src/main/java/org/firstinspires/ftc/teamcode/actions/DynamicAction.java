package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.utils.ActionMethod;


public class DynamicAction implements Action {
    private ActionMethod getAction;
    private Action action;

    public DynamicAction(ActionMethod getAction) {
        this.getAction = getAction;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        if (action == null) {
            this.action = this.getAction.execute();
        }

        return this.action.run(packet);
    }
}
