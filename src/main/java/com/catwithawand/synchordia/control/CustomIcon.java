package com.catwithawand.synchordia.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import org.kordamp.ikonli.javafx.FontIcon;

public class CustomIcon extends FontIcon {

  private StringProperty iconLiteral;

  public CustomIcon() {
    super();
  }

  public final StringProperty iconLiteralProperty() {
    if (iconLiteral == null) {
      iconLiteral = new SimpleStringProperty(this, "iconLiteral", "");
    }

    return iconLiteral;
  }

  @Override
  public final String getIconLiteral() {
    return iconLiteral == null ? "" : iconLiteral.get();
  }

  @Override
  public final void setIconLiteral(String iconCode) {
    iconLiteralProperty().set(iconCode);
    String[] parts = iconCode.split(":");
    setIconCode(org.kordamp.ikonli.javafx.IkonResolver.getInstance()
                         .resolve(parts[0])
                         .resolve(parts[0]));
    resolveSize(iconCode, parts);
    resolvePaint(iconCode, parts);
  }

  private void resolveSize(String iconCode, String[] parts) {
    if (parts.length > 1) {
      try {
        setIconSize(Integer.parseInt(parts[1]));
      } catch (NumberFormatException e) {
        throw invalidDescription(iconCode, e);
      }
    }
  }

  private void resolvePaint(String iconCode, String[] parts) {
    if (parts.length > 2) {
      Paint paint = resolvePaintValue(iconCode, parts[2]);
      if (paint != null) {
        setIconColor(paint);
      }
    }
  }

  private static Paint resolvePaintValue(String iconCode, String value) {
    try {
      return Color.valueOf(value);
    } catch (IllegalArgumentException e1) {
      try {
        return LinearGradient.valueOf(value);
      } catch (IllegalArgumentException e2) {
        try {
          return RadialGradient.valueOf(value);
        } catch (IllegalArgumentException e3) {
          throw invalidDescription(iconCode, e3);
        }
      }
    }
  }

}
