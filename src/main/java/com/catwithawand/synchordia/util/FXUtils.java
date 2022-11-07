package com.catwithawand.synchordia.util;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Set;

public class FXUtils {

  private static final Set<EventType<MouseEvent>> DIALLOWED_MOUSE_EVENTS = Set.of(
      MouseEvent.MOUSE_CLICKED,
      MouseEvent.MOUSE_DRAGGED,
      MouseEvent.MOUSE_PRESSED,
      MouseEvent.MOUSE_RELEASED
  );

  public static void consumeNonPrimaryClicks(Node node) {
    node.addEventFilter(MouseEvent.ANY, event -> {
      if (!event.getButton().equals(MouseButton.PRIMARY) && DIALLOWED_MOUSE_EVENTS.contains(
          event.getEventType())) {
        event.consume();
      }
    });
  }

}
