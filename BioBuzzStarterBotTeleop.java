package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "BioBuzz StarterBot Teleop", group = "StarterBot")
//@Disabled
public class BioBuzzStarterBotTeleop extends OpMode {

   // Declare OpMode members.
   private DcMotor leftDrive = null;
   private DcMotor rightDrive = null;
   private DcMotorEx launcher = null;
   private DcMotor intake = null;
   private CRServo leftIntakeServo = null;
   private CRServo rightIntakeServo = null;
   private CRServo windmillServo = null;
  
   public final int LAUNCHER_TARGET_VELOCITY = 1250;
   public final int LAUNCHER_MIN_VELOCITY = 1200;

   double leftPower;
   double rightPower;
   double speedMultiplier;
   double intakePower;
   double windmillPower;
   boolean isPressed = false;
   boolean on = false;
   boolean upToSpeed = false;

   @Override
   public void init() {

       leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
       rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
       intake = hardwareMap.get(DcMotor.class, "intake");
       launcher = hardwareMap.get(DcMotorEx.class, "launcher");
       windmillServo = hardwareMap.get(CRServo.class, "windmill");
       leftIntakeServo = hardwareMap.get(CRServo.class, "left_intake_servo");
       rightIntakeServo = hardwareMap.get(CRServo.class, "right_intake_servo");

       leftDrive.setDirection(DcMotor.Direction.FORWARD);
       rightDrive.setDirection(DcMotor.Direction.REVERSE);

       leftDrive.setZeroPowerBehavior(BRAKE);
       rightDrive.setZeroPowerBehavior(BRAKE);
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
   public void init_loop() {
   }

   @Override
   public void start() {
   }

   @Override
   public void loop() {

       arcadeDrive(-gamepad1.left_stick_y, gamepad1.right_stick_x);

       launch();

       telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
       telemetry.addLine();
	   telemetry.update();
   }

   @Override
   public void stop() {
   }

   void arcadeDrive(double forward, double rotate) {
	   speedMultiplier = gamepad1.left_bumper ? 0.5 : 1.0;
       leftPower = forward + rotate;
       rightPower = forward - rotate;

       // Scale both sides down together if either exceeds 1, so turning stays proportional.
       double max = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
       leftPower /= max;
       rightPower /= max;

       leftDrive.setPower(leftPower*speedMultiplier);
       rightDrive.setPower(rightPower*speedMultiplier);
   }

   void launch() {
	    
       if (gamepad2.x && !isPressed) {
	     isPressed = true;
           on = !on;
       } 
       else if (!gamepad2.x) {
           isPressed = false;
       }
	   
	   if (on) {
	       launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
       }
       else {
           launcher.setVelocity(0);
       }

	   if (launcher.getVelocity() > LAUNCHER_MIN_VELOCITY && !upToSpeed) {
		   upToSpeed = true;
		   gamepad1.rumble(500);
		   gamepad2.rumble(500);
	   }
	   else if (launcher.getVelocity() < LAUNCHER_MIN_VELOCITY) {
		   upToSpeed = false;
	   }

	   intakePower = 0;
   	   windmillPower = 0;

      // Check manual intake controls (bumpers/triggers)
      if (gamepad2.left_bumper) {
          intakePower = 1.0;
      } 
	  else if (gamepad2.left_trigger > 0.5) {
          intakePower = -1.0;
      }

      // Check manual indexer controls (right bumper/trigger)
      if (gamepad2.right_bumper && launcher.getVelocity() > LAUNCHER_MIN_VELOCITY) {
          windmillPower = 1.0;
      } 
	  else if (gamepad2.right_trigger > 0.5) {
          windmillPower = -1.0;
      }

      // Check simultaneous arrow controls (overrides if arrows are pressed)
      if (gamepad2.dpad_right && launcher.getVelocity() > LAUNCHER_MIN_VELOCITY) {
          intakePower = 1.0;
          windmillPower = 1.0;
      }
   	  else if (gamepad2.dpad_left) {
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
