package com.project;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/** Vista estreta (3/3): detall d'un element. */
public class ControllerMobileDetail {

    @FXML
    private Label title;
    @FXML
    private VBox detailBox;
    @FXML
    private ScrollPane scroll;

    public void show(String category, JSONObject item) {
        title.setText(item.getString("name"));
        Data.fillDetail(detailBox, category, item, 320);
        scroll.setVvalue(0);
    }

    @FXML
    private void goBack() {
        UtilsViews.setViewAnimating("MobileList");
    }
}
