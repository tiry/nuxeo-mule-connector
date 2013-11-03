package org.nuxeo.mule.blob;

import java.io.IOException;
import java.io.InputStream;

import org.nuxeo.ecm.automation.client.model.Blob;

public class NuxeoBlob extends Blob{

    protected final Blob wrappedBlob;

    public NuxeoBlob(Blob wrapped) {
        this.wrappedBlob = wrapped;
    }

    @Override
    public InputStream getStream() throws IOException {
        return wrappedBlob.getStream();
    }

    @Override
    public int getLength() {
        return wrappedBlob.getLength();
    }


}
