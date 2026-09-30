package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ControllerListItem {

    @FXML
    private HBox root;

    @FXML
    private Label title;

    @FXML
    private ImageView img;

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setImatge(String fileName) {
        this.img.setImage(Data.getImage(fileName));
    }

    // Marca l'element com a seleccionat (estil definit a style.css)
    public void setSelected(boolean selected) {
        root.getStyleClass().remove("selected");
        if (selected) {
            root.getStyleClass().add("selected");
        }
    }
}
