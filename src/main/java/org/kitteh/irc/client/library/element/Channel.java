/*
 * * Copyright (C) 2013-2015 Matt Baxter http://kitteh.org
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kitteh.irc.client.library.element;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.command.KickCommand;
import org.kitteh.irc.client.library.command.ModeCommand;
import org.kitteh.irc.client.library.event.channel.ChannelUsersUpdatedEvent;

import java.util.List;
import java.util.Set;

/**
 * Represents an IRC channel.
 */
public interface Channel extends MessageReceiver {
    interface Topic {
        /**
         * Gets the time the topic was set.
         *
         * @return epoch time in milliseconds or -1 if unknown
         */
        long getTime();

        /**
         * Gets the channel topic.
         *
         * @return the topic
         */
        String getTopic();

        /**
         * Gets the user who set the channel topic.
         *
         * @return topic setter or null if unknown
         */
        User getUser();
    }

    /**
     * Gets the latest snapshot of this channel.
     *
     * @return an updated snapshot
     */
    default Channel getLatest() {
        return this.getClient().getChannel(this.getName());
    }

    /**
     * Gets the nicknames of users in the channel, if the client is in the
     * channel.
     *
     * @return nicks in the channel
     */
    List<String> getNicknames();

    /**
     * Gets the channel's topic.
     *
     * @return channel topic or null if unknown
     */
    Topic getTopic();

    /**
     * Gets a user by their nick, if they are known to the client. Note that
     * the server may not have sent the User data over, while the nickname
     * may be known to the channel.
     *
     * @param nick user's nick
     * @return the user object, if known
     */
    User getUser(String nick);

    /**
     * Gets all Users known to be in the channel. Note that the server may
     * not have sent all User data over, while the nickname may be known to
     * the channel. If you just want a list of nicknames, see {@link
     * #getNicknames()}.
     *
     * @return users in the channel
     */
    List<User> getUsers();

    /**
     * Gets the user modes of a given nickname in the channel.
     *
     * @param nick user's nick
     * @return a set of modes the user is known to have
     */
    Set<ChannelUserMode> getUserModes(String nick);

    /**
     * Gets if this Channel has complete user data available, only possible
     * if the Client is in the channel and the WHO list has sent.
     *
     * @return true if Client is in channel and WHO has finished
     * @see ChannelUsersUpdatedEvent
     */
    boolean isComplete();

    /**
     * Joins the channel.
     *
     * @see Client#addChannel(Channel...)
     */
    default void join() {
        this.getClient().addChannel(this);
    }

    /**
     * Provides a new KICK command.
     *
     * @return new kick command
     */
    default KickCommand newKickCommand() {
        return new KickCommand(this.getClient(), this);
    }

    /**
     * Provides a new MODE command.
     *
     * @return new mode command
     */
    default ModeCommand newModeCommand() {
        return new ModeCommand(this.getClient(), this);
    }

    /**
     * Parts the channel.
     *
     * @param reason leaving reason
     * @see Client#removeChannel(Channel, String)
     */
    default void part(String reason) {
        this.getClient().removeChannel(this, reason);
    }
}