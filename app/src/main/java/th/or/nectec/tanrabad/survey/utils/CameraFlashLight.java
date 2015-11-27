/*
 * Copyright (c) 2015 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.List;

@SuppressWarnings("deprecation")
public class CameraFlashLight implements Torch {

    private static CameraFlashLight instance;

    private final Context context;
    private Camera camera;
    private boolean turningOn = false;

    private CameraFlashLight(Context context) {
        this.context = context;
    }

    public static CameraFlashLight getInstance(Context context) {
        if (instance == null)
            instance = new CameraFlashLight(context);
        return instance;
    }

    @Override
    public boolean isAvailable() {
        return isSystemSupport() && hasFlash();
    }

    private boolean isSystemSupport() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private boolean hasFlash() {
        Camera camera = getCamera();
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getFlashMode() == null) {
            return false;
        }
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        return !(supportedFlashModes == null || supportedFlashModes.isEmpty() || hasOnlyFlashOffMode(supportedFlashModes));
    }

    private Camera getCamera() {
        if (camera == null)
            camera = Camera.open();
        return camera;
    }

    private boolean hasOnlyFlashOffMode(List<String> supportedFlashModes) {
        return supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF);
    }

    public boolean isTurningOn() {
        return turningOn;
    }

    @Override
    public void turnOn() {
        Camera camera = getCamera();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
        turningOn = true;
    }

    @Override
    public void turnOff() {
        Camera camera = getCamera();
        camera.stopPreview();
        camera.release();
        this.camera = null;
        turningOn = false;
    }
}
