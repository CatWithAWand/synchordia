package com.catwithawand.synchordia.control;

import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class CustomSlider extends Slider {

  private static final String DEFAULT_STYLE_CLASS = "custom-slider";
  private String progressColor = "-slider-progress";

  private Node track;

  public CustomSlider() {
    super(0, 100, 0);
    getStyleClass().add(DEFAULT_STYLE_CLASS);

    valueProperty().addListener((observable, oldValue, newValue) -> updateStyle(newValue.doubleValue()));

    addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
      progressColor = "-theme-accent";
      lookup(".thumb").setOpacity(1);
      updateStyle(getValue());
    });

    addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
      if (event.isPrimaryButtonDown()) {
        return;
      }

      progressColor = "-slider-progress";
      lookup(".thumb").setOpacity(0);
      updateStyle(getValue());
    });

    addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      progressColor = "-theme-accent";
      lookup(".thumb").setOpacity(1);
      updateStyle(getValue());
    });

    addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
      progressColor = "-slider-progress";
      lookup(".thumb").setOpacity(0);
      updateStyle(getValue());
    });
  }

  public Node getTrack() {
    return track == null ? lookup(".track") : track;
  }

  private void updateStyle(double value) {
    String style = String.format("-fx-background-color: linear-gradient(to right, %s %f%%, "
                                     + "-slider-track "
                                     + "%f%%); -fx-max-height: 0.2em;",
                                 progressColor,
                                 value,
                                 value
    );

    getTrack().setStyle(style);
  }

}

