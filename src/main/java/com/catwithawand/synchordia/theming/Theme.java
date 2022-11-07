package com.catwithawand.synchordia.theming;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Theme {

  public static final CustomColor brandNormal = new CustomColor(
      163,
      166,
      170,
      1,
      "-brand-normal"
  );
  public static final CustomColor backgroundPrimary = new CustomColor(
      54,
      57,
      63,
      1,
      "-background-primary"
  );

  // BACKGROUND
  public static final CustomColor backgroundSecondary = new CustomColor(
      32,
      34,
      37,
      1,
      "-background"
          + "-secondary"
  );
  public static final CustomColor backgroundTertiary = new CustomColor(
      24,
      24,
      24,
      1,
      "-background-tertiary"
  );
  public static final CustomColor backgroundQuaternary = new CustomColor(
      18,
      18,
      18,
      1,
      "-background"
          + "-quaternary"
  );
  public static final CustomColor backgroundAccent = new CustomColor(
      79,
      84,
      92,
      1,
      "-background-accent"
  );
  public static final CustomColor backgroundGradientEnd = new CustomColor(
      18,
      18,
      18,
      1,
      "-background"
          + "-gradient"
          + "-end"
  );
  public static final CustomColor backgroundModifierHover = new CustomColor(
      64,
      68,
      75,
      0.4,
      "-background"
          + "-modifier"
          + "-hover"
  );
  public static final CustomColor backgroundModifierActive = new CustomColor(
      64,
      68,
      75,
      0.48,
      "-background"
          + "-modifier"
          + "-active"
  );
  public static final CustomColor textNormal = new CustomColor(
      255,
      255,
      255,
      1,
      "-text-normal"
  );

  // TEXT
  public static final CustomColor textNormalAlt = new CustomColor(
      220,
      221,
      222,
      1,
      "-text-normal-alt"
  );
  public static final CustomColor textMuted = new CustomColor(
      163,
      166,
      170,
      1,
      "-text-muted"
  );
  public static final CustomColor interactiveNormal = new CustomColor(
      185,
      187,
      190,
      1,
      "-interactive-normal"
  );

  // INTERACTIVE CONTROLS
  public static final CustomColor interactiveHover = new CustomColor(
      220,
      221,
      222,
      1,
      "-interactive-hover"
  );
  public static final CustomColor interactiveActive = new CustomColor(
      255,
      255,
      255,
      1,
      "-interactive-active"
  );
  public static final CustomColor interactiveMuted = new CustomColor(
      79,
      84,
      92,
      1,
      "-interactive-muted"
  );
  public static final CustomColor navigationButtonHover = new CustomColor(
      255,
      255,
      255,
      0.04,
      "-navigation-button-hover"
  );

  // SIDEBAR
  public static final CustomColor navigationButtonActive = new CustomColor(
      49,
      51,
      54,
      1,
      "-navigation-button"
          + "-active"
  );
  public static final CustomColor playbackButtonNormal = new CustomColor(
      185,
      187,
      190,
      1,
      "-playback-button-normal"
  );

  // PLAYBACK BUTTONS
  public static final CustomColor playbackButtonHover = new CustomColor(
      255,
      255,
      255,
      1,
      "-playback-button-hover"
  );
  public static final CustomColor sliderTrack = new CustomColor(
      94,
      94,
      94,
      1,
      "-slider-track"
  );

  // SLIDER TRACK
  public static final CustomColor sliderProgress = new CustomColor(
      255,
      255,
      255,
      1,
      "-slider-progress"
  );
  public static final CustomColor sliderThumb = new CustomColor(
      255,
      255,
      255,
      1,
      "-slider-thumb"
  );
  public static final CustomColor windowControlNormal = new CustomColor(
      0,
      0,
      0,
      0.5,
      "-window-control-normal"
  );

  // WINDOW CONTROLS
  public static final CustomColor windowControlHover = new CustomColor(
      255,
      255,
      255,
      0.4,
      "-window-control-hover"
  );
  public static final CustomColor windowControlIcon = new CustomColor(
      255,
      255,
      255,
      1,
      "-window-control-icon"
  );
  public static final CustomColor windowControlCloseButton = new CustomColor(
      205,
      26,
      43,
      1,
      "-window-control-close-button"
  );
  public static final CustomColor textPositive = new CustomColor(
      70,
      196,
      110,
      1,
      "-text-positive"
  );

  // ACTIONABLE TEXT
  public static final CustomColor textWarning = new CustomColor(
      250,
      168,
      26,
      1,
      "-text-warning"
  );
  public static final CustomColor textDanger = new CustomColor(
      243,
      134,
      136,
      1,
      "-text-danger"
  );
  public static final CustomColor buttonDangerBackground = new CustomColor(
      216,
      60,
      62,
      1,
      "-button-danger-background"
  );
  public static final CustomColor buttonDangerBackgroundHover = new CustomColor(
      161,
      45,
      47,
      1,
      "-button-dange-background-hover"
  );
  public static final CustomColor buttonDangerBackgroundActive = new CustomColor(
      140,
      39,
      41,
      1,
      "-button-danger-background-active"
  );
  public static final CustomColor buttonDangerBackgroundDisabled = new CustomColor(
      216,
      60,
      62,
      1,
      "-button-danger-background-disabled"
  );
  public static final CustomColor buttonPositiveBackground = new CustomColor(
      45,
      125,
      70,
      1,
      "-button-positive-background"
  );
  public static final CustomColor buttonPositiveBackgroundHover = new CustomColor(
      33,
      91,
      50,
      1,
      "-button-positive-background-hover"
  );
  public static final CustomColor buttonPositiveBackgroundActive = new CustomColor(
      30,
      83,
      46,
      1,
      "-button-positive-background-active"
  );
  public static final CustomColor buttonPositiveBackgroundDisabled = new CustomColor(
      45,
      125,
      70,
      1,
      "-button-positive-background-disabled"
  );
  public static final CustomColor switchBackgroundNormal = new CustomColor(
      83,
      83,
      83,
      1,
      "-switch-background-normal"
  );

  // SWITCH
  public static final CustomColor switchBackgroundHover = new CustomColor(
      179,
      179,
      179,
      1,
      "-switch-background-hover"
  );
  public static final CustomColor switchThumb = new CustomColor(
      255,
      255,
      255,
      1,
      "-switch-thumb"
  );
  public static final CustomColor cardBackground = new CustomColor(
      24,
      24,
      24,
      1,
      "-card-background"
  );

  // CARDS
  public static final CustomColor cardBackgroundHover = new CustomColor(
      40,
      40,
      40,
      1,
      "-card-background-hover"
  );
  public static final CustomColor scrollbarThinThumb = new CustomColor(
      255,
      255,
      255,
      0.3,
      "-scrollbar-thin-thumb"
  );

  // SCROLLBAR
  public static final CustomColor scrollbarThinThumbHover = new CustomColor(
      255,
      255,
      255,
      0.5,
      "-scrollbar-thin-thumb-hover"
  );
  public static final CustomColor scrollbarThinThumbPressed = new CustomColor(
      255,
      255,
      255,
      0.7,
      "-scrollbar-thin-thumb-pressed"
  );
  public static final CustomColor scrollbarThinTrack = new CustomColor(
      46,
      51,
      56,
      1,
      "-scrollbar-thin-track"
  );
  public static final CustomColor divider = new CustomColor(40, 40, 40, 1, "-divider");

  // OTHER
  public static CustomColor themeAccent = new CustomColor(
      30,
      215,
      96,
      1,
      "-theme-accent"
  );

  public static void setThemeAccent(String colorString) {
    themeAccent = new CustomColor(colorString, 1.0, "-theme-accent");
  }

  public static String getCssDataURI() {
    String cssStr = "*{";

    Field[] fields = Theme.class.getFields();
    for (Field field : fields) {
      try {
        CustomColor customColor = (CustomColor) field.get(Theme.class);
        cssStr += customColor.getCss();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    cssStr += "}\n";

    return "data:text/css;base64," + Base64.getEncoder()
        .encodeToString(cssStr.getBytes(StandardCharsets.UTF_8));
  }

}
