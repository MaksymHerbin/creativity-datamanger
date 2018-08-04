package com.creativity.datamanager.file.storage;

import com.creativity.datamanager.domain.Photo;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface PhotosStorageService {

    Set<String> copyFile(String albumName, File targetFolder, File sourceFolder) throws IOException;

    boolean photoFileExists(Photo photo, File targetFolder);
}
