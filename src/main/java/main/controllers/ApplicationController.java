package main.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import main.models.Folder;
import main.models.Request;
import main.models.Tag;
import main.repositories.FolderRepository;
import main.repositories.RequestRepository;
import main.repositories.TagRepository;
import main.services.FolderService;
import main.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Главный контроллер", description = "Позволяет управлять приложением")
public class ApplicationController {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    RequestService requestService;

    @Autowired
    FolderService folderService;

    @GetMapping("/request")
    @Operation(summary = "Получение списка запросов", description = "Позволяет получить полный список запросов")
    public List<Request> getRequests() {
        return requestRepository.findAll();
    }

    @PostMapping("/request")
    @Operation(summary = "Добавление запроса", description = "Позволяет добавить запрос в базу данных")
    public Map<String, Boolean> addRequest(@RequestParam @Parameter(description = "Текст запроса") String text, @RequestParam @Parameter(description = "Дата изменения") Long modifiedDate, @RequestParam @Parameter(description = "Длина запроса") Long length) {
        if (text.isEmpty() || modifiedDate == 0 || length == 0 || requestRepository.findByText(text) != null) {
            return Map.of("result", false);
        }

        Request request = new Request(text, modifiedDate, length);
        requestRepository.save(request);

        return Map.of("result", true);
    }

    @GetMapping("/tag")
    @Operation(summary = "Получение списка тегов", description = "Позволяет получить полный список тегов")
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @PostMapping("/tag")
    @Operation(summary = "Добавление тега", description = "Позволяет добавить тег в базу данных")
    public Map<String, Boolean> addTag(@RequestParam @Parameter(description = "Имя тега") String name) {
        if (name.isEmpty() || tagRepository.findByName(name) != null) {
            return Map.of("result", false);
        }

        Tag tag = new Tag(name);
        tagRepository.save(tag);

        return Map.of("result", true);
    }

    @GetMapping("/folder")
    @Operation(summary = "Получение списка папок", description = "Позволяет получить полный список папок")
    public List<Folder> getFolders() {
        return folderRepository.findAll();
    }

    @PostMapping("/folder")
    public Map<String, Boolean> addFolder(@RequestParam @Parameter(description = "Имя папки") String name) {
        if (name.isEmpty() || folderRepository.findByFolderName(name) != null) {
            return Map.of("result", false);
        }

        Folder folder = new Folder(name);
        folderRepository.save(folder);

        return Map.of("result", true);
    }

    @PostMapping("/tagToRequest")
    @Operation(summary = "Добавление тега в запрос", description = "Позволяет добавить тег в запрос по id")
    public Map<String, Boolean> addTagToRequest(@RequestParam @Parameter(description = "Id запроса") String requestId, @RequestParam @Parameter(description = "Имя тега") String tagName) {
        if (requestId.isEmpty() || tagName.isEmpty()) {
            return Map.of("result", false);
        }

        Request request = requestRepository.findById(requestId).get();
        Set<Tag> tags = request.getTags();
        if (tags != null && tags.size() == 10) {
            return Map.of("result", false);
        }

        Tag tag = new Tag(tagName);
        tagRepository.save(tag);

        Tag newTag = tagRepository.findByName(tagName);

        requestService.addTagToRequest(requestId, newTag);

        return Map.of("result", true);
    }

    @PostMapping("/requestToFolder")
    @Operation(summary = "Добавение запроса в папку", description = "Позволяет добавить запрос в папку по id")
    public Map<String, Boolean> addRequestToFolder(@RequestParam @Parameter(description = "Id папки") String folderId, @RequestParam @Parameter(description = "Текст запроса") String text, @RequestParam @Parameter(description = "Дата изменения") Long modifiedDate, @RequestParam @Parameter(description = "Длина запроса") Long length) {
        if (text.isEmpty() || modifiedDate == 0 || length == 0 || folderId.isEmpty()) {
            return Map.of("result", false);
        }

        Folder folder = folderRepository.findById(folderId).get();
        Set<Request> requests = folder.getRequests();

        if (requests != null && requests.size() == 1) {
            return Map.of("result", false);
        }
        Request request = new Request(text, modifiedDate, length);
        requestRepository.save(request);

        Request newRequest = requestRepository.findByText(text);

        folderService.addRequestToFolder(folderId, newRequest);

        return Map.of("result", true);
    }

    @GetMapping("/getRequestByTag")
    @Operation(summary = "Получение запроса по тегу", description = "Позволяет получить запрос по id тега")
    public List<Request> getRequestByTag(@RequestParam @Parameter(description = "Id тега") String tagId) {
        if (tagId.isEmpty()) {
            return Collections.emptyList();
        }

        return requestService.getRequestByTag(tagId);
    }

    @GetMapping("/getRequestByFolder")
    @Operation(summary = "Получение запроса по папке", description = "Позволяет получить запрос по id папки")
    public Set<Request> getRequestByFolder(@RequestParam @Parameter(description = "Id папки") String folderId) {
        if (folderId.isEmpty()) {
            return Collections.emptySet();
        }

        return folderService.getRequestByFolder(folderId);
    }


}
