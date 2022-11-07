package com.catwithawand.synchordia;

import com.catwithawand.synchordia.event.ShutdownEvent;
import com.catwithawand.synchordia.event.StageReadyEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@Log4j2
@SpringBootApplication
@EnableAsync
public class SynchordiaApplication extends Application {

  private ConfigurableApplicationContext applicationContext;

  @Override
  public void init() {
    log.info("Creating SpringApplication instance and application context");
    applicationContext = new SpringApplicationBuilder(SynchordiaApplication.class).web(
        WebApplicationType.NONE).headless(false).bannerMode(Banner.Mode.OFF).run();
  }

  @Override
  public void start(Stage stage) {
    applicationContext.publishEvent(new StageReadyEvent(stage));
  }


  @Override
  public void stop() {
    log.debug("Application exit requested");
    applicationContext.publishEvent(new ShutdownEvent());
    applicationContext.close();
    log.info("Exiting application");
  }

}