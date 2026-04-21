package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

import Modules.Constants;
import Modules.OCBHWM;

@Config
public class RTPAxonChase {

    public static class Params {
        public double kP = 0.0058; //0.0058
        public double kI =0.001; //0.001
        public double kD = 0.0005; // 0.0005
        public double kH = 0.0002;//0.0002
        public double kS = 0.05; // 0.05

    }
    public static Params PARAMS = new Params();
    // Encoder for servo position feedback
    private final AnalogInput servoEncoder;
    // Continuous rotation servo
    private final CRServo servo;
    private final CRServo servo2;
    // Run-to-position mode flag
    private boolean rtp;
    // Current power applied to servo
    private double power;
    // Maximum allowed power
    private double maxPower;
    // Minimum allowed power
    private double minPower = 0;//0.12
    // Direction of servo movement
    private Direction direction;
    // Last measured angle
    private double previousAngle;
    private double previousAngleAxon;

    // Accumulated rotation in degrees
    private double totalRotation;
    public double totalRotationAxon;

    // Target rotation in degrees
    private double targetRotation;
    private double integralSum;
    private double lastError;
    private double lastTargetVelocity;
    public double lastVelocityError;
    private double lastBaseVelocity;
    private double maxIntegralSum;
    private ElapsedTime pidTimer;

    // Initialization and debug fields
    public double STARTPOS;
    public double AxonStartPos;
    public int ntry = 0;
    public int cliffs = 0;
    public int AxonCliffs = 0;
    public boolean SnapBack = false;
    public double SnapBackSine = 0.0;
    public double homeAngle;
    public double AxonHomeAngle;
    public double maxAngle = 470;
    public double minAngle = -150;
    public KalmanFilter filter;
    public KalmanFilter Axonfilter;

    // Direction enum for servo
    public enum Direction {
        FORWARD,
        REVERSE
    }

    // region constructors

    // Basic constructor, defaults to FORWARD direction
    public RTPAxonChase(CRServo servo, CRServo servo2, AnalogInput encoder) {
        rtp = true;
        this.servo = servo;
        this.servo2 = servo2;
        servoEncoder = encoder;
        direction = Direction.FORWARD;
        initialize();
    }

    // Constructor with explicit direction
    public RTPAxonChase(CRServo servo, CRServo servo2, AnalogInput encoder, Direction direction) {
        this(servo, servo2, encoder);
        this.direction = direction;
        initialize();
    }

    // Initialization logic for servo and encoder
    private void initialize() {
        servo.setPower(0);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }

        // Try to get a valid starting position
        do {
            STARTPOS = getCurrentAngle();
            AxonStartPos = getCurrentAxonAngle();
            if (Math.abs(STARTPOS) > 1 && Math.abs(AxonStartPos)>1) {
                previousAngle = getCurrentAngle();
                previousAngleAxon = getCurrentAxonAngle();
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
            ntry++;
        } while (Math.abs(previousAngle) < 0.2 && (ntry < 50));

        totalRotationAxon = (AxonStartPos - Constants.TURRETHOME);
        AxonHomeAngle = Constants.TURRETHOME;

        totalRotation = 0;
        homeAngle = previousAngle;

        integralSum = 0.0;
        lastError = 0.0;
        lastBaseVelocity = 0;
        lastVelocityError = 0;
        lastTargetVelocity = 0;
        maxIntegralSum = 100.0;
        pidTimer = new ElapsedTime();
        pidTimer.reset();

        maxPower = 0.75;
        cliffs = 0;
        AxonCliffs = 0;
        filter = new KalmanFilter(Constants.TURRETSYSTEMNOISE, Constants.TURRETFEEDBACKNOISE);
        Axonfilter = new KalmanFilter(Constants.TURRETSYSTEMNOISE, Constants.TURRETFEEDBACKNOISE);
    }

    // endregion

    // Set servo direction
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // Set power to servo, respecting direction and maxPower
    public void setPower(double power) {
        this.power = Math.max(-maxPower, Math.min(maxPower, power));
//        if (power > 0) {
//            this.power = Math.max(minPower, power);
//        } else if (power < 0) {
//            this.power = Math.min(-minPower, power);
//        } else {
//            this.power = power;
//        }
        if (SnapBack){
            this.power = SnapBackSine*Math.abs(this.power);
//            this.power = 0;
        }

        servo.setPower(this.power * (direction == Direction.REVERSE ? -1 : 1));
        servo2.setPower(this.power * (direction == Direction.REVERSE ? -1 : 1));
    }

    // Get current power
    public double getPower() {
        return power;
    }

    // Set maximum allowed power
    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    // Get maximum allowed power
    public double getMaxPower() {
        return maxPower;
    }

    // Enable or disable run-to-position mode
    public void setRtp(boolean rtp) {
        this.rtp = rtp;
        if (rtp) {
            resetPID();
        }
    }

    // Get run-to-position mode state
    public boolean getRtp() {
        return rtp;
    }

    // Set PID P coefficient
    public void setKP(double kP) {
        this.PARAMS.kP = kP;
    }

    // Set PID I coefficient and reset integral
    public void setKI(double kI) {
        this.PARAMS.kI = kI;
        resetIntegral();
    }

    // Set PID D coefficient
    public void setKD(double kD) {
        this.PARAMS.kD = kD;
    }

    // Set all PID coefficients
    public void setPidCoeffs(double kP, double kI, double kD) {
        setKP(kP);
        setKI(kI);
        setKD(kD);
    }

    // Get PID P coefficient
    public double getKP() {
        return PARAMS.kP;
    }

    // Get PID I coefficient
    public double getKI() {
        return PARAMS.kI;
    }

    // Get PID D coefficient
    public double getKD() {
        return PARAMS.kD;
    }

    // Set only P coefficient (alias)
    public void setK(double k) {
        setKP(k);
    }

    // Get only P coefficient (alias)
    public double getK() {
        return getKP();
    }

    // Set maximum allowed integral sum
    public void setMaxIntegralSum(double maxIntegralSum) {
        this.maxIntegralSum = maxIntegralSum;
    }

    // Get maximum allowed integral sum
    public double getMaxIntegralSum() {
        return maxIntegralSum;
    }

    // Get total rotation since initialization
    public double getTotalRotation() {
        return totalRotation;
    }

    // Get current target rotation
    public double getTargetRotation() {
        return targetRotation;
    }

    // Increment target rotation by a value
    public void changeTargetRotation(double change) {
        if (targetRotation + change > maxAngle) {
            targetRotation = maxAngle;
        } else if (targetRotation + change < minAngle) {
            targetRotation = minAngle;
        }else {
        targetRotation += change;
        }
    }

    // Set target rotation and reset PID
    public void setTargetRotation(double target) {
        if (target > maxAngle) {
            targetRotation = maxAngle;
        } else if (target < minAngle) {
            targetRotation = minAngle;
        } else {
        targetRotation = target;
        }
        resetPID();
    }

    // Get current angle from encoder (in degrees)
    public double getCurrentAngle() {
    return OCBHWM.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
    public double getCurrentAxonAngle() {
        if (servoEncoder == null) return 0;
        return (servoEncoder.getVoltage() / 3.3) * (direction.equals(RTPAxon.Direction.REVERSE) ? -360 : 360);
    }

    // Check if servo is at target (default tolerance)
    public boolean isAtTarget() {
        return isAtTarget(2);
    }

    // Check if servo is at target (custom tolerance)
    public boolean isAtTarget(double tolerance) {
        return Math.abs(targetRotation - totalRotation) < tolerance;
    }

    // Force reset total rotation and PID state
    public void forceResetTotalRotation() {
        totalRotation = 0;
        previousAngle = getCurrentAngle();
        resetPID();
    }

    // Reset PID controller state
    public void resetPID() {
        resetIntegral();
        lastError = 0;
        pidTimer.reset();
    }

    // Reset integral sum
    public void resetIntegral() {
        integralSum = 0;
    }

    // Main update loop: updates rotation, computes PID, applies power
    public synchronized void update() {
//        double currentAngle = filter.filter(getCurrentAngle()); //slip ring
        double currentAngle = getCurrentAngle();
        double angleDifference = currentAngle - previousAngle;

        double currentAxonAngle = getCurrentAxonAngle();
        double AxonAngleDifference = currentAxonAngle - previousAngleAxon;

         //Handle wraparound at 0/360 degrees
        if (angleDifference > 180) {
            angleDifference -= 360;
            cliffs--;
        } else if (angleDifference < -180) {
            angleDifference += 360;
            cliffs++;
        }
        if (AxonAngleDifference > 180) {
            AxonAngleDifference -= 360;
            AxonCliffs = AxonCliffs -1 ;
        } else if (AxonAngleDifference < -180) {
            AxonAngleDifference += 360;
            AxonCliffs = AxonCliffs + 1;
        }

        // Update total rotation with wraparound correction
        totalRotation = currentAngle - homeAngle;
        previousAngle = currentAngle;

        totalRotationAxon = currentAxonAngle - AxonHomeAngle + AxonCliffs * 360;
        previousAngleAxon = currentAxonAngle;

        if (totalRotationAxon>maxAngle  && !SnapBack || totalRotationAxon<minAngle &&!SnapBack){
            SnapBack = true;
            if(totalRotationAxon>maxAngle){
                SnapBackSine = 1.0;
            } else if (totalRotationAxon<minAngle){
                SnapBackSine = -1.0;
            }
        }
//        else if (totalRotationAxon<maxAngle || totalRotationAxon>minAngle){
//            SnapBack = false;
//        }


        if (!rtp) return;

        double dt = pidTimer.seconds();
        pidTimer.reset();

        // Ignore unreasonable dt values
        if (dt < 0.001 || dt > 1.0) {
            return;
        }

        double error = targetRotation - totalRotation;

        // PID integral calculation with clamping
        integralSum += error * dt;
        integralSum = Math.max(-maxIntegralSum, Math.min(maxIntegralSum, integralSum));

        // Integral wind-down in deadzone
        final double INTEGRAL_DEADZONE = 2.0;
        if (Math.abs(error) < INTEGRAL_DEADZONE) {
            integralSum *= 0.95;
        }

        // PID derivative calculation
        double derivative = (error - lastError) / dt;
        lastError = error;

        // Heading feed forward calculation
        double baseHeadingVel = OCBHWM.pinPoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
        double hTerm = PARAMS.kH * baseHeadingVel;
        lastVelocityError = derivative;

        double sTerm = PARAMS.kS * Math.signum(error);



        // PID output calculation
        double pTerm = PARAMS.kP * error;
        double iTerm = PARAMS.kI * integralSum;
        double dTerm = PARAMS.kD * derivative;

        double output = pTerm + iTerm + dTerm + (hTerm+sTerm);

        if(SnapBack){
            if (Math.abs(error)<30){
            SnapBack = false;
            }
        }

        // Deadzone for output
        final double DEADZONE = 0.5; // 0.5
        if (Math.abs(error) > DEADZONE) {
            double power = Math.min(maxPower, Math.abs(output)) * Math.signum(output);
            setPower(power);
        } else {
            setPower(0);
        }
    }

    // Log current state for telemetry/debug
    @SuppressLint("DefaultLocale")
    public String log() {
        return String.format(
                "Current Volts: %.3f\n" +
                        "Current Angle: %.2f\n" +
                        "Total Rotation: %.2f\n" +
                        "Target Rotation: %.2f\n" +
                        "Current Power: %.3f\n" +
                        "PID Values: P=%.3f I=%.3f D=%.3f\n" +
                        "PID Terms: Error=%.2f Integral=%.2f",
                servoEncoder.getVoltage(),
                getCurrentAngle(),
                totalRotation,
                targetRotation,
                power,
                PARAMS.kP, PARAMS.kI, PARAMS.kD,
                targetRotation - totalRotation,
                integralSum
        );
    }

}