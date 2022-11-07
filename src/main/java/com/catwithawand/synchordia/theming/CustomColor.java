package com.catwithawand.synchordia.theming;

import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class CustomColor {

  private final Color color;
  private final String cssName;
  private final String css;

  public CustomColor(String colorString, double opacity, String cssName) {
    color = Color.web(colorString, opacity);
    this.cssName = cssName;

    int red = (int) Math.round(color.getRed() * 255);
    int green = (int) Math.round(color.getGreen() * 255);
    int blue = (int) Math.round(color.getBlue() * 255);

    css = MessageFormat.format(
        "{0}: rgba({1}, {2}, {3}, {4});",
        cssName,
        red,
        green,
        blue,
        opacity
    );
  }

  public CustomColor(int red, int green, int blue, double opacity, String cssName) {
    color = Color.rgb(red, green, blue, opacity);
    this.cssName = cssName;
    css = MessageFormat.format(
        "{0}: rgba({1}, {2}, {3}, {4});",
        cssName,
        red,
        green,
        blue,
        opacity
    );
  }

  public static CustomColor fromExistingColor(Color color, String cssName) {
    int red = (int) Math.round(color.getRed() * 255);
    int green = (int) Math.round(color.getGreen() * 255);
    int blue = (int) Math.round(color.getBlue() * 255);

    return new CustomColor(red, green, blue, color.getOpacity(), cssName);
  }

  public Paint getPaint() {
    return Paint.valueOf(this.color.toString());
  }

  public BackgroundFill createFill(CornerRadii radii, Insets insets) {
    return new BackgroundFill(this.getPaint(), radii, insets);
  }

}
