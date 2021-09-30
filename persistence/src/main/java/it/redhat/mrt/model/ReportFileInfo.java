package it.redhat.mrt.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;

public class ReportFileInfo {
    
    public String month;
    public String creationTime;
    public String size;
    public int version;
    public String name;

    public static List<ReportFileInfo> findFiles(String path) throws IOException {

        List<ReportFileInfo> list = new ArrayList<ReportFileInfo>();

        File basedir = new File(path);
        if (!basedir.isDirectory()) {
            throw new IllegalArgumentException("path must be a directory!");
        }

        File[] files = basedir.listFiles((dir, name) -> name.endsWith(".pdf"));
        for (File file : files) {
            Path p = Paths.get(file.getAbsolutePath());
            BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
            ReportFileInfo info = new ReportFileInfo();
            info.name = file.getName();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            info.creationTime = sdf.format(attr.creationTime().toMillis());
            info.size = "" + attr.size();
            String[] parts = file.getName().substring(0, file.getName().length() - 4).split("_");
            int monthNumber = Integer.parseInt(parts[2]);
            info.month = Month.of(monthNumber).getDisplayName(TextStyle.FULL, Locale.getDefault());
            int verNumber = Integer.parseInt(parts[3]);
            info.version = verNumber;
            list.add(info);
        }
        return list;
    }

}
