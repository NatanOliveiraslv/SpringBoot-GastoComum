package com.br.gasto_comum.repositorys;

import com.br.gasto_comum.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
