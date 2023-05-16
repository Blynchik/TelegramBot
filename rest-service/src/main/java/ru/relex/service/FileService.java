package ru.relex.service;

import org.springframework.core.io.FileSystemResource;
import ru.relex.entity.AppDocument;
import ru.relex.entity.AppPhoto;
import ru.relex.entity.BinaryContent;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    //Последний метод необходим для передачи контента в tg
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
