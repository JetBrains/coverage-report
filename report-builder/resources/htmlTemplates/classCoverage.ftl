<#-- @ftlvariable name="sortOption" type="jetbrains.coverage.report.impl.html.SortOption" -->
<#-- @ftlvariable name="module" type="jetbrains.coverage.report.impl.html.ModuleInfo" -->
<#-- @ftlvariable name="namespace" type="java.lang.String" -->
<#-- @ftlvariable name="classDataBean" type="jetbrains.coverage.report.impl.ClassDataBean" -->
<#include "macros.ftl">

<#assign ns><@namespaceName namespace = classDataBean.namespace/></#assign>
<#assign className="${classDataBean.name?html}">
<#if className?length = 0><#assign className="&lt;empty ${resources['coverage.class']} name&gt;"></#if>

<#macro classStat classDataBean>
<h2>${resources['coverage.class']?cap_first}: ${className}</h2>
<@overallStatTable labelName="${resources['coverage.class']?cap_first}" labelValue="${className}" coverageStatistics=statsCalculator.getForClass(classDataBean.classData)/>
</#macro>

<@page title="${className}">
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
    <a href="${paths.getClassesIndexPath(module, namespace, sortOption)}">${ns}</a>
</div>

<h1>Coverage Summary for ${resources['coverage.class']?cap_first}: ${className} (${ns})</h1>

<table class="coverageStats">
<#if classDataBean.innerClasses?size == 0>
<tr>
  <th class="name">${resources['coverage.class']?cap_first}</th>
  <@coverageStatHeaderRow coverageStatistics=statsCalculator.getForClass(classDataBean.classData) />
</tr>
<tr>
  <td class="name">${className}</td>
  <@coverageStatRow coverageStatistics=statsCalculator.getForClass(classDataBean.classData)/>
</tr>
</#if>

<#if classDataBean.innerClasses?size &gt; 0>
<tr>
  <th class="name">${resources['coverage.class']?cap_first}</th>
  <@coverageStatHeaderRow coverageStatistics=statsCalculator.getForClassWithInnerClasses(classDataBean.classData) showForClass=false/>
</tr>
<#assign classStat=statsCalculator.getForClass(classDataBean.classData)/>
<#if classStat.lineStats.total &gt;= 0>
<tr>
  <td class="name">${className}</td>
  <@coverageStatRow coverageStatistics=classStat showForClass=false/>
</tr>
</#if>
<#list classDataBean.innerClasses as innerClass>
  <tr>
    <td class="name">${innerClass.name?html}</td>
    <@coverageStatRow coverageStatistics=statsCalculator.getForClass(innerClass.classData) showForClass=false/>
  </tr>
</#list>
<tr>
  <td class="name"><strong>Total</strong></td>
  <@coverageStatRow coverageStatistics=statsCalculator.getForClassWithInnerClasses(classDataBean.classData) showForClass=false/>
</tr>
</#if>
</table>

<br/>
<br/>

<#assign files = classDataBean.files/>
<#if files?size == 0>
  <div class="sourceCode">Source code is not available<br /></div>
<#else>
<#list files as file>
<#assign lines = file.lines/>
<#if file.caption?has_content><h2>${file.caption}</h2></#if>
<pre>
<div class="sourceCode" id="sourceCode"><#list lines as lineBean
><#assign class=""
><#if lineBean.executable
><#switch lineBean.coverage
><#case "NONE"><#assign class="nc"><#break
><#case "FULL"><#assign class="fc"><#break
><#case "PARTIAL"><#assign class="pc"><#break
></#switch
></#if
><#if class?length &gt; 0><b class="${class}"></#if><i class="no-highlight">${lineBean.lineNum}</i>&nbsp;${lineBean.sourceCode?xhtml}<#if class?length &gt; 0></b></#if>
</#list>
</div>
</pre>
</#list>
</#if>
</@page>
