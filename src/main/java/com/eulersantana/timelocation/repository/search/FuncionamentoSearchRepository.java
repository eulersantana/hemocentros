package com.eulersantana.timelocation.repository.search;

import com.eulersantana.timelocation.domain.Funcionamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Funcionamento entity.
 */
public interface FuncionamentoSearchRepository extends ElasticsearchRepository<Funcionamento, Long> {
}
