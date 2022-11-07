package com.catwithawand.synchordia;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.catwithawand.synchordia.controller.ApplicationController;
import com.catwithawand.synchordia.event.SceneReadyEvent;
import com.catwithawand.synchordia.event.StageReadyEvent;
import com.catwithawand.synchordia.theming.Theme;
import com.google.common.primitives.Ints;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.log4j.Log4j2;
import org.scenicview.ScenicView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;

@Log4j2
@Component
public class SynchordiaInitializer {

  @Value("classpath:com/catwithawand/synchordia/fxml/Application.fxml")
  private Resource applicationView;

  private ConfigurableApplicationContext applicationContext;

  private ApplicationController applicationController;

  @Autowired
  public SynchordiaInitializer(ConfigurableApplicationContext applicationContext,
      ApplicationController applicationController) {
    this.applicationContext = applicationContext;
    this.applicationController = applicationController;
  }

  @EventListener
  public void handleStageReady(StageReadyEvent event) {
    try {
      Stage stage = event.getStage();

      FXMLLoader fxmlLoader = new FXMLLoader(applicationView.getURL());
      fxmlLoader.setController(applicationController);
      Parent root = fxmlLoader.load();

      BorderlessScene scene = new BorderlessScene(
          stage,
          StageStyle.TRANSPARENT,
          root,
          Color.TRANSPARENT
      );
      scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
      scene.getStylesheets()
          .add(getClass().getResource("css/scrollbar.css").toExternalForm());
      scene.getStylesheets()
          .add(getClass().getResource("css/colorpicker.css").toExternalForm());
      // Apply theming styles
      scene.getStylesheets().add(Theme.getCssDataURI());

      applicationContext.publishEvent(new SceneReadyEvent(scene));

      Image icon = new Image(getClass().getResource("images/icon.png").openStream());
      stage.getIcons().add(icon);

      stage.setTitle("Synchordia");
      stage.setScene(scene);
      stage.setMinWidth(810);
      stage.setMinHeight(600);

      Dimension initialDimensions = getInitialDimensions();
      stage.setWidth(initialDimensions.getWidth());
      stage.setHeight(initialDimensions.getHeight());

      stage.show();
      ScenicView.show(scene);

      Platform.runLater(() -> {
        root.requestFocus();
        stage.toFront();
      });
    } catch (IOException e) {
      log.error("Failed to initialize app", e);
      Platform.exit();
    }
  }

  private Dimension getInitialDimensions() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    int initialWidth = Ints.constrainToRange(
        (int) (screenSize.getWidth() * 0.78125),
        810,
        3840
    );
    int initialHeight = Ints.constrainToRange(
        (int) (screenSize.getHeight() * 0.83333),
        600,
        2160
    );

    return new Dimension(initialWidth, initialHeight);
  }
}
