package oogasalad.sharedDependencies.backend.database;

import java.io.IOException;

public class MockDatabase extends Database {
  private static final String INFO_PATH = System.getProperty("user.dir")
      + "/src/main/resources/backend/database/duvalley-boiz-test-firebase-adminsdk-tafql-0dbd665e45.json";
  private static final String URL = "https://duvalley-boiz-test.firebaseio.com/";

  private static final String PROJECT_ID = "duvalley-boiz-test";

  public MockDatabase() {
    this(PROJECT_ID, INFO_PATH, URL);
  }

  public MockDatabase(String projectId, String infoPath, String url) {
    super(projectId, infoPath, url);
  }
}
