package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         19.10.10 13:46
 */
public interface CoverageCodeRenderer {
  /**
   * Creates new source file section with header before
   * @param caption section header text
   */
  void writeSectionHeader(@NotNull String caption);

  /**
   * Attached a source code line to a source code section.
   * @param lineNumber number of line to show
   * @param source text of a line
   * @param status covered status
   */
  void writeCodeLine(int lineNumber, @NotNull CharSequence source, @Nullable CoverageStatus status);

  /**
   * Should be called at the end of source file rendering.
   */
  void codeWriteFinished();
}
