var path = args.get('path')[0];

var scanner = new java.util.Scanner(new java.io.File(path)).useDelimiter('\\Z');

if (scanner.hasNext()) {
    write(scanner.next());
}
scanner.close();
