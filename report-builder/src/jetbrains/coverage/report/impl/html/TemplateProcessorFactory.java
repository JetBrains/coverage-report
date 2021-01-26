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
