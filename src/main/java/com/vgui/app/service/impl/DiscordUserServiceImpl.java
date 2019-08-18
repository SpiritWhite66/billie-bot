package com.vgui.app.service.impl;

import com.vgui.app.service.DiscordUserService;
import com.vgui.app.domain.DiscordUser;
import com.vgui.app.repository.DiscordUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DiscordUser}.
 */
@Service
@Transactional
public class DiscordUserServiceImpl implements DiscordUserService {

    private final Logger log = LoggerFactory.getLogger(DiscordUserServiceImpl.class);

    private final DiscordUserRepository discordUserRepository;

    public DiscordUserServiceImpl(DiscordUserRepository discordUserRepository) {
        this.discordUserRepository = discordUserRepository;
    }

    /**
     * Save a discordUser.
     *
     * @param discordUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DiscordUser save(DiscordUser discordUser) {
        log.debug("Request to save DiscordUser : {}", discordUser);
        return discordUserRepository.save(discordUser);
    }

    /**
     * Get all the discordUsers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DiscordUser> findAll() {
        log.debug("Request to get all DiscordUsers");
        return discordUserRepository.findAll();
    }


    /**
     * Get one discordUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DiscordUser> findOne(Long id) {
        log.debug("Request to get DiscordUser : {}", id);
        return discordUserRepository.findById(id);
    }

    /**
     * Delete the discordUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DiscordUser : {}", id);
        discordUserRepository.deleteById(id);
    }
}
