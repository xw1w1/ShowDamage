   ![Logo](https://www.spigotmc.org/data/resource_icons/109/109643.jpg)
## ShowDamage 
### What does this do?
This is a simple (yet configurable) plugin that allows to see the damage done to any entity. This is how it looks:

Non critical damage dealt by an entity:

![non-critical-damage](https://media.discordapp.net/attachments/877237543819702322/1108856891003654214/xp6yfP3.png)

Critical damage dealt by an entity has an indicator:

![critical-damage](https://media.discordapp.net/attachments/877237543819702322/1108856891490185306/DGsQ6me.png)


Damage dealt to entity plus sweep attack to other entity (can be disabled in config):

![sweep-attack](https://media.discordapp.net/attachments/877237543819702322/1108856891251114014/lm7aGQr.png)

Damage dealt to entity with an explosion by block (or entity):

![explosion-attack-gif](https://cdn.discordapp.com/attachments/877237543819702322/1108858979628613673/13WrEhT.gif)

The plugin also has a message output if damage was caused by a projectile:

![projectile-message](https://media.discordapp.net/attachments/918548823134077058/1108231482075517058/image.png)

#### Config
##### Colors
colors.crit-damage.first: First HEX color of crit-damage pop-up gradient
colors.crit-damage.second: Second HEX color of crit-damage pop-up gradient

colors.default-damage.first: First HEX color of non-crit-damage pop-up gradient
colors.default-damage.second: Second HEX color of non-crit-damage pop-up gradient

colors.accent.first: Main projectile-damage message color. Dark green by default
colors.accent.second: Entity name and damage + HP values color
colors.accent.third: Color of "i" inside square brackets

colors.crit-sign: Special sign showing that the damage was critical

#### Releases
Check releases for the latest jar files, this plugin gets updated often as of 18/05/2023.
You can build the jar file if you would like, as releases might not contain the latest fixes and features, however expect them to be more buggy than an official release. 
