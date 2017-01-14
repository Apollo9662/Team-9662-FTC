package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Autonomus_Front", group="Pushbot")
//@Disabled
public class Autonomus_Front extends LinearOpMode {

    /* Declare OpMode members. */
    Hardware robot = new Hardware();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 1440;
    static final double DRIVE_GEAR_REDUCTION = 1;
    static final double WHEEL_DIAMETER_CM = 10.0;
    static final double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_CM * 3.1415);
    static final double WHEEL_DISTANCE_CM = 34;

    static final double     DRIVE_SPEED             = 0.5;
    static final double     TURN_SPEED              = 0.3;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);



        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.Motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.Motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.Motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        robot.Motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robot.Motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.Motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robot.Motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        Throw(100, 4000);

        drive(DRIVE_SPEED, 30);

        turn(TURN_SPEED, 100);

        drive(DRIVE_SPEED, 60);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }


    public void drive(double speed, double cm) {

        int newTarget;

        if (opModeIsActive()) {

            robot.Motor1.setDirection(DcMotor.Direction.FORWARD);
            robot.Motor2.setDirection(DcMotor.Direction.REVERSE);
            robot.Motor3.setDirection(DcMotor.Direction.REVERSE);
            robot.Motor4.setDirection(DcMotor.Direction.FORWARD);

            robot.Motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.Motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Determine new target position, and pass to motor controller
            newTarget = (int)(cm * COUNTS_PER_CM);
            robot.Motor1.setTargetPosition(newTarget);
            //robot.Motor2.setTargetPosition(newTarget);
            robot.Motor3.setTargetPosition(newTarget);
            //robot.Motor4.setTargetPosition(newTarget);

            // Turn On RUN_TO_POSITION
            robot.Motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //robot.Motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.Motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //robot.Motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.Motor1.setPower(Math.abs(speed));
            robot.Motor2.setPower(Math.abs(speed));
            robot.Motor3.setPower(Math.abs(speed));
            robot.Motor4.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() && robot.Motor1.getCurrentPosition() < newTarget && robot.Motor3.getCurrentPosition() < newTarget) {

                // Display it for the driver.
                telemetry.addData("Running...", "%d");
                telemetry.addData("Motor 1,2 Pos: ", robot.Motor1.getCurrentPosition());
                telemetry.addData("Motor 3,4 Pos: ", robot.Motor3.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.Motor1.setPower(0);
            robot.Motor2.setPower(0);
            robot.Motor3.setPower(0);
            robot.Motor4.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.Motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //robot.Motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.Motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //robot.Motor4.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }

    public void turn(double speed, double deg) {

        robot.Motor1.setDirection(DcMotor.Direction.FORWARD);
        robot.Motor2.setDirection(DcMotor.Direction.REVERSE);
        robot.Motor3.setDirection(DcMotor.Direction.REVERSE);
        robot.Motor4.setDirection(DcMotor.Direction.FORWARD);

        double newTargetLeft;
        double newTargetRight;

        robot.Motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(opModeIsActive()) {
            newTargetLeft = (WHEEL_DISTANCE_CM / WHEEL_DIAMETER_CM) * deg * 4;
            newTargetRight = -(WHEEL_DISTANCE_CM / WHEEL_DIAMETER_CM) * deg * 4;

            robot.Motor1.setTargetPosition((int)newTargetLeft);
            robot.Motor3.setTargetPosition((int)newTargetRight);

            robot.Motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.Motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.Motor1.setPower(Math.abs(speed));
            robot.Motor2.setPower(Math.abs(speed));
            robot.Motor3.setPower(Math.abs(speed));
            robot.Motor4.setPower(-Math.abs(speed));

            while (opModeIsActive() && robot.Motor1.getCurrentPosition() < newTargetLeft && (-1 * robot.Motor3.getCurrentPosition()) > newTargetRight) {
                telemetry.addData("Running...", "%d");
                telemetry.addData("Motor 1,2 Pos: ", robot.Motor1.getCurrentPosition());
                telemetry.addData("Motor 3,4 Pos: ", robot.Motor3.getCurrentPosition());
                telemetry.update();
            }

            robot.Motor1.setPower(0);
            robot.Motor2.setPower(0);
            robot.Motor3.setPower(0);
            robot.Motor4.setPower(0);

            robot.Motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.Motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
    }

    public void Throw(int power, int windUpTime)
    {
        if (opModeIsActive()) {
            long time = System.currentTimeMillis();

            robot.Door.setPosition(0.45);
            robot.BarrierR.setPosition(0.4);
            robot.BarrierL.setPosition(0.6);
            robot.MotorThrow.setPower(power);

            while (System.currentTimeMillis() - time < windUpTime) {
                telemetry.addData("Warming Up...", "%d");
                telemetry.update();
            }

            robot.BarrierR.setPosition(0);
            robot.BarrierL.setPosition(1);
            sleep(250);
            robot.Door.setPosition(0.75);
            sleep(250);
            robot.MotorThrow.setPower(0);
            sleep(250);
        }
    }
}
