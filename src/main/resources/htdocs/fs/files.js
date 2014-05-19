importClass(java.io.File);

var path = args.get("directory");
path = path && String(path[0]) || ".";

var root = new File('.'),
    dir = new File(root, path),
    absPath = root.absolutePath,
    ls = dir.list();

var data = {
    path: path,
    list: []
};

for (var i = 0; i < ls.length; i++) {
    var file = new File(dir, ls[i]);
    data.list.push({
        path: String(file.absolutePath.substr(absPath.length())),
        name: String(file.name),
        size: file.length(),
        directory: file.isDirectory()
    });
}

write(JSON.stringify(data));
