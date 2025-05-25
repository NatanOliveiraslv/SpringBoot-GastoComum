package com.br.gasto_comum.services;

import com.br.gasto_comum.dtos.group.GroupRequestAddSpendingDTO;
import com.br.gasto_comum.dtos.group.GroupRequestDTO;
import com.br.gasto_comum.dtos.group.GroupResponseDTO;
import com.br.gasto_comum.dtos.group.GroupResponseDatailDTO;
import com.br.gasto_comum.exceptions.ObjectNotFound;
import com.br.gasto_comum.exceptions.UnauthorizedUser;
import com.br.gasto_comum.models.Group;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public GroupResponseDatailDTO createGroup(GroupRequestDTO data, User user){
        var groupEntity = new Group(data);
        groupEntity.setUser(user);
        groupRepository.save(groupEntity);
        return new GroupResponseDatailDTO(groupEntity);
    }

    public GroupResponseDatailDTO addSpendingToGroup(GroupRequestAddSpendingDTO data, User user) {
        var group = groupRepository.findById(data.groupId()).orElseThrow(() -> new ObjectNotFound("Grupo não encontrado"));
        if (!group.getUser().equals(user)) {
            throw new UnauthorizedUser("Usuário não possui autorização para alterar o Grupo!");  // Forbidden
        }

        for(var spendingId : data.spendingId()) {
            var spendingEntity = group.getSpendings().stream().filter(s -> s.getId().equals(spendingId)).findFirst().orElseThrow(() -> new ObjectNotFound("Gasto não encontrado. ID:" + spendingId));
            if (!spendingEntity.getUser().equals(user)) {
                throw new UnauthorizedUser("Acesso negado ao gasto. ID:" + spendingId);  // Forbidden
            }
            group.addSpending(spendingEntity);
        }
        groupRepository.save(group);
        return new GroupResponseDatailDTO(group);
    }

    public List<GroupResponseDTO> listGroup(User user) {
        return groupRepository.findByUser(user).stream().map(GroupResponseDTO::new).toList();
    }

}
