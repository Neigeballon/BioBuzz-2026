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

@Autonomous(name = "BioBuzz StarterBot Autonomous", group = "StarterBot")
//@Disabled
public class BioBuzzStarterBotAutonomous extends OpMode {

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
