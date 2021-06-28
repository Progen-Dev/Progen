package de.progen_bot.utils.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link NoInject NoInject} annotation interface for disabling auto-injection of
 * {@link de.progen_bot.core.command.CommandHandler CommandHandler} and {@link net.dv8tion.jda.api.hooks.ListenerAdapter ListenerAdapter}
 * on startup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NoInject
{
}
