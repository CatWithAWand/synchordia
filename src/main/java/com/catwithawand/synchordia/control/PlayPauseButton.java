package com.catwithawand.synchordia.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class PlayPauseButton extends VBox {

  private static final String DEFAULT_STYLE_CLASS = "play-pause-button";
  private static final String DEFAULT_ICON_STYLE_CLASS = "play-pause-button-icon";
  private static final double DEFAULT_SIZE = 56.0;

  @Getter
  private CustomIcon playIcon = new CustomIcon();
  private BooleanProperty playing;

  public PlayPauseButton() {
    super();
    getStyleClass().add(DEFAULT_STYLE_CLASS);
    playIcon.getStyleClass().add(DEFAULT_ICON_STYLE_CLASS);
    playIcon.setIconLiteral("mdrmz-play_circle_filled");
    setPlaying(false);
    getChildren().add(playIcon);
  }

  public final BooleanProperty playingProperty() {
    if (playing == null) {
      playing = new SimpleBooleanProperty(this, "playing", false) {
        @Override
        protected void invalidated() {
          if (playing.get()) {
            playIcon.setIconLiteral("mdrmz-pause_circle_filled");
          } else {
            playIcon.setIconLiteral("mdrmz-play_circle_filled");
          }
        }
      };
    }

    return playing;
  }

  public final boolean isPlaying() {
    return playing == null ? false : playing.get();
  }

  public final void setPlaying(boolean value) {
    playingProperty().set(value);
  }

}
