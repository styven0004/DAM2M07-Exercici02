package com.project;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/** Vista estreta (2/3): llista d'una categoria. */
public class ControllerMobileList {

    @FXML
    private Label title;
    @FXML
    private VBox listBox;
    @FXML
    private ScrollPane scroll;

    private String category;

    public void load(String category) {
        this.category = category;
        title.setText(Data.getLabel(category));
        listBox.getChildren().clear();
        scroll.setVvalue(0);

        JSONArray items = Data.getList(category);
        try {
            URL resource = getClass().getResource("/assets/listItem.fxml");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                FXMLLoader loader = new FXMLLoader(resource);
                Parent node = loader.load();
                ControllerListItem controller = loader.getController();
                controller.setTitle(item.getString("name"));
                controller.setImatge(item.getString("image"));

                node.setOnMouseClicked(e -> {
                    ControllerMobileDetail detail = (ControllerMobileDetail) UtilsViews.getController("MobileDetail");
                    detail.show(this.category, item);
                    UtilsViews.setViewAnimating("MobileDetail");
                });

                listBox.getChildren().add(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        UtilsViews.setViewAnimating("MobileMenu");
    }
}
