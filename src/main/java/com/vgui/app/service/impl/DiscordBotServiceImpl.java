package com.vgui.app.service.impl;

import java.util.List;

import javax.security.auth.login.LoginException;

import com.codahale.metrics.ConsoleReporter.Builder;
import com.netflix.discovery.shared.Application;
import com.vgui.app.config.ApplicationProperties;
import com.vgui.app.listener.impl.DiscordBotListenerImpl;
import com.vgui.app.service.DiscordBotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Service Implementation.
 */
@Service
@Transactional
public class DiscordBotServiceImpl implements DiscordBotService {

    private final Logger LOGGER = LoggerFactory.getLogger(DiscordBotServiceImpl.class);

    private JDA jda;

    @Autowired
    private ApplicationProperties appProperties;

    @Override
    public void start() {
        try {
            jda = new JDABuilder(AccountType.BOT) // The token of the account that is logging in.
                    .setToken(appProperties.getToken())
                    .addEventListener(new DiscordBotListenerImpl()) // An instance of a class that will handle events.
                    .buildAsync();
        } catch (LoginException e) {
            LOGGER.error("Authentification Failed", e);
        }
        LOGGER.debug(jda.getStatus().toString());
    }

    @Override
    public void addListener(ListenerAdapter listenerAdapter) {
        jda.addEventListener(listenerAdapter);
    }

    @Override
    public void addListeners(List<ListenerAdapter> listeners) {
        jda.addEventListener(listeners);
    }

    public void finalize()
    {
        jda.removeEventListener(jda.getRegisteredListeners());
        jda.shutdownNow();
    }


}
