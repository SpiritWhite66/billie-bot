package com.vgui.app.repository;

import com.vgui.app.domain.DiscordUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DiscordUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long>, JpaSpecificationExecutor<DiscordUser> {

}
