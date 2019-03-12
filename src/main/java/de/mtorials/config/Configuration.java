package de.mtorials.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mtorials.exceptions.GuildHasNoConfigException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Configuration {

    private String TOKEN;

    private MessageEmbed setupInstructions;

    private HashMap<Guild, GuildConfiguration> guildConfiguratiosByGuild = new HashMap<>();

    public Configuration(String configFileName) {

        String contents = null;
        try {
            contents = new String(Files.readAllBytes(Paths.get(configFileName)));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Config file not found!");
        }

        JsonNode nodeConfig = null;
        try {

            nodeConfig = new ObjectMapper().readValue(contents, JsonNode.class);
            TOKEN = nodeConfig.get("TOKEN").textValue();

            JsonNode nodeSetupInstructions = nodeConfig.get("setupInstructions");
            this.setupInstructions = new EmbedBuilder()
                    .setTitle("Setup Instructions")
                    .setDescription(nodeSetupInstructions.get("description").textValue())
                    .build();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void loadGuildsConfig(List<Guild> guilds) {

        for (Guild g : guilds) {

            String contents = null;
            try {
                contents = new String(Files.readAllBytes(Paths.get("guilds/" + g.getName() + ".json")));
                guildConfiguratiosByGuild.put(g, new ObjectMapper().readValue(contents, GuildConfiguration.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public GuildConfiguration getGuildConfiguration(Guild g) {

        if (!this.guildConfiguratiosByGuild.containsKey(g)) throw new GuildHasNoConfigException();
        return this.guildConfiguratiosByGuild.get(g);
    }

    public void writeGuildConfiguration(Guild g, GuildConfiguration guildConfiguration) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(new File("guilds/" + g.getName() + ".json"), guildConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MessageEmbed getSetupInstructions() {
        return this.setupInstructions;
    }
}
