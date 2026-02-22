import java.nio.charset.StandardCharsets;

public class CsvExporter extends Exporter {

    @Override
    protected ExportResult doExport(ExportRequest req) {
        // Proper CSV escaping instead of lossy replacement
        String title = escape(req.title);
        String body = escape(req.body);

        String csv =
            "title,body\n" +
            title + "," + body + "\n";

        return new ExportResult(
            "text/csv",
            csv.getBytes(StandardCharsets.UTF_8)
        );
    }

    private String escape(String s) {
        // standard CSV escaping
        if (s.contains(",") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}