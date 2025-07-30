package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.File.FileResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.users.UserResponseDTO;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listUser(@RequestParam(required = false) String searchQuery, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {

        Page<UserResponseDTO> userPage;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Se um termo de busca foi fornecido, busque por nome OU e-mail
            userPage = userService.findUsersByNameOrEmailContaining(searchQuery, pageable);
        } else {
            // Caso contrário, liste todos (paginado)
            userPage = userService.listUsers(pageable);
        }

        return ResponseEntity.ok(userPage);
    }

    @PostMapping(consumes = "multipart/form-data", name = "profile-picture/upload")
    public ResponseEntity<FileResponseDTO> uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user) throws IOException, NoSuchAlgorithmException {
        return ResponseEntity.ok(userService.uploadProfilePicture(file, user));
    }

    @GetMapping("/profile-picture/download/{userId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID userId, HttpServletRequest request) {
        Resource resource = userService.downloadProfilePicture(userId);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Não foi possível determinar o tipo de arquivo." + ex.getMessage());
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Retorna o ResponseEntity com o recurso e os cabeçalhos apropriados
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"") // Força o download com o nome original
                .body(resource);
    }

}
