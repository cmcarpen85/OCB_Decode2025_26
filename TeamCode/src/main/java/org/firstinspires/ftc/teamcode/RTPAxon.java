package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import Modules.Constants;

public class RTPAxon {
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
    private double minPower = 0.12;
    // Direction of servo movement
    private Direction direction;
    // Last measured angle
    private double previousAngle;
    // Accumulated rotation in degrees
    private double totalRotation;
    // Target rotation in degrees
    private double targetRotation;

    // PID controller coefficients and state
    private double kP;
    private double kI;
    private double kD;
    private double integralSum;
    private double lastError;
    private double maxIntegralSum;
    private ElapsedTime pidTimer;

    // Initialization and debug fields
    public double STARTPOS;
    public int ntry = 0;
    public int cliffs = 0;
    public double homeAngle;
    public double maxAngle = 145;
    public double minAngle= -145;

    // Direction enum for servo
    public enum Direction {
        FORWARD,
        REVERSE
    }

    // region constructors

    // Basic constructor, defaults to FORWARD direction
    public RTPAxon(CRServo servo, CRServo servo2, AnalogInput encoder) {
        rtp = true;
        this.servo = servo;
        this.servo2 = servo2;
        servoEncoder = encoder;
        direction = Direction.FORWARD;
        initialize();
    }

    // Constructor with explicit direction
    public RTPAxon(CRServo servo, CRServo servo2, AnalogInput encoder, Direction direction) {
        this(servo, servo2, encoder);
        this.direction = direction;
        initialize();
    }

    // Initialization logic for servo and encoder
    private void initialize() {
        servo.setPower(0);
        servo2.setPower(0);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }

        // Try to get a valid starting position
        do {
            STARTPOS = getCurrentAngle();
            if (Math.abs(STARTPOS) > 1) {
                previousAngle = getCurrentAngle();
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
            ntry++;
        } while (Math.abs(previousAngle) < 0.2 && (ntry < 50));

        totalRotation = (STARTPOS - Constants.TURRETHOME  );
        homeAngle = Constants.TURRETHOME;
        targetRotation = totalRotation;

        // Default PID coefficients
        kP = 0.015;
        kI = 0.0007;
        kD = 0.0005;
        integralSum = 0.0;
        lastError = 0.0;
        maxIntegralSum = 100.0;
        pidTimer = new ElapsedTime();
        pidTimer.reset();

        maxPower = 0.95;
        cliffs = 0;
    }
    // endregion

    // Set servo direction
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    // Set power to servo, respecting direction and maxPower
    public void setPower(double power) {
        this.power = Math.max(-maxPower, Math.min(maxPower, power));
        if (power > 0){
            this.power = Math.max(minPower, power);
        } else if (power < 0) {
            this.power = Math.min(-minPower, power);
        } else {
            this.power = power;
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
        this.kP = kP;
    }

    // Set PID I coefficient and reset integral
    public void setKI(double kI) {
        this.kI = kI;
        resetIntegral();
    }

    // Set PID D coefficient
    public void setKD(double kD) {
        this.kD = kD;
    }

    // Set all PID coefficients
    public void setPidCoeffs(double kP, double kI, double kD){
        setKP(kP);
        setKI(kI);
        setKD(kD);
    }

    // Get PID P coefficient
    public double getKP() {
        return kP;
    }

    // Get PID I coefficient
    public double getKI() {
        return kI;
    }

    // Get PID D coefficient
    public double getKD() {
        return kD;
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
        if (servoEncoder == null) return 0;
        return (servoEncoder.getVoltage() / 3.3) * (direction.equals(Direction.REVERSE) ? -360 : 360);
    }

    // Check if servo is at target (default tolerance)
    public boolean isAtTarget() {
        return isAtTarget(5);
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
        double currentAngle = getCurrentAngle();
        double angleDifference = currentAngle - previousAngle;

        // Handle wraparound at 0/360 degrees
        if (angleDifference > 180) {
            angleDifference -= 360;
            cliffs--;
        } else if (angleDifference < -180) {
            angleDifference += 360;
            cliffs++;
        }

        // Update total rotation with wraparound correction
        totalRotation = currentAngle - homeAngle + cliffs * 360;
        previousAngle = currentAngle;

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

        // PID output calculation
        double pTerm = kP * error;
        double iTerm = kI * integralSum;
        double dTerm = kD * derivative;

        double output = pTerm + iTerm + dTerm;

        // Deadzone for output
        final double DEADZONE = 1; // 0.5
        if (Math.abs(error) > DEADZONE) {
            double power = Math.min(maxPower, Math.abs(output)) * Math.signum(output);
            setPower(-power);
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
                kP, kI, kD,
                targetRotation - totalRotation,
                integralSum
        );
    }

    // TeleOp test class for manual tuning and testing
    @TeleOp(name = "Cont. Rotation Axon Test", group = "test")
    public static class CRAxonTest extends LinearOpMode {

        @Override
        public void runOpMode() throws InterruptedException {
            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
            CRServo crservo = hardwareMap.crservo.get("CRturretServo");
            CRServo crServo2 = hardwareMap.crservo.get("CRturretServo2");
            AnalogInput encoder = hardwareMap.get(AnalogInput.class, "turretFeedback");
            RTPAxon servo = new RTPAxon(crservo, crServo2, encoder);

//            servo.rtp = false;
            waitForStart();

            while (!isStopRequested()) {
                servo.update();

                if (gamepad1.start){
                    servo.setRtp(false);
                } else if (gamepad1.right_trigger>0.4){
                    servo.setRtp(true);
                }

                // Manual controls for target and PID tuning
                if (gamepad1.dpad_left) {
                    servo.changeTargetRotation(1);
                }
                if (gamepad1.dpad_right) {
                    servo.changeTargetRotation(-1);
                }
                if (gamepad1.a) {
                    servo.setTargetRotation(0);
                } else if (gamepad1.left_bumper){
                    servo.setTargetRotation(50);
                } else if (gamepad1.right_bumper){
                    servo.setTargetRotation(-50);
                }

                if (gamepad1.b) {
                    servo.setKP(servo.getKP() + 0.0001);
                }
                if (gamepad1.x) {
                    servo.setKP(Math.max(0, servo.getKP() - 0.0001));
                }

                if (gamepad1.dpad_up) {
                    servo.setKI(servo.getKI() + 0.0001);
                }
                if (gamepad1.dpad_down) {
                    servo.setKI(Math.max(0, servo.getKI() - 0.0001));
                }

                if(gamepad1.right_stick_y>0.4){
                    servo.setKD(servo.getKD() + 0.0001);
                } else if (gamepad1.right_stick_y<-0.4){
                    servo.setKD(servo.getKD() - 0.0001);
                }

                if (gamepad1.back) {
                    servo.setKP(0.015);
                    servo.setKI(0.0002);
                    servo.setKD(0.0005);
                    servo.resetPID();
                }

                telemetry.addData("Starting angle", servo.STARTPOS);
                telemetry.addData("target rotation", servo.targetRotation);
                telemetry.addData("total rotation", servo.totalRotation);
                telemetry.addLine(servo.log());
                telemetry.addData("NTRY", servo.ntry);
                telemetry.update();
            }
        }
    }
}