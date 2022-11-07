package com.catwithawand.synchordia.controller;


import com.catwithawand.synchordia.control.Card;
import com.catwithawand.synchordia.layout.BootstrapColumn;
import com.catwithawand.synchordia.layout.BootstrapPane;
import com.catwithawand.synchordia.layout.BootstrapRow;
import com.catwithawand.synchordia.layout.Breakpoint;
import javafx.fxml.FXML;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class View1Controller {

  private final Set<String> FILE_EXTENSIONS;

  @FXML
  private BootstrapPane root;

  @Autowired
  public View1Controller(@Value("${synchordia.file-extensions}") Set<String> fileExtensions) {
    this.FILE_EXTENSIONS = fileExtensions;
  }

  @FXML
  private void initialize() throws IOException {
    root.setStyle("-fx-background-color: -background-quaternary;");
    root.setHgap(16);
    root.setVgap(16);

    BootstrapRow row = new BootstrapRow();

    for (int i = 0; i < 13; i += 1) {
      row.addColumn(createColumn(new Card("Hello", "This is a test!")));
    }

    root.addRow(row);

    BootstrapRow row1 = new BootstrapRow();

    for (int i = 0; i < 7; i += 1) {
      row1.addColumn(createColumn(new Card("Hello 2", "This is a test 2!")));
    }

    root.addRow(row1);
  }

  private BootstrapColumn createColumn(Node node) {
    BootstrapColumn column = new BootstrapColumn(node);
    column.setBreakpointColumnWidth(Breakpoint.XSMALL, 6);
    column.setBreakpointColumnWidth(Breakpoint.SMALL, 4);
    column.setBreakpointColumnWidth(Breakpoint.MEDIUM, 3);
    column.setBreakpointColumnWidth(Breakpoint.LARGE, 2);
    column.setBreakpointColumnWidth(Breakpoint.XLARGE, 2);

    return column;
  }

}
