package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

@Autonomous(name = "BioBuzz StarterBot Autonomous", group = "StarterBot")
public class BioBuzzStarterbotAutonomous extends OpMode {
    private enum AutonState {
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        DONE
    }
    private AutonState currentState = AutonState.FIRST;
    private ElapsedTime stateTimer = new ElapsedTime();
    private int steps = 0;
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotorEx launcher = null;
    private DcMotor intake = null;
    private CRServo leftIntakeServo = null;
    private CRServo rightIntakeServo = null;
    private CRServo windmillServo = null;

    public final int LAUNCHER_TARGET_VELOCITY = 1300;
    public final int LAUNCHER_MIN_VELOCITY = 1275;

    @Override
    public void init() {

        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        windmillServo = hardwareMap.get(CRServo.class, "windmill");
        leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
        rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        intake.setZeroPowerBehavior(BRAKE);

        // Auto leaves the drive motors in RUN_TO_POSITION, so switch back to plain power control.
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
        windmillServo.setPower(0);
        rightIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        windmillServo.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        switch (currentState) {
            case FIRST:
                launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
                if (stateTimer.seconds() > 5.0) {
                    intake.setPower(1.0);
                    stateTimer.reset();
                    currentState = AutonState.SECOND;
                }
                break;
            case SECOND:
                windmillServo.setPower(1.0);
                if (stateTimer.seconds() > 2.0) {
                    windmillServo.setPower(0);
                    stateTimer.reset();
                    currentState = AutonState.THIRD;
                }
                break;
            case THIRD:
                if (steps >= 3) {
                    launcher.setVelocity(0);
                    intake.setPower(0);
                    windmillServo.setPower(0);
                    currentState = AutonState.FOURTH;
                }
                if (stateTimer.seconds() > 2.0) {
                    stateTimer.reset();
                    steps++;
                    currentState = AutonState.SECOND;
                }
                break;
            case FOURTH:
                leftDrive.setPower(-0.5);
                rightDrive.setPower(-0.5);
                if (stateTimer.seconds() > 1.0) {
                    leftDrive.setPower(0);
                    rightDrive.setPower(0);
                    stateTimer.reset();
                    currentState = AutonState.DONE;
                }
                break;
            case DONE:
                break;
        }
    }
}
