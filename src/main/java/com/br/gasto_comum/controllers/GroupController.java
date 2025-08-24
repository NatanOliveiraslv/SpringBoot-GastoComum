package com.br.gasto_comum.controllers;

import com.br.gasto_comum.dtos.group.GroupRequestAddSpendingDTO;
import com.br.gasto_comum.dtos.group.GroupRequestDTO;
import com.br.gasto_comum.dtos.group.GroupResponseDTO;
import com.br.gasto_comum.dtos.group.GroupResponseDatailDTO;
import com.br.gasto_comum.dtos.spending.SpendingResponseDetailDTO;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.services.GroupService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createGroup(@RequestBody @Valid GroupRequestDTO data, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user) {
        GroupResponseDTO groupEntity = groupService.createGroup(data, user);
        var uri = uriBuilder.path("/group/{id}").buildAndExpand(groupEntity.id()).toUri();
        return ResponseEntity.created(uri).body(groupEntity);
    }

    // Endpoint para adicionar gastos a um grupo
    @PostMapping("/add/spending")
    @Transactional
    public ResponseEntity<GroupResponseDatailDTO> addSpendingToGroup(@RequestBody @Valid GroupRequestAddSpendingDTO data, @AuthenticationPrincipal User user) {
        GroupResponseDatailDTO response = groupService.addSpendingToGroup(data, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<GroupResponseDTO>> listGroups(@AuthenticationPrincipal User user, @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(groupService.listGroup(user, pageable));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<GroupResponseDatailDTO> detailGroup(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(groupService.detailGroup(id, user));
    }

}
