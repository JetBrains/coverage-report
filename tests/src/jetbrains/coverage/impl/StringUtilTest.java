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
