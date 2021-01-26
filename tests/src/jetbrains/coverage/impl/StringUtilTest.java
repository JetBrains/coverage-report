package jetbrains.coverage.impl;

import jetbrains.coverage.report.impl.StringUtil;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Pavel.Sher
 */
public class StringUtilTest extends Assert {
  @Test
  public void test_getLines() {
    assertEquals(Arrays.asList(""), StringUtil.getLines("\n"));
    assertEquals(Arrays.asList(""), StringUtil.getLines("\r"));
    assertEquals(Arrays.asList(""), StringUtil.getLines("\r\n"));
    assertEquals(Arrays.asList(""), StringUtil.getLines("\n\r"));
    assertEquals(Arrays.asList("", ""), StringUtil.getLines("\r\r"));
    assertEquals(Arrays.asList("", ""), StringUtil.getLines("\n\n"));

    assertEquals(Arrays.asList("aaa", "bbb"), StringUtil.getLines("aaa\r\nbbb"));
    assertEquals(Arrays.asList("aaa", "", "bbb"), StringUtil.getLines("aaa\r\rbbb"));
    assertEquals(Arrays.asList("aaa", "bbb"), StringUtil.getLines("aaa\n\rbbb"));
    assertEquals(Arrays.asList("aaa", "", "bbb"), StringUtil.getLines("aaa\r\r\nbbb"));
    assertEquals(Arrays.asList("aaa", "", "bbb"), StringUtil.getLines("aaa\n\r\rbbb"));
    assertEquals(Arrays.asList("aaa", "bbb"), StringUtil.getLines("aaa\nbbb"));
    assertEquals(Arrays.asList("aaa", "", "bbb"), StringUtil.getLines("aaa\n\nbbb"));
    assertEquals(Arrays.asList("aaa", "", "bbb"), StringUtil.getLines("aaa\r\n\r\nbbb"));
  }
}
