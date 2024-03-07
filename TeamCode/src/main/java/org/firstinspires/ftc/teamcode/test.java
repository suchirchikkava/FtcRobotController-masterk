package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name="TEST")
public class test extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor elbow;
    private Servo purplePixel;
    private final int placing = -300;
    private final int base = 0; //end of elbow variables
    private final double secure = 0.0;
    private final double release = 0.5;

    //1 foot = 1000
    //Arm moves up to a foot
    //1 inch is 83.3


    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        elbow = hardwareMap.get(DcMotor.class, "elbow");
        purplePixel = hardwareMap.get(Servo.class, "purplePixel");

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setPower(0.8);
        frontLeft.setPower(0.8);
        backRight.setPower(0.8);
        backLeft.setPower(0.8);

        elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setTargetPosition(0);
        elbow.setPower(1);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        purplePixel.setPosition(secure);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        moveBackward(500);
        sleep(750);
        moveForward(500);
        sleep(750);
        turnLeft(500);
        sleep(750);
        turnRight(500);
        sleep(750);
        moveLeft(500);
        sleep(750);
        moveRight(500);

    }

    public void runWheels() {
        while (frontLeft.getCurrentPosition() != frontLeft.getTargetPosition()) {

            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void reset() {
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void moveForward(int distance) {
        reset();
        frontRight.setTargetPosition(distance);
        frontLeft.setTargetPosition(distance);
        backRight.setTargetPosition(distance);
        backLeft.setTargetPosition(distance);
        runWheels();
    }

    public void moveBackward(int distance) {
        reset();
        frontRight.setTargetPosition(-distance);
        frontLeft.setTargetPosition(-distance);
        backRight.setTargetPosition(-distance);
        backLeft.setTargetPosition(-distance);
        runWheels();
    }

    public void turnRight(int distance) {
        reset();
        frontRight.setTargetPosition(distance);
        frontLeft.setTargetPosition(-distance);
        backRight.setTargetPosition(distance);
        backLeft.setTargetPosition(-distance);
        runWheels();
    }

    public void turnLeft(int distance) {
        reset();
        frontRight.setTargetPosition(-distance);
        frontLeft.setTargetPosition(distance);
        backRight.setTargetPosition(-distance);
        backLeft.setTargetPosition(distance);
        runWheels();
    }

    public void moveRight(int distance) {
        reset();
        frontRight.setTargetPosition(distance);
        frontLeft.setTargetPosition(-distance);
        backRight.setTargetPosition(-distance);
        backLeft.setTargetPosition(distance);
        runWheels();
    }

    public void moveLeft(int distance) {
        reset();
        frontRight.setTargetPosition(-distance);
        frontLeft.setTargetPosition(distance);
        backRight.setTargetPosition(distance);
        backLeft.setTargetPosition(-distance);
        runWheels();
    }

    public void leftMovements() {
        sleep(750);
        moveBackward(100);
        sleep(750);
        moveForward(300);
        sleep(750);
        moveRight(250);
        sleep(750);
        turnRight(1200);
        sleep(750);
        moveBackward(1640);
        sleep(750);
        frontRight.setPower(0.5);
        frontLeft.setPower(0.5);
        backRight.setPower(0.5);
        backLeft.setPower(0.5);
        moveLeft(675);
        sleep(750);
        dropBlock();
        sleep(750);
        moveLeft(800);
        sleep(750);
        moveBackward(250);
    }

    public void centerMovements() {
        turnRight(350);
        sleep(750);
        moveBackward(480);
        sleep(750);
        moveForward(280);
        sleep(750);
        turnRight(900);
        sleep(750);
        moveBackward(1660);
        sleep(750);
        dropBlock();
        sleep(750);
        moveLeft(800);
        sleep(750);
        moveBackward(250);
    }

    public void rightMovements() {
        moveForward(100);
        sleep(750);
        turnRight(900);
        sleep(750);
        moveBackward(1660);
        sleep(750);
        moveLeft(100);
        sleep(750);
        dropBlock();
        sleep(750);
        moveLeft(900);
        sleep(750);
        moveBackward(250);
    }

    public void dropBlock() {
        elbow.setPower(0.5);
        sleep(100);
        elbow.setTargetPosition(-240);
        sleep(500);
        elbow.setPower(0);
        sleep(2000);
        elbow.setPower(0.05);
        sleep(100);
        elbow.setTargetPosition(-200);
        sleep(500);
        elbow.setPower(0.0);
        setElbow(base);
    }

    public void setElbow(int position) {
        elbow.setTargetPosition(position);
        elbow.setPower(0.2);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

}

