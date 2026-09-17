package Modules;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class DCMotorController {
    public static double R = 12 / 9.2;
    public static double W_NL = 5800.0 * 2.0 * Math.PI / 60.0;
    public static double K_E = (12.0 - 0.25 * R) / W_NL;
    public static double Ticks_Per_Rev = 28;

    public static double MAX_Voltage = 12;

    public static double MAX_Current = 2;


    public void CurrentControlMotor(DcMotorEx motor, VoltageSensor battery, double targetCurrentAmps) {
        // encoder velocity
        double omega = (motor.getVelocity() / Ticks_Per_Rev) * 2.0 * Math.PI;

        // back - emf voltage
        double vBemf = K_E * omega;

        //required voltage
        double vCmd = targetCurrentAmps * R + vBemf;

        //apply volate to motor
        double vBus = battery.getVoltage();
        double vClamped = Math.min(Math.abs(vCmd), MAX_Voltage) * Math.signum(vCmd);
        motor.setPower(vClamped / vBus);
    }

    public void VoltageControlMotor(DcMotorEx motor, VoltageSensor battery, double targetVoltage) {
        // encoder velocity
        double omega = (motor.getVelocity() / Ticks_Per_Rev) * 2.0 * Math.PI;

        // back - emf voltage
        double vBemf = K_E * omega;

        //required voltage
        double current = (targetVoltage - vBemf) / R;
        double currentClamped = Math.max(-MAX_Current,Math.min(MAX_Current,current));
        double vCmd = currentClamped * R + vBemf;

        //apply volate to motor
        double vBus = battery.getVoltage();
        double vClamped = Math.min(Math.abs(vCmd), MAX_Voltage) * Math.signum(vCmd);
        motor.setPower(vClamped / vBus);
    }


}
