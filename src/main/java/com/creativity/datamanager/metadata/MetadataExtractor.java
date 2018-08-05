package com.creativity.datamanager.metadata;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

import java.io.File;
import java.io.IOException;

public interface MetadataExtractor {

    Metadata extractMetaData(File file) throws ImageProcessingException, IOException;

}
