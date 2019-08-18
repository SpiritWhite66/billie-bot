package com.vgui.app.service;

import com.vgui.app.domain.DiscordUser;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DiscordUser}.
 */
public interface DiscordUserService {

    /**
     * Save a discordUser.
     *
     * @param discordUser the entity to save.
     * @return the persisted entity.
     */
    DiscordUser save(DiscordUser discordUser);

    /**
     * Get all the discordUsers.
     *
     * @return the list of entities.
     */
    List<DiscordUser> findAll();


    /**
     * Get the "id" discordUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DiscordUser> findOne(Long id);

    /**
     * Delete the "id" discordUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
