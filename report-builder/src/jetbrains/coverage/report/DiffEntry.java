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
