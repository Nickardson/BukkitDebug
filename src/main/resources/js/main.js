// Helpful classes.
var BukkitDebug = com.nickardson.bukkitdebug.BukkitDebug.getPlugin(com.nickardson.bukkitdebug.BukkitDebug);
var Bukkit = org.bukkit.Bukkit;

function write(string) {
    output.write(String(string));
}

function print(args) {
    args = String(args) + "\n";
    write(args.replace("\n", "<br/>"));
}

function stdout(args) {
    if (args == undefined) {
        args = "undefined";
    }
    java.lang.System.out.println(String(args));
}

/**
 * Writes an HTML table.
 * @param table The table to write out.
 * @param header (Optional) The header of the table.
 * @example
 * writeTable([
 ["Nickardson", "world"],
 ["jeb_", "nether"],
 ],
 ["Username", "World"]);
 */
function writeTable(table, header) {
    write('<table class="table">');
    if (header) {
        write('<tr>');
        header.forEach(function (head) {
            write('<th>' + head + '</th>');
        });
        write('</tr>');
    }

    table.forEach(function (row) {
        write('<tr>');
        row.forEach(function (column) {
            write('<td>' + column + '</td>');
        });
        write('</tr>');
    });
    write('</table>');
}

/**
 * Writes an HTML list.
 * @param list The list of subelements.
 * @example
 * writeList([
 'A',
 'B',
 'C'
 ]);
 */
function writeList(list) {
    write('<ul>');
    list.forEach(function (element) {
        write('<li>' + element + '</li>');
    });
    write('</ul>');
}

function writeCollapsable(title, contents) {
    var randomID = "id" + String(Math.random()).substr(2);
    write('<div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#'+randomID+'">');
    write(title);
    write('</a></h4></div><div id="'+randomID+'" class="panel-collapse collapse out"><div class="panel-body">');
    if (typeof contents == "function") {
        contents();
    } else {
        write(contents);
    }
    write('</div></div>');
}