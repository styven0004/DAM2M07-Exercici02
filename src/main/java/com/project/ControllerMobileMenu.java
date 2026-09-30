package com.project;

import javafx.fxml.FXML;

/** Vista estreta (1/3): menú amb les tres categories. */
public class ControllerMobileMenu {

    @FXML
    private void goCharacters() {
        open(Data.CHARACTERS);
    }

    @FXML
    private void goGames() {
        open(Data.GAMES);
    }

    @FXML
    private void goConsoles() {
        open(Data.CONSOLES);
    }

    private void open(String category) {
        ControllerMobileList ctrl = (ControllerMobileList) UtilsViews.getController("MobileList");
        ctrl.load(category);
        UtilsViews.setViewAnimating("MobileList");
    }
}
