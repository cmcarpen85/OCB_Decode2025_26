package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.enums.PrepShootActionType;

import Modules.Constants;
import Modules.Hood;
import Modules.Shoota;
import Modules.Turret;

public class PrepShootAction implements FailableAction {
    public Telemetry tel;

    private boolean initialized = false;
    private boolean isFailed = false;

    private final PrepShootActionType actionType;
    private long duration = -1;
    private long timeout = 0;

    private long startTime = 0;
    private double ShootSpeed = 0;
    private double TurretAngle = 0;
    private double color = 1;
    private double HoodAngle = 0;

    public PrepShootAction(PrepShootActionType actionType, double color) {
        this.actionType = actionType;
        this.color = color;
        this.duration = 100;
    }

    public PrepShootAction(PrepShootActionType actionType, double ShootSpeed, double TurretAngle, double HoodAngle) {
        this.actionType = actionType;
        this.duration = 100;
        this.ShootSpeed = ShootSpeed;
        this.TurretAngle = TurretAngle;
        this.HoodAngle = HoodAngle;
    }

    public PrepShootAction(PrepShootActionType actionType, long milliseconds,double color) {
        this.actionType = actionType;
        this.color = color;
        this.duration = milliseconds;
    }

    private void initialize() {
        this.startTime = System.currentTimeMillis();

        switch (this.actionType) {
            case PREP_SHOOT:
                Shoota.setSpeed(this.ShootSpeed);
                Turret.setToAngle(this.TurretAngle * this.color);
                Hood.setToAngle(this.HoodAngle);
                break;

            case PREP_FAR_SHOOT:
                Shoota.setSpeed(Constants.FARSHOTSPEED);
                Turret.setToAngle(Constants.AUTOFARSHOTTURRETANGLE * this.color);
                Hood.setToAngle(Constants.FARSHOTHOODSERVO);
                this.ShootSpeed = Constants.FARSHOTSPEED;
                break;

            case PREP_MID_SHOOT:
                Shoota.setSpeed(Constants.MIDSHOTSPEED);
                Turret.setToAngle(Constants.MIDSHOTTURRETANGLE * this.color);
                Hood.setToAngle(Constants.MIDSHOTHOODSERVO);
                this.ShootSpeed = Constants.MIDSHOTSPEED;
                break;

            case PREP_CLOSE_SHOOT:
                Shoota.setSpeed(Constants.CLOSESHOTSPEED);
                Turret.setToAngle(Constants.CLOSESHOTTURRETANGLE * this.color);
                Hood.setToAngle(Constants.CLOSESHOTHOODSERVO);
                this.ShootSpeed = Constants.CLOSESHOTSPEED;
                break;

            case PREP_STARTING_SHOT:
                Shoota.setSpeed(Constants.STARTSHOTSPEED);
                Turret.setToAngle(Constants.STARTSHOTTURRETANGLE * this.color);
                Hood.setToAngle(Constants.STARTSHOTHOODSERVO);
                this.ShootSpeed = Constants.STARTSHOTSPEED;
                break;

            case STOP:
                Shoota.stop();
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
        Shoota.setSpeed(this.ShootSpeed);
        return true;
    }

    public boolean didFail() {
        return isFailed;
    }
}
