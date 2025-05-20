package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.spending.SpendingRequestDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDetailDTO;
import com.br.gasto_comum.dtos.spending.SpendingUpdateDTO;
import com.br.gasto_comum.services.SpendingService;
import com.br.gasto_comum.models.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/spending")
public class SpendingController {

    @Autowired
    private SpendingService spendingService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<SpendingResponseDTO> createSpending(@RequestPart("data") @Valid SpendingRequestDTO data, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user, @RequestPart(value = "voucher", required = false) MultipartFile file) {
        SpendingResponseDTO spending = null;
        try {
            spending = spendingService.createSpending(data, user, file);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
        var uri = uriBuilder.path("/spending/{id}").buildAndExpand(spending.id()).toUri();
        return ResponseEntity.created(uri).body(spending);
    }

    @GetMapping
    public ResponseEntity<List<SpendingResponseDTO>> listSpending(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spendingService.listSpending(user));
    }

    @PutMapping(consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> updateSpending(@RequestPart("data") @Valid SpendingUpdateDTO data, @AuthenticationPrincipal User user, @RequestPart(value = "voucher", required = false) MultipartFile file) {
        try {
            return ResponseEntity.ok(spendingService.updateSpending(data,user,file));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<SpendingResponseDetailDTO> detailSpending(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(spendingService.detailSpending(id, user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSpending(@PathVariable Long id, @AuthenticationPrincipal User user) {
        spendingService.deleteSpending(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/voucher")
    @ResponseBody
    public ResponseEntity<Resource> returnVoucher(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Resource file = spendingService.returnVoucher(id, user);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
