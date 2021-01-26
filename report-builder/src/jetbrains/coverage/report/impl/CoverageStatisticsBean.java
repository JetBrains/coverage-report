package jetbrains.coverage.report.impl;

import jetbrains.coverage.report.CoverageStatistics;
import jetbrains.coverage.report.Entry;
import jetbrains.coverage.report.StatEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Pavel.Sher
 */
public class CoverageStatisticsBean implements CoverageStatistics {
  private Map<String, Counter> myValues = new TreeMap<String, Counter>();
  private CoverageStatistics myPrevStats;

  private static final String TOTAL_CLASSES = "classes-total";
  private static final String COVERED_CLASSES = "classes-covered";
  private static final String TOTAL_METHODS = "methods-total";
  private static final String COVERED_METHODS = "methods-covered";
  private static final String TOTAL_LINES = "lines-total";
  private static final String COVERED_LINES = "lines-covered";
  private static final String TOTAL_BLOCKS = "blocks-total";
  private static final String COVERED_BLOCKS = "blocks-covered";
  private static final String TOTAL_STATEMENTS = "statements-total";
  private static final String COVERED_STATEMENTS = "statements-covered";

  private static final CoverageStatistics NULL_STATS = new CoverageStatistics() {
    public StatEntry getClassStats() {
      return null;
    }

    public StatEntry getMethodStats() {
      return null;
    }

    public StatEntry getBlockStats() {
      return null;
    }

    public StatEntry getLineStats() {
      return null;
    }

    public StatEntry getStatementStats() {
      return null;
    }
  };

  public CoverageStatisticsBean(@Nullable CoverageStatistics prevStats) {
    myValues.put(TOTAL_CLASSES, new Counter(-1));
    myValues.put(TOTAL_METHODS, new Counter(-1));
    myValues.put(TOTAL_BLOCKS, new Counter(-1));
    myValues.put(TOTAL_LINES, new Counter(-1));
    myValues.put(TOTAL_STATEMENTS, new Counter(-1));

    myValues.put(COVERED_CLASSES, new Counter(-1));
    myValues.put(COVERED_METHODS, new Counter(-1));
    myValues.put(COVERED_BLOCKS, new Counter(-1));
    myValues.put(COVERED_LINES, new Counter(-1));
    myValues.put(COVERED_STATEMENTS, new Counter(-1));
    myPrevStats = prevStats != null ? prevStats : NULL_STATS;
  }

  public void incrementTotalClasses(int count) {
    incrementKey(TOTAL_CLASSES, count);
  }

  public void incrementCoveredClasses(int count) {
    incrementKey(COVERED_CLASSES, count);
  }

  public void incrementMethods(@Nullable final Entry e) {
    if (e == null) return;
    incrementTotalMethods(e.getTotal());
    incrementCoveredMethods(e.getCovered());
  }

  public void incrementBlocks(@Nullable final Entry e) {
    if (e == null || e.getTotal() <= 0) return;
    incrementTotalBlocks(e.getTotal());
    incrementCoveredBlocks(e.getCovered());
  }

  public void incrementLines(@Nullable final Entry e) {
    if (e == null || e.getTotal() <= 0) return;
    incrementTotalLines(e.getTotal());
    incrementCoveredLines(e.getCovered());
  }

  public void incrementStatements(@Nullable final Entry e) {
    if (e == null || e.getTotal() <= 0) return;
    incrementTotalStatements(e.getTotal());
    incrementCoveredStatements(e.getCovered());
  }

  public void incrementTotalMethods(int count) {
    incrementKey(TOTAL_METHODS, count);
  }

  public void incrementCoveredMethods(int count) {
    incrementKey(COVERED_METHODS, count);
  }

  public void incrementTotalBlocks(int count) {
    incrementKey(TOTAL_BLOCKS, count);
  }

  public void incrementCoveredBlocks(int count) {
    incrementKey(COVERED_BLOCKS, count);
  }

  public void incrementTotalLines(int count) {
    incrementKey(TOTAL_LINES, count);
  }

  public void incrementCoveredLines(int count) {
    incrementKey(COVERED_LINES, count);
  }

  public void incrementCoveredStatements(int count) {
    incrementKey(COVERED_STATEMENTS, count);
  }

  public void incrementTotalStatements(int count) {
    incrementKey(TOTAL_STATEMENTS, count);
  }

  private void incrementKey(String key, int count) {
    if (count < 0) return;
    Counter cnt = myValues.get(key);
    if (cnt.value() < 0) {
      cnt.set(0);
    }
    cnt.increment(count);
  }

  private int value(String key) {
    return myValues.get(key).value();
  }

  public StatEntry getClassStats() {
    return new StatEntry(value(TOTAL_CLASSES), value(COVERED_CLASSES), myPrevStats.getClassStats());
  }

  public StatEntry getMethodStats() {
    return new StatEntry(value(TOTAL_METHODS), value(COVERED_METHODS), myPrevStats.getMethodStats());
  }

  public StatEntry getBlockStats() {
    return new StatEntry(value(TOTAL_BLOCKS), value(COVERED_BLOCKS), myPrevStats.getBlockStats());
  }

  public StatEntry getLineStats() {
    return new StatEntry(value(TOTAL_LINES), value(COVERED_LINES), myPrevStats.getLineStats());
  }

  public StatEntry getStatementStats() {
    return new StatEntry(value(TOTAL_STATEMENTS), value(COVERED_STATEMENTS), myPrevStats.getStatementStats());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    for (Map.Entry<String, Counter> entry : myValues.entrySet()) {
      sb.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
    }
    return "CoverageStatisticsBean{" + sb + '}';
  }

  private static class Counter {
    private int myCounter;

    private Counter(final int counter) {
      myCounter = counter;
    }

    public void set(int val) {
      myCounter = val;
    }

    public int value() {
      return myCounter;
    }

    public void increment(int count) {
      myCounter += count;
    }

    @Override
    public String toString() {
      return "Counter: " + myCounter;
    }
  }
}
