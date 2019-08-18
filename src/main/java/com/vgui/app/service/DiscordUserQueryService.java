package com.vgui.app.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.vgui.app.domain.DiscordUser;
import com.vgui.app.domain.*; // for static metamodels
import com.vgui.app.repository.DiscordUserRepository;
import com.vgui.app.service.dto.DiscordUserCriteria;

/**
 * Service for executing complex queries for {@link DiscordUser} entities in the database.
 * The main input is a {@link DiscordUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DiscordUser} or a {@link Page} of {@link DiscordUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DiscordUserQueryService extends QueryService<DiscordUser> {

    private final Logger log = LoggerFactory.getLogger(DiscordUserQueryService.class);

    private final DiscordUserRepository discordUserRepository;

    public DiscordUserQueryService(DiscordUserRepository discordUserRepository) {
        this.discordUserRepository = discordUserRepository;
    }

    /**
     * Return a {@link List} of {@link DiscordUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DiscordUser> findByCriteria(DiscordUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DiscordUser> specification = createSpecification(criteria);
        return discordUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DiscordUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DiscordUser> findByCriteria(DiscordUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DiscordUser> specification = createSpecification(criteria);
        return discordUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DiscordUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DiscordUser> specification = createSpecification(criteria);
        return discordUserRepository.count(specification);
    }

    /**
     * Function to convert DiscordUserCriteria to a {@link Specification}.
     */
    private Specification<DiscordUser> createSpecification(DiscordUserCriteria criteria) {
        Specification<DiscordUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DiscordUser_.id));
            }
            if (criteria.getPseudo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPseudo(), DiscordUser_.pseudo));
            }
            if (criteria.getBirthday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthday(), DiscordUser_.birthday));
            }
            if (criteria.getIdDiscord() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdDiscord(), DiscordUser_.idDiscord));
            }
        }
        return specification;
    }
}
