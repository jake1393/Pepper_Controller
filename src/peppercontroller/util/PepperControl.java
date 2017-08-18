/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peppercontroller.util;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAutonomousLife;
import com.aldebaran.qi.helper.proxies.ALBackgroundMovement;
import com.aldebaran.qi.helper.proxies.ALBasicAwareness;
import com.aldebaran.qi.helper.proxies.ALBehaviorManager;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import java.util.List;

/**
 *
 * @author Hank
 */
public class PepperControl {

    private Application application;
    private Session session;
    private ALAutonomousLife autoLife;
    private ALBackgroundMovement bgMove;
    private ALBasicAwareness awareness;
    private ALMotion motion;
    private ALRobotPosture posture;
    private ALBehaviorManager behaManager;

    public PepperControl(Application application) throws Exception {
        this.application = application;
        application.start(); // need to make thread to avoid inactive connection
        session = application.session();
        autoLife = new ALAutonomousLife(session);
        //autoLife.stopFocus();
        //autoLife.stopAll();
        bgMove = new ALBackgroundMovement(session);
        /*
        if (bgMove.isEnabled())
            bgMove.setEnabled(false);
         */
        awareness = new ALBasicAwareness(session);
        /*
        if (awareness.isEnabled())
            awareness.setEnabled(false);
         */
        motion = new ALMotion(session);
        posture = new ALRobotPosture(session);

        behaManager = new ALBehaviorManager(session);

        awareness.stopAwareness();
        posture.goToPosture("StandInit", 1.5f);
    }

    public void move(float x, float y, float z) throws CallError, InterruptedException{
        if (!motion.getMoveArmsEnabled("Arms")) {
            motion.setMoveArmsEnabled(true, true);
        }
        motion.move(x, y, z);
    }

    public void stopMove() throws CallError, InterruptedException {
        motion.stopMove();
    }

    public void restore() throws CallError, InterruptedException {
        autoLife.setState("solitary");
        bgMove.setEnabled(true);
        awareness.setEnabled(true);
    }

    public void checkCurrentStatus() throws CallError, InterruptedException {
        //autoLife.stopFocus();
        //autoLife.stopAll();
        /*
        if (bgMove.isEnabled())
            bgMove.setEnabled(false);
        if (awareness.isEnabled())
            awareness.setEnabled(false);
         */
    }

    public void stopService() throws CallError, InterruptedException {
        awareness.startAwareness();
        session.close();
        application.stop();
    }

    public List<String> getBehaviorList() throws CallError, InterruptedException {
        return behaManager.getBehaviorNames();
    }

    public void runBehavior(String inputURL) throws CallError, InterruptedException {

        if (!behaManager.isBehaviorRunning(inputURL)) {
            behaManager.runBehavior(inputURL);
        }
    }
}
