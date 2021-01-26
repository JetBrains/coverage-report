package jetbrains.coverage.report.impl.html.paths;

import org.jetbrains.annotations.Nullable;

/**
* @author Eugene Petrenko (eugene.petrenko@gmail.com)
*         Date: 27.02.11 18:41
*/
public class NamespaceInfo {
  private final String myNamespace;

  public NamespaceInfo(String namespace) {
    myNamespace = namespace;
  }

  @Nullable
  public String getNamespace() {
    return myNamespace;
  }
}
