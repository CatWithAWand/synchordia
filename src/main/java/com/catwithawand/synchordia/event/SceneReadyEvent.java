package com.catwithawand.synchordia.event;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;

public class SceneReadyEvent {

  private BorderlessScene scene;

  public SceneReadyEvent(BorderlessScene scene) {
    this.scene = scene;
  }

  public BorderlessScene getScene() {
    return scene;
  }

}
