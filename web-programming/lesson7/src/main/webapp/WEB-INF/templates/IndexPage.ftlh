<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <template>
            <article>
                <div class="title"></div>
                <div class="information"></div>
                <div class="body"></div>
            </article>
        </template>
    </div>

    <script>
        $(function () {
            window.ajax({
                    action: "findAll"
                },
                function (response) {
                    response["articles"].forEach(function (article) {
                        const $tr = $($(".articles template").prop("content")).clone();
                        $tr.find(".title").text(article["title"]);
                        $tr.find(".information").text("By " +
                            (response["users"].find(function (user) {
                                return user.id === article["userId"];
                            }))["login"]);
                        $tr.find(".body").text(article["text"]);
                        $(".articles").append($tr);
                    });
                }, undefined, "/article")
        })
    </script>
</@c.page>
