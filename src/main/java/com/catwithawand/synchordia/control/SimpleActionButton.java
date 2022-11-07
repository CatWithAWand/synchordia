package com.catwithawand.synchordia.control;

import com.catwithawand.synchordia.geometry.IconPos;
import com.catwithawand.synchordia.geometry.TextPos;
import com.catwithawand.synchordia.theming.Theme;
import de.schlegel11.jfxanimation.JFXAnimationTemplate;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.EnumConverter;
import javafx.css.converter.SizeConverter;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;

public class SimpleActionButton extends BorderPane {

  @Getter
  private final Text textNode = new Text();

  @Getter
  private final CustomIcon leftIcon = new CustomIcon();

  @Getter
  private final CustomIcon rightIcon = new CustomIcon();

  private StringProperty text;
  private StringProperty leftIconLiteral;
  private StringProperty rightIconLiteral;
  private ObjectProperty<TextPos> textPosition;
  private BooleanProperty staticIcons;
  private DoubleProperty nonStaticHoverOpacity;

  @Getter
  private Timeline mouseEnterAnimation;

  @Getter
  private Timeline mouseExitedAnimation;

  @Getter
  private Timeline unfocusedAnimation;

  @Getter
  private Timeline iconShowAnimation;

  @Getter
  private Timeline iconHideAnimation;

  public SimpleActionButton() {
    super();
    getStyleClass().add("simple-action-button");

    setCenter(textNode);
    setTextPosition(getTextPositionInternal());

    textNode.textProperty().bind(textProperty());
    textNode.setFill(Theme.interactiveNormal.getColor());
    textNode.getStyleClass().add("simple-action-button-text");

    leftIcon.iconLiteralProperty().bindBidirectional(leftIconLiteralProperty());
    leftIcon.setIconColor(Theme.interactiveNormal.getColor());
    leftIcon.getStyleClass().add("simple-action-button-left-icon");

    rightIcon.iconLiteralProperty().bindBidirectional(rightIconLiteralProperty());
    rightIcon.setIconColor(Theme.interactiveNormal.getColor());
    rightIcon.getStyleClass().add("simple-action-button-right-icon");

    BorderPane.setAlignment(leftIcon, Pos.CENTER);
    BorderPane.setAlignment(rightIcon, Pos.CENTER);

    buildAnimations();
    addEventHandlers();
  }

  public SimpleActionButton(String text, String iconLiteral, IconPos iconPosition) {
    this();
    setText(text);

    if (iconPosition.equals(IconPos.LEFT)) {
      setLeftIconLiteral(iconLiteral);
    } else {
      setRightIconLiteral(iconLiteral);
    }
  }

  public SimpleActionButton(String text, String iconLiteral, IconPos iconPosition,
      TextPos textPosition) {
    this();
    setText(text);
    setTextPosition(textPosition);

    if (iconPosition.equals(IconPos.LEFT)) {
      setLeftIconLiteral(iconLiteral);
    } else {
      setRightIconLiteral(iconLiteral);
    }
  }

  public SimpleActionButton(String text, String leftIconLiteral, String rightIconLiteral,
      TextPos textPosition, Boolean staticIcons) {
    this();
    setText(text);
    setLeftIconLiteral(leftIconLiteral);
    setRightIconLiteral(rightIconLiteral);
    setTextPosition(textPosition);
    setStaticIcons(staticIcons);
  }

  public final StringProperty leftIconLiteralProperty() {
    if (leftIconLiteral == null) {
      leftIconLiteral = new SimpleStringProperty(this, "leftIconLiteral", "");
    }

    return leftIconLiteral;
  }

  public final StringProperty rightIconLiteralProperty() {
    if (rightIconLiteral == null) {
      rightIconLiteral = new SimpleStringProperty(this, "rightIconLiteral", "");
    }

    return rightIconLiteral;
  }

  public final StringProperty textProperty() {
    if (text == null) {
      text = new SimpleStringProperty(this, "text", "");
    }

    return text;
  }

  public final ObjectProperty<TextPos> textPositionProperty() {
    if (textPosition == null) {
      textPosition = new StyleableObjectProperty<TextPos>() {
        @Override
        public Object getBean() {
          return SimpleActionButton.this;
        }

        @Override
        public String getName() {
          return "textPosition";
        }

        @Override
        public CssMetaData<? extends Styleable, TextPos> getCssMetaData() {
          return StyleableProperties.TEXT_POS;
        }
      };
    }

    return textPosition;
  }

  public final BooleanProperty staticIconsProperty() {
    if (staticIcons == null) {
      staticIcons = new StyleableBooleanProperty() {
        @Override
        public Object getBean() {
          return SimpleActionButton.this;
        }

        @Override
        public String getName() {
          return "staticIcons";
        }

        @Override
        public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
          return StyleableProperties.STATIC_ICONS;
        }
      };
    }

    return staticIcons;
  }

  public final DoubleProperty nonStaticHoverOpacityProperty() {
    if (nonStaticHoverOpacity == null) {
      nonStaticHoverOpacity = new StyleableDoubleProperty() {
        @Override
        public Object getBean() {
          return SimpleActionButton.this;
        }

        @Override
        public String getName() {
          return "nonStaticHoverOpacity";
        }

        @Override
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
          return StyleableProperties.NON_STATIC_HOVER_OPACITY;
        }
      };
    }

    return nonStaticHoverOpacity;
  }

  public final String getText() {
    return text == null ? "" : text.get();
  }

  public final void setText(String text) {
    textProperty().set(text);
  }

  public final String getLeftIconLiteral() {
    return leftIconLiteral == null ? "" : leftIconLiteral.get();
  }

  public final void setLeftIconLiteral(String iconCode) {
    leftIconLiteralProperty().set(iconCode);
    leftIcon.setIconLiteral(iconCode);
    setLeft(leftIcon);
  }

  public final String getRightIconLiteral() {
    return rightIconLiteral == null ? "" : rightIconLiteral.get();
  }

  public final void setRightIconLiteral(String iconCode) {
    rightIconLiteralProperty().set(iconCode);
    rightIcon.setIconLiteral(iconCode);
    setRight(rightIcon);
  }

  public final TextPos getTextPosition() {
    return textPosition == null ? TextPos.LEFT : textPosition.get();
  }

  public final void setTextPosition(TextPos pos) {
    textPositionProperty().set(pos);

    switch (pos) {
      case LEFT:
        BorderPane.setAlignment(textNode, Pos.CENTER_LEFT);
        break;
      case CENTER:
        BorderPane.setAlignment(textNode, Pos.CENTER);
        break;
      case RIGHT:
        BorderPane.setAlignment(textNode, Pos.CENTER_RIGHT);
        break;
    }
  }

  private TextPos getTextPositionInternal() {
    TextPos localPos = getTextPosition();
    return localPos == null ? TextPos.RIGHT : localPos;
  }

  public final boolean isStaticIcons() {
    return staticIcons == null ? true : staticIcons.get();
  }

  public final void setStaticIcons(boolean value) {
    staticIconsProperty().set(value);
    setNonStaticHoverOpacity(value ? 1 : 0.65);

    if (!value) {
      leftIcon.setOpacity(0);
      rightIcon.setOpacity(0);
    }
  }

  public final Double getNonStaticHoverOpacity() {
    return nonStaticHoverOpacity == null ? 0.65 : nonStaticHoverOpacity.get();
  }

  public final void setNonStaticHoverOpacity(Double opacity) {
    nonStaticHoverOpacityProperty().set(opacity);

    // rebuild animations for new opacity
    buildAnimations();
  }

  private void buildAnimations() {
    mouseEnterAnimation = JFXAnimationTemplate.create(Text.class)
        .percent(0)
        .action(b -> b.target(Shape::fillProperty)
            .endValue(Theme.interactiveNormal.getPaint()))
        .percent(100)
        .action(b -> b.target(Shape::fillProperty)
            .endValue(Theme.interactiveHover.getPaint()))
        .config(b -> b.interpolator(Interpolator.LINEAR).duration(Duration.millis(200)))
        .build(b -> b.defaultObject(textNode, leftIcon, rightIcon));

    mouseExitedAnimation = JFXAnimationTemplate.create(Text.class)
        .percent(0)
        .action(b -> b.target(Shape::fillProperty)
            .endValue(Theme.interactiveHover.getPaint()))
        .percent(100)
        .action(b -> b.target(Shape::fillProperty)
            .endValue(Theme.interactiveNormal.getPaint()))
        .config(b -> b.interpolator(Interpolator.LINEAR).duration(Duration.millis(200)))
        .build(b -> b.defaultObject(textNode, leftIcon, rightIcon));

    unfocusedAnimation = JFXAnimationTemplate.create(Text.class)
        .percent(0)
        .action(b -> b.target(Shape::fillProperty)
            .endValue(Theme.interactiveActive.getPaint()))
        .percent(100)
        .action(b -> b.target(Shape::fillProperty)
            .endValue(Theme.interactiveNormal.getPaint()))
        .config(b -> b.interpolator(Interpolator.LINEAR).duration(Duration.millis(200)))
        .build(b -> b.defaultObject(textNode, leftIcon, rightIcon));

    iconShowAnimation = JFXAnimationTemplate.create(CustomIcon.class)
        .percent(0)
        .action(b -> b.target(Node::opacityProperty).endValue(0))
        .percent(100)
        .action(b -> b.target(Node::opacityProperty)
            .endValue(getNonStaticHoverOpacity()))
        .config(b -> b.interpolator(Interpolator.LINEAR).duration(Duration.millis(200)))
        .build(b -> b.defaultObject(leftIcon, rightIcon));

    iconHideAnimation = JFXAnimationTemplate.create(CustomIcon.class)
        .percent(0)
        .action(b -> b.target(Node::opacityProperty)
            .endValue(getNonStaticHoverOpacity()))
        .percent(100)
        .action(b -> b.target(Node::opacityProperty).endValue(0))
        .config(b -> b.interpolator(Interpolator.LINEAR).duration(Duration.millis(200)))
        .build(b -> b.defaultObject(leftIcon, rightIcon));
  }

  private void addEventHandlers() {
    setOnMouseEntered(event -> {
      if (isFocused()) {
        return;
      }

      if (!isStaticIcons()) {
        iconShowAnimation.play();
      }

      mouseEnterAnimation.play();
    });

    setOnMouseExited(event -> {
      if (isFocused()) {
        return;
      }

      if (!isStaticIcons()) {
        iconHideAnimation.play();
      }

      mouseExitedAnimation.play();
    });

    setOnMousePressed(event -> {
      if (!event.getButton().equals(MouseButton.PRIMARY)) {
        return;
      }

      iconShowAnimation.stop();
      mouseEnterAnimation.stop();
      leftIcon.setIconColor(Theme.interactiveActive.getColor());
      rightIcon.setIconColor(Theme.interactiveActive.getColor());
      textNode.setFill(Theme.interactiveActive.getColor());
    });

    setOnMouseReleased(event -> {
      if (!event.getButton().equals(MouseButton.PRIMARY) || isFocused()) {
        return;
      }

      Color color = isHover()
          ? Theme.interactiveHover.getColor()
          : Theme.interactiveNormal.getColor();

      leftIcon.setIconColor(color);
      rightIcon.setIconColor(color);
      textNode.setFill(color);
    });

    focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        leftIcon.setIconColor(Theme.interactiveActive.getColor());
        rightIcon.setIconColor(Theme.interactiveActive.getColor());
        textNode.setFill(Theme.interactiveActive.getColor());
      } else {
        if (!isStaticIcons()) {
          iconHideAnimation.play();
        }

        unfocusedAnimation.play();
      }
    });

    leftIcon.setOnMouseEntered(event -> leftIcon.setOpacity(1));
    leftIcon.setOnMouseExited(event -> leftIcon.setOpacity(getNonStaticHoverOpacity()));
    leftIcon.setOnMousePressed(event -> leftIcon.setOpacity(1));

    rightIcon.setOnMouseEntered(event -> rightIcon.setOpacity(1));
    rightIcon.setOnMouseExited(event -> rightIcon.setOpacity(getNonStaticHoverOpacity()));
    rightIcon.setOnMousePressed(event -> rightIcon.setOpacity(1));
  }

  public void setFocus(boolean value) {
    super.setFocused(value);
  }

  private static class StyleableProperties {

    private static final CssMetaData<SimpleActionButton, Number> NON_STATIC_HOVER_OPACITY =
        new CssMetaData<SimpleActionButton, Number>(
            "-fx-non-static-hover-opacity",
            SizeConverter.getInstance(),
            0.65
        ) {

          @Override
          public boolean isSettable(SimpleActionButton node) {
            return node.nonStaticHoverOpacity == null
                || !node.nonStaticHoverOpacity.isBound();
          }

          @Override
          public StyleableProperty<Number> getStyleableProperty(SimpleActionButton node) {
            return (StyleableProperty<Number>) node.nonStaticHoverOpacityProperty();
          }
        };
    private static final CssMetaData<SimpleActionButton, TextPos> TEXT_POS =
        new CssMetaData<SimpleActionButton, TextPos>(
            "-fx-sap-text-alignment",
            new EnumConverter<TextPos>(
                TextPos.class),
            TextPos.LEFT
        ) {

          @Override
          public boolean isSettable(SimpleActionButton node) {
            return node.textPosition == null || !node.textPosition.isBound();
          }

          @Override
          public StyleableProperty<TextPos> getStyleableProperty(SimpleActionButton node) {
            return (StyleableProperty<TextPos>) node.textPositionProperty();
          }
        };

    private static final CssMetaData<SimpleActionButton, Boolean> STATIC_ICONS =
        new CssMetaData<SimpleActionButton, Boolean>(
            "-fx-static-icons",
            BooleanConverter.getInstance(),
            Boolean.TRUE
        ) {

          @Override
          public boolean isSettable(SimpleActionButton node) {
            return node.staticIcons == null || !node.staticIcons.isBound();
          }

          @Override
          public StyleableProperty<Boolean> getStyleableProperty(SimpleActionButton node) {
            return (StyleableProperty<Boolean>) node.staticIconsProperty();
          }
        };

  }

}
