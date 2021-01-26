package jetbrains.coverage;

import org.junit.After;
import org.junit.Before;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 16:30
 */
public class BaseTestCase {
  protected TempFiles myTempFiles;

  @Before
  public void setUp() throws Exception {
    myTempFiles = new TempFiles();
  }

  @After
  public void tearDown() {
    myTempFiles.cleanup();
  }
}
