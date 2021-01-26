package jetbrains.coverage.report;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a block (condition or loop)
 */
public class ClassBlock {
  private CoverageStatus myCoverage;

  public ClassBlock(@Nullable CoverageStatus coverage) {
    myCoverage = coverage;
  }

  /**
   * Returns block coverage.
   * @return see above
   */
  @Nullable 
  public CoverageStatus getCoverage() {
    return myCoverage;
  }
}
