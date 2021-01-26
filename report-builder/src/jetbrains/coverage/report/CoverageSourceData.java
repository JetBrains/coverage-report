package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         19.10.10 16:40
 */
public interface CoverageSourceData {
  /**
   * This method is called for class to procude sources.
   * If no methods is called, it's decided class contains no sources
   * @param clazz class to render sources for
   * @param renderer renderer object given for class to render it's contents
   */
  void renderSourceCodeFor(@NotNull ClassInfo clazz, @NotNull CoverageCodeRenderer renderer);
}
