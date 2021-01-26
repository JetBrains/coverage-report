package jetbrains.coverage.report.impl.html.fs;

import jetbrains.coverage.report.impl.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * @author Eugene Petrenko (eugene.petrenko@gmail.com)
 *         Date: 27.02.11 19:18
 */
public class RealFSImpl implements FileSystem {
  @NotNull
  public OutputStream openFile(@NotNull File path) throws IOException {
    IOUtil.createDir(path.getParentFile());
    return new BufferedOutputStream(new FileOutputStream(path));
  }
}
