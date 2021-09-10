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
      System.err.println("Failed to set configuration properties: " + e.getMessage());
      e.printStackTrace();
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
