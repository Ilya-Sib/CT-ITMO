<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="/" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"},
            {"href": "/users", "name": "Users"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if item.href?starts_with(uri)>
                            <a class="active" href="${item.href}">
                                ${item.name}
                            </a>
                        <#else>
                            <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>
            <@post p true true/>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly>
<#-- @ftlvariable name="user" type="ru.itmo.web.lesson4.model.User" -->
<#-- @ftlvariable name="nameOnly" type="java.lang.Boolean" -->

    <#if nameOnly>
        <a href="/user?handle=${user.handle}">
            ${user.name}
        </a>
    <#else>
        <a href="/user?handle=${user.handle}"
           class="userlink">
            <span style="color: ${user.color.code};
                    font-family: helvetica neue,Helvetica,Arial,sans-serif;
                    font-weight: bold;">
                ${user.name}
            </span>
        </a>
    </#if>
</#macro>

<#macro postlink post isSidebar>
<#-- @ftlvariable name="post" type="ru.itmo.web.lesson4.model.Post" -->
<#-- @ftlvariable name="isTitle" type="java.lang.Boolean" -->
    <a href="/post?post_id=${post.id}">
        <#if isSidebar>
            View all
        <#else>
            ${post.title}
        </#if>
    </a>
</#macro>

<#macro userpostslink id>
<#-- @ftlvariable name="id" type="java.lang.Long" -->
    posts: <a href="/posts?user_id=${id}">${numberOf(posts, "userId", id)}</a>
</#macro>

<#macro post p short insideSidebar>
<#-- @ftlvariable name="post" type="ru.itmo.web.lesson4.model.Post" -->
<#-- @ftlvariable name="short" type="java.lang.Boolean" -->
<#-- @ftlvariable name="insideSidebar" type="java.lang.Boolean" -->
    <#assign user = findBy(users, "id", p.userId)/>
    <article>
        <#if insideSidebar>
            <div class="header"> Post ${p.id} </div>
        <#else>
            <div class="title"> <@postlink p insideSidebar/> </div>
            <div class="information">By <@userlink user false/>, 2 days ago, translation</div>
        </#if>
        <div class="body">
            <#if short>
                ${p.text?truncate(250)}
            <#else>
                ${p.text}
            </#if>
        </div>
        <#if insideSidebar>
            <div class="footer"><@postlink p insideSidebar/></div>
        </#if>
    </article>
</#macro>

<#function findIndexBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item_index/>
        </#if>
    </#list>
</#function>

<#function findBy items key value>
    <#assign index = findIndexBy(items, key, value)>
    <#if index?has_content>
        <#return items[index]/>
    </#if>
</#function>

<#function numberOf items key value>
    <#assign count = 0/>
    <#list items as item>
        <#if item[key] == value>
            <#assign count++/>
        </#if>
    </#list>
    <#return count/>
</#function>