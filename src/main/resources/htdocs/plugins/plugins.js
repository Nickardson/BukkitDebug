$(function () {
    $("#navbar").load("/ajax/navbar.html");

    $("#plugins").load("/api/run/", {
        "file": 'js/server/plugins.js'
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
            url: "/api/run/",
            dataType: "text",
            data: {
                file: 'js/server/plugin_query.js',
                name: btn.data("plugin")
            },
            success: function (data) {
                btn.removeClass("disabled");
                $("#plugin-info").html(data);
                $("#plugin-info-modal").modal();
            }
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