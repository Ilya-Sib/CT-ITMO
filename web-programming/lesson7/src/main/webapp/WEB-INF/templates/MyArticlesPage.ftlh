<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="my-articles datatable">
        <div class="caption">Articles</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Show/Hide</th>
            </tr>
            </thead>
            <tbody>
            <#list articles as article>
                <tr>
                    <td class="article-id"> ${article.id} </td>
                    <td class="article-title"> ${article.title} </td>
                    <td class="article-hidden">
                        <button data-article-id=${article.id}><#if article.hidden>Show<#else>Hide</#if></button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <script>
        $(".my-articles button").click(function () {
            const button = $(this);

            window.ajax({
                    action: "changeHidden",
                    articleId: $(this).attr("data-article-id"),
                    buttonText: button.text()
                },
                function (response) {
                    if (button.text() === "Hide") {
                        button.text("Show");
                        notify("Article hidden");
                    } else {
                        button.text("Hide");
                        notify("Article opened");
                    }
                })
        })
    </script>
</@c.page>
