package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

@TeleOp(name = "BioBuzz StarterBot Teleop", group = "StarterBot")
public class BioBuzzStarterbotTeleop extends OpMode {
        private DcMotor leftDrive = null;
        private DcMotor rightDrive = null;
        private DcMotorEx launcher = null;
        private DcMotor intake = null;
        private CRServo leftIntakeServo = null;
        private CRServo rightIntakeServo = null;
        private CRServo windmillServo = null;

        public final int LAUNCHER_TARGET_VELOCITY = 1300;
        public final int LAUNCHER_MIN_VELOCITY = 1275;

        double leftPower;
        double rightPower;
        double intakePower;
        double windmillPower;

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
            arcadeDrive(-gamepad1.left_stick_y, gamepad1.right_stick_x);

            launch();

            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addLine();
            telemetry.update();
        }

        void arcadeDrive(double forward, double rotate) {
            double speedMultiplier = gamepad1.left_bumper ? 0.5 : 1.0;
            leftPower = forward - rotate;
            rightPower = forward + rotate;
            // Scale both sides down together if either exceeds 1, so turning stays proportional.
            double max = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
            leftPower /= max;
            rightPower /= max;
            leftDrive.setPower(leftPower*speedMultiplier);
            rightDrive.setPower(rightPower*speedMultiplier);
        }
        void launch() {

            if (gamepad2.left_trigger > 0.5) {
                launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
            }
            else if (gamepad2.left_bumper) {
                launcher.setVelocity(-LAUNCHER_TARGET_VELOCITY);
            }
            else {
                launcher.setVelocity(0);
            }
            intakePower = 0;
            windmillPower = 0;

            // Check manual intake controls (bumpers/triggers)
            if (gamepad2.right_trigger > 0.5) {
                intakePower = 1.0;
            }
            else if (gamepad2.right_bumper) {
                intakePower = -1.0;
            }

            // Check manual indexer controls (right bumper/trigger)
            if (gamepad2.a) {
                windmillPower = 1.0;
            }
            else if (gamepad2.b) {
                windmillPower = -1.0;
            }

            // Check simultaneous arrow controls (overrides if arrows are pressed)
            if (gamepad2.dpad_up) {
                intakePower = 1.0;
                windmillPower = 1.0;
            }
            else if (gamepad2.dpad_down) {
                intakePower = -1.0;
                windmillPower = -1.0;
            }

            // 3. Apply final powers ONCE to motors and servos
            intake.setPower(intakePower);
            leftIntakeServo.setPower(intakePower);
            rightIntakeServo.setPower(intakePower);
            windmillServo.setPower(windmillPower);
        }
}

