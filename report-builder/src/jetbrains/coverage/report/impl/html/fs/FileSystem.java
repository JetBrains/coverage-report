package jetbrains.coverage.report.impl.html.fs;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Eugene Petrenko (eugene.petrenko@gmail.com)
 *         Date: 27.02.11 19:17
 */
public interface FileSystem {
  @NotNull
  OutputStream openFile(@NotNull File path) throws IOException;
}
