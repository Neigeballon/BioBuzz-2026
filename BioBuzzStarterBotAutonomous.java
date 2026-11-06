package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "BioBuzz StarterBot Autonomous", group = "StarterBot")
//@Disabled
public class BioBuzzStarterBotAutonomous extends OpMode {

   private enum AutonState {
      FIRST,
      SECOND,
      THIRD,
      FOURTH,
      FIFTH,
      SIXTH,
      SEVENTH,
      EIGHTH,
      NINETH,
      TENTH,
      DONE
   }
   private AutonState currentState = AutonState.FIRST;

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

   private ElapsedTime stateTimer = new ElapsedTime();
  
   public final int LAUNCHER_TARGET_VELOCITY = 1250;
   public final int LAUNCHER_MIN_VELOCITY = 1200;

   @Override
   public void init() {

       LFD = hardwareMap.get(DcMotor.class, "left_front_drive");
       LBD = hardwareMap.get(DcMotor.class, "left_back_drive");
       RFD = hardwareMap.get(DcMotor.class, "right_front_drive");
	   RBD = hardwareMap.get(DcMotor.class, "right_back_drive");
       intake = hardwareMap.get(DcMotor.class, "intake");
       launcher = hardwareMap.get(DcMotorEx.class, "launcher");
       
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
   public void start() {stateTimer.reset();}

   @Override
   public void loop() {
      LimeLight();

      switch (currentState) {
         case FIRST:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.SECOND;
            }
            break;
         }
         case SECOND:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.THIRD;
            }
            break;
         }
         case THIRD:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.FOURTH;
            }
            break;
         }
         case FOURTH:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.FIFTH;
            }
            break;
         }
         case FIFTH:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.SIXTH;
            }
            break;
         }
         case SIXTH:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.SEVENTH;
            }
            break;
         }
         case SEVENTH:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.EIGHTH;
            }
            break;
         }
         case EIGHTH:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.NINETH;
            }
            break;
         }
         case NINETH:

            if (stateTimer.seconds() >= 1.0) {
               stateTimer.reset();
               currentState = AutonState.TENTH;
            }
            break;
         }
         case TENTH:

            if (stateTimer.seconds() >= 1.000) {
               stateTimer.reset();
               currentState = AutonState.DONE;
            }
            break;
         }
         case DONE:
            
            break;
         }
      }
   }

   void LimeLight() {
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            for (LLResultTypes.FiducialResult f : fiducials) {
                if (ids.contains(f.getFiducialId()) && f.getRobotPoseTargetSpace().getPosition().z > 1.0) {
                    turret.setPosition(Range.clip(0.5 + f.getTargetXDegrees() / 360.0, 0.0, 1.0));
                    break;
                }
            }
        }
    }

}
