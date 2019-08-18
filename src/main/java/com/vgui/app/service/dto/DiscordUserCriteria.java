package com.vgui.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.vgui.app.domain.DiscordUser} entity. This class is used
 * in {@link com.vgui.app.web.rest.DiscordUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /discord-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiscordUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pseudo;

    private LocalDateFilter birthday;

    private StringFilter idDiscord;

    public DiscordUserCriteria(){
    }

    public DiscordUserCriteria(DiscordUserCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.pseudo = other.pseudo == null ? null : other.pseudo.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.idDiscord = other.idDiscord == null ? null : other.idDiscord.copy();
    }

    @Override
    public DiscordUserCriteria copy() {
        return new DiscordUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPseudo() {
        return pseudo;
    }

    public void setPseudo(StringFilter pseudo) {
        this.pseudo = pseudo;
    }

    public LocalDateFilter getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateFilter birthday) {
        this.birthday = birthday;
    }

    public StringFilter getIdDiscord() {
        return idDiscord;
    }

    public void setIdDiscord(StringFilter idDiscord) {
        this.idDiscord = idDiscord;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscordUserCriteria that = (DiscordUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(pseudo, that.pseudo) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(idDiscord, that.idDiscord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        pseudo,
        birthday,
        idDiscord
        );
    }

    @Override
    public String toString() {
        return "DiscordUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (pseudo != null ? "pseudo=" + pseudo + ", " : "") +
                (birthday != null ? "birthday=" + birthday + ", " : "") +
                (idDiscord != null ? "idDiscord=" + idDiscord + ", " : "") +
            "}";
    }

}
