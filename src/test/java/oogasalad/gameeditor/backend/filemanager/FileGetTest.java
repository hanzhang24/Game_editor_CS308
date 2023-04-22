package oogasalad.gameeditor.backend.filemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import oogasalad.sharedDependencies.backend.filemanagers.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class for testing FileManager functionalities of getting information from configuration files
 *
 * @author Rodrigo Bassi Guerreiro
 */
public class FileGetTest {
  private static final String FILE_FOLDER = System.getProperty("user.dir") + "/data/testfiles";
  private static final String RESOURCES_PATH = "TestConfigFiles";

  FileManager fileManager;
  ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PATH);

  @BeforeEach
  void setup() {
    fileManager = new FileManager();
  }

  @Test
  void noHierarchyGetTest() throws FileNotFoundException {
    fileManager = new FileManager(getPath("SINGLE_TAG"));
    assertEquals(fileManager.getString("name"), "Rodrigo");
    assertEquals(fileManager.getArray("name"), new ArrayList<>(Arrays.asList("Rodrigo")));

    fileManager = new FileManager(getPath("DIFFERENT_TAG"));
    assertEquals(fileManager.getString("name"), "Rodrigo");
    assertEquals(fileManager.getString("nickname"), "Hot Rod");
  }

  @Test
  void hierarchyGetTest() throws FileNotFoundException {
    fileManager = new FileManager(getPath("DOUBLE_HIERARCHY"));
    assertEquals(fileManager.getString("person1", "name"), "Rodrigo");
    assertEquals(fileManager.getString("person2", "nickname"), "EeEEe");
  }

  @Test
  void arrayGetTest() throws FileNotFoundException {
    fileManager = new FileManager(getPath("SAME_TAG"));
    List<String> compare = Arrays.asList("Rodrigo", "Hot Rod");
    Iterator<String> iteratorFromFile = fileManager.getArray("name").iterator();
    Iterator<String> iteratorExpected = compare.iterator();

    while (iteratorFromFile.hasNext() && iteratorExpected.hasNext()) {
      assertEquals(iteratorFromFile.next(), iteratorExpected.next());
    }
  }

  @Test
  void sadTests() throws FileNotFoundException {
    fileManager = new FileManager(getPath("SAME_TAG"));
    assertThrows(IllegalArgumentException.class, () -> {
      fileManager.getString("invalid key");
    });
    assertThrows(IllegalArgumentException.class, () -> {
      fileManager.getString("name");
    });
    assertThrows(FileNotFoundException.class, () -> {
      FileManager sadFileManager = new FileManager("bad file path");
    });
  }

  private String getPath(String key) {
    return FILE_FOLDER + "/" + resources.getString(key);
  }
}