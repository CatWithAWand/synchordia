package com.catwithawand.synchordia.controller;

import com.catwithawand.synchordia.event.StageReadyEvent;
import com.catwithawand.synchordia.theming.Theme;
import com.sun.javafx.scene.control.CustomColorDialog;
import com.sun.javafx.tk.Toolkit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.ToggleSwitch;
import org.scenicview.ScenicView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Component
public class SettingsController {

  private Stage stage;

  @FXML
  private Button colorPicker;

  @FXML
  private ToggleSwitch metadataToggleSwitch;

  private Optional<Color> pickedColor = Optional.ofNullable(Theme.themeAccent.getColor());

  @FXML
  private GridPane rootSettings;

  @Autowired
  public SettingsController() {
  }

  @FXML
  private void initialize() {
    log.debug("Initializing SettingsController");


    metadataToggleSwitch.setDisable(true);

    metadataToggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
      log.debug("metadata switch selected: " + newValue.booleanValue());
    });
  }

  @FXML
  private void handleAccentPickerBtnClick(MouseEvent event) {
    Optional<Color> prevColor = pickedColor;
    pickedColor = showColorDialog(stage, "Color Picker", prevColor);

    if (pickedColor.equals(prevColor)) {
      return;
    }

    pickedColor.ifPresent(color -> {
      log.debug("Accent color changed to " + color);
//      Session session = hibernateUtil.getSessionFactory().openSession();
//      session.beginTransaction();
//      Preference preference = session.get(Preference.class, "accent_color");
//      preference.setValue(color.toString());
//      session.persist(preference);
//      session.getTransaction().commit();
//      session.close();
    });
  }

  public static Optional<Color> showColorDialog(Window owner, String title,
      Optional<Color> initialColor) {
    AtomicReference<Color> selectedColor = new AtomicReference<>();

    // Create custom-color-dialog.
    CustomColorDialog customColorDialog = new CustomColorDialog(owner);
    Stage dialogStage = customColorDialog.getDialog();

    dialogStage.initStyle(StageStyle.TRANSPARENT);
    dialogStage.getScene().setFill(Color.TRANSPARENT);

    initialColor.ifPresent(customColorDialog::setCurrentColor);

    customColorDialog.setShowUseBtn(false);
    customColorDialog.setShowOpacitySlider(false);
    customColorDialog.setSaveBtnToOk();

    customColorDialog.setOnSave(() -> selectedColor.set(customColorDialog.getCustomColor()));
    customColorDialog.setOnCancel(() -> selectedColor.set(customColorDialog.getCurrentColor()));

    // Exit the nested-event-loop when the dialog is hidden.
    customColorDialog.setOnHidden(event -> {
      Toolkit.getToolkit().exitNestedEventLoop(dialogStage, null);
    });

    dialogStage.setTitle(title);
    customColorDialog.show();
    ScenicView.show(dialogStage.getScene());

    dialogStage.toFront();
    dialogStage.requestFocus();
    dialogStage.centerOnScreen();

    // Enter nested-event-loop to simulate a showAndWait(). This will
    // basically cause the dialog to block input from the rest of the
    // window until the dialog is closed.
    Toolkit.getToolkit().enterNestedEventLoop(dialogStage);

    return Optional.ofNullable(selectedColor.get());
  }

  @EventListener
  public void handleStageReady(StageReadyEvent event) {
    stage = event.getStage();
  }

}
