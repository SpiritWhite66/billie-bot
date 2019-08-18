package com.vgui.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;



/**
 * Service Interface for use Discord Bot.
 */
public interface DiscordBotService {
    /**
     * Métode pour start la connexion
     */
    void start();

    /**
     * Méthode pour rajouter un listener au bot
     * @param listenerAdapter
     */
    void addListener(ListenerAdapter listener);

    /**
     * Permet d'ajouter des Listeners
     * @param listeners
     */
    void addListeners(List<ListenerAdapter> listeners);


}
