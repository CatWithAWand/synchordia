package com.catwithawand.synchordia.control;

import com.sun.javafx.scene.control.skin.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.SizeConverter;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextBoundsType;
import lombok.Getter;

public class MultilineLabel extends VBox {

  @Getter
  private ObservableList<Label> labels = FXCollections.observableArrayList();

  private StringProperty text;
  private StringProperty ellipsisString;
  private IntegerProperty maxLines;

  public MultilineLabel() {
    super();
    getStyleClass().add("multiline-label");
    setMaxLines(1);

    Bindings.bindContent(getChildren(), labels);
  }

  public MultilineLabel(String text, int maxLines) {
    this();
    setText(text);
    setMaxLines(maxLines);
  }

  public final StringProperty textProperty() {
    if (text == null) {
      text = new SimpleStringProperty(this, "text", "");
    }

    return text;
  }

  public final StringProperty ellipsisStringProperty() {
    if (ellipsisString == null) {
      ellipsisString = new SimpleStringProperty(this, "ellipsisString", "...");
    }

    return ellipsisString;
  }

  public final IntegerProperty maxLinesProperty() {
    if (maxLines == null) {
      maxLines = new StyleableIntegerProperty() {
        @Override
        public Object getBean() {
          return MultilineLabel.this;
        }

        @Override
        public String getName() {
          return "maxLines";
        }

        @Override
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
          return StyleableProperties.MAX_LINES;
        }
      };
    }

    return maxLines;
  }

  public final String getText() {
    return text == null ? "" : text.get();
  }

  public final void setText(String text) {
    textProperty().set(text);
  }

  public final String getEllipsisString() {
    return ellipsisString == null ? "..." : ellipsisString.get();
  }

  public final void setEllipsisString(String ellipsisString) {
    ellipsisStringProperty().set(ellipsisString);
  }

  public final Integer getMaxLines() {
    return maxLines == null ? 0 : maxLines.getValue();
  }

  public final void setMaxLines(Integer maxLines) {
    int oldMaxLines = getMaxLines();
    maxLinesProperty().setValue(maxLines);

    if (oldMaxLines > maxLines) {
      labels.remove(maxLines, oldMaxLines);
    } else {
      int diff = maxLines - oldMaxLines;

      for (int i = 0; i < diff; i += 1) {
        labels.add(createLabel());
      }
    }
  }

  @Override
  protected void layoutChildren() {
    super.layoutChildren();

    String remainingText = getText();

    for (int i = 0; i < labels.size(); i += 1) {
      final Label currentLine = labels.get(i);

      if (i == labels.size() - 1) {
        currentLine.setText(remainingText);
        currentLine.setTextOverrun(OverrunStyle.ELLIPSIS);
      } else {
        final String computedWrappedText =
            Utils.computeClippedWrappedText(
                currentLine.getFont(),
                remainingText,
                getWidth(),
                getHeight(),
                10,
                OverrunStyle.CLIP,
                getEllipsisString(),
                TextBoundsType.VISUAL
            );

        currentLine.setText(computedWrappedText);
        remainingText = remainingText.substring(computedWrappedText.length());
      }
    }
  }

  private Label createLabel() {
    Label label = new Label();
    label.setEllipsisString(getEllipsisString());
    label.setTextOverrun(OverrunStyle.CLIP);

    return label;
  }

  private static class StyleableProperties {

    private static final CssMetaData<MultilineLabel, Number> MAX_LINES =
        new CssMetaData<MultilineLabel, Number>(
            "-fx-max-lines",
            SizeConverter.getInstance(),
            1
        ) {

          @Override
          public boolean isSettable(MultilineLabel node) {
            return node.maxLines == null || !node.maxLines.isBound();
          }

          @Override
          public StyleableProperty<Number> getStyleableProperty(MultilineLabel node) {
            return (StyleableProperty<Number>) node.maxLinesProperty();
          }
        };

  }

}
