package com.catwithawand.synchordia.controller;

import com.catwithawand.synchordia.control.SimpleActionButton;
import com.catwithawand.synchordia.event.HomeNavigationEvent;
import com.catwithawand.synchordia.theming.Theme;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.textfield.CustomTextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SideBarController {

  private ConfigurableApplicationContext applicationContext;

  private FontIcon searchIcon = new FontIcon();
  @FXML
  private CustomTextField searchBox;

  @FXML
  private SimpleActionButton homeBtn;

  @FXML
  private SimpleActionButton musicLibraryBtn;

  @FXML
  private SimpleActionButton likedSongsBtn;

  @FXML
  private SimpleActionButton createPlaylistBtn;

  private SimpleActionButton currFocused;


  public SideBarController(ConfigurableApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void removeFocus() {
    this.currFocused.setFocus(false);
    this.currFocused = null;
  }

  @FXML
  private void initialize() {
    log.debug("Initializing SideBarController");

    this.searchIcon.setIconLiteral("mdrmz-search");
    this.searchIcon.setIconSize(20);
    this.searchIcon.setIconColor(Theme.textMuted.getColor());

    this.searchBox.setAlignment(Pos.CENTER_LEFT);
    this.searchBox.setPromptText("Search");
    this.searchBox.setRight(searchIcon);

    this.searchBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        searchIcon.setIconColor(Theme.interactiveNormal.getColor());
      } else {
        searchIcon.setIconColor(Theme.textMuted.getColor());
      }
    });
  }


  @FXML
  private void handleHomeBtnClick() {
    this.applicationContext.publishEvent(new HomeNavigationEvent(this.homeBtn));
  }

  @FXML
  private void handleMusicLibraryBtnClick() {
  }

  @FXML
  private void handleLikedSongsBtnClick() {
  }

  @FXML
  private void handleCreatePlaylistBtnClick() {
  }
}
