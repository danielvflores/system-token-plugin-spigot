# System Token Plugin (STP) - SPIGOT PLUGIN

System Token Plugin (STP) is a **Minecraft Spigot 1.8.8 ** Spigot plugin for a custom token-based economy and pickaxe enchantment system, designed for prison servers.  
This project is in an **early/incomplete stage** and under active development.

For any questions or feedback, feel free to contact me on Discord: `@danielvflores` or [here](https://discord.com/users/835022014795874324)

---
**⬇️ [Download Latest Release](https://github.com/danielvflores/system-token-plugin/releases/tag/minecraft-plugin)**

**⬇️ [Download Latest Plugin Update from Spigot](https://www.spigotmc.org/resources/system-token-plugin-stp.126264/)**

> 📁 **All Download links are available.**

---

## 🌟 Features

- 🪙 **Token Economy**  
  Add, remove, set, and check player tokens with commands. Tokens are stored in a local SQLite database.

- ⛏️ **Custom Pickaxe System**  
  Give players a special pickaxe with custom display name, lore, and persistent NBT data.

- ✨ **Flexible Custom Enchantments**
  - Speed, Explosive, Efficiency, Fortune, Fly, Nuke, GiveToken, GiveMoney (all configurable)
  - **Per-enchant price model system:**
    - LINEAR, EXPONENTIAL, LOGARITHMIC, PROGRESSIVE_ARITHMETIC
    - Each enchantment can have its own model and parameters in `config.yml`.
    - Cost is calculated dynamically based on the model and level.
  - Enchantments are stored as custom NBT tags and reflected in the pickaxe lore.
  - Custom effects (e.g., Explosive, Fortune, GiveToken, GiveMoney).
  - Leveling up costs tokens according to the configured model; leveling down refunds 90% of the cost.

- 🖼️ **Enchantments GUI**
  - Fully configurable interface from `config.yml`.
  - Placeholder support in name and lore.
  - Clicking an enchantment charges the correct cost, applies the enchantment, and refreshes the GUI.
  - Feedback if you lack tokens or permissions.

- 🔒 **Pickaxe Protection**  
  Prevent accidental dropping of the custom pickaxe (requires double-tap Q).

- 💾 **Persistent Storage**  
  - Tokens and pickaxes are saved in SQLite databases.
  - Pickaxes are restored on player respawn after death.

- ⚙️ **Configurable**  
  - All enchantments, pickaxe display, allowed blocks, and database settings are configurable in `config.yml`.
  - Tab-completion para todos los comandos y subcomandos.

- 💬 **Customizable Messages**  
  - All plugin messages (prefix, errors, notifications, etc.) are fully customizable in `config.yml` under the `messages:` section.
  - Supports placeholders like `%player%`, `%amount%`, `%enchant%`, `%level%`, etc.
  - Example:  
    ```yaml
    message-prefix: "&e&lTOKENS &f» &7"
    messages:
      general:
        no-permission: "No tienes permiso para hacer esto."
        plugin-enabled: "Plugin habilitado correctamente."
      token:
        add-success: "Se añadieron &e%amount% &7tokens a &e%player%&7."
        insufficient-tokens: "No tienes suficientes tokens."
      enchant:
        applied: "Encantamiento &e%enchant% &7aplicado al jugador &e%player% &7con nivel &e%level%&7."
    ```

---

## 🧩 PlaceholderAPI Support (STPExpansion)

- The plugin includes support for [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) via **STPExpansion**.
- You can use custom placeholders in chat, scoreboards, holograms, and more.
- Available placeholders:
  - `%stp_token_balance%` — Returns the player's token balance as a string (1, 10, 100, 1000, 100000000).
  - `%stp_token_balance_formatted%` — Returns the player's token balance formatted with suffixes (1, 10, 100, 1K, 10K, 100K, 1M, etc.).
  - `%stp_enchant_<enchant>_<suffix>%` — Suffix supported:
    - `%stp_enchant_<enchant>_current_level%` — Returns the current level of the enchantment.
    - `%stp_enchant_<enchant>_next_level%` — Returns the next level of the enchantment, or the max level if the current level is already at max.
    - `%stp_enchant_<enchant>_max_level%` — Returns the max level of the enchantment.
    - `%stp_enchant_<enchant>_name%` — Returns the name of the enchantment.
  - `%stp_pickaxe_name%` — Display Pickaxe's name.
  - `%stp_pickaxe_lore%` — Display all Pickaxe's lore.

**How to use:**
1. Install PlaceholderAPI on your server.
2. The plugin automatically registers the placeholders on startup.
3. Use the placeholders in any plugin compatible with PlaceholderAPI.

---

## 🖼️ Preview

**Pickaxe with current enchantments (BETA 1.0.1):**
![Pickaxe Preview](https://i.imgur.com/AuxgCtv.png)

**EnchantGUI menu (native to the plugin, BETA 1.0.2):**
![EnchantGUIMenu Preview](https://i.imgur.com/VVasNZz.png)

**EnchantGUI menu showing current pickaxe's enchants (BETA 1.0.2):**
![PickaxeEnchantGUI Preview](https://i.imgur.com/tyA91aE.png)

**EnchantGUI menu showing available enchants to unlock (BETA 1.0.2):**
![EnchantsfromEnchantGUI Preview](https://i.imgur.com/wOzCWPe.png)

---

## 📦 Project Structure

```
project-root/
├── src/
│   ├── main/
│   │   ├── java/com/stp/...
│   │   └── resources/
│   │       ├── config.yml
│   │       └── plugin.yml
│   └── test/
├── target/
│   └── ... (build output)
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Requirements
- Java 17+ (for compiling, but runs on Spigot 1.8.8)
- Maven
- Spigot 1.8.8 server

### Build

```bash
mvn clean package
```

Copy the generated `.jar` from `target/` to your server's `plugins/` folder.

### Configuration
- Edit `config.yml` for pickaxe display, enchantments, price models, GUI, messages, etc.
- Edit `plugin.yml` for command registration.

---

## 📝 Commands

| Command              | Description                                 | Permission         |
|----------------------|---------------------------------------------|--------------------|
| `/token`             | Main command (see subcommands below)        | -                  |
| `/token balance`     | Check your token balance                    | -                  |
| `/token add <player> <amount>`    | Add tokens to a player           | `token.add`        |
| `/token remove <player> <amount>` | Remove tokens from a player      | `token.remove`     |
| `/token set <player> <amount>`    | Set a player's token balance     | `token.set`        |
| `/token enchant <sub> <player> <enchant> <level>` | Manage enchantments (`set`, `nextlevel`, `downlevel`) | - |
| `/givepickaxe`       | Give yourself the custom pickaxe            | -                  |
| `/enchantsreload`    | Reload enchantments from config             | `stp.reload`       |
| `/openenchantgui`    | Open the enchantments GUI                   | -                  |

**Tab-completion** is available for all commands and subcommands.

---

## ⚡ Example Config (`config.yml`)

```yaml
# CONFIG BETA 1.0.2
pickaxe:
  display-name: "&f&lPICO &7&l| &a&lINICIAL"
  lore:
    - ""
    - "&fDueño: &f%player%"
    - ""
    - "&a&lEncantamientos:"
    - "&2» {}"


# MODELS PRICES:
# - LINEAR: price = cost-per-level * level
# - LOGARITHMIC: price = cost-per-level * log(level + 2)
# - EXPONENTIAL: price = cost-per-level * (factor ^ level)   # default factor: 1.5
# - PROGRESSIVE_ARITHMETIC: price = cost-per-level + (level - 1) * increment   # default increment: 500
#
# Only EXPONENTIAL uses 'factor'.
# Only PROGRESSIVE_ARITHMETIC uses 'increment'.
# IMPORTANT: If the model is LINEAR or LOGARITHMIC, the other parameters are ignored.
# LINEAL IS THE DEFAULT MODEL, IF YOU WANT TO CHANGE IT, JUST CHANGE THE MODEL NAME BELOW.
model: LINEAL

# ENCHANTS PROPERTIES
enchants:

  # GENERAL

  # POTS
  speed:
    display: "&7Speed"
    max-level: 2
    enabled: true
    cost-per-level: 50000
    factor: 1.5 # EXPO
    increment: 500 # P-A
    enchants-item-avaible:
      - "_PICKAXE"
      - "_SWORD"
    enchant-strict: false
  
  strength:
    display: "&7Strength"
    max-level: 2
    enabled: true
    cost-per-level: 50000
    factor: 1.5 # EXPO
    increment: 500 # P-A
    enchants-item-avaible:
      - "_SWORD"
      - "_AXE"
    enchant-strict: false

  # MINER
  explosive:
    display: "&7Explosive"
    max-level: 50
    enabled: true
    chance: 70
    cost-per-level: 100000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
  efficiency:
    display: "&7Efficiency"
    max-level: 100
    enabled: true
    cost-per-level: 1000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
  fortune:
    display: "&7Fortune"
    max-level: 20
    enabled: true
    cost-per-level: 5000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
  fly:
    display: "&7Fly"
    max-level: 1
    enabled: true
    cost-per-level: 1000000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
  nuke:
    display: "&7Nuke"
    max-level: 1
    enabled: true
    chance: 0.1
    cost-per-level: 1000000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
  givetoken:
    display: "&7Recolector de tokens"
    max-level: 20
    price-for-level: 10
    messageStatus: true
    enabled: true
    cost-per-level: 10000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
  givemoney:
    display: "&7Recolector de dinero"
    max-level: 50
    price-for-level: 30
    messageStatus: true
    enabled: true
    cost-per-level: 10000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true

  # PVP


# ALLOWEDS
allowed-blocks:
  - STONE
  - COBBLESTONE
  - IRON_ORE
  - GOLD_ORE
  - DIAMOND_ORE
  - DIAMOND_BLOCK
  - EMERALD_BLOCK

fortune-blocks:
  - DIAMOND_BLOCK
  - EMERALD_BLOCK

# DATABASE
database:
  host: localhost
  name: minecraft
  username: usuario
  password: contraseña



# MANAGE MESSAGES
message-prefix: "&e&lTOKENS &f» &7"

messages:
  general:
    no-permission: "No tienes permiso para hacer esto."
    plugin-enabled: "Plugin habilitado correctamente."
    plugin-disabled: "Plugin deshabilitado correctamente."
    enchants-reloaded: "¡Encantamientos y configuración recargados!"

  nuke:
    activated: "¡Nuke activado exitosamente!"
    invalid-mine: "No estás en una mina válida."

  explosive:
    activated: "¡Explosión activada!"

  give-token:
    received: "Has recibido &e%tokens% &7tokens por romper un bloque con &e%enchant%&7."
  give-money:
    received: "Has recibido &e%money% &7tokens por romper un bloque con &e%enchant%&7."
  
  token:
    add-usage: "Uso: /token add <jugador> <cantidad>"
    remove-usage: "Uso: /token remove <jugador> <cantidad>"
    set-usage: "Uso: /token set <jugador> <cantidad>"
    give-usage: "Uso: /token give <jugador> <cantidad>"
    pay-usage: "Uso: /token pay <jugador> <cantidad>"
    balance-usage: "Uso: /token balance [jugador]"
    player-not-found: "Jugador no encontrado."
    invalid-amount: "La cantidad debe ser un número positivo válido (ej: 1500, 1.5K, 2M, 3B, 1.5Q)."
    add-success: "Se añadieron &e%amount% &7tokens a &e%player%&7."
    remove-success: "Se eliminaron &e%amount% &ctokens de &e%player%&c."
    set-success: "El saldo de tokens de &e%player% &7se ha establecido en &e%amount%&7."
    give-success: "Has dado &e%amount% &7tokens a &e%player%&7."
    pay-success: "Has pagado &e%amount% &7tokens a &e%player%&7."
    pay-received: "Has recibido &e%amount% &7tokens de &e%player%&7."
    insufficient-tokens: "No tienes suficientes tokens."
    new-balance: "Tu nuevo saldo de tokens es: &e%balance%"
    balance: "Saldo de tokens de &e%player%&7: &e%balance%"
    self-balance: "Tu saldo de tokens es: &e%balance%"
    already-has-tokens: "El jugador ya tiene esa cantidad de tokens."
    negative-amount: "La cantidad debe ser mayor que cero."
    not-a-number: "La cantidad debe ser un número válido."
    give-token-received: "Has recibido &e%tokens% &7tokens por romper un bloque con &e%enchant%&7."
    give-money-received: "Has recibido &e%money% &7tokens por romper un bloque con &e%enchant%&7."
    only-player: "Este comando solo puede usarlo un jugador."
    self-balance: "Tu saldo de tokens es: &e%balance%"
    

  fortune:
    applied: "¡Fortune aplicado! Has recibido &e%amount% &7bonus."

  pickaxe:
    invalid-block: "No puedes picar este bloque con tu pico personalizado."
    only-player: "Este comando solo puede usarse en el juego."
    received: "Has recibido el &bPico Supremo&7."
    

  enchant:
    register-success: "Encantamiento registrado: &e%enchant%"
    register-fail: "No se pudo registrar encantamiento: &e%enchant%"
    usage: "Uso: /token enchant <jugador> <encantamiento> <nivel>"
    player-not-found: "El jugador no está conectado."
    invalid-level: "El nivel debe ser un número válido."
    no-pickaxe: "El jugador debe tener un pico en la mano."
    not-custom-pickaxe: "El pico no es un Pico Supremo."
    unknown: "Encantamiento desconocido: &e%enchant%"
    invalid-range: "Nivel inválido. Debe estar entre %min% y %max%."
    removed: "Encantamiento &e%enchant% &7removido del jugador &e%player%&7."
    applied: "Encantamiento &e%enchant% &7aplicado al jugador &e%player% &7con nivel &e%level%&7."


# GUI CONFIGURATION
enchant-gui:
  title: "&7Enchant Menu"
  size: 45
  items:
    pickaxe:
      slot: 10
      material: diamond_pickaxe
      name: "%pickaxe_name%"
      lore:
        - "%pickaxe_lore%"
    efficiency:
      slot: 12
      material: ENCHANTED_BOOK
      name: "&f%enchant_efficiency_name%"
      lore:
        - "&7Este encantamiento aumenta la velocidad del minado mientras sostenga el pico en la mano."
        - "&7"
        - "&f• &7Niveles: &f%enchant_efficiency_current_level% / %enchant_efficiency_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_efficiency_cost_per_level% tokens"
    fortune:
      slot: 13
      material: ENCHANTED_BOOK
      name: "&f%enchant_fortune_name%"
      lore:
        - "&7Este encantamiento aumenta la cantidad de bloques que mina el jugador mientras sostenga el pico en la mano."
        - "&7"
        - "&f• &7Niveles: &f%enchant_fortune_current_level% / %enchant_fortune_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_fortune_cost_per_level% tokens"
    speed:
      slot: 14
      material: ENCHANTED_BOOK
      name: "&f%enchant_speed_name%"
      lore:
        - "&7Este encantamiento aumenta la velocidad del jugador mientras sostenga el pico en la mano."
        - "&7"
        - "&f• &7Niveles: &f%enchant_speed_current_level% / %enchant_speed_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_speed_cost_per_level% tokens"
    givemoney:
      slot: 15
      material: ENCHANTED_BOOK
      name: "&f%enchant_givemoney_name%"
      lore:
        - "&7Este encantamiento aumenta el dinero del jugador mientras rompa bloques con el pico."
        - "&7"
        - "&f• &7Niveles: &f%enchant_givemoney_current_level% / %enchant_givemoney_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_givemoney_cost_per_level% tokens"
    givetoken:
      slot: 16
      material: ENCHANTED_BOOK
      name: "&f%enchant_givetoken_name%"
      lore:
        - "&7Este encantamiento aumenta los tokens del jugador mientras rompa bloques con el pico."
        - "&7"
        - "&f• &7Niveles: &f%enchant_givetoken_current_level% / %enchant_givetoken_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_givetoken_cost_per_level% tokens"
    explosive:
      slot: 21
      material: ENCHANTED_BOOK
      name: "&f%enchant_explosive_name%"
      lore:
        - "&7Este encantamiento aumenta los bloques que rompe el jugador, entre más nivel más bloques rompe."
        - "&7"
        - "&f• &7Niveles: &f%enchant_explosive_current_level% / %enchant_explosive_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_explosive_cost_per_level% tokens"
    nuke:
      slot: 22
      material: ENCHANTED_BOOK
      name: "&f%enchant_nuke_name%"
      lore:
        - "&7Este encantamiento tiene una probabilidad de romper todos los bloques alrededor de la mina que rompe el jugador."
        - "&7"
        - "&f• &7Niveles: &f%enchant_nuke_current_level% / %enchant_nuke_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_nuke_cost_per_level% tokens"
    fly:
      slot: 23
      material: ENCHANTED_BOOK
      name: "&f%enchant_fly_name%"
      lore:
        - "&7Este encantamiento hace poder volar al jugador mientras sostenga el pico en la mano."
        - "&7"
        - "&f• &7Niveles: &f%enchant_fly_current_level% / %enchant_fly_max_level%"
        - "&f• &7Costo por nivel: &f%enchant_fly_cost_per_level% tokens"


locked-menus:
  - "enchant menu"
```

---

## ⚠️ Status

> **This plugin is in an early/incomplete stage.**  
> Many features are experimental or under development.  
> Use at your own risk and expect breaking changes.

---

## 📜 License

MIT License  
Copyright (c) 2024 Daniel Flores Viera

---

## 🤝 Contributions

Contributions are welcome!  
Open issues or pull requests to help improve the project.
