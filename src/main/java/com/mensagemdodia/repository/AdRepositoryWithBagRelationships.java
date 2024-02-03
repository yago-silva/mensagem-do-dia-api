package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Ad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AdRepositoryWithBagRelationships {
    Optional<Ad> fetchBagRelationships(Optional<Ad> ad);

    List<Ad> fetchBagRelationships(List<Ad> ads);

    Page<Ad> fetchBagRelationships(Page<Ad> ads);
}
