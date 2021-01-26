package jetbrains.coverage.report;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a class line.
 */
public class ClassLine {

  private final int myLineNumber;
  private final CoverageStatus myCoverageStatus;

  public ClassLine(final int lineNumber, @Nullable final CoverageStatus coverageStatus) {
    myLineNumber = lineNumber;
    myCoverageStatus = coverageStatus;
  }

  /**
   * Returns line number
   * @return see above
   */
  public int getLineNumber() {
    return myLineNumber;
  }

  /**
   * Returns line coverage, or null. If null is returned this line is not considered as executable and thus
   * does not affect coverage statistics.
   * @return see above
   */
  @Nullable
  public CoverageStatus getCoverage() {
    return myCoverageStatus;
  }
}
