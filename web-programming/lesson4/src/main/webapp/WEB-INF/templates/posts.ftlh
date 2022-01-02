<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#list posts as p>
        <#if user_id?has_content && p.userId == user_id>
            <@c.post p=p short=true insideSidebar=false/>
        <#else>
            No posts
            <#break>
        </#if>
    </#list>
</@c.page>