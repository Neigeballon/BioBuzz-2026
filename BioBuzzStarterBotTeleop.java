package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

@TeleOp(name = "BioBuzz StarterBot Teleop", group = "StarterBot")
public class BioBuzzStarterbotTeleop extends OpMode {
   // Declare OpMode members.
   private DcMotor LFD = null;
   private DcMotor LBD  = null;
   private DcMotor RFD = null;
   private DcMotor RBD = null;
   private DcMotorEx launcher = null;
   private DcMotor intake = null;
   private Limelight3A limelight = null;
   private Servo turret = null;
   private ArrayList<Integer> ids = new ArrayList<>();
  
   public final int LAUNCHER_TARGET_VELOCITY = 1375;
   public final int LAUNCHER_MIN_VELOCITY = 1350;

   double LFP = 0;
   double LBP = 0;
   double RFP = 0;
   double RBP = 0;
   boolean isPressed = false;
   boolean on = false;

   @Override
   public void init() {

       LFD = hardwareMap.get(DcMotor.class, "left_front_drive");
       LBD = hardwareMap.get(DcMotor.class, "left_back_drive");
       RFD = hardwareMap.get(DcMotor.class, "right_front_drive");
	   RBD = hardwareMap.get(DcMotor.class, "right_back_drive");
       intake = hardwareMap.get(DcMotor.class, "intake");
       launcher = hardwareMap.get(DcMotorEx.class, "launcher");
	   limelight = hardwareMap.get(Limelight3A.class, "limelight");

	   limelight.pipelineSwitch(0);
       
       LFD.setDirection(DcMotor.Direction.REVERSE);
	   LBD.setDirection(DcMotor.Direction.REVERSE);
       RFD.setDirection(DcMotor.Direction.FORWARD);
	   RBD.setDirection(DcMotor.Direction.FORWARD);

       LFD.setZeroPowerBehavior(BRAKE);
	   LBD.setZeroPowerBehavior(BRAKE);
       RFD.setZeroPowerBehavior(BRAKE);
	   RBD.setZeroPowerBehavior(BRAKE);
       intake.setZeroPowerBehavior(BRAKE);

       // Auto leaves the drive motors in RUN_TO_POSITION, so switch back to plain power control.
       LFD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	   LBD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       RFD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
	   RBD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

       launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

       telemetry.addData("Status", "Initialized");
	   telemetry.update();
   }

   @Override
   public void init_loop() {
	   if (gamepad1.b) {
		   ids.clear();
		   ids.add(30); ids.add(31); ids.add(32); ids.add(33); ids.add(34); ids.add(35); ids.add(36); ids.add(37);
	   }
	   else if (gamepad1.x) {
		   ids.clear();
		   ids.add(38); ids.add(39); ids.add(40); ids.add(41); ids.add(42); ids.add(43); ids.add(44); ids.add(45);
	   }
   }

   @Override
   public void start() {
   }

   @Override
   public void loop() {
	   double speedDivider = gamepad1.left_bumper ? 2 : 1;
	   double y = -gamepad1.left_stick_y;
	   double x = gamepad1.left_stick_x*1.1;
	   double rx = gamepad1.right_stick_x;
	   double divider = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

	   LFP = (y + x + rx) / (divider * speedDivider);
	   LBP = (y - x + rx) / (divider * speedDivider);
	   RFP = (y - x - rx) / (divider * speedDivider);
	   RBP = (y + x - rx) / (divider * speedDivider);

	   LFD.setPower(LFP);
	   LBD.setPower(LBP);
	   RFD.setPower(RFP);
	   RBD.setPower(RBP);

	   limelight();
	  
       launch();
   }

   @Override
   public void stop() {
	   ids.clear();
   }

   void limelight() {
	   LLResult result = limelight.getLatestResult();

	   if (result != null && result.isValid()) {
		    List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
		    if (!fiducials.isEmpty()) {
				LLResultTypes.FiducialResult tag = fiducials.get(0);
				int tagID = tag.getFiducialId();
				double angle = tag.getTargetXDegrees() * (1/360);
				double z = tag.getRobotPoseTargetSpace().getZ();
			}
	   }
	   
	   if (ids.contains(tagID) && z > 1) {
		   turret.setPosition(turret.getPosition() + angle);
	   }
	   
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
