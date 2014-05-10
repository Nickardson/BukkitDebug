/**
 * Turns an object into a list of data.
 * @param data The data to turn into an HTML tree.
 * @param singlekey (Optional) If the given data is not an array or object, this is the key used.
 * @returns {HTMLElement}
 */
function json2html(data, singlekey) {
    function inner(obj, singlekey) {
        if (typeof(obj) == 'object') {
            var res = '<ul>';
            for (var i in obj) {
                if (obj.hasOwnProperty(i)) {
                    res += '<li data-key="' + String(i) + '">' +
                        '<span class="json-key">' + i + ': </span>' +
                        inner(obj[i]) +
                        '</li>';
                }
            }
            res += ('</ul>');
            return res;
        } else {
            return '<span>' + (singlekey ? '<span class="json-key">'+singlekey+'</span>' : '') + obj + '</span>';
        }
    }

    return inner(data, singlekey);
}

function formatObject(value) {
    // Colorize java hashcode IDs.
    return String(value).replace(
        /@[0-9a-f]{8}/g,
        '<span class="text-muted">$&</span>');
}

function inspect(object, name) {
    write('<h4>Inspect</h4>');

    if (name != undefined) {
        if (object instanceof java.lang.Object) {
            var f = reflection.getClass(object).getDeclaredField(name);
            f.accessible = true;
            object = f.get(object);
        } else {
            object = object[name];
        }
    }

    write('<code>' + formatObject(object) + '</code> : ');
    write('<code>' + reflection.getClass(object) + '</code>');
}

global.inspect = inspect;
