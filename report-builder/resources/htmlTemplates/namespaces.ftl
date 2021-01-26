<#-- @ftlvariable name="module" type="jetbrains.coverage.report.impl.html.ModuleInfo" -->
<#-- @ftlvariable name="namespaces" type="java.util.Collection<String>" -->
<#-- @ftlvariable name="sortOption" type="jetbrains.coverage.report.impl.html.SortOption" -->
<#include "macros.ftl">

<#assign moduleName><@moduleName module=module/></#assign>
<#assign moduleCaption><#if include_modules>for ${resources['coverage.module']?cap_first}: ${moduleName}</#if></#assign>

<@page title="Summary">
<div class="breadCrumbs">
    <@currentScope/>
    <#if include_modules>
    <a href="${paths.getModulesIndexPath(sortOption)}">all ${resources['coverage.module_plural']}</a>
    <span class="separator">|</span>
    ${moduleName}
    <#else>
    all ${resources['coverage.class_plural']}
    </#if>
</div>

<h1>Overall Coverage Summary ${moduleCaption}</h1>
<#assign overallStats=statsCalculator.getForModule(module.name)>
<@overallStatTable labelName="${resources['coverage.namespace']?cap_first}" labelValue="all ${resources['coverage.class_plural']}" coverageStatistics=overallStats/>

<br/>
<h2>Coverage Breakdown</h2>

<table class="coverageStats">
<#list namespaces as ns>
  <#if ns == namespaces?first>
  <#assign sortDesc=sortOption.descendingOrder>
  <#assign sortByName=sortOption.orderByName()>
  <tr>
    <th class="name  <@sortableCellClass sorted=sortByName sortedDesc=sortDesc/>">
      <@sortableCellLabel label=resources['coverage.namespace']?cap_first sortOption=sortOption.nextOrderByName()/>
    </th>
    <@coverageStatHeaderRow coverageStatistics=statsCalculator.getForNamespace(module.name, ns) sortOption=sortOption/>
  </tr>
  </#if>
  <tr>
    <td class="name"><a href="${paths.getClassesIndexPath(module, ns, sortOption)}"><@namespaceName namespace=ns/></a></td>
    <@coverageStatRow coverageStatistics=statsCalculator.getForNamespace(module.name, ns)/>
  </tr>
</#list>
</table>
</@page>

