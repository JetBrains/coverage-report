package jetbrains.coverage.report;

import org.jetbrains.annotations.Nullable;

/**
 * Coverage status
*/
public enum CoverageStatus {
  NONE,
  PARTIAL,
  FULL,
  ;

  @Nullable
  public static CoverageStatus merge(@Nullable CoverageStatus c1, @Nullable CoverageStatus c2) {
    if (c1 == null) return c2;
    if (c2 == null) return c1;

    if (c1 == c2) return c1;
    return PARTIAL;
  }
}
