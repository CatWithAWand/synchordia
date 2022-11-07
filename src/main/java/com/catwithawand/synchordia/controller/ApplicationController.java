package com.catwithawand.synchordia.controller;


import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.catwithawand.synchordia.SynchordiaApplication;
import com.catwithawand.synchordia.control.SimpleActionButton;
import com.catwithawand.synchordia.event.HomeNavigationEvent;
import com.catwithawand.synchordia.event.NavigationEvent;
import com.catwithawand.synchordia.event.SceneReadyEvent;
import com.catwithawand.synchordia.event.SettingsNavigationEvent;
import com.catwithawand.synchordia.event.StageReadyEvent;
import com.catwithawand.synchordia.layout.BootstrapPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.MigPane;

import java.io.IOException;

@Log4j2
@Getter
@Component
public class ApplicationController {

  @Getter(AccessLevel.NONE)
  private ConfigurableApplicationContext applicationContext;

  private Stage stage;
  private BorderlessScene scene;
  private TopBarController topBarController;
  private SideBarController sideBarController;
  private BottomBarController bottomBarController;
  private SettingsController settingsController;
  private View1Controller view1Controller;
  private BorderPane topBarRoot;
  private GridPane sideBarRoot;
  private MigPane bottomBarRoot;
  private GridPane settingsRoot;
  private BootstrapPane view1Root;
  private SimpleActionButton currFocused;

  @FXML
  private BorderPane appBorderPane;

  @FXML
  private MigPane appCenterMigPane;

  @FXML
  private ScrollPane mainContentScrollPane;

  @Autowired
  public ApplicationController(ConfigurableApplicationContext applicationContext,
      TopBarController topBarController, SideBarController sideBarController,
      BottomBarController bottomBarController, SettingsController settingsController,
      View1Controller view1Controller) {
    this.applicationContext = applicationContext;
    this.topBarController = topBarController;
    this.sideBarController = sideBarController;
    this.bottomBarController = bottomBarController;
    this.settingsController = settingsController;
    this.view1Controller = view1Controller;
  }

  @FXML
  protected void initialize() throws IOException {
    log.debug("Initializing ApplicationController");

    topBarRoot = setupController("fxml/TopBar.fxml", topBarController);
    sideBarRoot = setupController("fxml/SideBar.fxml", sideBarController);
    bottomBarRoot = setupController("fxml/BottomBar.fxml", bottomBarController);
    settingsRoot = setupController("fxml/Settings.fxml", settingsController);
    view1Root = setupController("fxml/View1.fxml", view1Controller);

    Rectangle clip = new Rectangle(
        mainContentScrollPane.getWidth() - 11,
        mainContentScrollPane.getHeight()
    );

    clip.widthProperty().bind(mainContentScrollPane.widthProperty().subtract(11));
    clip.heightProperty().bind(mainContentScrollPane.heightProperty());

    clip.setArcWidth(16);
    clip.setArcHeight(16);

    mainContentScrollPane.setClip(clip);
    mainContentScrollPane.setFitToWidth(true);
    mainContentScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    mainContentScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

    appBorderPane.setTop(topBarRoot);
    appCenterMigPane.add(0, sideBarRoot);
    appBorderPane.setBottom(bottomBarRoot);
  }

  @EventListener
  public void handleStageReady(StageReadyEvent event) {
    stage = event.getStage();
  }

  @EventListener
  public void handleSceneReady(SceneReadyEvent event) {
    scene = event.getScene();
    scene.setMoveControl(topBarRoot);
  }

  @EventListener
  public void handleNavigationEvent(NavigationEvent event) {
    mainContentScrollPane.setVvalue(0);
    setCurrFocusedNavBtn((SimpleActionButton) event.getSource());
  }

  @EventListener
  public void handleSettingsNavigation(SettingsNavigationEvent event) {
    mainContentScrollPane.setContent(settingsRoot);
  }

  @EventListener
  public void handleHomeNavigation(HomeNavigationEvent event) {
    mainContentScrollPane.setContent(view1Root);
  }

  private <T extends Node> T setupController(String fxmlPath, Object controller)
      throws IOException {
    try {
      FXMLLoader fxmlLoader =
          new FXMLLoader(SynchordiaApplication.class.getResource(fxmlPath));
      fxmlLoader.setController(controller);
      return fxmlLoader.load();
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException(e);
    }
  }

  private void setCurrFocusedNavBtn(SimpleActionButton btn) {
    if (currFocused != null && currFocused.equals(btn)) {
      return;
    }

    if (currFocused != null) {
      currFocused.setFocus(false);
    }

    currFocused = btn;
    currFocused.setFocus(true);
  }
}