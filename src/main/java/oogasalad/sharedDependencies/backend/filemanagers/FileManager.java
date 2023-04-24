package oogasalad.sharedDependencies.backend.filemanagers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Rodrigo Bassi Guerreiro
 *
 * Class used to programatically store information in configuration files
 * Currently implemented to store information as JSON files (using the Gson library)
 **/
public class FileManager {
  protected static String SEPARATOR = ",";
  protected static String RESOURCES_PATH = "backend.filemanager.ValidTags";

  private final JsonObject myFileInfo;
  private Collection<String> myValidTags;

  /**
   * Standard constructor
   */
  public FileManager() {
    myFileInfo = new JsonObject();
    myValidTags = new HashSet<>();
  }

  public FileManager(String filePath) throws FileNotFoundException {
    Gson gson = new Gson();
    myFileInfo = gson
        .fromJson(new FileReader(filePath), JsonElement.class)
        .getAsJsonObject();
    myValidTags = new HashSet<>();
  }

  /**
   * Adds content to currently stored file structure in the specified hierarchical order
   *
   * @param content String containing content to be added to file
   * @param tags arbitrary number of String specifying hierarchical sequence (from highest to lowest)
   */
  public void addContent(String content, String... tags) {
    updateHierarchy(myFileInfo, content, tags);
  }

  /**
   * Modifies the internally stored JsonObject with the specified information
   *
   * @param object JsonObject to be modified
   * @param content String containing content to be added to file
   * @param tags arbitrary number of String specifying hierarchical sequence (from highest to lowest)
   */
  private void updateHierarchy(JsonObject object, String content, String... tags) {
    if (tags.length == 0) {
      throw new IllegalArgumentException();
    }
    if (! isValid(tags[0])) {
      // TODO: throw custom exception
    }

    if (tags.length == 1) {
      addLowestContent(object, tags[0], new JsonPrimitive(content));
    }
    else if (object.has(tags[0])) {
      updateHierarchy(object.getAsJsonObject(tags[0]),
          content, Arrays.copyOfRange(tags, 1, tags.length));
    }
    else {
      object.add(tags[0], makeHierarchy(content, Arrays.copyOfRange(tags, 1, tags.length)));
    }
  }

  /**
   * Makes JsonObject representing hierarchical structure
   * @param content text content to be added at end of hierarchy
   * @param tags arbitrary number of tags in order of hierarchy (from highest to lowest)
   * @return JsonObject representing hierarchical structure
   */
  private JsonObject makeHierarchy(String content, String... tags) {
    JsonObject object = new JsonObject();
    if (tags.length == 1) {
      object.add(tags[0], new JsonPrimitive(content));
    }
    else {
      object.add(tags[0], makeHierarchy(content, Arrays.copyOfRange(tags, 1, tags.length)));
    }
    return object;
  }

  /**
   * Add information that will be stored in file
   *
   * @param tag     key name in JSON file where data should go
   * @param content information to be stored in file
   */
  protected void addLowestContent(JsonObject object, String tag, JsonElement content) {
    if (!object.isEmpty() && !isValid(tag)) {
      // TODO: maybe make this into a custom exception
      throw new RuntimeException("Invalid tag!");
    }
    if (object.has(tag)) {
      JsonArray array;
      if (object.get(tag).isJsonArray()) {
        array = object.getAsJsonArray(tag);
        array.add(content);
      } else {
        array = new JsonArray();
        array.add(object.get(tag));
        array.add(content);
        object.add(tag, array);
      }
    } else {
      object.add(tag, content);
    }
  }

  /**
   * Saves currently stored JSON content into a file in the system
   *
   * @param path file and directory where JSON file should be stored
   */
  public void saveToFile(String path) {
    try (Writer writer = new FileWriter(path)) {
      writer.write(myFileInfo.toString());
    } catch (IOException e) {
      // TODO: maybe make this into a custom exception for Controller to handle
      throw new RuntimeException(e);
    }
  }

  /**
   * Define collection of acceptable tags for FileManager instance
   *
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
   *
   * @param tag String containing tag to be checked
   * @return Returns true if tag is valid, else false
   */
  protected boolean isValid(String tag) {
    if (myValidTags == null || myValidTags.isEmpty()) {
      return true;
    }
    return myValidTags.contains(tag);
  }

  /**
   * Gets information in the form of a String from configuration file
   * by following the specified hierarchy
   *
   * @param tags variable number of String parameters in order of hierarchy (from high to low)
   * @return String found by following specified hierarchy
   */
  public String getString(String... tags) {
    if (tags.length == 0) {
      throw new IllegalArgumentException();
    }
    JsonElement element = traverse(tags);
    if (element.isJsonPrimitive()) {
      return element.getAsString();
    }
    else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Gets information in the form of an Iterable of Strings from configuration file
   * by following the specified hierarchy
   *
   * @param tags variable number of String parameters in order of hierarchy (from high to low)
   * @return Iterable of Strings found by following specified hierarchy
   */
  public Iterable<String> getArray(String... tags) {
    if (tags.length == 0) {
      throw new IllegalArgumentException();
    }
    JsonElement element = traverse(tags);
    if (element.isJsonArray()) {
      return JsonArrayToIterable(element.getAsJsonArray());
    }
    else if (element.isJsonPrimitive()) {
      return new ArrayList<>(Arrays.asList(element.getAsString()));
    }
    throw new IllegalArgumentException();
  }

  /**
   * Returns an Iterable of Strings containing all the tags at the specified hierarchy
   *
   * @param tags variable number of String parameters in order of hierarchy (from high to low)
   * @return Iterable of Strings containing tags at specified hierarchy
   */
  public Iterable<String> getTagsAtLevel(String... tags) {
    JsonObject object = myFileInfo;
    for (String tag : tags) {
      object = object.getAsJsonObject(tag);
    }
    List<String> tagList = new LinkedList<>();
    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      tagList.add(entry.getKey());
    }
    return tagList;
  }

  private JsonElement traverse(String... tags) {
    JsonElement element = myFileInfo;
    JsonObject object;
    for (String tag : tags) {
      object = element.getAsJsonObject();
      if (! object.has(tag)) {
        throw new IllegalArgumentException();
      }
      element = object.get(tag);
    }
    return element;
  }

  private Iterable<String> JsonArrayToIterable(JsonArray array) {
    List<String> list = new LinkedList<>();
    for (JsonElement element : array) {
      list.add(element.getAsString());
    }
    return list;
  }
}
