package jetbrains.coverage.report.impl.html;

import freemarker.template.Template;
import jetbrains.coverage.report.impl.FooterInfos;
import jetbrains.coverage.report.impl.html.fs.FileSystem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 12:51
 */
public class TemplateProcessorFactory {
  private final TemplateFactory myFactory;
  private final String myResourceBundleName;
  private final boolean myIncludeModule;
  private final FooterInfos myFooterInfos;
  private final FileSystem myFS;


  public TemplateProcessorFactory(@NotNull final TemplateFactory factory,
                                  @NotNull final String resourceBundleName,
                                  boolean includeModule,
                                  @NotNull FooterInfos footerText,
                                  @NotNull FileSystem fs) {
    myFactory = factory;
    myResourceBundleName = resourceBundleName;
    myIncludeModule = includeModule;
    myFooterInfos = footerText;
    myFS = fs;
  }

  @NotNull
  public TemplateProcessor createEmptyTemplate() {
    return new TemplateProcessorBase(myResourceBundleName, myIncludeModule, myFS) {
      @Override
      protected String getFooterText() {
        return myFooterInfos.getModulesIndexFooterHTML();
      }

      @NotNull
      @Override
      protected Template createTemplate() throws IOException {
        return myFactory.createEmptyTemplate();
      }
    };
  }

  @NotNull
  public TemplateProcessor createModulesIndexProcessor() {
    return new TemplateProcessorBase(myResourceBundleName, myIncludeModule, myFS) {
      @NotNull
      @Override
      protected Template createTemplate() throws IOException {
        return myFactory.createModulesIndexTemplate();
      }

      @Override
      protected String getFooterText() {
        return myFooterInfos.getModulesIndexFooterHTML();
      }
    };
  }

  @NotNull
  public TemplateProcessor createNamespacesIndexProcessor() {
    return new TemplateProcessorBase(myResourceBundleName, myIncludeModule, myFS) {
      @NotNull
      @Override
      protected Template createTemplate() throws IOException {
        return myFactory.createNamespacesPerModuleIndexTemplate();
      }

      @Override
      protected String getFooterText() {
        return myFooterInfos.getNamespacesIndexFooterHTML();
      }
    };
  }

  @NotNull
  public TemplateProcessor createClassesIndexProcessor() {
    return new TemplateProcessorBase(myResourceBundleName, myIncludeModule, myFS) {
      @NotNull
      @Override
      protected Template createTemplate() throws IOException {
        return myFactory.createNamespaceIndexTemplate();
      }

      @Override
      protected String getFooterText() {
        return myFooterInfos.getClassesIndexFooterHTML();
      }
    };
  }

  @NotNull
  public TemplateProcessor createClassSourceProcessor() {
    return new TemplateProcessorBase(myResourceBundleName, myIncludeModule, myFS) {
      @NotNull
      @Override
      protected Template createTemplate() throws IOException {
        return myFactory.createClassCoverageTemplate();
      }

      @Override
      protected String getFooterText() {
        return myFooterInfos.getSourceFooterHTML();
      }
    };
  }
}
