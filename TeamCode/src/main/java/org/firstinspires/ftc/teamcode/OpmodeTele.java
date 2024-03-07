package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="NEW TeleOp")
public class OpmodeTele extends OpMode{
    private DcMotor slideLeft;
    private DcMotor slideRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor intake;
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

        double x = gamepad1.left_stick_x * 0.55;
        double y = gamepad1.left_stick_y * 0.5;
        double rx = gamepad1.right_stick_x * 0.5 ;

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

    public void plane() {
        if (gamepad2.left_trigger == 1) {
            airplane.setPosition(holdPlane);
        }//holds airplane

        if (gamepad2.right_trigger == 1) {
            airplane.setPosition(launchPlane);
        }
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

    @Override
    public void init() {
        slideLeft = hardwareMap.get(DcMotor.class, "slideLeft");
        slideRight = hardwareMap.get(DcMotor.class, "slideRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        airplane = hardwareMap.get(Servo.class, "plane");
        purplePixel = hardwareMap.get(Servo.class, "purplePixel");


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
    }

    @Override
    public void loop() {
        moveBot();
        runIntake();
        plane();
        moveSlides();
        purpleServo();
    }
}
