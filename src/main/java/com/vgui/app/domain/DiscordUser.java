package com.vgui.app.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DiscordUser.
 */
@Entity
@Table(name = "discord_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DiscordUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "pseudo", nullable = false, unique = true)
    private String pseudo;

    @Column(name = "birthday")
    private LocalDate birthday;

    @NotNull
    @Column(name = "id_discord", nullable = false, unique = true)
    private String idDiscord;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public DiscordUser pseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public DiscordUser birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIdDiscord() {
        return idDiscord;
    }

    public DiscordUser idDiscord(String idDiscord) {
        this.idDiscord = idDiscord;
        return this;
    }

    public void setIdDiscord(String idDiscord) {
        this.idDiscord = idDiscord;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordUser)) {
            return false;
        }
        return id != null && id.equals(((DiscordUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
            "id=" + getId() +
            ", pseudo='" + getPseudo() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", idDiscord='" + getIdDiscord() + "'" +
            "}";
    }
}
