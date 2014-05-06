var editor = ace.edit("editor");
editor.setTheme("ace/theme/monokai");
editor.getSession().setMode("ace/mode/javascript");

editor.load = function(url){
    $.ajax({
        "url": url,
        "dataType": "text"
    }).done(function (src) {
        editor.setValue(src);
        editor.gotoLine(0)
    });
};

function scrollConsoleDown() {
    var out = $("#consoleoutput")[0];
    out.scrollTop = out.scrollHeight;
}

function appendConsole(d) {
    $("<div>").appendTo("#consoleoutput").append(d);
    scrollConsoleDown();
}

editor.commands.addCommand({
    name: "send",
    bindKey: {win: "Ctrl-Enter", mac: "Command-Option-Enter"},
    exec: function () {
        runCode();
    }
});