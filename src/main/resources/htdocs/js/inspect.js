function inspectFields(query) {
    $.ajax({
        url: "/api/run/",
        dataType: "text",
        data: {
            file: 'js/server/jsonReflect.js',
            query: query
        },
        success: function (data) {
            appendConsole(data);
        }
    });
}

/**
 * Turns an object into a list of data.
 * @param data The data to turn into an HTML tree.
 * @param singlekey (Optional) If the given data is not an array or object, this is the key used.
 * @returns {HTMLElement}
 */
function json2html(data, singlekey) {
    var res = "";

    function conv(data, singlekey) {
        if (typeof(data) == 'object') {
            res += ('<ul>');
            for (var i in data) {
                if (i == "displaytype") {
                    continue;
                }

                if (data.hasOwnProperty(i)) {
                    res += ('<li data-key="' + String(i) + '">');
                    res += ('<span class="json-key">' + i + '</span>');
                    res += (conv(data[i]));

                    if (typeof data[i] == "object") {
                        if (data[i].displaytype) {
                            res += ('<span class="json-type"> (' + data[i].displaytype + ')</span>');
                        }
                    }

                    res += ('</li>');
                }
            }
            res += ('</ul>');
        } else {
            res = '<span>' + (singlekey ? '<span class="json-key">'+singlekey+'</span>' : '') + ': ' + data + '</span>';
        }
    }

    conv(data, singlekey);

    return res;
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