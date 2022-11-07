package com.catwithawand.synchordia.control;

import com.catwithawand.synchordia.SynchordiaApplication;
import com.catwithawand.synchordia.util.FXUtils;
import com.sun.javafx.css.StyleManager;
import de.schlegel11.jfxanimation.JFXAnimationTemplate;
import de.schlegel11.jfxanimation.JFXAnimationTemplates;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.css.converter.SizeConverter;
import javafx.css.converter.URLConverter;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;

public class Card extends GridPane {


  private static final String DEFAULT_STYLE_CLASS = "card-container";
  private static final String DEFAULT_IMAGE_STYLE_CLASS = "card-image-container";
  private static final String DEFAULT_TITLE_STYLE_CLASS = "card-title";
  private static final String DEFAULT_DESCRIPTION_STYLE_CLASS = "card-description";

  @Getter
  private final Rectangle imageRect = new Rectangle();

  @Getter
  private final PlayPauseButton playButton = new PlayPauseButton();

  @Getter
  private final Label titleLabel = new Label();

  @Getter
  private final MultilineLabel descriptionLabel = new MultilineLabel("", 2);

  private ObjectProperty<Image> image;
  private StringProperty imageUrl;
  private DoubleProperty imageSize;
  private StringProperty title;
  private StringProperty description;

  @Getter
  private Timeline fadeInUpAnimation;

  @Getter
  private Timeline fadeOutDownAnimation;


  public Card() {
    super();
    getStyleClass().add(DEFAULT_STYLE_CLASS);
    setOpaqueInsets(Insets.EMPTY);

    DoubleBinding heightBinding = Bindings.createDoubleBinding(() -> (6.0 / 11.0) * (
        2.0 * widthProperty().get() + 119.0), widthProperty());

    minHeightProperty().bind(heightBinding);
    maxHeightProperty().bind(heightBinding);

    imageSizeProperty().bind(widthProperty().subtract(32));

    imageRect.widthProperty().bind(imageSizeProperty());
    imageRect.heightProperty().bind(imageSizeProperty());
    imageRect.setArcWidth(8);
    imageRect.setArcHeight(8);

    FXUtils.consumeNonPrimaryClicks(playButton);
    playButton.setOpacity(0);
    playButton.setOnMouseClicked(event -> playButton.setPlaying(!playButton.isPlaying()));

    StackPane imageContainer = new StackPane(imageRect, playButton);
    StackPane.setAlignment(imageRect, Pos.CENTER);
    StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);
    imageContainer.getStyleClass().add(DEFAULT_IMAGE_STYLE_CLASS);

    titleLabel.getStyleClass().add(DEFAULT_TITLE_STYLE_CLASS);
    titleLabel.textProperty().bind(titleProperty());

    descriptionLabel.setSpacing(6);

    for (Label label : descriptionLabel.getLabels()) {
      label.getStyleClass().add(DEFAULT_DESCRIPTION_STYLE_CLASS);
    }

    VBox textContainer = new VBox(titleLabel, descriptionLabel);
    textContainer.getStyleClass().add("card-text-container");

    addColumn(0);
    addRow(0, imageContainer);
    addRow(1, textContainer);

    ColumnConstraints colConstraints = new ColumnConstraints();
    colConstraints.setHalignment(HPos.CENTER);
    colConstraints.setHgrow(Priority.SOMETIMES);
    colConstraints.setPercentWidth(100);

    getColumnConstraints().add(0, colConstraints);

    RowConstraints row0Constraints = new RowConstraints();
    row0Constraints.setValignment(VPos.TOP);
    row0Constraints.setPercentHeight(68);

    RowConstraints row1Constraints = new RowConstraints();
    row1Constraints.setValignment(VPos.CENTER);
    row1Constraints.setPercentHeight(32);

    getRowConstraints().add(0, row0Constraints);
    getRowConstraints().add(1, row1Constraints);

    try {
      Image defaultImage = new Image(SynchordiaApplication.class.getResource(
          "images/placeholder.png").openStream());

      setImage(defaultImage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    buildAnimations();
    addEventHandlers();
  }

  public Card(String title, String description) {
    this();
    setTitle(title);
    setDescription(description);
  }

  public final ObjectProperty<Image> imageProperty() {
    if (image == null) {
      image = new ObjectPropertyBase<Image>() {
        @Override
        public void invalidated() {

        }

        @Override
        public Object getBean() {
          return Card.this;
        }

        @Override
        public String getName() {
          return "image";
        }
      };
    }

    return image;
  }

  public final StringProperty imageUrlProperty() {
    if (imageUrl == null) {
      imageUrl = new StyleableStringProperty() {
        @Override
        protected void invalidated() {
          final String imageUrl = get();
          if (imageUrl != null) {
            setImage(StyleManager.getInstance().getCachedImage(imageUrl));
          } else {
            setImage(null);
          }
        }

        @Override
        public Object getBean() {
          return Card.this;
        }

        @Override
        public String getName() {
          return "imageUrl";
        }

        @Override
        public CssMetaData<? extends Styleable, String> getCssMetaData() {
          return StyleableProperties.IMG_URL;
        }
      };
    }

    return imageUrl;
  }

  public final DoubleProperty imageSizeProperty() {
    if (imageSize == null) {
      imageSize = new StyleableDoubleProperty() {
        @Override
        public Object getBean() {
          return Card.this;
        }

        @Override
        public String getName() {
          return "imageSize";
        }

        @Override
        public CssMetaData<? extends Styleable, Number> getCssMetaData() {
          return StyleableProperties.IMG_SIZE;
        }
      };
    }

    return imageSize;
  }

  public final StringProperty titleProperty() {
    if (title == null) {
      title = new SimpleStringProperty(this, "title", "");
    }

    return title;
  }

  public final StringProperty descriptionProperty() {
    if (description == null) {
      description = new SimpleStringProperty(this, "description", "");
    }

    return description;
  }

  public final Image getImage() {
    return image == null ? null : image.get();
  }

  public final void setImage(Image img) {
    imageProperty().set(img);

    ImagePattern imagePattern = new ImagePattern(img);
    imageRect.setFill(imagePattern);
  }

  public final Double getImageSize() {
    return imageSize == null ? calculateImageSize() : imageSize.get();
  }

  public final void setImageSize(Double size) {
    imageSizeProperty().set(size);
  }

  public final String getTitle() {
    return title == null ? "" : title.get();
  }

  public final void setTitle(String text) {
    titleProperty().set(text);
  }

  public final String getDescription() {
    return description == null ? "" : description.get();
  }

  public final void setDescription(String text) {
    descriptionProperty().set(text);
    descriptionLabel.setText(text);
  }

  private final Double calculateImageSize() {
    return getWidth() - 32;
  }

  private final void resizeImageRect() {

  }

  private void buildAnimations() {
    this.fadeInUpAnimation = JFXAnimationTemplate.create()
        .percent(0)
        .action(b -> b.target(Node::translateYProperty).endValue(0))
        .action(b -> b.target(Node::opacityProperty).endValue(0))
        .percent(100)
        .action(b -> b.target(Node::translateYProperty).endValue(-12))
        .action(b -> b.target(Node::opacityProperty).endValue(1))
        .config(b -> b.duration(Duration.millis(300)).interpolator(Interpolator.EASE_BOTH))
        .build(JFXAnimationTemplates::buildTimeline, playButton);

    this.fadeOutDownAnimation = JFXAnimationTemplate.create()
        .percent(0)
        .action(b -> b.target(Node::translateYProperty).endValue(-12))
        .action(b -> b.target(Node::opacityProperty).endValue(1))
        .percent(100)
        .action(b -> b.target(Node::translateYProperty).endValue(0))
        .action(b -> b.target(Node::opacityProperty).endValue(0))
        .config(b -> b.duration(Duration.millis(300)).interpolator(Interpolator.EASE_BOTH))
        .build(JFXAnimationTemplates::buildTimeline, playButton);
  }

  private void addEventHandlers() {
    setOnMouseEntered(event -> {
      if (playButton.isPlaying()) {
        return;
      }

      this.fadeInUpAnimation.play();
    });

    setOnMouseExited(event -> {
      if (playButton.isPlaying()) {
        return;
      }

      this.fadeOutDownAnimation.play();
    });
  }

  private static class StyleableProperties {

    private static final CssMetaData<Card, String> IMG_URL = new CssMetaData<Card, String>(
        "-fx-img-url",
        URLConverter.getInstance()
    ) {
      @Override
      public boolean isSettable(Card node) {
        return node.imageRect == null;
      }

      @Override
      public StyleableProperty<String> getStyleableProperty(Card node) {
        return (StyleableProperty<String>) node.imageUrlProperty();
      }
    };

    private static final CssMetaData<Card, Number> IMG_SIZE =
        new CssMetaData<Card, Number>(
            "-fx-image-size",
            SizeConverter.getInstance(),
            172
        ) {
          @Override
          public boolean isSettable(Card node) {
            return node.imageSize == null || !node.imageSize.isBound();
          }

          @Override
          public StyleableProperty<Number> getStyleableProperty(Card node) {
            return (StyleableProperty<Number>) node.imageSizeProperty();
          }
        };

  }

}
