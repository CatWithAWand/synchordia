package com.catwithawand.synchordia.task;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Preference;
import com.catwithawand.synchordia.database.service.AlbumService;
import com.catwithawand.synchordia.database.service.ArtistService;
import com.catwithawand.synchordia.database.service.PreferenceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Log4j2
@Component
@Scope("prototype")
public class VerifyDatabaseTask implements Runnable {

  private static final HashSet<String> REQUIRED_PREFERENCES = new HashSet<>(Arrays.asList(
      "music_libraries",
      "accent_color",
      "volume",
      "last_played_track"
  ));

  private ArtistService artistService;
  private AlbumService albumService;
  private PreferenceService preferenceService;

  @Autowired
  public VerifyDatabaseTask(ArtistService artistService, AlbumService albumService,
      PreferenceService preferenceService) {
    this.artistService = artistService;
    this.albumService = albumService;
    this.preferenceService = preferenceService;
  }

  @Override
  public void run() {
    log.info("Verifying database");

    // Verify all preferences are present, if not add them
    for (String preference : REQUIRED_PREFERENCES) {
      if (!preferenceService.exists(preference)) {
        log.debug("Preference \"{}\" does not exist, creating it", preference);
        Preference newPreference = new Preference();
        newPreference.setKey(preference);
        newPreference.setValue("");
        preferenceService.savePreferenceAndFlush(newPreference);
      }

      // Verify Unknown Artist and Unknown Album are present
      if (!artistService.existsByName("Unknown Artist")) {
        log.debug("Unknown Artist does not exist, creating it");
        Artist unknownArtist = new Artist();
        unknownArtist.setName("Unknown Artist");
        artistService.saveArtistAndFlush(unknownArtist);
      }

      if (!albumService.existsByName("Unknown Album")) {
        log.debug("Unknown Album does not exist, creating it");
        Album unknownAlbum = new Album();
        unknownAlbum.setName("Unknown Album");
        albumService.saveAlbumAndFlush(unknownAlbum);
      }

      // Verify music libraries and database entries
    }

  }

}
