<#import "commons.ftlh" as c>

<@c.page>
    <#if user??>
        <div>${user.id}</div>
        <div>${user.login}</div>
        <div>${user.creationTime}</div>
    <#else>
        <h1>No such user</h1>
    </#if>
</@c.page>
