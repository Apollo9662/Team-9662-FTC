package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 *
 * Note: the configuration of the servos is such that:
 *   As the arm servo approaches 0, the arm position moves up (away from the floor).
 *   As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class Hardware
{
    /* Public OpMode members. */
    public DcMotor Motor1 = null;
    public DcMotor Motor2 = null;
    public DcMotor Motor3 = null;
    public DcMotor Motor4 = null;

    public DcMotor MotorThrow = null;

    public Servo BarrierL = null;
    public Servo BarrierR = null;
    public Servo Door = null;

    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Hardware() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        Motor1 = hwMap.dcMotor.get("motor 1");
        Motor2 = hwMap.dcMotor.get("motor 2");
        Motor3 = hwMap.dcMotor.get("motor 3");
        Motor4 = hwMap.dcMotor.get("motor 4");

        BarrierL = hwMap.servo.get("BarrierL");
        BarrierR = hwMap.servo.get("BarrierR");
        Door =  hwMap.servo.get("Door");


        // Set all motors to zero power
        Motor1.setPower(0);
        Motor2.setPower(0);
        Motor3.setPower(0);
        Motor4.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        Motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        BarrierL.setPosition(0);
        BarrierR.setPosition(0);
        Door.setPosition(0);
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
