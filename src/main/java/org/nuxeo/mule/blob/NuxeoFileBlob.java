package org.nuxeo.mule.blob;

import java.io.File;

import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.HasFile;

public class NuxeoFileBlob extends NuxeoBlob implements HasFile {

    private static final long serialVersionUID = 1L;

    protected FileBlob fileBlob;

    public NuxeoFileBlob(FileBlob wrapped) {
        super(wrapped);
        fileBlob = wrapped;
    }

    @Override
    public File getFile() {
        return fileBlob.getFile();
    }


}
