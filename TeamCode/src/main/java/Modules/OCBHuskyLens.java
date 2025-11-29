package Modules;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.concurrent.TimeUnit;


import com.qualcomm.hardware.dfrobot.HuskyLens;


public class OCBHuskyLens {

    public static double leftRegionCount = 0;
    public static double midRegionCount = 0;
    public static double rightRegionCount = 0;

    public static void getRegions() {
        HuskyLens.Block[] blocks = OCBHWM.huskyLens.blocks();
                telemetry.addData("Block count", blocks.length);
        for (int i = 0; i < blocks.length; i++) {
            telemetry.addData("Block", blocks[i].toString());

            if (blocks[i].x > Constants.LEFTBOUND) {
                leftRegionCount = leftRegionCount + (blocks[i].width * blocks[i].height * ((double) 1 / blocks[i].y));
            } else if (blocks[i].x < Constants.LEFTBOUND && blocks[i].x > Constants.RIGHTBOUND) {
                midRegionCount = midRegionCount + (blocks[i].width * blocks[i].height * ((double) 1 / blocks[i].y));
            } else {
                rightRegionCount = rightRegionCount + (blocks[i].width * blocks[i].height * ((double) 1 / blocks[i].y));
            }
            /*
             * Here inside the FOR loop, you could save or evaluate specific info for the currently recognized Bounding Box:
             * - blocks[i].width and blocks[i].height   (size of box, in pixels)
             * - blocks[i].left and blocks[i].top       (edges of box)
             * - blocks[i].x and blocks[i].y            (center location)
             * - blocks[i].id                           (Color ID)
             *
             * These values have Java type int (integer).
             */
        }
    }

    public static void resetCounts(){
        leftRegionCount = 0;
        midRegionCount = 0;
        rightRegionCount = 0;
    }
}