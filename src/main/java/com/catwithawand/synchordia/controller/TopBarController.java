package com.catwithawand.synchordia.controller;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.catwithawand.synchordia.control.SimpleActionButton;
import com.catwithawand.synchordia.event.SceneReadyEvent;
import com.catwithawand.synchordia.event.SettingsNavigationEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class TopBarController {

  @FXML
  BorderPane topBarRoot;

  private ConfigurableApplicationContext applicationContext;

  @Setter
  private BorderlessScene scene;

  @FXML
  private FontIcon maximizeIcon;

  @FXML
  private SimpleActionButton settingsBtn;


  public TopBarController(ConfigurableApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @FXML
  private void initialize() {
    log.debug("Initializing TopBarController");

    topBarRoot.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
      if (!event.isPrimaryButtonDown() || !event.getTarget().equals(topBarRoot)) {
        event.consume();
      }
    });
  }

  @FXML
  private void handleSettingsBtnClick(MouseEvent event) {
    this.applicationContext.publishEvent(new SettingsNavigationEvent(this.settingsBtn));
  }


  @FXML
  private void handleMinimizeBtnClick(MouseEvent event) {
    if (!event.getButton().equals(MouseButton.PRIMARY)) {
      return;
    }

    scene.setMinimized(true);
  }

  @FXML
  private void handleMaximizeBtnClick(MouseEvent event) {
    if (!event.getButton().equals(MouseButton.PRIMARY)) {
      return;
    }

    scene.maximizeStage();
  }

  @FXML
  private void handleCloseBtnClick(MouseEvent event) {
    if (!event.getButton().equals(MouseButton.PRIMARY)) {
      return;
    }

    Platform.exit();
  }

  @EventListener
  public void handleSceneReady(SceneReadyEvent event) {
    scene = event.getScene();
    scene.setMoveControl(topBarRoot);

    scene.maximizedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        maximizeIcon.setIconLiteral("fltrmz-restore-16");
        scene.getRoot().getStyleClass().removeIf(s -> s.equals("rounded-lg"));
      } else {
        maximizeIcon.setIconLiteral("fltrmz-maximize-16");
        scene.getRoot().getStyleClass().add("rounded-lg");
      }
    });
  }

}
