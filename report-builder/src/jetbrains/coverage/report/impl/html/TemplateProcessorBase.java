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

  protected TemplateProcessorBase(String resourceBundleName, boolean includeModule, @NotNull FileSystem fs) {
    myResourceBundleName = resourceBundleName;
    myIncludeModule = includeModule;
    myFS = fs;
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
