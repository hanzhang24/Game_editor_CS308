package oogasalad.sharedDependencies.backend.filemanagers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Rodrigo Bassi Guerreiro
 *
 * Class used to programatically store information in JSON files
 **/
public class FileMaker {
  protected static String SEPARATOR = ",";
  protected static String RESOURCES_PATH = "backend.filemanager.ValidTags";

  private JsonObject myFileInfo;
  private Collection<String> myValidTags;

  /**
   * Standard constructor
   */
  public FileMaker() {
    myFileInfo = new JsonObject();
    myValidTags = new ArrayList<>();
  }

  public FileMaker(String validTagsKey) {
    this();
    setValidTagsFromResources(validTagsKey);
  }

  /**
   * Add information that will be stored in file
   * @param tag key name in JSON file where data should go
   * @param content information to be stored in file
   */
  public void addContent(String tag, JsonElement content) {
    if (! myValidTags.isEmpty() && ! isValid(tag)) {
      // TODO: maybe make this into a custom exception
      throw new RuntimeException("Invalid tag!");
    }
    if (myFileInfo.has(tag)) {
      JsonArray array;
      if (myFileInfo.get(tag).isJsonArray()) {
        array = myFileInfo.getAsJsonArray(tag);
        array.add(content);
      }
      else {
        array = new JsonArray();
        array.add(myFileInfo.get(tag));
        array.add(content);
        myFileInfo.add(tag, array);
      }
    }
    else {
      myFileInfo.add(tag, content);
    }
  };

  /**
   * Saves currently stored JSON content into a file in the system
   * @param path file and directory where JSON file should be stored
   */
  public void saveToFile(String path) {
    try (Writer writer = new FileWriter(path)) {
      writer.write(myFileInfo.toString());
    }
    catch (IOException e) {
      // TODO: maybe make this into a custom exception for Controller to handle
      throw new RuntimeException(e);
    }
  }

  /**
   * Define collection of acceptable tags for FileManager instance
   * @param tags Collection of Strings containing all valid tags
   */
  protected void setValidTags(Collection<String> tags) {
    myValidTags = tags;
  }

  public void setValidTagsFromResources(String key) {
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PATH);
    String[] validTags = resources.getString(key).split(SEPARATOR);
    this.setValidTags(new HashSet<>(List.of(validTags)));
  }

  /**
   * Check whether tag is valid
   * @param tag String containing tag to be checked
   * @return Returns true if tag is valid, else false
   */
  protected boolean isValid(String tag) {
    return myValidTags.contains(tag);
  }
}
