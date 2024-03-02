package com.mensagemdodia.web.rest;

import com.mensagemdodia.service.PhraseGroupingService;
import com.mensagemdodia.service.dto.PhraseGroupingDTO;
import com.mensagemdodia.service.dto.SluggedGroupDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/phrase-groupings")
public class PhraseGroupingResource {

    private final Logger log = LoggerFactory.getLogger(PhraseGroupingResource.class);

    @Autowired
    private final PhraseGroupingService phraseGroupingService;

    public PhraseGroupingResource(PhraseGroupingService phraseGroupingService) {
        this.phraseGroupingService = phraseGroupingService;
    }

    @GetMapping("{slug}")
    public ResponseEntity<PhraseGroupingDTO> getAllPhrasesByGroupSlug(@PathVariable("slug") String groupSlug) {
        log.debug("REST request to get all Phrases by group slug: " + groupSlug);
        return phraseGroupingService
            .getPhrasesGroupingSlug(groupSlug, true)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
