package jetbrains.coverage.report.impl;

import org.jetbrains.annotations.Nullable;

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 *         09.11.10 16:59
 */
public interface FooterInfos {
  @Nullable
  String getModulesIndexFooterHTML();

  @Nullable
  String getNamespacesIndexFooterHTML();

  @Nullable
  String getClassesIndexFooterHTML();

  @Nullable
  String getSourceFooterHTML();
}
