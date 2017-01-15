package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="#9662: TeleOp Drive", group="#9662")
//@Disabled
public class TeleopTank extends LinearOpMode {

    /* Declare OpMode members. */
    Hardware robot = new Hardware();
    StopWatch stopwatch = new StopWatch();

    boolean motorReversed = false;
    boolean barrierOpen = false;
    boolean throwReady = false;
    boolean throwCounting = false;
    long timeReverse = 0;
    long timeDoor = 0;
    long timeBarrier = 0;
    long timeMotorSpeed = 0;
    int DoorPosition = 0;
    float Speed = 1;


    @Override
    public void runOpMode() {
        float left = 0;
        float right = 0;
        float powerThrow = 1;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Hi", "Teleop Idle, Press Play to activate");
        telemetry.addData("As Nadafni Sais", "NEVO SUCKS");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
            if (!motorReversed) {
                left = -gamepad1.left_stick_y;
                right = -gamepad1.right_stick_y;
                robot.Motor1.setDirection(DcMotor.Direction.FORWARD);
                robot.Motor2.setDirection(DcMotor.Direction.REVERSE);
                robot.Motor3.setDirection(DcMotor.Direction.REVERSE);
                robot.Motor4.setDirection(DcMotor.Direction.FORWARD);
            } else {
                right = -gamepad1.left_stick_y;
                left = -gamepad1.right_stick_y;
                robot.Motor1.setDirection(DcMotor.Direction.REVERSE);
                robot.Motor2.setDirection(DcMotor.Direction.FORWARD);
                robot.Motor3.setDirection(DcMotor.Direction.FORWARD);
                robot.Motor4.setDirection(DcMotor.Direction.REVERSE);
            }
            robot.Motor1.setPower(left * Speed);
            robot.Motor2.setPower(left * Speed);
            robot.Motor3.setPower(right * Speed);
            robot.Motor4.setPower(right * Speed);

            //Gamepad1 Button X Reverse Driving Motors
            if (gamepad1.x && System.currentTimeMillis() - timeReverse > 300) {
                motorReversed = !motorReversed;
                timeReverse = System.currentTimeMillis();
            }

            //Gamepad1 Button Left Bumper Set Door Position
            if (gamepad1.left_bumper && System.currentTimeMillis() - timeDoor > 300) {
                if (DoorPosition < 2) {
                    DoorPosition++;
                } else {
                    DoorPosition = 0;
                }
                timeDoor = System.currentTimeMillis();
            }

            //Gamepad1 Button Left Bumper Set Door Position. Set segment
            if (DoorPosition == 0) {
                robot.Door.setPosition(0);
            } else if (DoorPosition == 1) {
                robot.Door.setPosition(0.45);
            } else {
                robot.Door.setPosition(0.75);
            }

            //Gamepad1 Button Y Set Barrier Position

            if (gamepad1.y && System.currentTimeMillis() - timeBarrier > 300) {
                barrierOpen = !barrierOpen;
                timeBarrier = System.currentTimeMillis();
            }


            //Gamepad1 Button Y Set Barrier Position. Set Segment.
            if (barrierOpen) {
                robot.BarrierL.setPosition(1);
                robot.BarrierR.setPosition(0);
            } else {
                robot.BarrierL.setPosition(0.4);
                robot.BarrierR.setPosition(0.6);
            }

            //Gamepad1 Button B Open Barrier And Insert Ball
            if (gamepad1.b && throwReady) {
                barrierOpen = true;
                DoorPosition = 0;
            }

            //Gamepad1 RightTrigger Activate Motor Throw
            if (gamepad1.right_trigger > 0.8) {
                robot.MotorThrow.setPower(powerThrow);
                if (!throwCounting) {
                    stopwatch.start();
                    throwCounting = true;
                } else if (stopwatch.getElapsedTime() > 3000) {
                    throwReady = true;
                }
            } else {
                robot.MotorThrow.setPower(0);
                throwReady = false;
                throwCounting = false;
                stopwatch.stop();

            }

            //Gamepad1 Dpad Up/Down Control Driving Speed
            if (gamepad1.dpad_up && System.currentTimeMillis() - timeMotorSpeed > 200) {
                if (Speed < 1) {
                    Speed += 0.1;
                }
                timeMotorSpeed = System.currentTimeMillis();
            }
            if (gamepad1.dpad_down && System.currentTimeMillis() - timeMotorSpeed > 200) {
                if (Speed > 0.1) {
                    Speed -= 0.1;
                }
                timeMotorSpeed = System.currentTimeMillis();
            }

            // Send telemetry message to signify robot running;
            telemetry.addData("Throw Ready? ", throwReady);
            telemetry.addData("Throw Power", powerThrow);
            telemetry.addData("\n", "");
            telemetry.addData("Driving Power", "%.1f", Speed * 100);
            telemetry.addData("Left", "%.1f", left * 100);
            telemetry.addData("Right ", "%.1f", right * 100);
            telemetry.addData("Motors Reversed? ", motorReversed);
            telemetry.addData("\n", "");
            telemetry.addData("Servo's Positions", "");
            telemetry.addData("Barrier Open? ", barrierOpen);
            telemetry.addData("Door Position", DoorPosition);

            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}

