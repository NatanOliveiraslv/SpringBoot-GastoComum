package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDetailDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.services.SpendingService;
import com.br.gasto_comum.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/spending")
public class SpendingController {

    @Autowired
    private SpendingService spendingService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<SpendingResponseDTO> createSpending(
            @RequestPart("data") @Valid SpendingRequestDTO spendingData,
            @RequestPart(value = "voucher", required = false) MultipartFile file,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal User user
    )  {
        SpendingResponseDTO spending = null;
        try {
            spending = spendingService.createSpending(spendingData, user, file);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        var uri = uriBuilder.path("/spending/{id}").buildAndExpand(spending.id()).toUri();
        return ResponseEntity.created(uri).body(spending);
    }


    @GetMapping
    public ResponseEntity<Page<SpendingResponseDTO>> listSpending(@AuthenticationPrincipal User user, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(spendingService.listSpending(user, pageable));
    }

    @PutMapping(consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<SpendingResponseDTO> updateSpending(@RequestPart("data") @Valid SpendingUpdateDTO data, @AuthenticationPrincipal User user, @RequestPart(value = "voucher", required = false) MultipartFile file) {
        try {
            return ResponseEntity.ok(spendingService.updateSpending(data,user,file));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> detailSpending(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spendingService.detailSpending(id, user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSpending(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        spendingService.deleteSpending(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/voucher/download/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = spendingService.downloadFile(fileName);
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
