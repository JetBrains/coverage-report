package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Contains a set of classes for which coverage was gathered.
 */
public interface CoverageData {
  /**
   * Returns classes
   * @return classes
   */
  @NotNull
  Collection<ClassInfo> getClasses();

  @Nullable
  CoverageSourceData getSourceData();
}
