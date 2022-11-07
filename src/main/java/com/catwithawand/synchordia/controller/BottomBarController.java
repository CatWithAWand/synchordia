package com.catwithawand.synchordia.controller;

import com.catwithawand.synchordia.SynchordiaApplication;
import com.catwithawand.synchordia.control.CustomSlider;
import com.catwithawand.synchordia.control.PlayPauseButton;
import com.catwithawand.synchordia.database.entity.Track;
import com.catwithawand.synchordia.database.service.TrackService;
import com.catwithawand.synchordia.player.MusicPlayer;
import com.catwithawand.synchordia.util.FXUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer.Status;
import lombok.extern.log4j.Log4j2;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class BottomBarController {

  private ConfigurableApplicationContext applicationContext;

  private final TrackService trackService;

  private final MusicPlayer musicPlayer;

  private double lastVolumeValue;

  @FXML
  private ImageView trackImageView;

  @FXML
  private Label trackTitleLabel;

  @FXML
  private Label trackArtistLabel;

  @FXML
  private VBox musicControlsVbox;

  @FXML
  private PlayPauseButton playPauseBtn;

  @FXML
  private FontIcon re;

  @FXML
  private Label timeElapsedLabel;

  @FXML
  private CustomSlider progressSlider;

  @FXML
  private Label totalDurationLabel;

  @FXML
  private HBox musicExtraControlsHBox;

  @FXML
  private FontIcon volumeIcon;

  @FXML
  private CustomSlider volumeSlider;

  @Autowired
  public BottomBarController(ConfigurableApplicationContext applicationContext,
      MusicPlayer musicPlayer, TrackService trackService) {
    this.applicationContext = applicationContext;
    this.musicPlayer = musicPlayer;
    this.trackService = trackService;
  }

  public void setVolume(double volume) {
    volumeSlider.setValue(volume);
  }

  @FXML
  private void initialize() throws IOException {
    log.debug("Initializing BottomBarController");

    FXUtils.consumeNonPrimaryClicks(musicControlsVbox);
    FXUtils.consumeNonPrimaryClicks(musicExtraControlsHBox);

    Image placeholderImg = new Image(SynchordiaApplication.class.getResource(
        "images/placeholder.png").openStream());

    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      updateVolumeIcon(newValue.intValue());
    });

    System.out.println(musicPlayer.getImageViews());
    musicPlayer.addImageView(trackImageView);

    trackTitleLabel.textProperty().bind(musicPlayer.trackTitleProperty());
    trackArtistLabel.textProperty().bind(musicPlayer.trackArtistProperty());

    timeElapsedLabel.textProperty()
                    .bind(Bindings.createStringBinding(
                        () -> formatTime(musicPlayer.timeElapsedProperty()
                                                    .get()
                                                    .toSeconds()),
                        musicPlayer.timeElapsedProperty()
                    ));

    musicPlayer.progressProperty().bindBidirectional(progressSlider.valueProperty());

    progressSlider.setOnMousePressed(event -> {
      musicPlayer.setSeeking(true);
    });

    progressSlider.setOnMouseReleased(event -> {
      musicPlayer.seekFromPercentage(progressSlider.getValue());
      musicPlayer.setSeeking(false);
    });

    totalDurationLabel.textProperty()
                      .bind(Bindings.createStringBinding(
                          () -> formatTime(musicPlayer.totalDurationProperty()
                                                      .get()
                                                      .toSeconds()),
                          musicPlayer.totalDurationProperty()
                      ));

    musicPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());


    playPauseBtn.playingProperty().bind(musicPlayer.statusProperty()
                                                   .isEqualTo(Status.PLAYING));
  }

  private String formatTime(double seconds) {
    String time = String.format(
        "%02d:%02d",
        (int) (seconds % 3600) / 60,
        (int) (seconds % 60)
    );

    return time;
  }

  @FXML
  private void handlePlayPauseBtnClick() {
    if (musicPlayer.getStatus() == Status.PLAYING) {
      musicPlayer.pause();
    } else if (musicPlayer.getStatus().equals(Status.PAUSED)) {
      musicPlayer.resume();
    } else {
      Track track = trackService.getByTitleContains("Calm Down").get(0);
      musicPlayer.play(track);
    }
  }

  @FXML
  private void handleVolumeBtnClick() {
    if (volumeSlider.getValue() == 0) {
      volumeSlider.setValue(lastVolumeValue);
    } else {
      lastVolumeValue = volumeSlider.getValue();
      volumeSlider.setValue(0);
    }
  }

  @FXML
  private void handleRepeatBtnClick() {
//    musicPlayer.setRepeat(!musicPlayer.isRepeat());
  }

  private void updateVolumeIcon(int value) {
    if (value >= 50) {
      volumeIcon.setIconLiteral("fltrmz-speaker-16");
    } else if (value > 0) {
      volumeIcon.setIconLiteral("fltrmz-speaker-1-16");
    } else {
      volumeIcon.setIconLiteral("fltrmz-speaker-none-16");
    }
  }

}
