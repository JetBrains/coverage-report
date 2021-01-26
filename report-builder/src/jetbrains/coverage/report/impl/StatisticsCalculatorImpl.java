/*
 * Copyright 2000-2021 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.coverage.report.impl;

import jetbrains.coverage.report.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Pavel.Sher
 */
public class StatisticsCalculatorImpl implements StatisticsCalculator {
  private Map<String, CoverageStatisticsBean> myStats = new HashMap<String, CoverageStatisticsBean>();
  private StatisticsCalculatorImpl myPrevStatsHolder;

  public void compute(@NotNull final CoverageData coverageData) {
    myPrevStatsHolder = null;
    doCompute(coverageData);
  }

  private void doCompute(final CoverageData coverageData) {
    myStats.clear();
    for (ClassInfo cd: coverageData.getClasses()) {
      computeForClass(cd, null);
    }
  }

  public void compute(@NotNull final CoverageData before, @NotNull final CoverageData after) {
    myPrevStatsHolder = new StatisticsCalculatorImpl();
    myPrevStatsHolder.compute(before);
    doCompute(after);
  }

  private void computeForClass(final ClassInfo cd, final ClassInfo outerClass) {
    final Entry classStatements = cd.getStatementStats();
    final Entry classLines = cd.getLineStats();
    final Entry methodStats = cd.getMethodStats();
    final Entry blockStats = cd.getBlockStats();

    for (CoverageStatisticsBean bean : getIncrementLayers(cd, outerClass)) {
      boolean incrementCoveredClasses = false;
      boolean incrementTotalClasses = false;
      if (classLines != null) {
        bean.incrementLines(classLines);

        if (classLines.getCovered() > 0) {
          incrementCoveredClasses = true;
        }
        if (classLines.getTotal() > 0) {
          incrementTotalClasses = true;
        }
      }

      if (classStatements != null) {
        bean.incrementStatements(classStatements);

        if (classStatements.getCovered() > 0) {
          incrementCoveredClasses = true;
        }
        if (classStatements.getTotal() > 0) {
          incrementTotalClasses = true;
        }
      }

      if (incrementCoveredClasses) {
        bean.incrementCoveredClasses(1);
      }

      if (incrementTotalClasses) {
        bean.incrementTotalClasses(1);
      }

      bean.incrementMethods(methodStats);
      bean.incrementBlocks(blockStats);
    }

    Collection<ClassInfo> innerClasses = cd.getInnerClasses();
    if (innerClasses != null) {
      for (ClassInfo inner: innerClasses) {
        computeForClass(inner, outerClass == null ? cd : outerClass);
      }
    }
  }

  private List<CoverageStatisticsBean> getIncrementLayers(ClassInfo cd, ClassInfo outerClass) {
    return Arrays.asList(
            getOverallStats(),
            getForModule(cd.getModule()),
            getForNamespace(cd.getModule(), cd.getNamespace()),
            getForClass(cd),
            getForClassWithInnerClasses(outerClass == null ? cd : outerClass)
    );
  }

  private CoverageStatisticsBean getStatistics(final String key) {
    CoverageStatisticsBean stats = myStats.get(key);
    if (stats == null) {
      CoverageStatistics prevStats = myPrevStatsHolder == null ? null : myPrevStatsHolder.getStatistics(key);
      stats = new CoverageStatisticsBean(prevStats);
      myStats.put(key, stats);
    }
    return stats;
  }

  @NotNull
  public CoverageStatisticsBean getForClass(@NotNull ClassInfo classInfo) {
    return getStatistics(makeModuleKey(classInfo.getModule()) + "*" + makeKey(classInfo));
  }

  @NotNull
  public CoverageStatisticsBean getForClassWithInnerClasses(@NotNull final ClassInfo classInfo) {
    return getStatistics(makeModuleKey(classInfo.getModule()) + "*" + makeKey(classInfo) + "#allInner");
  }

  @NotNull
  public CoverageStatisticsBean getForNamespace(@Nullable String module, @NotNull String namespace) {
    return getStatistics(makeModuleKey(module) + "$$" + namespace);
  }

  @NotNull
  public CoverageStatisticsBean getForModule(@Nullable String module) {
    return getStatistics(makeModuleKey(module));
  }

  @NotNull
  public CoverageStatisticsBean getOverallStats() {
    return getStatistics(getOverallStatKey());
  }

  private String makeKey(final ClassInfo classInfo) {
    return classInfo.getFQName();
  }

  private String makeModuleKey(final String module) {
    return "$Module$" + module + "$%$";
  }

  private String getOverallStatKey() {
    return "<overall>";
  }
}
