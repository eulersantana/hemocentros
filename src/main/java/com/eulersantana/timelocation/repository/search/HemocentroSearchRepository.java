package com.eulersantana.timelocation.repository.search;

import com.eulersantana.timelocation.domain.Hemocentro;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Hemocentro entity.
 */
public interface HemocentroSearchRepository extends ElasticsearchRepository<Hemocentro, Long> {
}
