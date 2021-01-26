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

package jetbrains.coverage.report;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         19.10.10 13:05
 */
public class Entry implements Comparable<Entry>{
  private final int myTotal;
  private final int myCovered;

  public Entry(int total, int covered) {
    myTotal = total;
    myCovered = total > 0 && covered < 0 ? 0 : covered;
  }

  /**
   * Returns total value. If value is {@literal <} 0 then such statentry should be ignored and should not affect overall statistics
   * @return total value
   */
  public int getTotal() {
    return myTotal;
  }

  /**
   * Returns covered value. If value is {@literal <} 0 then such statentry should be ignored and should not affect overall statistics
   * @return covered value
  */
  public int getCovered() {
    return myCovered;
  }

  /**
   * Computes coverage percent. If returned percent is {@literal <} 0 then this statentry should be ignored.
   * @return see above
   */
  public float getPercent() {
    if (getTotal() <= 0 || getCovered() < 0) return -1;
    return 100.0f * Math.max(0.0f, Math.min(1.0f, (float)getCovered() / getTotal()));
  }

  /**
   * Returns negative value if coverage percent of this stat entry is less then percent of the specified stat entry
   * @param o stat entry to compare with
   * @return see above
   */
  public int compareTo(final Entry o) {
    return (int)Math.signum(getPercent() - o.getPercent());
  }

  @Override
  public String toString() {
    return "Entry{" +
            "myTotal=" + myTotal +
            ", myCovered=" + myCovered +
            '}';
  }
}
