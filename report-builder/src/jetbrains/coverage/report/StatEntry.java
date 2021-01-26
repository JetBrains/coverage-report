package jetbrains.coverage.report;

import org.jetbrains.annotations.Nullable;

/**
 * Single statistics entry
 */
public class StatEntry extends Entry {
  private Entry myPrevValue;

  public StatEntry(int total, int covered) {
    this(total, covered, null);
  }

  public StatEntry(int total, int covered, @Nullable Entry prevValue) {
    super(total, covered);

    myPrevValue = prevValue;
  }

  /**
   * Returns diff with previous value if such value is available or null
   * @return see above
   */
  @Nullable 
  public DiffEntry getDiff() {
    if (myPrevValue == null) return null;
    return new DiffEntry(myPrevValue, this);
  }
}
