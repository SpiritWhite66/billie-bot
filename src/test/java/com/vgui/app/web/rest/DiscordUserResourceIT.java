package com.vgui.app.web.rest;

import com.vgui.app.BotApp;
import com.vgui.app.domain.DiscordUser;
import com.vgui.app.repository.DiscordUserRepository;
import com.vgui.app.service.DiscordUserService;
import com.vgui.app.web.rest.errors.ExceptionTranslator;
import com.vgui.app.service.dto.DiscordUserCriteria;
import com.vgui.app.service.DiscordUserQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.vgui.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link DiscordUserResource} REST controller.
 */
@SpringBootTest(classes = BotApp.class)
public class DiscordUserResourceIT {

    private static final String DEFAULT_PSEUDO = "AAAAAAAAAA";
    private static final String UPDATED_PSEUDO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ID_DISCORD = "AAAAAAAAAA";
    private static final String UPDATED_ID_DISCORD = "BBBBBBBBBB";

    @Autowired
    private DiscordUserRepository discordUserRepository;

    @Autowired
    private DiscordUserService discordUserService;

    @Autowired
    private DiscordUserQueryService discordUserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDiscordUserMockMvc;

    private DiscordUser discordUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscordUserResource discordUserResource = new DiscordUserResource(discordUserService, discordUserQueryService);
        this.restDiscordUserMockMvc = MockMvcBuilders.standaloneSetup(discordUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscordUser createEntity(EntityManager em) {
        DiscordUser discordUser = new DiscordUser()
            .pseudo(DEFAULT_PSEUDO)
            .birthday(DEFAULT_BIRTHDAY)
            .idDiscord(DEFAULT_ID_DISCORD);
        return discordUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscordUser createUpdatedEntity(EntityManager em) {
        DiscordUser discordUser = new DiscordUser()
            .pseudo(UPDATED_PSEUDO)
            .birthday(UPDATED_BIRTHDAY)
            .idDiscord(UPDATED_ID_DISCORD);
        return discordUser;
    }

    @BeforeEach
    public void initTest() {
        discordUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscordUser() throws Exception {
        int databaseSizeBeforeCreate = discordUserRepository.findAll().size();

        // Create the DiscordUser
        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isCreated());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeCreate + 1);
        DiscordUser testDiscordUser = discordUserList.get(discordUserList.size() - 1);
        assertThat(testDiscordUser.getPseudo()).isEqualTo(DEFAULT_PSEUDO);
        assertThat(testDiscordUser.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testDiscordUser.getIdDiscord()).isEqualTo(DEFAULT_ID_DISCORD);
    }

    @Test
    @Transactional
    public void createDiscordUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discordUserRepository.findAll().size();

        // Create the DiscordUser with an existing ID
        discordUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPseudoIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setPseudo(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdDiscordIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setIdDiscord(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscordUsers() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].pseudo").value(hasItem(DEFAULT_PSEUDO.toString())))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].idDiscord").value(hasItem(DEFAULT_ID_DISCORD.toString())));
    }
    
    @Test
    @Transactional
    public void getDiscordUser() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get the discordUser
        restDiscordUserMockMvc.perform(get("/api/discord-users/{id}", discordUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discordUser.getId().intValue()))
            .andExpect(jsonPath("$.pseudo").value(DEFAULT_PSEUDO.toString()))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.idDiscord").value(DEFAULT_ID_DISCORD.toString()));
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByPseudoIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where pseudo equals to DEFAULT_PSEUDO
        defaultDiscordUserShouldBeFound("pseudo.equals=" + DEFAULT_PSEUDO);

        // Get all the discordUserList where pseudo equals to UPDATED_PSEUDO
        defaultDiscordUserShouldNotBeFound("pseudo.equals=" + UPDATED_PSEUDO);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByPseudoIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where pseudo in DEFAULT_PSEUDO or UPDATED_PSEUDO
        defaultDiscordUserShouldBeFound("pseudo.in=" + DEFAULT_PSEUDO + "," + UPDATED_PSEUDO);

        // Get all the discordUserList where pseudo equals to UPDATED_PSEUDO
        defaultDiscordUserShouldNotBeFound("pseudo.in=" + UPDATED_PSEUDO);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByPseudoIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where pseudo is not null
        defaultDiscordUserShouldBeFound("pseudo.specified=true");

        // Get all the discordUserList where pseudo is null
        defaultDiscordUserShouldNotBeFound("pseudo.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBirthdayIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where birthday equals to DEFAULT_BIRTHDAY
        defaultDiscordUserShouldBeFound("birthday.equals=" + DEFAULT_BIRTHDAY);

        // Get all the discordUserList where birthday equals to UPDATED_BIRTHDAY
        defaultDiscordUserShouldNotBeFound("birthday.equals=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBirthdayIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where birthday in DEFAULT_BIRTHDAY or UPDATED_BIRTHDAY
        defaultDiscordUserShouldBeFound("birthday.in=" + DEFAULT_BIRTHDAY + "," + UPDATED_BIRTHDAY);

        // Get all the discordUserList where birthday equals to UPDATED_BIRTHDAY
        defaultDiscordUserShouldNotBeFound("birthday.in=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBirthdayIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where birthday is not null
        defaultDiscordUserShouldBeFound("birthday.specified=true");

        // Get all the discordUserList where birthday is null
        defaultDiscordUserShouldNotBeFound("birthday.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBirthdayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where birthday greater than or equals to DEFAULT_BIRTHDAY
        defaultDiscordUserShouldBeFound("birthday.greaterOrEqualThan=" + DEFAULT_BIRTHDAY);

        // Get all the discordUserList where birthday greater than or equals to UPDATED_BIRTHDAY
        defaultDiscordUserShouldNotBeFound("birthday.greaterOrEqualThan=" + UPDATED_BIRTHDAY);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBirthdayIsLessThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where birthday less than or equals to DEFAULT_BIRTHDAY
        defaultDiscordUserShouldNotBeFound("birthday.lessThan=" + DEFAULT_BIRTHDAY);

        // Get all the discordUserList where birthday less than or equals to UPDATED_BIRTHDAY
        defaultDiscordUserShouldBeFound("birthday.lessThan=" + UPDATED_BIRTHDAY);
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByIdDiscordIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where idDiscord equals to DEFAULT_ID_DISCORD
        defaultDiscordUserShouldBeFound("idDiscord.equals=" + DEFAULT_ID_DISCORD);

        // Get all the discordUserList where idDiscord equals to UPDATED_ID_DISCORD
        defaultDiscordUserShouldNotBeFound("idDiscord.equals=" + UPDATED_ID_DISCORD);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByIdDiscordIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where idDiscord in DEFAULT_ID_DISCORD or UPDATED_ID_DISCORD
        defaultDiscordUserShouldBeFound("idDiscord.in=" + DEFAULT_ID_DISCORD + "," + UPDATED_ID_DISCORD);

        // Get all the discordUserList where idDiscord equals to UPDATED_ID_DISCORD
        defaultDiscordUserShouldNotBeFound("idDiscord.in=" + UPDATED_ID_DISCORD);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByIdDiscordIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where idDiscord is not null
        defaultDiscordUserShouldBeFound("idDiscord.specified=true");

        // Get all the discordUserList where idDiscord is null
        defaultDiscordUserShouldNotBeFound("idDiscord.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiscordUserShouldBeFound(String filter) throws Exception {
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].pseudo").value(hasItem(DEFAULT_PSEUDO)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].idDiscord").value(hasItem(DEFAULT_ID_DISCORD)));

        // Check, that the count call also returns 1
        restDiscordUserMockMvc.perform(get("/api/discord-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiscordUserShouldNotBeFound(String filter) throws Exception {
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscordUserMockMvc.perform(get("/api/discord-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiscordUser() throws Exception {
        // Get the discordUser
        restDiscordUserMockMvc.perform(get("/api/discord-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscordUser() throws Exception {
        // Initialize the database
        discordUserService.save(discordUser);

        int databaseSizeBeforeUpdate = discordUserRepository.findAll().size();

        // Update the discordUser
        DiscordUser updatedDiscordUser = discordUserRepository.findById(discordUser.getId()).get();
        // Disconnect from session so that the updates on updatedDiscordUser are not directly saved in db
        em.detach(updatedDiscordUser);
        updatedDiscordUser
            .pseudo(UPDATED_PSEUDO)
            .birthday(UPDATED_BIRTHDAY)
            .idDiscord(UPDATED_ID_DISCORD);

        restDiscordUserMockMvc.perform(put("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscordUser)))
            .andExpect(status().isOk());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeUpdate);
        DiscordUser testDiscordUser = discordUserList.get(discordUserList.size() - 1);
        assertThat(testDiscordUser.getPseudo()).isEqualTo(UPDATED_PSEUDO);
        assertThat(testDiscordUser.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testDiscordUser.getIdDiscord()).isEqualTo(UPDATED_ID_DISCORD);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscordUser() throws Exception {
        int databaseSizeBeforeUpdate = discordUserRepository.findAll().size();

        // Create the DiscordUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscordUserMockMvc.perform(put("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiscordUser() throws Exception {
        // Initialize the database
        discordUserService.save(discordUser);

        int databaseSizeBeforeDelete = discordUserRepository.findAll().size();

        // Delete the discordUser
        restDiscordUserMockMvc.perform(delete("/api/discord-users/{id}", discordUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscordUser.class);
        DiscordUser discordUser1 = new DiscordUser();
        discordUser1.setId(1L);
        DiscordUser discordUser2 = new DiscordUser();
        discordUser2.setId(discordUser1.getId());
        assertThat(discordUser1).isEqualTo(discordUser2);
        discordUser2.setId(2L);
        assertThat(discordUser1).isNotEqualTo(discordUser2);
        discordUser1.setId(null);
        assertThat(discordUser1).isNotEqualTo(discordUser2);
    }
}
