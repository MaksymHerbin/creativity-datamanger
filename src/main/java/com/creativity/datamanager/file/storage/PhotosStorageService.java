package com.creativity.datamanager.file.storage;

import com.creativity.datamanager.domain.Photo;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface PhotosStorageService {

    Set<String> copyFile(String albumName, File sourceFolder) throws IOException;

    File getTargetFolder();

    boolean photoFileExists(Photo photo);

    File getPhoto(String id);
}
