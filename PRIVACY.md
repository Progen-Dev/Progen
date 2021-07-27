# Privacy of policy
___

> Therefore, we inform you in this documentation which data Progen needs and what is done with them.

# Which Data is collected by Progen?
__

- Guild ID's
- User ID's  
- Role ID's
- Channel ID's
- Messages* 
- Reasons*
- Prefix

# What data does Progen store
___

*Progen stores data from the following commands or functions:*

- Guild configuration*
- Costume Channels
- Welcome/Goodbye messages
- Mute
- Playlist/Songs
- Warn
- XP

# Why is this data stored?
___

> Progen only stores data that is necessary for operation.

When Progen joins their server, the first thing that is stored is the GuildId and the prefix. 
The GuildId is necessary to be able to assign configurations to the desired Guild.

The user IDs are only stored in our database during a moderation step that was carried out with Progen. 
Only the ID of the victim and not the executor is saved. 
This storage is necessary to assign the punishment to the user.

The Role Id is needed for the autorole. 
This is stored in our guild configuration and retrieved when a user joins the guild to give them the correct role.

The channel Id is needed to send events to the channel you want. 
If you have not specified a channel, no channel ID has been saved by us.

Messages and reasons are not saved as IDs but as whole text.

# How the data is stored
___

Your data will be uploaded to our database and stored there. The database is located on our server, where Progen is also operated. 
We use the open source platform Maria DB as our database.

Your data will be permanently stored by us from the moment you enter the Guild until you deactivate the features. 
If you drop Progen from your Guild, the data will not be deleted but will remain.

# What Progen does with your data
___

Progen uses only its data for reliable operation. Only required data is requested from Progen. 
Furthermore, the data is not passed on to third party providers. 
Progen does not collect any personal data but stores the required data in the form of IDs. 
Exceptions are the reasons for our moderation commands.

## Additional Information
___

*Messages: Is saved as text in the database. Currently this function is used for welcome and leave messages.
*Reasons: If you execute a moderation command and enter a reason, this reason is also saved.

___

***Email: progenbot@gmail.com***

***Website: https://progen-bot.de***