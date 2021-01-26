package jetbrains.coverage.report;

/**
 * @author Pavel.Sher
 */
public class DiffEntry {
  private final Entry myPrevEntry;
  private final Entry myNewEntry;

  public DiffEntry(final Entry prevEntry, final Entry newEntry) {
    myPrevEntry = prevEntry;
    myNewEntry = newEntry;
  }

  public int getTotalDiff() {
    return value(myNewEntry.getTotal()) - value(myPrevEntry.getTotal());
  }

  public int getCoveredDiff() {
    return value(myNewEntry.getCovered()) - value(myPrevEntry.getCovered());
  }

  public float getPercentDiff() {
    return value(myNewEntry.getPercent()) - value(myPrevEntry.getPercent());
  }

  private int value(int value) {
    return value < 0 ? 0 : value;
  }

  private float value(float value) {
    return value < 0 ? 0 : value;
  }
}
