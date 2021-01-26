package jetbrains.coverage.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents class method
 */
public class ClassMethod {
  private final CoverageStatus myCoverage;
  private final String mySignature;

  public ClassMethod(@NotNull String signature, @Nullable CoverageStatus status) {
    mySignature = signature;
    myCoverage = status;
  }

  /**
   * Returns method signature
   * @return method signature
   */
  public String getSignature() {
    return mySignature;
  }

  /**
   * Returns method coverage, or null. If null is returned this method will not affect coverage statistics.
   * @return method coverage
   */
  @Nullable
  public CoverageStatus getCoverage() {
    return myCoverage;
  }
}
