package com.eulersantana.timelocation.repository.search;

import com.eulersantana.timelocation.domain.Local;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Local entity.
 */
public interface LocalSearchRepository extends ElasticsearchRepository<Local, Long> {
}
