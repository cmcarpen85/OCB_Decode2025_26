package org.firstinspires.ftc.teamcode.utils;

import com.acmerobotics.roadrunner.Action;

@FunctionalInterface
public interface ActionMethod {
    Action execute();
}
