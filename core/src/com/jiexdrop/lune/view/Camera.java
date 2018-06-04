package com.jiexdrop.lune.view;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.jiexdrop.lune.GameVariables;

/**
 * singleton
 * Created by jiexdrop on 11/08/17.
 */

public class Camera {
    public static PerspectiveCamera setupCamera(float width, float height){
        PerspectiveCamera setupCamera = new PerspectiveCamera(GameVariables.FIELD_OF_VIEW, width, height);
        setupCamera.position.set(0f, GameVariables.CAMERA_ZOOM_POSITION, GameVariables.CAMERA_ZOOM_POSITION);
        setupCamera.near = 0.1f;
        setupCamera.far = GameVariables.CAMERA_FAR;
        setupCamera.update();
        return setupCamera;
    }

    public static float getCameraRotation(PerspectiveCamera camera)
    {
        return (-(float) Math.atan2(camera.up.x, camera.up.y) * MathUtils.radiansToDegrees) + 180;
    }
}
