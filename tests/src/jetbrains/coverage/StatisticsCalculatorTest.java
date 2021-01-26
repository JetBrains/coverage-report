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

package jetbrains.coverage;

import jetbrains.coverage.html.MockClassInfo;
import jetbrains.coverage.html.MockCoverageData;
import jetbrains.coverage.report.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

/**
 * @author Pavel.Sher
 */
public class StatisticsCalculatorTest extends Assert {
  private StatisticsCalculator myCalculator;

  @Before
  public void setUp() {
    myCalculator = ReportBuilderFactory.createStatisticsCalculator();
  }

  @Test
  public void test_empty_coverage() {
    MockCoverageData mcd = new MockCoverageData();
    myCalculator.compute(mcd);

    assertTrue(myCalculator.getOverallStats().getClassStats().getTotal() < 0);
    assertTrue(myCalculator.getForNamespace(null, "ns").getClassStats().getTotal() < 0);
  }

  @Test
  public void test_class_stats() {
    MockCoverageData coverageData = new MockCoverageData();
    MockClassInfo cl1 = new MockClassInfo("com.pkg.Class1");
    MockClassInfo cl2 = new MockClassInfo("com.pkg.Class2");
    MockClassInfo cl3 = new MockClassInfo("Class3");
    cl1.setLines(new Entry(1, 0));
    cl2.setLines(new Entry(1, 1));
    cl3.setLines(new Entry(1, 0));

    cl1.setMethods(new Entry(1, 0));
    cl2.setMethods(new Entry(1, 1));
    cl3.setMethods(new Entry(1, 0));
    coverageData.addClass(cl1);
    coverageData.addClass(cl2);
    coverageData.addClass(cl3);

    myCalculator.compute(coverageData);
    assertStatistics(myCalculator.getOverallStats(), new int[][] {
            {3, 1},
            {3, 1},
            {3, 1},
    });
    assertStatistics(myCalculator.getForNamespace(null,"com.pkg"), new int[][] {
            {2, 1},
            {2, 1},
            {2, 1},
    });
    assertStatistics(myCalculator.getForClass(cl1), new int[][] {
            null, //{-1, -1},
            {1, 0},
            {1, 0},
    });
    assertStatistics(myCalculator.getForClass(cl2), new int[][] {
            null, //{-1, -1},
            {1, 1},
            {1, 1},
    });
    assertStatistics(myCalculator.getForClass(cl3), new int[][] {
            null, //{-1, -1},
            {1, 0},
            {1, 0},
    });
    assertStatistics(myCalculator.getForNamespace(null, ""), new int[][] {
            {1, 0},
            {1, 0},
            {1, 0},
    });
  }

  @Test
  public void test_class_with_inner_classes() {
    MockCoverageData coverageData = new MockCoverageData();
    MockClassInfo cl1 = new MockClassInfo("com.pkg.Class1");
    MockClassInfo inner = new MockClassInfo("com.pkg.Class1$Inner");

    cl1.setLines(new Entry(1, 1));
    cl1.setMethods(new Entry(1, 1));

    inner.setLines(new Entry(2, 1));
    inner.setMethods(new Entry(2, 1));

    cl1.setInnerClasses(Collections.<ClassInfo>singletonList(inner));
    coverageData.addClass(cl1);

    myCalculator.compute(coverageData);
    assertStatistics(myCalculator.getOverallStats(), new int[][] {
            {2, 2},
            {3, 2},
            {3, 2},
    });
    assertStatistics(myCalculator.getForClass(cl1), new int[][] {
            {1, 1},
            {1, 1},
            {1, 1},
    });
    assertStatistics(myCalculator.getForClassWithInnerClasses(cl1), new int[][] {
            {2, 2},
            {3, 2},
            {3, 2},
    });
    assertStatistics(myCalculator.getForClass(inner), new int[][] {
            {1, 1},
            {2, 1},
            {2, 1},
    });
  }

  private void assertStatistics(CoverageStatistics st, int[][] expected) {
    System.out.println("st = " + st);
    final int[] e2 = expected[2];
    if (e2 != null) {
      assertEquals(e2[0], st.getLineStats().getTotal());
      assertEquals(e2[1], st.getLineStats().getCovered());
    }

    final int[] e1 = expected[1];
    if (e1 != null) {
      assertEquals(e1[0], st.getMethodStats().getTotal());
      assertEquals(e1[1], st.getMethodStats().getCovered());
    }

    final int[] e0 = expected[0];
    if (e0 != null) {
      assertEquals(e0[0], st.getClassStats().getTotal());
      assertEquals(expected[0][1], st.getClassStats().getCovered());
    }

  }
}
