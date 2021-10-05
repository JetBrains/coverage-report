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

package jetbrains.coverage.impl;

import com.intellij.rt.coverage.data.ClassData;
import com.intellij.rt.coverage.data.LineData;
import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.StatisticsCalculator;
import jetbrains.coverage.report.idea.IDEACoverageClassInfo;
import jetbrains.coverage.report.impl.StatisticsCalculatorImpl;
import jetbrains.coverage.report.impl.html.SortOption;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class SortTest {
    @Test
    public void test_sort_by_line() {
        final StatisticsCalculator calculator = new StatisticsCalculatorImpl();

        final Comparator<ClassInfo> comparator = SortOption.SORT_BY_LINE.createClassComparator(calculator);
        final String cn1 = "Class1";
        final ClassData cd1 = createClassData(cn1, 2, 1);
        final ClassInfo ci1 = new IDEACoverageClassInfo(cn1, cd1, Collections.<ClassData>emptyList());

        final String cn2 = "Class2";
        final ClassData cd2 = createClassData(cn2, 2, 2);
        final ClassInfo ci2 = new IDEACoverageClassInfo(cn2, cd2, Collections.<ClassData>emptyList());

        assertEquals(-1, comparator.compare(ci1, ci2));
    }

    @Test
    public void test_sort_by_name_when_lines_equal() {
        final StatisticsCalculator calculator = new StatisticsCalculatorImpl();

        final Comparator<ClassInfo> comparator = SortOption.SORT_BY_LINE.createClassComparator(calculator);
        final String cn1 = "Class1";
        final ClassData cd1 = createClassData(cn1, 2, 2);
        final ClassInfo ci1 = new IDEACoverageClassInfo(cn1, cd1, Collections.<ClassData>emptyList());

        final String cn2 = "Class2";
        final ClassData cd2 = createClassData(cn2, 4, 4);
        final ClassInfo ci2 = new IDEACoverageClassInfo(cn2, cd2, Collections.<ClassData>emptyList());

        assertEquals(-1, comparator.compare(ci1, ci2));
    }

    private static ClassData createClassData(String name, int totalLines, int coveredLines) {
        final ClassData data = new ClassData(name);
        final LineData[] lines = new LineData[totalLines];
        for (int i = 0; i < totalLines; i++) {
            lines[i] = new LineData(i, "");
            if (i < coveredLines) lines[i].setHits(1);
        }
        data.setLines(lines);
        return data;
    }
}
