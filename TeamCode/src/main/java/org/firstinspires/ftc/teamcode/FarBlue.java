package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous (name="Blue Far")
public class FarBlue extends LinearOpMode {
    private static final boolean USE_WEBCAM = true;
    private static final String TFOD_MODEL_ASSET = "blueDarkModel.tflite";
    private static final String[] LABELS = {
            "blue",
    };
    private TfodProcessor tfod;
    private VisionPortal visionPortal;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private Servo purplePixel;
    private final int placing = -240;
    private final int base = 0; //end of elbow variables
    private final double secure = 0.15;
    private final double release = 1.0;

    //1 foot = 1000
    //Arm moves up to a foot
    //1 inch is 83.3


    @Override
    public void runOpMode() throws InterruptedException {

        initTfod();

        waitForStart();
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        purplePixel = hardwareMap.get(Servo.class, "purplePixel");

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontRight.setPower(1.0);
        frontLeft.setPower(1.0);
        backRight.setPower(1.0);
        backLeft.setPower(1.0);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        purplePixel.setPosition(secure);
        moveBackward(850);
        sleep(750);
        turnRight(375);
        sleep(750);
        getPosition();

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
        moveBackward(125);
        sleep(250);
        moveLeft(75);
        purplePixel.setPosition(release);
        sleep(100000);
    }

    public void centerMovements() {
        moveBackward(410);
        purplePixel.setPosition(release);
        sleep(100000);
    }

    public void rightMovements() {
        moveForward(100);
        sleep(750);
        turnLeft(900);
        sleep(750);
        moveBackward(290);
        sleep(750);
        moveLeft(220);
        purplePixel.setPosition(release);
        sleep(100000);
    }

    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()
                .setModelAssetName("blueDarkModel.tflite")
                .setModelLabels(LABELS)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        }

        else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        builder.addProcessor(tfod);

        visionPortal = builder.build();

        tfod.setMinResultConfidence(0.90f);

    }

    public void getPosition() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();

        if (currentRecognitions.size() > 0) {
            telemetry.addLine("SENSED");

            leftMovements();
        }

        else {
            turnLeft(350);
            sleep(750);
            moveBackward(100);
            sleep(2000);
        }

        currentRecognitions = tfod.getRecognitions();
        if (currentRecognitions.size() > 0) {
            telemetry.addLine("SENSED");

            centerMovements();
        }

        else {
            rightMovements();
        }
    }

}

