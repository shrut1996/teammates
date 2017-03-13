package teammates.ui.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import teammates.common.datatransfer.attributes.AccountAttributes;
import teammates.common.util.StatusMessage;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class ImageResult extends ActionResult {

    public String blobKey;

    public ImageResult(String destination, String blobKey, AccountAttributes account,
            List<StatusMessage> status) {
        super(destination, account, status);
        this.blobKey = blobKey;
    }

    @Override
    public void send(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (blobKey.isEmpty()) {
            resp.sendError(1, "No image found");
        } else {
            resp.setContentType("image/png");
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            blobstoreService.serve(new BlobKey(blobKey), resp);
        }
    }

}
