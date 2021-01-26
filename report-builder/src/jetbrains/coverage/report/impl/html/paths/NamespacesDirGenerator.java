package jetbrains.coverage.report.impl.html.paths;

import jetbrains.coverage.report.impl.html.SortOption;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
* @author Eugene Petrenko (eugene.petrenko@gmail.com)
*         Date: 27.02.11 18:41
*/
public class NamespacesDirGenerator extends NamesGenerator<NamespaceInfo, ClassesDirGenerator> {
  private final File myBase;

  public NamespacesDirGenerator(@NotNull File base) {
    myBase = base;
  }

  @NotNull
  @Override
  protected ClassesDirGenerator createV(long id) {
    return new ClassesDirGenerator(new File(myBase, "ns-" + Long.toHexString(id)));
  }

  @NotNull
  @Override
  protected Object makeKey(@NotNull NamespaceInfo namespaceInfo) {
    final String namespace = namespaceInfo.getNamespace();
    return namespace == null ? ")(*&^%$#@!___EMPTY_NAMESPACE_NAME__)(*&^%$#@!" :  namespace;
  }

  @NotNull
  public File getBase() {
    return myBase;
  }

  @NotNull
  public File getNamespaceIndexPath(@NotNull SortOption order) {
    return new File(myBase, order.getIndexFileName());
  }

}
