package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp (name="Omni With Tele Testing")
public class OmniAndTele extends OpMode{
    private DcMotor slideLeft;
    private DcMotor slideRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor intake;
    private DcMotor elbow;
    private Servo airplane;
    private Servo gate;
    private IMU imu;
    private final int high = 1050;
    private final int high2 = -high;
    private final int low = 0;
    private final int takeIn = 75;
    private final int place = 200;
    private final int place2 = -place;
    private final int placing = -300;
    private final int base = 5;
    private final double launchPlane = 0.7;
    private final double holdPlane = 0.0;
    private final double close = 0.0;
    private final double open = 0.3;
    private double x;
    private double y;
    private double rx;

    public void init() {
        slideLeft = hardwareMap.get(DcMotor.class, "slideLeft");
        slideRight = hardwareMap.get(DcMotor.class, "slideRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        airplane = hardwareMap.get(Servo.class, "hook");
        gate = hardwareMap.get(Servo.class, "gate");
        imu = hardwareMap.get(IMU.class, "imu");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.REVERSE);

        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setTargetPosition(0);
        slideLeft.setPower(1.0);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setTargetPosition(0);
        slideRight.setPower(1.0);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setTargetPosition(0);
        intake.setPower(0.4);
        intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setTargetPosition(0);
        elbow.setPower(1.0);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        /*IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));

        imu.initialize(parameters);*/
    }

    @Override
    public void loop() {

        telemetry.addData("Elbow Position:", elbow.getCurrentPosition());
        telemetry.update();

        // controls
        double x = gamepad1.left_stick_x * 0.55; // Counteract imperfect strafing
        double y = gamepad1.left_stick_y * 0.5;
        double rx = gamepad1.right_stick_x * 0.5;

        moveSlides();
        runIntake();
        moveElbow();
        openGate();
        robotCentricMovement();
        moveBotSlow();
        plane();
    }

    public void setSlides(int distance, int distance2) {
        slideRight.setTargetPosition(distance2);
        slideRight.setPower(1.0);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideLeft.setTargetPosition(distance);
        slideLeft.setPower(1.0);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void moveSlides() {
        if (gamepad2.y) {
            setSlides(high, high2);
        }

        if (gamepad2.a) {
            setSlides(low, low);
        }
    }

    public void robotCentricMovement() {
        // motor power equations
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    public void fieldCentricMovement() {
        // This button choice was made so that it is hard to hit on accident,
        // it can be freely changed based on preference.
        if (gamepad1.back) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    public void moveBotSlow() {
        if (gamepad1.right_stick_y > 0.3) {
            frontLeft.setPower(0.3);
            backLeft.setPower(-0.3);
            frontRight.setPower(0.3);
            backRight.setPower(-0.3);
        }

        if (gamepad1.right_stick_y < -0.3) {
            frontLeft.setPower(-0.3);
            backLeft.setPower(0.3);
            frontRight.setPower(-0.3);
            backRight.setPower(0.3);
        }

        if (0.3 > gamepad1.right_stick_y && gamepad1.right_stick_y > -0.3) {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }
    }

    public void runIntake() {
        if (gamepad1.dpad_down) {
            intake.setTargetPosition(intake.getCurrentPosition() - takeIn);
            intake.setPower(0.3);
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if (gamepad1.dpad_up) {
            intake.setTargetPosition(intake.getCurrentPosition() + takeIn);
            intake.setPower(0.4);
            intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void moveElbow() {
        if (gamepad2.x) {
            setElbow(base);
        }

        if (gamepad2.b) {
            setElbow(placing);
        }

        if (gamepad2.right_stick_y > 0.3) {
            elbow.setPower(1.0);
            elbow.setTargetPosition(elbow.getCurrentPosition() - 20);
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if (gamepad2.right_stick_y < -0.3) {
            elbow.setPower(1.0);
            elbow.setTargetPosition(elbow.getCurrentPosition() + 20);
            elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }

    public void plane() {
        if (gamepad1.x) {
            airplane.setPosition(holdPlane);
        }//holds airplane

        if (gamepad1.b) {
            airplane.setPosition(launchPlane);
        }//lets airplane go
    }

    public void openGate() {
        if (gamepad2.left_bumper) {
            gate.setPosition(close);
            telemetry.addData("Close", gate.getPosition());
        }

        if (gamepad2.right_bumper) {
            gate.setPosition(open);
            telemetry.addData("Open", gate.getPosition());
        }
    }

    public void setElbow(int position) {
        elbow.setTargetPosition(position);
        elbow.setPower(0.35);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
