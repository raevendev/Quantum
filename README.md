**THIS MOD IS IN ALPHA, MANY FEATURES ARE CURRENTLY MISSING**

# Quantum

Quantum is a multi-world mod for Fabric, it allows players to create new worlds in both single player and multiplayer.

Because the mod is in alpha, it lacks many features like permissions configurations, etc. All commands require to be a server operator or enable 'Allow Cheats' in single-player.

**Keep in mind, worlds and portals must be created by a server operator**.

# How to use Quantum

Bellow you are going to learn how to use Quantum.

- All command arguments that are in **BOLD** are required.
- All command arguments are suggested in game as you type, so don't worry about not being sure about what you should write.

# How to create a new world

To create a new world, you must be a server operator and use the following command: 

/quantum create world <**worldName**> <_difficulty_> <_worldDimension_>

Arguments: 
- **worldName** : The name of your world.
- _difficulty_ : The difficulty of your world.
- _worldDimension_ : The dimension of your world. (eg. overworld, end, nether)

When a world is created, it will be automaticly loaded when the server starts. 
The worlds configurations are saved in the config directory config/quantum/worlds.

# How to create a portal

To create a portal, you must be a server operator and use the following command:

**NOTE: If you want your portal to go to a new world, you have to use the command above to create a new world before creating a portal**

/quantum create portal <**worldDestination**> <**portalFrameBlock**> <**portalActivationItem**> <**portalColor**>

Arguments: 
- **worldDestination** : The destination of your portal.
- **portalFrameBlock** : The block that your portal will be built with (Only solid and full blocks).
- **portalActivationItem** : The item to use to activate the portal (items, water and lava are supported).
- **portalColor** : The color of the portal.

# How to teleport to different worlds without a portal

Actually only server operators can do that with the following command: 

/quantum tp <**worldIdentifier**>

Arguments:
- **worldIdentifier** : The identifier of the world where you want to go.

# Whats next ?

I already planned to add mod configuration, teleporting to worlds by right clicking signs. 

# A last word
This mod is mainly created for my personal use, i may or may not update it regulary. I'll still try to maintains it updated to the last minecraft version.

# Thanks
[CustomPortalAPI](https://github.com/kyrptonaught/customportalapi) : I use this lib for creating custom portals.

[Fantasy](https://github.com/NucleoidMC/fantasy) : I use this lib for creating worlds at runtime.
