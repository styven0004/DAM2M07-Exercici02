package com.project;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Accés a les dades (.json) i a les imatges, i construcció del detall
 * d'un element. Compartit per la vista Desktop i la vista Mobile.
 */
public class Data {

    // Identificadors de categoria (coincideixen amb el nom de l'arxiu .json)
    public static final String GAMES = "games";
    public static final String CHARACTERS = "characters";
    public static final String CONSOLES = "consoles";

    private static final Map<String, JSONArray> cacheJson = new HashMap<>();
    private static final Map<String, Image> cacheImages = new HashMap<>();

    // Nom que es mostra a la UI per a cada categoria
    public static String getLabel(String category) {
        switch (category) {
            case GAMES:      return "Jocs";
            case CHARACTERS: return "Personatges";
            case CONSOLES:   return "Consoles";
            default:         return category;
        }
    }

    // Carrega (i guarda en memòria) el .json d'una categoria
    public static JSONArray getList(String category) {
        JSONArray list = cacheJson.get(category);
        if (list == null) {
            try (InputStream is = Data.class.getResourceAsStream("/assets/" + category + ".json")) {
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                list = new JSONArray(content);
                cacheJson.put(category, list);
            } catch (Exception e) {
                e.printStackTrace();
                list = new JSONArray();
            }
        }
        return list;
    }

    // Carrega (i guarda en memòria) una imatge de /assets/images
    public static Image getImage(String fileName) {
        Image img = cacheImages.get(fileName);
        if (img == null) {
            try (InputStream is = Data.class.getResourceAsStream("/assets/images/" + fileName)) {
                img = new Image(is);
                cacheImages.put(fileName, img);
            } catch (Exception e) {
                System.err.println("Error loading image asset: " + fileName);
                e.printStackTrace();
            }
        }
        return img;
    }

    /**
     * Omple 'box' amb la informació d'un element (imatge, títol i dades).
     * La imatge s'adapta a l'amplada disponible, amb un màxim de 'maxImage'.
     */
    public static void fillDetail(VBox box, String category, JSONObject item, double maxImage) {
        box.getChildren().clear();
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(14);

        // Imatge
        ImageView img = new ImageView(getImage(item.getString("image")));
        img.setPreserveRatio(true);
        img.fitWidthProperty().bind(Bindings.max(60.0, Bindings.min(maxImage, box.widthProperty().subtract(50))));
        img.fitHeightProperty().bind(img.fitWidthProperty());
        box.getChildren().add(img);

        // Títol
        Label title = new Label(item.getString("name"));
        title.setWrapText(true);
        title.setTextAlignment(TextAlignment.CENTER);
        boolean bold = !category.equals(GAMES);
        title.setFont(Font.font(Font.getDefault().getFamily(), bold ? FontWeight.BOLD : FontWeight.NORMAL, 18));
        box.getChildren().add(title);

        switch (category) {
            case GAMES:
                box.getChildren().add(text(item.getString("plot")));
                break;

            case CHARACTERS:
                box.getChildren().add(colorSquare(item.getString("color")));
                box.getChildren().add(text(item.getString("game")));
                break;

            case CONSOLES:
                box.getChildren().add(colorSquare(item.getString("color")));
                box.getChildren().add(text("Data: " + formatDate(item.getString("date"))));
                box.getChildren().add(text("Processador: " + item.getString("procesador")));
                String units = String.format(Locale.forLanguageTag("ca"), "%,d", item.getLong("units_sold"));
                box.getChildren().add(text("Unitats venudes: " + units));
                break;
        }
    }

    // Etiqueta de text centrada i amb salt de línia automàtic
    private static Label text(String s) {
        Label label = new Label(s);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    // Quadradet del color indicat al .json
    private static Region colorSquare(String color) {
        Region square = new Region();
        square.setMinSize(12, 12);
        square.setPrefSize(12, 12);
        square.setMaxSize(12, 12);
        Color c;
        try {
            c = Color.web(color);
        } catch (Exception e) {
            c = Color.GRAY;
        }
        square.setStyle("-fx-background-color: " + toHex(c) + ";");
        return square;
    }

    private static String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int) Math.round(c.getRed() * 255),
                (int) Math.round(c.getGreen() * 255),
                (int) Math.round(c.getBlue() * 255));
    }

    // "2017-3-3" -> "03/03/2017"
    private static String formatDate(String date) {
        try {
            String[] p = date.split("-");
            return String.format("%02d/%02d/%s", Integer.parseInt(p[2]), Integer.parseInt(p[1]), p[0]);
        } catch (Exception e) {
            return date;
        }
    }
}
