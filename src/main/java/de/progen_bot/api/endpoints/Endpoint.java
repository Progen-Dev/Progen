package de.progen_bot.api.endpoints;

import de.progen_bot.api.entities.APIResponseObject;
import de.progen_bot.core.Main;
import de.progen_bot.database.DaoHandler;
import de.progen_bot.database.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public abstract class Endpoint
{
    private final DaoHandler daoHandler = Main.getDaoHandler();
    private final String invoke;

    public Endpoint(String invoke)
    {
        this.invoke = invoke;
    }

    public abstract APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration);

    public String getInvoke()
    {
        return invoke;
    }

    public DaoHandler getDaoHandler()
    {
        return daoHandler;
    }
}
