package com.catwithawand.synchordia.player;

import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Track;
import com.catwithawand.synchordia.database.service.TrackService;
import com.google.common.base.Joiner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;

@Log4j2
@Component
public class MusicPlayer {

  public enum Repeat {
    NONE, ALL, ONE
  }

  @Value("classpath:com/catwithawand/synchordia/images/placeholder.png")
  private static Resource DEFAULT_COVER_IMAGE;
  private final Double DEFAULT_PLAY_COUNT_TIME_LIMIT;
  private final Double DEFAULT_VOLUME;
  @Getter
  private final ObservableList<ImageView> imageViews =
      FXCollections.observableArrayList();
  @Getter
  private final LinkedList<Track> queue = new LinkedList<>();
  @Getter
  private final LinkedList<Track> history = new LinkedList<>();
  private final TrackService trackService;
  private final ThreadPoolTaskExecutor taskExecutor;
  @Getter
  private MediaPlayer mediaPlayer;
  private Track track;
  private StringProperty trackTitle;
  private StringProperty trackArtist;
  private ReadOnlyObjectWrapper<Duration> timeElapsed;
  private ReadOnlyObjectWrapper<Duration> totalDuration;
  private DoubleProperty progress;
  private DoubleProperty volume;
  private BooleanProperty seeking;
  private ObjectProperty<Status> status;
  private ObjectProperty<Repeat> repeat;

  @Autowired
  public MusicPlayer(@Value("${synchordia.player.volume}") double defaultVolume,
      @Value("${synchordia.player.play-count-time-limit}") double playCountTimeLimit,
      TrackService trackService, ThreadPoolTaskExecutor taskExecutor) {
    DEFAULT_VOLUME = defaultVolume;
    DEFAULT_PLAY_COUNT_TIME_LIMIT = playCountTimeLimit;
    this.trackService = trackService;
    this.taskExecutor = taskExecutor;
    setSeeking(false);
  }

  public void load(Track track) {
    this.track = track;

    if (mediaPlayer != null) {
      mediaPlayer.dispose();
    }

    try {
      Media media = new Media(Paths.get(track.getFilePath())
                                   .toUri()
                                   .toString());
      mediaPlayer = new MediaPlayer(media);

      log.debug("track file url: {}", track.getFilePath());

      trackTitle.set(track.getTitle());
      trackArtist.set(Joiner.on(", ")
                            .skipNulls()
                            .join(track.getAllArtists()
                                       .stream()
                                       .map(Artist::getName)
                                       .toArray()));

      totalDurationProperty().bind(mediaPlayer.totalDurationProperty());

      mediaPlayer.currentTimeProperty()
                 .addListener((observable, oldValue, newValue) -> {
                   if (seeking.not().get()) {
                     progressProperty().set(durationToPercentage(mediaPlayer.getCurrentTime()));
                   }
                 });

      mediaPlayer.volumeProperty().bindBidirectional(volumeProperty());
      statusProperty().bind(mediaPlayer.statusProperty());

      displayTrackCover(track.getThumbnailUrl());

      log.info("track play count: {}", track.getPlayCount());

      taskExecutor.submit(this::playCountTrackingTask);

      mediaPlayer.setOnEndOfMedia(() -> {
        history.add(track);

        if (queue.isEmpty()) {
          mediaPlayer.stop();
        } else {
          load(queue.poll());
          mediaPlayer.play();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void queue(Track track) {
    queue.add(track);
  }

  public void changeQueuePosition(Track track, int index) {
    queue.remove(track);
    queue.add(index, track);
  }

  public void playNext() {
    if (!queue.isEmpty()) {
      load(queue.remove());
      mediaPlayer.play();
    }
  }

  public void playPrevious() {
    if (!history.isEmpty()) {
      load(history.remove());
      mediaPlayer.play();
    }
  }

  public void play(Track track) {
    queue.clear();
    queue(track);
    playNext();
  }

  public void pause() {
    if (mediaPlayer != null) {
      mediaPlayer.pause();
    }
  }

  public void resume() {
    if (mediaPlayer != null) {
      mediaPlayer.play();
    }
  }

  public void stop() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
    }
  }

  public void seek(Duration duration) {
    if (mediaPlayer != null) {
      new Thread(() -> mediaPlayer.seek(duration)).start();
    }
  }

  public void seekFromPercentage(double percentage) {
    seek(percentageToDuration(percentage));
  }

  public final Track getTrack() {
    return track;
  }

  public final Media getMedia() {
    return mediaPlayer == null ? null : mediaPlayer.getMedia();
  }

  public final boolean isPlaying() {
    return mediaPlayer != null && mediaPlayer.getStatus()
                                             .equals(Status.PLAYING);
  }

  public final StringProperty trackTitleProperty() {
    if (trackTitle == null) {
      trackTitle = new SimpleStringProperty(this, "trackTitle", "");
    }

    return trackTitle;
  }

  public final StringProperty trackArtistProperty() {
    if (trackArtist == null) {
      trackArtist = new SimpleStringProperty(this, "trackArtist", "");
    }

    return trackArtist;
  }

  public final ReadOnlyObjectWrapper<Duration> timeElapsedProperty() {
    if (timeElapsed == null) {
      timeElapsed = new ReadOnlyObjectWrapper<Duration>(
          this,
          "timeElapsed",
          Duration.ZERO
      );
    }

    return timeElapsed;
  }

  public final ReadOnlyObjectWrapper<Duration> totalDurationProperty() {
    if (totalDuration == null) {
      totalDuration = new ReadOnlyObjectWrapper<Duration>(
          this,
          "totalDuration",
          Duration.ZERO
      );
    }

    return totalDuration;
  }

  public final DoubleProperty progressProperty() {
    if (progress == null) {
      progress = new DoublePropertyBase(0.0) {
        @Override
        protected void invalidated() {
          if (!getStatus().equals(Status.UNKNOWN)
              && !getStatus().equals(Status.DISPOSED)) {
            timeElapsedProperty().set(percentageToDuration(progress.get()));
          }
        }

        @Override
        public Object getBean() {
          return MusicPlayer.this;
        }

        @Override
        public String getName() {
          return "progress";
        }
      };
    }

    return progress;
  }

  public final DoubleProperty volumeProperty() {
    if (volume == null) {
      volume = new DoublePropertyBase(DEFAULT_VOLUME) {
        @Override
        protected void invalidated() {
          if (!getStatus().equals(Status.UNKNOWN)
              && !getStatus().equals(Status.DISPOSED)) {
            // We do not need to clamp the value because the media player
            // does it for us
            mediaPlayer.setVolume(volume.get() / 100.0);
          }
        }

        @Override
        public Object getBean() {
          return MusicPlayer.this;
        }

        @Override
        public String getName() {
          return "volume";
        }
      };
    }

    return volume;
  }

  public final BooleanProperty seekingProperty() {
    if (seeking == null) {
      seeking = new SimpleBooleanProperty(this, "seeking", false);
    }

    return seeking;
  }

  public final ObjectProperty<Status> statusProperty() {
    if (status == null) {
      status = new SimpleObjectProperty<>(this, "status", Status.UNKNOWN);
    }

    return status;
  }

  public final ObjectProperty<Repeat> repeatProperty() {
    if (repeat == null) {
      repeat = new SimpleObjectProperty<>(this, "repeat", Repeat.NONE);
    }

    return repeat;
  }

  public final String getTrackTitle() {
    return trackTitle == null ? "" : trackTitle.get();
  }

  public final void setTrackTitle(String trackTitle) {
    trackTitleProperty().set(trackTitle);
  }

  public final String getTrackArtist() {
    return trackArtist == null ? "" : trackArtist.get();
  }

  public final void setTrackArtist(String trackArtist) {
    trackArtistProperty().set(trackArtist);
  }

  public final Duration getTimeElapsed() {
    return timeElapsed == null ? Duration.UNKNOWN : timeElapsed.get();
  }

  public final Duration getTotalDuration() {
    return totalDuration == null ? Duration.UNKNOWN : totalDuration.get();
  }

  public final double getProgress() {
    return progress == null ? 0.0 : progress.get();
  }

  public final void setProgress(double progress) {
    progressProperty().set(progress);
  }

  public final double getVolume() {
    return volume == null ? DEFAULT_VOLUME : volume.get();
  }

  public final void setVolume(double volume) {
    volumeProperty().set(volume);
  }

  public final boolean isSeeking() {
    return seeking == null ? false : seeking.get();
  }

  public final void setSeeking(boolean seeking) {
    seekingProperty().set(seeking);
  }

  public final Status getStatus() {
    return status == null ? Status.UNKNOWN : status.get();
  }

  public final Repeat getRepeat() {
    return repeat == null ? Repeat.NONE : repeat.get();
  }

  public final void setRepeat(Repeat repeat) {
    repeatProperty().set(repeat);
  }

  public final void addImageView(ImageView imageView) {
    imageViews.add(imageView);
  }

  private void displayTrackCover(String url) throws IOException {
    Image cover = new Image(
        url == null ? DEFAULT_COVER_IMAGE.getURL().toExternalForm() : url);
    imageViews.forEach(imageView -> imageView.setImage(cover));
  }

  private Duration percentageToDuration(double percentage) {
    return totalDuration.get().multiply(percentage / 100);
  }

  private double durationToPercentage(Duration duration) {
    return duration.toSeconds() / totalDuration.get().toSeconds() * 100;
  }

  private Boolean playCountTrackingTask() {
    double seconds = 0;

    while (true) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        log.error("Play count tracking task interrupted");
        return false;
      }

      if (mediaPlayer.getStatus().equals(Status.PLAYING)) {
        seconds += 0.1;
      } else if (!mediaPlayer.getStatus().equals(Status.PAUSED)) {
        return false;
      }

      if (seconds >= 10) {
        trackService.incrementPlayCount(track);
        return true;
      }
    }
  }

}
