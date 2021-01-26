package jetbrains.coverage.report;

import org.jetbrains.annotations.Nullable;

/**
 * Represents coverage statistics
 */
public interface CoverageStatistics {
  /**
   * Returns class statistics
   * @return see above
   */
  @Nullable
  StatEntry getClassStats();

  /**
   * Returns methods statistics
   * @return see above
   */
  @Nullable
  StatEntry getMethodStats();

  /**
   * Returns blocks (conditions {@literal &} loops) statistics
   * @return see above
   */
  @Nullable
  StatEntry getBlockStats();

  /**
   * Returns lines statistics
   * @return see above
   */
  @Nullable
  StatEntry getLineStats();

  /**
   * Returns statements statistics
   * @return see above
   */
  @Nullable
  StatEntry getStatementStats();
}
