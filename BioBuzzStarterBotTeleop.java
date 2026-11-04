package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

@TeleOp(name = "BioBuzz StarterBot Teleop", group = "StarterBot")
//@Disabled
public class BioBuzzStarterbotTeleop extends OpMode {

   // Declare OpMode members.
   private DcMotor leftFrontDrive = null;
   private DcMotor leftBackDrive  = null;
   private DcMotor rightFrontDrive = null;
   private DcMotor rightBackDrive = null;
   private DcMotorEx launcher = null;
   private DcMotor intake = null;
  
   public final int LAUNCHER_TARGET_VELOCITY = 1375;
   public final int LAUNCHER_MIN_VELOCITY = 1350;

   boolean isPressed = false;
   boolean on = false;
   boolean upToSpeed = false;

   @Override
   public void init() {

       leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
       leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
       rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
	   rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
       intake = hardwareMap.get(DcMotor.class, "intake");
       launcher = hardwareMap.get(DcMotorEx.class, "launcher");
       
       leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
	   leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
       rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
	   rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

       leftFrontDrive.setZeroPowerBehavior(BRAKE);
	   leftBackDrive.setZeroPowerBehavior(BRAKE);
       rightFrontDrive.setZeroPowerBehavior(BRAKE);
	   rightBackDrive.setZeroPowerBehavior(BRAKE);
       intake.setZeroPowerBehavior(BRAKE);

       // Auto leaves the drive motors in RUN_TO_POSITION, so switch back to plain power control.
       leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	   leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	   rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

       launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

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
	   double speedMultiplier = gamepad1.left_bumper ? 0.5 : 1.0;
       launch();
   }

   @Override
   public void stop() {
   }

   void launch() {
	    
       if (gamepad2.left_trigger > 0.5) {
		   launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
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
