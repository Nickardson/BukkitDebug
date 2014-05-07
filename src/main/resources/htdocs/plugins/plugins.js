$(function () {
    $("#navbar").load("/ajax/navbar.html");

    $.ajax({
        url: "/js/server/plugins.js",
        dataType: "text"
    }).done(function (code) {
        $("#plugins").load("/eval/", {
            "code": code
        });
    });

    function pluginAction(name, action) {
        $.ajax({
            url: "/api/plugin/",
            data: {
                name: name,
                action: action
            }
        }).done(function () {
            window.location.reload(true);
        });
    }

    $("body").on("click", ".plugin-toggle", function () {
        pluginAction($(this).data("plugin"), "toggle");
    }).on("click", ".plugin-reload", function () {
        pluginAction($(this).data("plugin"), "reload");
    }).on("click", ".plugin-query", function () {
        var btn = $(this).addClass("disabled");
        $.ajax({
            url: "/js/server/plugin_query.js",
            dataType: "text"
        }).done(function (code) {
            $("#plugin-info").load("/eval/", {
                "code": code,
                "name": btn.data("plugin")
            }, function () {
                btn.removeClass("disabled");
                $("#plugin-info-modal").modal();
            });
        });
    });

    $("#userplugin").change(function (e) {
        console.log("changed file", e);
        var file = $(this)[0].files[0];
        $("#userpluginName").html(file.name);

        var size = file.size;
        var units = ["bytes", "kb", "mb", "gb", "tb"],
            unitDepth = 0;
        while (size > 1024) {
            size = size / 1024;
            unitDepth++;
        }
        $("#userpluginSize").html(Math.floor(size) + " " + units[unitDepth]);
    }).bootstrapFileInput();
});