package com.catwithawand.synchordia.task;

import com.catwithawand.synchordia.controller.BottomBarController;
import com.catwithawand.synchordia.database.entity.Preference;
import com.catwithawand.synchordia.database.service.PreferenceService;
import com.catwithawand.synchordia.theming.Theme;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
@Scope("prototype")
public class PreferencesLoadTask implements Runnable {

  private static final List<String> PREFERENCES_TO_LOAD = Arrays.asList(
      "accent_color",
      "volume",
      "last_played_track"
  );

  private PreferenceService preferenceService;
  private BottomBarController bottomBarController;

  @Autowired
  public PreferencesLoadTask(PreferenceService preferenceService,
      BottomBarController bottomBarController) {
    this.preferenceService = preferenceService;
    this.bottomBarController = bottomBarController;
  }

  @Override
  public void run() {
    log.info("Loading preferences");

    List<Preference> preferences = preferenceService.getPreferences(PREFERENCES_TO_LOAD);

    for (Preference preference : preferences) {
      String key = preference.getKey();
      String value = preference.getValue();

      log.debug("Loading preference \"{}\" with value \"{}\"", key, value);

      switch (key) {
        case "accent_color":
          Theme.setThemeAccent(value);
          break;
        case "volume":
          bottomBarController.setVolume(Double.parseDouble(value));
          break;
        case "last_played_track":
//          bottomBarController.setLastPlayedTrack(value);
          break;
      }
    }

  }

}
