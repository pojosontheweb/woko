package woko.persistence;

import org.json.JSONObject;
import woko.Woko;
import woko.facets.builtin.RenderObjectJson;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DbExporter {

    private static final WLogger logger = WLogger.getLogger(DbExporter.class);

    static final int BUFFER = 2048;

    public void export(Woko woko, OutputStream dest, HttpServletRequest request) throws IOException {
        logger.info("Exporting database...");
        long startTime = System.currentTimeMillis();
        ObjectStore os = woko.getObjectStore();
        List<Class<?>> mappedClasses = os.getMappedClasses();
        CheckedOutputStream checksum = new CheckedOutputStream(dest, new CRC32());
        ZipOutputStream zip = new ZipOutputStream(checksum);
        byte[] data = new byte[BUFFER];
        for (Class<?> clazz : mappedClasses) {
            String className = os.getClassMapping(clazz);
            ResultIterator<?> list = os.list(className, 0, null);
            logger.info("Exporting " + list.getTotalSize() + " objects for class " + className);
            zip.putNextEntry(new ZipEntry(className + "/"));
            while (list.hasNext()) {
                Object o = list.next();
                RenderObjectJson roj = (RenderObjectJson)woko.getFacet(RenderObjectJson.FACET_NAME, request, o);
                String key = os.getKey(o);
                if (roj==null) {
                    throw new IllegalArgumentException("could not find facet " + RenderObjectJson.FACET_NAME + " for object " + key + "@" + className);
                }
                logger.debug("  - " + key + "@" + className);
                JSONObject obj = roj.objectToJson(request);
                String jsonStr = obj.toString();
                zip.putNextEntry(new ZipEntry(className + "/" + key + ".json"));
                BufferedInputStream origin = new BufferedInputStream(new ByteArrayInputStream(jsonStr.getBytes()), BUFFER);
                int count;
                while((count = origin.read(data, 0, BUFFER)) != -1) {
                   zip.write(data, 0, count);
                }
                origin.close();
            }
        }
        logger.info("... database exported. Took " + (System.currentTimeMillis() - startTime) + " ms");
    }

}
