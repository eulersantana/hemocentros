package com.eulersantana.timelocation.repository.search;

import com.eulersantana.timelocation.domain.Telefone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Telefone entity.
 */
public interface TelefoneSearchRepository extends ElasticsearchRepository<Telefone, Long> {
}
