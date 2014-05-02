var table = [];

Bukkit.pluginManager.plugins.forEach(function (plugin) {
    var name = plugin.name;

    if (name == 'BukkitDebug') {
        table.push([
            name,
            '<span class="label label-info">N/A</span>',
            '<span class="label label-info">N/A</span>',
            '<span class="label label-info">N/A</span>'
        ])
    } else {
        table.push([
            name,
            '<button data-plugin="' + name + '" class="btn btn-success plugin-enable">Enable</button>',
            '<button data-plugin="' + name + '" class="btn btn-danger plugin-disable">Disable</button>',
            '<button data-plugin="' + name + '" class="btn btn-info plugin-query">?</button>'
        ]);
    }
});

writeTable(table, ["Name", "Enable", "Disable", "Check Enabled"]);
