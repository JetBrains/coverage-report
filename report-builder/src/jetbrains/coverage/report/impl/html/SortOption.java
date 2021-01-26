package jetbrains.coverage.report.impl.html;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.CoverageStatistics;
import jetbrains.coverage.report.StatEntry;
import jetbrains.coverage.report.StatisticsCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * @author Pavel.Sher
 */
public enum SortOption {
  NONE,
  SORT_BY_NAME,
  SORT_BY_NAME_DESC,
  SORT_BY_CLASS,
  SORT_BY_CLASS_DESC,
  SORT_BY_METHOD,
  SORT_BY_METHOD_DESC,
  SORT_BY_BLOCK,
  SORT_BY_BLOCK_DESC,
  SORT_BY_LINE,
  SORT_BY_LINE_DESC,
  SORT_BY_STATEMENT,
  SORT_BY_STATEMENT_DESC,
  ;

  @NotNull
  public String getIndexFileName() {
    if (this == SortOption.SORT_BY_NAME || this == NONE) return "index.html";
    return "index_" + name() + ".html";
  }

  @NotNull
  public SortOption inverse() {
    switch (this) {
      default: return NONE;
      case SORT_BY_BLOCK: return SORT_BY_BLOCK_DESC;
      case SORT_BY_BLOCK_DESC: return SORT_BY_BLOCK;

      case SORT_BY_NAME: return SORT_BY_NAME_DESC;
      case SORT_BY_NAME_DESC: return SORT_BY_NAME;

      case SORT_BY_CLASS: return SORT_BY_CLASS_DESC;
      case SORT_BY_CLASS_DESC: return SORT_BY_CLASS;

      case SORT_BY_METHOD: return SORT_BY_METHOD_DESC;
      case SORT_BY_METHOD_DESC: return SORT_BY_METHOD;

      case SORT_BY_LINE: return SORT_BY_LINE_DESC;
      case SORT_BY_LINE_DESC: return SORT_BY_LINE;

      case SORT_BY_STATEMENT: return SORT_BY_STATEMENT_DESC;
      case SORT_BY_STATEMENT_DESC: return SORT_BY_STATEMENT;
    }
  }

  public boolean isDescendingOrder() {
    return name().endsWith("_DESC");
  }

  public Comparator<ClassInfo> createClassComparator(final StatisticsCalculator covStatsCalculator) {
    final boolean desc = isDescendingOrder();
    if (orderByName()) {
        return new Comparator<ClassInfo>() {
          public int compare(final ClassInfo o1, final ClassInfo o2) {
            int result = o1.getName().compareTo(o2.getName());
            return desc ? -result : result;
          }
        };
    }

    return comparator(new Func<ClassInfo>() {
      public CoverageStatistics compute(ClassInfo classInfo) {
        return covStatsCalculator.getForClassWithInnerClasses(classInfo);
      }
    });
  }

  public Comparator<String> createNamespaceComparator(final ModuleInfo module, final StatisticsCalculator covStatsCalculator) {
    final boolean desc = isDescendingOrder();
    final String moduleName = module.getName();

    if (orderByName()) {
        return new Comparator<String>() {
          public int compare(final String o1, final String o2) {
            int result = o1.compareTo(o2);
            return desc ? -result : result;
          }
        };
    }
    return comparator(new Func<String>() {
      public CoverageStatistics compute(String s) {
        return covStatsCalculator.getForNamespace(moduleName, s);
      }
    });
  }

  public Comparator<ModuleInfo> createModulesComparator(final StatisticsCalculator covStatsCalculator) {
    if (orderByName()) {
      return new Comparator<ModuleInfo>() {
          public int compare(final ModuleInfo o1, final ModuleInfo o2) {
            final String n1 = o1.getName();
            final String n2 = o2.getName();
            if (n1 == null && n2 == null) return 0;
            if (n1 != null && n2 == null) return 1;
            if (n1 == null && n2 != null) return -1;

            int result = n1.compareTo(n2);
            return isDescendingOrder() ? -result : result;
          }
        };
    }
    return comparator(new Func<ModuleInfo>() {
      public CoverageStatistics compute(ModuleInfo moduleInfo) {
        return covStatsCalculator.getForModule(moduleInfo.getName());
      }
    });
  }

  private static interface Func<T> {
    CoverageStatistics compute(T t);
  }

  private static interface Selector {
    StatEntry compute(@NotNull CoverageStatistics stat);
  }

  @NotNull
  private Selector getSelector() {
    if (orderByMethod()) {
      return new Selector() {
        public StatEntry compute(@NotNull CoverageStatistics stat) {
          return stat.getMethodStats();
        }
      };
    }

    if (orderByBlock()) {
      return new Selector() {
        public StatEntry compute(@NotNull CoverageStatistics stat) {
          return stat.getBlockStats();
        }
      };
    }

    if (orderByStatement()) {
      return new Selector() {
        public StatEntry compute(@NotNull CoverageStatistics stat) {
          return stat.getStatementStats();
        }
      };
    }

    if (orderByLine()) {
      return new Selector() {
        public StatEntry compute(@NotNull CoverageStatistics stat) {
          return stat.getLineStats();
        }
      };
    }

    if (orderByClass()) {
      return new Selector() {
        public StatEntry compute(@NotNull CoverageStatistics stat) {
          return stat.getClassStats();
        }
      };
    }

    throw new IllegalArgumentException("Failed to get statistics selector for: " + this);
  }

  private <T> Comparator<T> comparator(@NotNull final Func<T> fun) {
    return new Comparator<T>() {
      public int compare(T o1, T o2) {
        return compareStatEntries(
                getSelector().compute(fun.compute(o1)),
                getSelector().compute(fun.compute(o2)),
                isDescendingOrder());
      }

      private int compareStatEntries(StatEntry stat1, StatEntry stat2, boolean desc) {
        int result = stat1.compareTo(stat2);
        if (desc) {
          result = -result;
        }
        return result;
      }
    };
  }

  public boolean orderByLine() {
    return this == SORT_BY_LINE || this == SORT_BY_LINE_DESC;
  }

  public boolean orderByStatement() {
    return this == SORT_BY_STATEMENT || this == SORT_BY_STATEMENT_DESC;
  }

  public boolean orderByMethod() {
    return this == SORT_BY_METHOD || this == SORT_BY_METHOD_DESC;
  }

  public boolean orderByClass() {
    return this == SORT_BY_CLASS || this == SORT_BY_CLASS_DESC;
  }

  public boolean orderByBlock() {
    return this == SORT_BY_BLOCK || this == SORT_BY_BLOCK_DESC;
  }

  public boolean orderByName() {
    return this == SORT_BY_NAME || this == SORT_BY_NAME_DESC;
  }

  public SortOption nextOrderByClass() {
    if (this == NONE) return NONE;
    return orderByClass() ? inverse() : SORT_BY_CLASS;
  }

  public SortOption nextOrderByName() {
    if (this == NONE) return NONE;
    return orderByName() ? inverse() : SORT_BY_NAME;
  }

  public SortOption nextOrderByLine() {
    if (this == NONE) return NONE;
    return orderByLine() ? inverse() : SORT_BY_LINE;
  }

  public SortOption nextOrderByStatement() {
    if (this == NONE) return NONE;
    return orderByStatement() ? inverse() : SORT_BY_STATEMENT;
  }

  public SortOption nextOrderByMethod() {
    if (this == NONE) return NONE;
    return orderByMethod() ? inverse() : SORT_BY_METHOD;
  }

  public SortOption nextOrderByBlock() {
    if (this == NONE) return NONE;
    return orderByBlock() ? inverse() : SORT_BY_BLOCK;
  }
}
