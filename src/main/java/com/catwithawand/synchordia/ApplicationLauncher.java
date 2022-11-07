package com.catwithawand.synchordia;

import javafx.application.Application;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApplicationLauncher {

  public static void main(String[] args) {
    log.info("Starting application");
    Application.launch(SynchordiaApplication.class, args);
  }
}
