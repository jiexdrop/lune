package com.jiexdrop.lune.model.entity.routine;

import com.jiexdrop.lune.model.entity.Entity;
import com.jiexdrop.lune.view.World;

/**
 * Created by jiexdrop on 05/11/17.
 */

public class Repeat extends Routine {

    private Routine routine;
    private int times;
    private int originalTimes;

    public Repeat(Routine routine){
        super.start();
        this.routine = routine;
        this.times = -1; // infinite
        this.originalTimes = times;
    }

    public Repeat(Routine routine, int times){
        super.start();
        if(times<1){
            fail();
        } else {
            this.routine = routine;
            this.times = times;
            this.originalTimes = times;
        }
    }

    @Override
    public void start() {
        super.start();
        routine.start();
    }

    public void reset(){
        times = originalTimes;
    }


    @Override
    public void act(Entity entity, World world) {
        if (routine.hasFailed()) {
            fail();
        } else if (routine.hasSucceeded()) {
            if (times == 0) {
                succeed();
                return;
            }
            if (times > 0 || times <= -1) {
                times--;

                reset();
                routine.start();
            }
        }
        if (routine.isRunning()) {
            routine.act(entity, world);
        }
    }
}
