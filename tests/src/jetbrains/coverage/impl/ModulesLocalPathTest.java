package jetbrains.coverage.impl;

import jetbrains.coverage.BaseTestCase;
import jetbrains.coverage.html.MockClassInfo;
import jetbrains.coverage.report.ClassInfo;
import jetbrains.coverage.report.impl.html.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         14.10.10 16:29
 */
public class ModulesLocalPathTest extends BaseTestCase {
  private LocalGeneratorPaths myPaths;

  @Before
  public void setup() throws IOException {
    myPaths = new ModulesLocalPaths(myTempFiles.createTempDir());
  }

  @Test
  public void test_modulesIndex() {
    for (SortOption sort : SortOption.values()) {
      Assert.assertEquals(sort.getIndexFileName(), myPaths.getModulesIndexPath(sort).getModulesIndexPath(sort));
      Assert.assertEquals(".", myPaths.getModulesIndexPath(sort).getResourcesPath());
    }
  }

  @Test
  public void test_namespacesIndex() {
    for (SortOption sort : SortOption.values()) {
      final ModuleInfo mi = new ModuleInfo("aaa");
      Assert.assertEquals("..", myPaths.getNamespacesIndexPath(mi, sort).getResourcesPath());
      Assert.assertEquals(sort.getIndexFileName(), myPaths.getNamespacesIndexPath(mi, sort).getNamespacesIndexPath(mi, sort));
    }

  }

  @Test
  public void test_classesesIndex() {
    for (SortOption sort : SortOption.values()) {
      final ModuleInfo mi = new ModuleInfo("aaa");
      final String ns = "namespace";
      Assert.assertEquals("../..", myPaths.getClassesIndexPath(mi, ns, sort).getResourcesPath());
      Assert.assertEquals("../" + sort.getIndexFileName(), myPaths.getClassesIndexPath(mi, ns, sort).getNamespacesIndexPath(mi, sort));
      Assert.assertEquals(sort.getIndexFileName(), myPaths.getClassesIndexPath(mi, ns, sort).getClassesIndexPath(mi, ns, sort));
    }
  }

  @Test
  public void test_sources() {
    for (SortOption sort : SortOption.values()) {
      final ModuleInfo mi = new ModuleInfo("aaa");
      final String ns = "namespace";
      final ClassInfo ci = new MockClassInfo("uuu");
      Assert.assertEquals("../../..", myPaths.getClassCoveragePath(mi, ns, ci).getResourcesPath());
      Assert.assertEquals("../../" + sort.getIndexFileName(), myPaths.getClassCoveragePath(mi, ns, ci).getNamespacesIndexPath(mi, sort));
      Assert.assertEquals("../" + sort.getIndexFileName(), myPaths.getClassCoveragePath(mi, ns, ci).getClassesIndexPath(mi, ns, sort));
      Assert.assertEquals("source-1.html", myPaths.getClassCoveragePath(mi, ns, ci).getClassCoveragePath(mi, ns, ci));
    }
  }

  @Test
  public void test_namespacePathFromModuleIndex() {
    for (int i = 0; i < 5; i++) {
      final ModuleInfo mi = new ModuleInfo("aaa");
      final String ns = "namespace";

      final GeneratorPaths p1 = myPaths.getModulesIndexPath(SortOption.SORT_BY_NAME);
      final String p2 = p1.getClassesIndexPath(mi, ns, SortOption.SORT_BY_NAME);

      Assert.assertEquals("mod-1/ns-1/index.html", p2);
    }
  }
}
