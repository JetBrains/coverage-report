package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Provides class source code
 */
public interface SourceCodeProvider {
  /**
   * Returns source code of the class with specified fully qualified name
   * @param className fully qualified class name
   * @return source code
   * @throws IOException if error occurs
   */
  @Nullable
  CharSequence getSourceCode(@NotNull String className) throws IOException;
}
