package com.catwithawand.synchordia.event;

import javafx.stage.Stage;

public class StageReadyEvent {

  private Stage stage;

  public StageReadyEvent(Stage stage) {
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }

}
