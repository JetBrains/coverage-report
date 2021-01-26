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
