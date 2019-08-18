package com.vgui.app.listener.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.vgui.app.domain.DiscordUser;
import com.vgui.app.service.DiscordUserService;
import com.vgui.app.service.impl.DiscordUserServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;



@Service
public class DiscordBotListenerImpl extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBotListenerImpl.class);

    @Autowired
    private DiscordUserService discordUserService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DiscordBotListenerImpl() {
    }

    public DiscordBotListenerImpl(DiscordUserService discordUserService) {
        this.discordUserService = discordUserService;
    }

    @Override
    public void onReady(ReadyEvent event) {
        LOGGER.info("DiscordBotListenerImpl - READY ! ");
        super.onReady(event);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        long responseNumber = event.getResponseNumber();
        String msg = null;
        //Event specific information
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        if(!author.isBot()) {
            msg = message.getContentDisplay();

            boolean bot = author.isBot();
            
            //Generate date
            LOGGER.debug(message.getContentDisplay());
            try {
                DiscordUser user = new DiscordUser();
                user.setIdDiscord(author.getDiscriminator());
                user.setPseudo(author.getName());
                LocalDate localDate = LocalDate.parse(msg, formatter);
                user.setBirthday(localDate);

                LOGGER.debug(discordUserService.toString());
                discordUserService.save(user);
                channel.sendMessage("It's Ok!");
                
            } catch (DateTimeParseException ex)
            {
                LOGGER.info("Date au mauvais format (jj/mm/YYYY)");
                channel.sendMessage("Date au mauvais format (jj/mm/YYYY)").queue();
            }
        }

        super.onMessageReceived(event);
    }


}
