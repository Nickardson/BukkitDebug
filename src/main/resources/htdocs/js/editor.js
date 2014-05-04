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

/**
 * Turns an object into a list of data.
 * @param data The data to turn into an HTML tree.
 * @param singlekey (Optional) If the given data is not an array or object, this is the key used.
 * @returns {HTMLElement}
 */
function json2html(data, singlekey) {
    if (typeof(data) == 'object') {
        var ul = $('<ul>');
        for (var i in data) {
            if (i == "displaytype") {
                continue;
            }

            if (data.hasOwnProperty(i)) {
                var li = $('<li data-key="' + String(i) + '">'),
                    key = $('<span class="json-key">' + i + '</span>'),
                    json = json2html(data[i]);

                li.append(key);

                if (typeof data[i] == "object") {
                    if (data[i].displaytype) {
                        li.append('<span class="json-type"> (' + data[i].displaytype + ')</span>');
                    }
                }

                li.append(json);
                ul.append(li);
            }
        }
        return ul;
    } else {
        return $('<span>' + (singlekey ? '<span class="json-key">'+singlekey+'</span>' : '') + ': ' + data + '</span>');
    }
}

/**
 * Turns a group of lists into a collapsable group.
 * @param obj The HTML element to convert.
 * @returns {*} The object passed, for chaining.
 * @tutorial
 * collapsify(json2html({
     *    "x": [1, 2, 3],
     *    "y": {"a": "Alligator", "b": "Bear", "c": "Cat"},
     *    "z": "String",
     *    "w": [
     *        [[1,2,3], [4,5,6], [7,8,[9,10,11]]],
     *        ["a", "b", "c"]
     *    ]
     * }).appendTo("#consoleoutput")).on("expanded",function(e, key) {
     *    console.log("expanded", key);
     * });
 */
function collapsify(obj) {
    obj.find('li:has(ul)')
        .click( function(event) {
            if (this == event.target) {
                $(this).toggleClass('collapsed');
                $(this).children('ul').toggle('medium');
                $(this).trigger($(this).hasClass('collapsed') ? "collapsed" : "expanded", [$(this).data("key"), $(this)]);
            }
            return false;
        }).addClass("collapsable collapsed").children('ul').css("display", "none");
    return obj;
}

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