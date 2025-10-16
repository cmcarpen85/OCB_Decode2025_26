package org.firstinspires.ftc.teamcode.actions;

import com.acmerobotics.roadrunner.Action;

public interface FailableAction extends Action {
    boolean didFail();
}
