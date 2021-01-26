package jetbrains.coverage.report.impl.html;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
* @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
*         14.10.10 12:48
*/
public interface TemplateProcessor {
  void renderTemplate(Map<String, Object> params, @NotNull GeneratorPaths outputFile) throws IOException;
}
