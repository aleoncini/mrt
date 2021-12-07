package it.redhat.mrt.service;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import it.redhat.mrt.model.ReportFileInfo;

@QuarkusTest
public class ArchiveServiceTest {

    @ConfigProperty(name = "mrt.reports.dirname")
    String dirname;

    @Inject
    ArchiveService archiveService;
    
    Random random = new Random();
    
    @Test
    public void testGetFileList() throws IOException {

        UUID rhid = UUID.randomUUID();

        Path path = Paths.get(dirname + File.separator + rhid.toString() + File.separator + "2021");

        Files.createDirectories(path);

        List<ReportFileInfo> l = archiveService.getFileList(2021, rhid.toString());

        Assertions.assertEquals(0, l.size());

        File f1 = new File(path.toFile(), "dummy.pdf");
        f1.createNewFile();

        l = archiveService.getFileList(2021, rhid.toString());
        Assertions.assertEquals(0, l.size());

        File f2 = new File(path.toFile(), "136715_2021_11_2.pdf");
        f2.createNewFile();

        l = archiveService.getFileList(2021, rhid.toString());
        Assertions.assertEquals(1, l.size());

        File f3 = new File(path.toFile(), "136715_2021_11_4.pdf");
        f3.createNewFile();

        l = archiveService.getFileList(2021, rhid.toString());
        Assertions.assertEquals(2, l.size());
    }

    @Test
    public void testGetPdf() throws Exception {

        UUID rhid = UUID.randomUUID();

        Path path = Paths.get(dirname + File.separator + rhid.toString() + File.separator + "2021");

        Files.createDirectories(path);

        try {
            File f = archiveService.getPdf("dummy.pdf", rhid.toString());
            Assertions.fail("Expected Exception");
        } catch (Exception ex) {
            // Expected
        }

        try {
            File f = archiveService.getPdf("136715_2021_11_2.pdf", rhid.toString());
            Assertions.fail("Expected Exception");
        } catch (Exception ex) {
            // Expected
        }

        File f2 = new File(path.toFile(), "136715_2021_11_2.pdf");
        f2.createNewFile();

        File f = archiveService.getPdf("136715_2021_11_2.pdf", rhid.toString());
        Assertions.assertNotNull(f);
    }

    @Test
    public void testDelete() throws IOException {

        UUID rhid = UUID.randomUUID();

        Path path = Paths.get(dirname + File.separator + rhid.toString() + File.separator + "2021");

        Files.createDirectories(path);

        try {
            archiveService.delete("dummy.pdf", rhid.toString());
            Assertions.fail("Expected Exception");
        } catch (Exception ex) {
            // Expected
        }

        try {
            archiveService.delete("136715_2021_11_2.pdf", rhid.toString());
        } catch (Exception ex) {
            Assertions.fail("UnExpected Exception");
        }

        File f2 = new File(path.toFile(), "136715_2021_11_2.pdf");
        f2.createNewFile();

        try {
            archiveService.delete("136715_2021_11_2.pdf", rhid.toString());
        } catch (Exception ex) {
            Assertions.fail("UnExpected Exception");
        }
        Assertions.assertFalse(f2.exists());
    }

    @BeforeAll
    public static void setup() throws IOException {

        String dirname = ConfigProvider.getConfig().getValue("mrt.reports.dirname", String.class);
        Path path = Paths.get(dirname);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
