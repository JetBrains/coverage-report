package jetbrains.coverage.report.impl.html;

import com.intellij.openapi.diagnostic.Logger;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Pavel.Sher
 */
public class TemplateFactory {
  private static final Logger LOG = Logger.getInstance(TemplateFactory.class.getName());

  private Template myNamespacesTeamplate;
  private Template myModulesTemplate;
  private Template myNamespaceIndexTemplate;
  private Template myClassTemplate;
  private Template myEmpty;

  public TemplateFactory() throws IOException {
    Configuration configuration = new Configuration();
    configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), "/htmlTemplates"));

    try {
      configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:420, soft:400");
      configuration.setSetting(Configuration.TEMPLATE_UPDATE_DELAY_KEY, "0");
    } catch (TemplateException e) {
      LOG.warn("Failed to set configuration properties: " + e.getMessage(), e);
    }

    myNamespacesTeamplate = configuration.getTemplate("namespaces.ftl");
    myModulesTemplate = configuration.getTemplate("modules.ftl");
    myNamespaceIndexTemplate = configuration.getTemplate("namespaceIndex.ftl");
    myClassTemplate = configuration.getTemplate("classCoverage.ftl");
    myEmpty = configuration.getTemplate("empty.ftl");
  }

  @NotNull
  public Template createEmptyTemplate() throws IOException {
    return myEmpty;
  }

  @NotNull
  public Template createNamespacesPerModuleIndexTemplate() throws IOException {
    return myNamespacesTeamplate;
  }

  @NotNull
  public Template createModulesIndexTemplate() throws IOException {
    return myModulesTemplate;
  }

  @NotNull
  public Template createNamespaceIndexTemplate() throws IOException {
    return myNamespaceIndexTemplate;
  }

  @NotNull
  public Template createClassCoverageTemplate() throws IOException {
    return myClassTemplate;
  }
}
