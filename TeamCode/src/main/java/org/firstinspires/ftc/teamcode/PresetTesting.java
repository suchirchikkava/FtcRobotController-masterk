package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="Preset TeleOp")
public class PresetTesting extends LinearOpMode{
    private DcMotor slideLeft;
    private DcMotor slideRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor intake;
    private DcMotor elbow;
    private Servo airplane;
    private Servo purplePixel;
    private final int high = 950;
    private final int high2 = -high;
    private final int low = 0; //end of slides variable
    private final int takeIn = 75; //end of intake variables
    private final int placing = -300;
    private final int base = 0; //end of elbow variables
    private final double launchPlane = 0.2;
    private final double holdPlane = 1.0; //end of plane variables
    private final double secure = 0.15;
    private final double release = 1.0; // end of purplepixel variables
    private double power = 0.1;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        slideLeft = hardwareMap.get(DcMotor.class, "slideLeft");
        slideRight = hardwareMap.get(DcMotor.class, "slideRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        airplane = hardwareMap.get(Servo.class, "plane");
        purplePixel = hardwareMap.get(Servo.class, "purplePixel");

        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.REVERSE);

        slideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideLeft.setTargetPosition(0);
        slideLeft.setPower(1.0);
        slideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRight.setTargetPosition(0);
        slideRight.setPower(0.0);
        slideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setTargetPosition(0);
        elbow.setPower(1);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);



        while (opModeIsActive()) {
            telemetry.addData("wheel speed", power);
            telemetry.update();
            
            moveBot();
            runIntake();
            moveElbow();
            moveBotSlow();
            plane();
            moveSlides();
            purpleServo();
            decreasePower();
        }
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

        if (gamepad2.left_stick_y > 0.3) {
            setSlides(slideLeft.getCurrentPosition() + 100, slideRight.getCurrentPosition() - 100);
        }

        if (gamepad2.left_stick_y < -0.3) {
            setSlides(slideLeft.getCurrentPosition() - 100, slideRight.getCurrentPosition() + 100);
        }

        if (gamepad2.left_stick_y > -0.3 && gamepad2.left_stick_y < 0.3) {
            setSlides(slideLeft.getCurrentPosition(), slideRight.getCurrentPosition());
        }
    }

    public void moveBot() {

        if (gamepad1.left_stick_y > 0.3) {
            frontLeft.setPower(power);
            backLeft.setPower(power);
            frontRight.setPower(power);
            backRight.setPower(power);
        }

        if (gamepad1.left_stick_y < -0.3) {
            frontLeft.setPower(-power);
            backLeft.setPower(-power);
            frontRight.setPower(-power);
            backRight.setPower(-power);
        }

        if (0.3 > gamepad1.left_stick_y && gamepad1.left_stick_y > -0.3) {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }

        if (gamepad1.left_stick_x > 0.5) {
            frontLeft.setPower(-power);
            backLeft.setPower(power);
            frontRight.setPower(power);
            backRight.setPower(-power);
        }

        if (gamepad1.left_stick_x < -0.5) {
            frontLeft.setPower(power);
            backLeft.setPower(-power);
            frontRight.setPower(-power);
            backRight.setPower(power);
        }

        if (0.5 > gamepad1.left_stick_x && gamepad1.left_stick_x > -0.5) {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }

        if (gamepad1.right_bumper) {
            frontLeft.setPower(-power);
            backLeft.setPower(-power);
            frontRight.setPower(power);
            backRight.setPower(power);
        }

        if (gamepad1.left_bumper) {
            frontLeft.setPower(power);
            backLeft.setPower(power);
            frontRight.setPower(-power);
            backRight.setPower(-power);
        }
    }

    public void moveBotSlow() {

        if (gamepad1.right_stick_y > 0.3) {
            frontLeft.setPower(0.3);
            backLeft.setPower(0.3);
            frontRight.setPower(0.3);
            backRight.setPower(0.3);
        }

        if (gamepad1.right_stick_y < -0.3) {
            frontLeft.setPower(-0.3);
            backLeft.setPower(-0.3);
            frontRight.setPower(-0.3);
            backRight.setPower(-0.3);
        }

        if (0.3 > gamepad1.right_stick_y && gamepad1.right_stick_y > -0.3) {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }

        if (gamepad1.right_stick_x < -0.5) {
            frontLeft.setPower(-0.3);
            backLeft.setPower(0.3);
            frontRight.setPower(0.3);
            backRight.setPower(-0.3);
        }

        if (gamepad1.right_stick_x > 0.5) {
            frontLeft.setPower(0.3);
            backLeft.setPower(-0.3);
            frontRight.setPower(-0.3);
            backRight.setPower(0.3);
        }

        if (0.5 > gamepad1.right_stick_x && gamepad1.right_stick_x > -0.5) {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }

        if (gamepad1.left_trigger == 1) {
            frontLeft.setPower(0.3);
            backLeft.setPower(0.3);
            frontRight.setPower(-0.3);
            backRight.setPower(-0.3);
        }

        if (gamepad1.right_trigger == 1) {
            frontLeft.setPower(-0.3);
            backLeft.setPower(-0.3);
            frontRight.setPower(0.3);
            backRight.setPower(0.3);
        }
    }

    public void runIntake() {
        if (gamepad1.dpad_down) {
            intake.setPower(-0.3);
        }

        if (gamepad1.dpad_up) {
            intake.setPower(0.4);
        }

        if (!gamepad1.dpad_down && !gamepad1.dpad_up) {
            intake.setPower(0.0);
        }
    }

    public void moveElbow() {
        if (gamepad2.b) {
            elbow.setPower(0.5);
            sleep(100);
            elbow.setTargetPosition(-230);
            sleep(500);
            elbow.setPower(0);
        }

        if (gamepad2.x) {
            elbow.setPower(0.05);
            sleep(100);
            elbow.setTargetPosition(-200);
            sleep(500);
            elbow.setPower(0.0);
            setElbow(base);;
        }

        if (gamepad2.dpad_up) {
            setElbow(-150);
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
        if (gamepad2.left_trigger == 1) {
            airplane.setPosition(holdPlane);
            sleep(3000);
            airplane.setPosition(launchPlane);
        }//holds airplane
    }

    public void purpleServo() {
        if (gamepad2.left_bumper) {
            purplePixel.setPosition(release);
        }

        if (gamepad2.right_bumper) {
            purplePixel.setPosition(secure);
        }

        if (gamepad2.dpad_down) {
            telemetry.addData("Position", purplePixel.getPosition());
            telemetry.update();
            
            purplePixel.setPosition(purplePixel.getPosition() + 0.1);
        }
    }

    public void setElbow(int position) {
        elbow.setTargetPosition(position);
        elbow.setPower(0.4);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public double decreasePower() {
        if (gamepad1.dpad_left) {
            power += 0.1;
        }
        return power;
    }
}
