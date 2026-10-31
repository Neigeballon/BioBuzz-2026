package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
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
   private DcMotor leftDrive = null;
   private DcMotor rightDrive = null;
   private DcMotorEx launcher = null;
   private DcMotor intake = null;
   private CRServo leftIntakeServo = null;
   private CRServo rightIntakeServo = null;
   private CRServo windmillServo = null;

   private ElapsedTime stateTimer = new ElapsedTime();
  
   public final int LAUNCHER_TARGET_VELOCITY = 1250;
   public final int LAUNCHER_MIN_VELOCITY = 1200;

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
   public void start() {
      stateTimer.reset();
   }

   @Override
   public void loop() {
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

}
