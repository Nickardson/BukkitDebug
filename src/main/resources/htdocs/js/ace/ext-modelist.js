var modes = [];
/**
 * Suggests a mode based on the file extension present in the given path
 * @param {string} path The path to the file
 * @returns {object} Returns an object containing information about the
 *  suggested mode.
 */
function getModeForPath(path) {
    var mode = modesByName.text;
    var fileName = path.split(/[\/\\]/).pop();
    for (var i = 0; i < modes.length; i++) {
        if (modes[i].supportsFile(fileName)) {
            mode = modes[i];
            break;
        }
    }
    return mode;
}

var Mode = function(name, caption, extensions) {
    this.name = name;
    this.caption = caption;
    this.mode = "ace/mode/" + name;
    this.extensions = extensions;
    if (/\^/.test(extensions)) {
        var re = extensions.replace(/\|(\^)?/g, function(a, b){
            return "$|" + (b ? "^" : "^.*\\.");
        }) + "$";
    } else {
        var re = "^.*\\.(" + extensions + ")$";
    }

    this.extRe = new RegExp(re, "gi");
};

Mode.prototype.supportsFile = function(filename) {
    return filename.match(this.extRe);
};

// todo firstlinematch
var supportedModes = {
    BatchFile:   ["bat|cmd"],
    coffee:      ["coffee|cf|cson|^Cakefile"],
    CSS:         ["css"],
    HTML:        ["html|htm|xhtml"],
    INI:         ["ini|conf|cfg|prefs|properties"],
    Java:        ["java"],
    JavaScript:  ["js|jsm"],
    JSON:        ["json"],
    Markdown:    ["md|markdown"],
    Text:        ["txt"],
    YAML:        ["yaml|yml"]
};

var nameOverrides = {
    ObjectiveC: "Objective-C",
    CSharp: "C#",
    golang: "Go",
    C_Cpp: "C/C++",
    coffee: "CoffeeScript",
    HTML_Ruby: "HTML (Ruby)",
    FTL: "FreeMarker"
};
var modesByName = {};
for (var name in supportedModes) {
    var data = supportedModes[name];
    var displayName = (nameOverrides[name] || name).replace(/_/g, " ");
    var filename = name.toLowerCase();
    var mode = new Mode(filename, displayName, data[0]);
    modesByName[filename] = mode;
    modes.push(mode);
}