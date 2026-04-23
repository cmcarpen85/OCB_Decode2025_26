package org.firstinspires.ftc.teamcode.actions;

import static org.firstinspires.ftc.teamcode.enums.UpdateActionType.UPDATE;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.enums.PrepShootActionType;
import org.firstinspires.ftc.teamcode.enums.UpdateActionType;

import java.util.Objects;

import Modules.Constants;
import Modules.HeadingTracker;
import Modules.Hood;
import Modules.OCBHWM;
import Modules.Shoota;
import Modules.Turret;

public class UpdateAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final UpdateActionType actionType;
    private long duration = -1;
    private long timeout = 0;

    private long startTime = 0;
    private double ShootSpeed = 0;
    private double TurretAngle = 0;
    private String color = "red";
    private double HoodAngle = 0;
    public boolean FlywheelEnable = true;

    public UpdateAction(UpdateActionType actionType, String color) {
        this.actionType = actionType;
        this.color = color;
        this.duration = 30000;
    }


    private void initialize() {
        this.startTime = System.currentTimeMillis();
        OCBHWM.turretServo.setRtp(true);
//        HeadingTracker.setPinpointStart(-64.1575, 16.499);
        OCBHWM.pinPoint.update();
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
//        if (Math.abs(OCBHWM.turretServo.getTargetRotation()-OCBHWM.turretServo.getTotalRotation())<=1){
//            OCBHWM.turretServo.setRtp(false);
//            return false;
//        }
        if (this.duration != -1 && System.currentTimeMillis() - this.startTime >= this.duration) {
            OCBHWM.turretServo.setRtp(false);
            OCBHWM.turretServo.setPower(0);
            return false;
        }
//        FlywheelEnable = HeadingTracker.FlywheelEnable;
//        if (Objects.equals(color, "red")) {
//            HeadingTracker.headingTrackingRed(FlywheelEnable);
//        } else if (Objects.equals(color, "blue")){
//            HeadingTracker.headingTrackingBlueAuto(true);
//        }
        OCBHWM.pinPoint.update();
        OCBHWM.turretServo.update();

//        packet.put("PinpointX",String.valueOf( OCBHWM.pinPoint.getPosX(DistanceUnit.INCH)));
//        packet.put("PinpointY",String.valueOf( OCBHWM.pinPoint.getPosY(DistanceUnit.INCH)));
        return true;
    }

    public boolean didFail() {
        return isFailed;
    }
}
