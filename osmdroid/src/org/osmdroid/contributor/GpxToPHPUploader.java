package org.osmdroid.contributor;

import java.util.ArrayList;

import org.osmdroid.contributor.util.RecordedGeoPoint;

public class GpxToPHPUploader {

    // private static final Logger logger =
    // LoggerFactory.getLogger(GpxToPHPUploader.class);

    protected static final String UPLOADSCRIPT_URL = "http://www.PLACEYOURDOMAINHERE.com/anyfolder/gpxuploader/upload.php";

    /**
     * This is a utility class with only static members.
     */
    private GpxToPHPUploader() {
    }

    public static void uploadAsync(
            final ArrayList<RecordedGeoPoint> recordedGeoPoints) {
        // new Thread(new Runnable() {
        // @Override
        // public void run() {
        // try {
        // if (!Util.isSufficienDataForUpload(recordedGeoPoints))
        // return;
        //
        // final InputStream gpxInputStream = new ByteArrayInputStream(
        // RecordedRouteGPXFormatter.create(recordedGeoPoints).getBytes());
        // final HttpClient httpClient = new DefaultHttpClient();
        //
        // final HttpPost request = new HttpPost(UPLOADSCRIPT_URL);
        //
        // // create the multipart request and add the parts to it
        // final MultipartEntity requestEntity = new MultipartEntity();
        // requestEntity.addPart("gpxfile", new InputStreamBody(gpxInputStream,
        // ""
        // + System.currentTimeMillis() + ".gpx"));
        //
        // httpClient.getParams().setBooleanParameter("http.protocol.expect-continue",
        // false);
        //
        // request.setEntity(requestEntity);
        //
        // final HttpResponse response = httpClient.execute(request);
        // final int status = response.getStatusLine().getStatusCode();
        //
        // if (status != HttpStatus.SC_OK) {
        // logger.error("GPXUploader", "status != HttpStatus.SC_OK");
        // } else {
        // final Reader r = new InputStreamReader(new
        // BufferedInputStream(response
        // .getEntity().getContent()));
        // // see above
        // final char[] buf = new char[8 * 1024];
        // int read;
        // final StringBuilder sb = new StringBuilder();
        // while ((read = r.read(buf)) != -1)
        // sb.append(buf, 0, read);
        //
        // logger.debug("GPXUploader", "Response: " + sb.toString());
        // }
        // } catch (final Exception e) {
        // // logger.error("OSMUpload Error", e);
        // }
        // }
        // }).start();
    }
}
