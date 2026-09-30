package com.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    final int WINDOW_WIDTH = 700;
    final int WINDOW_HEIGHT = 530;
    final int MIN_WIDTH = 260;
    final int MIN_HEIGHT = 400;

    // Per sota d'aquesta amplada s'usa la vista Mobile
    final double BREAKPOINT = 600;

    @Override
    public void start(Stage stage) throws Exception {

        // Carrega les vistes. La primera és la vista per defecte.
        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "Desktop", "/assets/layoutDesktop.fxml");
        UtilsViews.addView(getClass(), "MobileMenu", "/assets/layoutMobileMenu.fxml");
        UtilsViews.addView(getClass(), "MobileList", "/assets/layoutMobileList.fxml");
        UtilsViews.addView(getClass(), "MobileDetail", "/assets/layoutMobileDetail.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);
        scene.getStylesheets().add(getClass().getResource("/assets/style.css").toExternalForm());

        // Canvia de vista segons l'amplada de la finestra
        scene.widthProperty().addListener((obs, oldW, newW) -> updateLayout(newW.doubleValue()));

        stage.setScene(scene);
        stage.setTitle("NintendoDB");
        stage.setMinWidth(MIN_WIDTH);
        stage.setWidth(WINDOW_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setHeight(WINDOW_HEIGHT);
        stage.show();

        updateLayout(scene.getWidth());

        // Afegeix una icona només si no és un Mac
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:icons/icon.png");
            stage.getIcons().add(icon);
        }
    }

    // Mostra la vista Desktop o la Mobile segons l'amplada
    private void updateLayout(double width) {
        String active = UtilsViews.getActiveView();
        boolean mobile = width < BREAKPOINT;

        if (mobile && "Desktop".equals(active)) {
            UtilsViews.setView("MobileMenu");
        } else if (!mobile && active != null && !"Desktop".equals(active)) {
            UtilsViews.setView("Desktop");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
