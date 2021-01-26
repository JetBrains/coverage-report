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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel.Sher
 */
public class StringUtil {
  public static final String EMPTY = "";

  public static List<CharSequence> getLines(@NotNull CharSequence text) {
    List<CharSequence> result = new ArrayList<CharSequence>();
    int lineStart = -1;
    int lineEnd = 0;
    for (int i=0; i< text.length(); i++) {
      char c = text.charAt(i);
      boolean addLine = false;
      switch (c) {
        default:
          if (lineStart < 0) { lineStart = i; }
          lineEnd = i+1;
          break;
        case '\r':
          addLine = true;
          Character next = charAtOrNull(i+1, text);
          if (next != null && next == '\n') i++;
          break;
        case '\n':
          Character nextChar = charAtOrNull(i+1, text);
          if (nextChar == null || nextChar != '\r') {
            addLine = true;
          }
          break;
      }

      if (addLine) {
        if (lineStart < 0 || lineStart >= lineEnd ) {
          result.add(EMPTY);
        } else {
          result.add(text.subSequence(lineStart, lineEnd));
        }
        lineStart = -1;
        lineEnd = -1;
      }
    }
    if (lineStart >= 0) {
      result.add(text.subSequence(lineStart, lineEnd));
    }
    return result;
  }

  private static Character charAtOrNull(int idx, CharSequence text) {
    if (idx < text.length()) {
      return text.charAt(idx);
    }
    return null;
  }
}
