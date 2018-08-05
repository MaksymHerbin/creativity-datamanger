package com.creativity.datamanager.client;

import com.creativity.datamanager.attributes.AttributesEnricher;
import com.creativity.datamanager.domain.Photo;
import com.creativity.datamanager.file.storage.PhotosStorageService;
import com.creativity.datamanager.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@ShellComponent
public class DatamanagerClient {

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PhotosStorageService photosStorageService;
    @Autowired
    @Qualifier("attributeEnricher")
    private AttributesEnricher attributesEnricher;

    @ShellMethod("List all existing photos in database")
    public List<Photo> list_all() {
        return photoRepository.findAll();
    }

    @ShellMethod("Load photos from specified folder")
    public String load_photos(String albumName, String downloadFrom) throws IOException {
        Set<String> filesCreated = photosStorageService.copyFile(albumName, new File(downloadFrom));
        for (String photoId : filesCreated) {
            Photo photo = new Photo(photoId);
            attributesEnricher.enrich(photo);
            photoRepository.save(photo);
        }
        return "Successfully copied all files from folder " + downloadFrom + " to " + photosStorageService.getTargetFolder().getAbsolutePath() + "\n Ids:\n" + filesCreated;
    }

    @ShellMethod("Validate all photos in database have matching file in target folder")
    public String validate_photo_files_vs_db() {
        Set<String> photosIdThatWereNotFound = photoRepository.findAll().stream()
                .filter(photo -> !photosStorageService.photoFileExists(photo))
                .map(Photo::getId)
                .collect(toSet());
        return "Following photos existing in database do not exist in " + photosStorageService.getTargetFolder().getAbsolutePath() + ":\n" + photosIdThatWereNotFound;
    }

    @ShellMethod("Removes all photos record from database for with file does not exist in target folder")
    public String clear_obsolete_files_from_db() {
        photoRepository.findAll().stream()
                .filter(photo -> !photosStorageService.photoFileExists(photo))
                .forEach(photoRepository::delete);
        return "Cleared database from obsolete photos";
    }

}
