package jetbrains.coverage.impl;

import jetbrains.coverage.BaseTestCase;
import jetbrains.coverage.report.impl.html.fs.ZipFileSystem;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Eugene Petrenko (eugene.petrenko@gmail.com)
 *         Date: 26.04.11 14:00
 */
public class ZipFileSystemTest extends BaseTestCase {

  @Test
  public void shouldAllowToOverwriteFiles() throws IOException {
    final File base = myTempFiles.createTempDir();
    final File file = new File(base, "zip.zip");

    ZipFileSystem zip = new ZipFileSystem(file);

    OutputStream stream;

    stream = zip.openFile(new File(base, "aaa/bbb.txt"));
    stream.write(42);
    stream.close();

    stream = zip.openFile(new File(base, "aaa/bbb.txt"));
    stream.write(42);
    stream.close();

    stream = zip.openFile(new File(base, "aaa/bbb.txt"));
    stream.write(42);
    stream.close();

    zip.close();
  }

}
