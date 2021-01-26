<#-- @ftlvariable name="module" type="jetbrains.coverage.report.impl.html.ModuleInfo" -->
<#-- @ftlvariable name="namespace" type="java.lang.String" -->
<#-- @ftlvariable name="classes" type="java.util.Collection<jetbrains.coverage.report.ClassInfo>" -->
<#-- @ftlvariable name="sortOption" type="jetbrains.coverage.report.impl.html.SortOption" -->
<#include "macros.ftl">

<#assign title="${namespace}">
<#if title?length = 0><#assign title="&lt;empty ${resources['coverage.namespace']}&gt;"></#if>
<@page title="${title}">

<div class="breadCrumbs">
   <@currentScope/>
    <#if include_modules>
    <a href="${paths.getModulesIndexPath(sortOption)}">all ${resources['coverage.module_plural']}</a>
    <span class="separator">|</span>
    <a href="${paths.getNamespacesIndexPath(module, sortOption)}"><@moduleName module=module/></a>
    <span class="separator">|</span>
    <#else>
    <a href="${paths.getNamespacesIndexPath(module, sortOption)}">all ${resources['coverage.class_plural']}</a>
    <span class="separator">|</span>
    </#if>
    <@namespaceName namespace=namespace/>
</div>

<h1>Coverage Summary for ${resources['coverage.namespace']?cap_first}: ${title}</h1>
<@overallStatTable labelName="${resources['coverage.namespace']?cap_first}" labelValue="${title}" coverageStatistics=statsCalculator.getForNamespace(module.name, namespace)/>

<br/>
<br/>

<table class="coverageStats">
<#list classes as class>
  <#if class == classes?first>
  <#assign sortDesc=sortOption.descendingOrder>
  <#assign sortByName=sortOption.orderByName()>
  <tr>
    <th class="name  <@sortableCellClass sorted=sortByName sortedDesc=sortDesc/>">
      <@sortableCellLabel label=resources['coverage.class']?cap_first sortOption=sortOption.nextOrderByName()/>
    </th>
    <@coverageStatHeaderRow coverageStatistics=statsCalculator.getForClassWithInnerClasses(class) sortOption=sortOption/>
  </tr>
  </#if>
  <tr>
    <td class="name"><a href="${paths.getClassCoveragePath(module, namespace, class)}"><@className clazz=class/></a></td>
    <@coverageStatRow coverageStatistics=statsCalculator.getForClassWithInnerClasses(class)/>
  </tr>
</#list>
</table>

</@page>
