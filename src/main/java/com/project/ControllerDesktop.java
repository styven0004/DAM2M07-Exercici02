package com.project;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

/** Vista ampla: selector, llista a l'esquerra i detall a la dreta. */
public class ControllerDesktop implements Initializable {

    private static final String[] CATEGORIES = { Data.GAMES, Data.CHARACTERS, Data.CONSOLES };

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private VBox listBox, detailBox;

    private final ArrayList<ControllerListItem> itemControllers = new ArrayList<>();
    private String category = Data.GAMES;
    private JSONArray items;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (String c : CATEGORIES) {
            choiceBox.getItems().add(Data.getLabel(c));
        }
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (newIdx.intValue() >= 0) {
                loadCategory(CATEGORIES[newIdx.intValue()]);
            }
        });
        choiceBox.getSelectionModel().select(0);
    }

    // Omple la llista amb els elements de la categoria i selecciona el primer
    private void loadCategory(String category) {
        this.category = category;
        this.items = Data.getList(category);

        listBox.getChildren().clear();
        itemControllers.clear();

        try {
            URL resource = getClass().getResource("/assets/listItem.fxml");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                FXMLLoader loader = new FXMLLoader(resource);
                Parent node = loader.load();
                ControllerListItem controller = loader.getController();
                controller.setTitle(item.getString("name"));
                controller.setImatge(item.getString("image"));

                final int index = i;
                node.setOnMouseClicked(e -> select(index));

                listBox.getChildren().add(node);
                itemControllers.add(controller);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (items.length() > 0) {
            select(0);
        }
    }

    // Mostra el detall de l'element i el ressalta a la llista
    private void select(int index) {
        for (int i = 0; i < itemControllers.size(); i++) {
            itemControllers.get(i).setSelected(i == index);
        }
        Data.fillDetail(detailBox, category, items.getJSONObject(index), 200);
    }
}
