package jetbrains.coverage.report.impl.html.paths;

import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.impl.html.SortOption;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
* @author Eugene Petrenko (eugene.petrenko@gmail.com)
*         Date: 27.02.11 18:41
*/
public class ClassesDirGenerator extends NamesGenerator<ClassInfo, File> {
  private final File myBase;

  public ClassesDirGenerator(@NotNull final File base) {
    myBase = base;
  }

  @NotNull
  @Override
  protected File createV(long id) {
    return new File(myBase, "sources/source-" + Long.toHexString(id) + ".html");
  }

  @NotNull
  @Override
  protected Object makeKey(@NotNull ClassInfo classInfo) {
    return classInfo.getFQName();
  }

  @NotNull
  public File getClassesIndexPath(@NotNull SortOption order) {
    return new File(myBase, order.getIndexFileName());
  }

  @NotNull
  public File getBase() {
    return myBase;
  }
}
