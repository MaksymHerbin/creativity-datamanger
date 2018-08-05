package com.creativity.datamanager.metadata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

import java.io.File;
import java.io.IOException;

public class DefaultMetaDataExtractor implements MetadataExtractor {
    @Override
    public Metadata extractMetaData(File file) throws ImageProcessingException, IOException {
        return ImageMetadataReader.readMetadata(file);
    }
}
