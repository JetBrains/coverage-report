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

package jetbrains.coverage.report.impl.html;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Template;
import jetbrains.coverage.report.ReportGenerationFailedException;
import jetbrains.coverage.report.impl.IOUtil;
import jetbrains.coverage.report.impl.html.fs.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
* @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
*         14.10.10 12:51
*/
public abstract class TemplateProcessorBase implements TemplateProcessor {
  private final String myResourceBundleName;
  private final boolean myIncludeModule;
  private final FileSystem myFS;
  private final String myReportTitle;
  private final String myCharSet;

  protected TemplateProcessorBase(String resourceBundleName, boolean includeModule, @NotNull FileSystem fs, String reportTitle, String charSet) {
    myResourceBundleName = resourceBundleName;
    myIncludeModule = includeModule;
    myFS = fs;
    myReportTitle = reportTitle;
    myCharSet = charSet;
  }

  protected abstract String getFooterText();

  @SuppressWarnings({"unchecked"})
  public void renderTemplate(Map<String, Object> params, @NotNull GeneratorPaths paths) throws IOException {
    final HashMap map = new HashMap();
    map.put("resources", new ResourceBundleModel(getResourceBundle(), new BeansWrapper()));
    map.put("generateDate", new Date());
    map.putAll(params);
    map.put("paths", paths);
    map.put("sort_option_sort_by_name", SortOption.SORT_BY_NAME);
    map.put("sort_option_none", SortOption.NONE);
    map.put("include_modules", myIncludeModule);
    map.put("footerTextHTML", getFooterText());
    map.put("reportTitle", myReportTitle);
    map.put("charset", myCharSet);
    createFileFromTemplate(createTemplate(), map, paths.getReportFileName());
  }

  @NotNull
  protected abstract Template createTemplate() throws IOException;

  private void createFileFromTemplate(@NotNull Template tpl, @NotNull Map model, @NotNull File file) {
    OutputStream fos = null;
    try {
      fos = myFS.openFile(file);

      Writer writer = new OutputStreamWriter(fos);
      tpl.process(model, writer);
    } catch (Throwable e) {
      throw new ReportGenerationFailedException("Failed to generate file: " + file.getAbsolutePath() + ". " + e.getMessage(), e);
    } finally {
      IOUtil.close(fos);
    }
  }

  private ResourceBundle getResourceBundle() {
    return ResourceBundle.getBundle(myResourceBundleName);
  }
}
