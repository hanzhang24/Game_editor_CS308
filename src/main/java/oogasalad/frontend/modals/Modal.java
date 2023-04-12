package oogasalad.frontend.modals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class Modal<T> extends Dialog<T> {

    public static final String MODAL_PROPERTIES_FILE_PATH = "frontend/properties/text/Modals.properties";
    public static final String MODAL_STYlE_FILE_PATH = "frontend/css/modalStyles.css";


    private final String MODAL_STYLE_SHEET = Objects
        .requireNonNull(getClass().getClassLoader().getResource(MODAL_STYlE_FILE_PATH)).toExternalForm();
    private Map<String, String> myPropertiesMap;
    private String myTitle;
    private static String MODAL_FILE_PATH = MODAL_PROPERTIES_FILE_PATH;
//    private static final ResourceBundle MODAL_BUNDLE = ResourceBundle.getBundle("frontend/properties/text/");
    private static final String CANT_LOAD_FILE_ID = "unableToLoadPropertiesFile";
    private static final String ERROR_LOADING_FILE_ID = "Error loading properties file";

    /**
     * Constructor for the modal dialog with a title parameter
     * 
     * @param title
     */
    public Modal(String title) {
        myTitle = title;
        myPropertiesMap = setPropertiesMap(title);
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        setResizable(false);
        setDialogPane(createDialogPane());
        getDialogPane().getStylesheets().add(MODAL_STYLE_SHEET);

        setResultConverter(this::convertResult);
    }

    /**
     * Default Constructor for the modal dialog
     * 
     */
    public Modal() {
        myTitle = "Default";
        myPropertiesMap = setPropertiesMap(myTitle);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        setResizable(false);
        setDialogPane(createDialogPane());
        setResultConverter(this::convertResult);
    }

    /**
     * Creates the dialog pane for the modal
     * 
     * @return
     */
    protected DialogPane createDialogPane() {
        this.getDialogPane().setHeaderText(myPropertiesMap.get("title"));
        myPropertiesMap.remove("title");
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ArrayList<String> stringFields = new ArrayList<>(myPropertiesMap.values());
        setContentAsLabels(stringFields);
        return this.getDialogPane();
    }

    /**
     * Returns a map of the properties from the modal properties file using the
     * title of the modal
     * 
     * @return
     */
    protected Map<String, String> setPropertiesMap(String title) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(MODAL_FILE_PATH);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
//                System.err.println(MODAL_BUNDLE.getString(CANT_LOAD_FILE_ID));
            }
        } catch (Exception e) {
//            System.out.println(MODAL_BUNDLE.getString(ERROR_LOADING_FILE_ID));
        }
        Map<String, List<String>> myPropertiesMap = new TreeMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith(title)) {
                String newKey = key.split("\\.")[1];
//                String startKey = key.substring(title.length() + 1 + newKey.length() + 1);
                myPropertiesMap.put(newKey, List.of(key, properties.getProperty(key)));
            }
        }

        Map<String, String> returnMap = new LinkedHashMap<>();
        for (String key : myPropertiesMap.keySet()) {
            System.out.println(key + " " + myPropertiesMap.get(key).get(0) + " " + myPropertiesMap.get(key).get(1));
            returnMap.put(myPropertiesMap.get(key).get(0), myPropertiesMap.get(key).get(1));

        }

//        System.out.println(returnMap);

        return returnMap;
    }

    protected void setContentAsLabels(ArrayList<String> content) {

        VBox vBox = new VBox();
        for (String s : content) {
            vBox.getChildren().add(makeLabel(s));
        }
        this.getDialogPane().setContent(vBox);
    }

    /**
     * Creates a text field with a prompt text
     * 
     * @param promptText
     * @return
     */
    protected TextField makeTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setPrefWidth(150); // TODO: export to stylesheet
        return textField;
    }

    /**
     * Creates a label with a prompt text
     * 
     * @param promptText
     * @return
     */
    protected Label makeLabel(String promptText) {
        Label label = new Label(promptText);
        label.setPrefWidth(600); 
        label.wrapTextProperty().setValue(true);
        return label;
    }


    /**
     * Returns an unmodifiable version of the map of properties
     * 
     * @return
     */
    protected Map<String, String> getPropertiesMap() {
        return new TreeMap<>(myPropertiesMap);
    }

    /**
     * Converts the button type to the result
     * 
     * @param buttonType
     * @return
     */
    protected T convertResult(ButtonType buttonType) {
        if (buttonType == ButtonType.OK) {
            return getResult();
        }
        return null;
    }
}
