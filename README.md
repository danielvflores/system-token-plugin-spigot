# System Token Plugin (STP) - SPIGOT PLUGIN

System Token Plugin (STP) is a **Minecraft Spigot 1.8.8** plugin for a custom token-based economy and pickaxe enchantment system, designed for prison servers.  
**Fully compatible with Java 8** and optimized for maximum compatibility with legacy servers.  
This project is in an **early/incomplete stage** and under active development.

## 🔧 Compatibility

- **Minecraft Version**: Spigot 1.8.8 (Legacy Support)
- **Java Version**: Java 8+ (Fully compatible with Java 8)
- **Compatible Plugins**: 
  - ✅ **PlayerKits2** - Recommended for creating kits with enchanted items (for NBT support)
  - ✅ **AutoSell** - Handles block selling (fortune-blocks disabled in this plugin)
  - ✅ **Vault** - Economy integration
  - ✅ **PlaceholderAPI** - Placeholder support
  - ✅ **WorldGuard** - Region support

**✅ Java 8 Compatible** - Fully optimized for Java 8 and Spigot 1.8.8  
**🔗 Plugin Compatibility** - Compatible with AutoSell and PlayerKits2 (recommended for kit creation due to NBT support)  
**⚙️ Robust Configuration** - All commands properly registered with permissions and tab-completion

## 📦 Installation & Setup}

1. **Download** the latest `.jar` file from the releases section
2. **Place** the plugin in your server's `plugins/` folder
3. **Restart** your server to generate the config files
4. **Configure** the `config.yml` file to your liking
5. **Restart** again or use `/enchantsreload` to apply changes

---

**⬇️ [Download Latest Release](https://github.com/danielvflores/system-token-plugin-spigot/releases/tag/minecraft-plugin-spigot)**

**⬇️ [Download Latest Plugin Update from Spigot](https://www.spigotmc.org/resources/system-token-plugin-stp.126264/)**

> 📁 **All Download links are available.**

---


### Recommended Plugin Setup
For the best experience, install these compatible plugins:
- **Vault** (required for economy integration)
- **PlaceholderAPI** (for placeholder support)
- **AutoSell** (handles automatic block selling)
- **PlayerKits2** (for creating kits with enchanted tools)
- **WorldGuard** (for region support)

For any questions or feedback, feel free to contact me on Discord: `@danielvflores` or [here](https://discord.com/users/835022014795874324)

---

## 🌟 Features

- 🪙 **Token Economy**  
  Add, remove, set, and check player tokens with commands. Tokens are stored in a local SQLite database.

- ⛏️ **Custom Pickaxe System**  
  Give players a special pickaxe with custom display name, lore, and persistent NBT data.
  - **Clean Lore System**: Configuration-driven lore with no duplication
  - **Automatic Lore Reconstruction**: Lore is always rebuilt from config and current enchants
  - **NBT-Safe**: Compatible with kit plugins and item serialization

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
  - **Robust Reload System**: `/enchantsreload` command for safe config and enchantment reloading

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

**Example for Sword with enchants using PlayerKits2 (STABLE 1.1.0):**
![PlayerKits2 Sword Example](https://i.imgur.com/uklWhg8.png)

**EnchantGUI menu showing available enchants to unlock to Sword (STABLE 1.1.0):**
![Sword EnchantGUI Preview](https://i.imgur.com/oENOyD8.png)

**EnchantGUI menu showing available enchants to unlock (BETA 1.0.2):**
![EnchantsfromEnchantGUI Preview](https://i.imgur.com/wOzCWPe.png)

**EnchantGUI menu showing current pickaxe's enchants (BETA 1.0.2):**
![PickaxeEnchantGUI Preview](https://i.imgur.com/tyA91aE.png)

**EnchantGUI menu (native to the plugin, BETA 1.0.2):**
![EnchantGUIMenu Preview](https://i.imgur.com/VVasNZz.png)

**Pickaxe with current enchantments (BETA 1.0.1):**
![Pickaxe Preview](https://i.imgur.com/AuxgCtv.png)


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

| Command              | Description                                 | Permission         | Aliases |
|----------------------|---------------------------------------------|--------------------|---------| 
| `/token`             | Check your token balance (default action)   | `stp.token.use`    | - |
| `/token balance`     | Check your token balance                    | `stp.token.use`    | - |
| `/token add <player> <amount>`    | Add tokens to a player           | `stp.token.add`        | - |
| `/token remove <player> <amount>` | Remove tokens from a player      | `stp.token.remove`     | - |
| `/token set <player> <amount>`    | Set a player's token balance     | `stp.token.set`        | - |
| `/token enchant <sub> <player> <enchant> <level>` | Manage enchantments (`set`, `nextlevel`, `downlevel`) | `stp.token.enchant` | - |
| `/givepickaxe`       | Give yourself the custom pickaxe            | `stp.givepickaxe`  | `gp`, `givep`, `pickaxe`, `kitpickaxe` |
| `/enchantsreload`    | Reload enchantments and config              | `stp.reload`       | `ereload`, `enchantreload`, `stpreload` |
| `/openenchantgui`    | Open the enchantments GUI                   | `stp.openenchantgui` | `egui`, `enchantgui`, `ogui`, `enchant` |

**Tab-completion** is available for all commands and subcommands.

---

## 🔒 Permissions

| Permission           | Description                                  | Default |
|----------------------|----------------------------------------------|---------|
| `stp.token.use`      | Allows using basic token commands           | ALL     |
| `stp.token.add`      | Allows adding tokens to players             | OP      |
| `stp.token.remove`   | Allows removing tokens from players         | OP      |
| `stp.token.set`      | Allows setting player token balances        | OP      |
| `stp.token.enchant`  | Allows managing player enchantments         | OP      |
| `stp.givepickaxe`    | Allows giving pickaxes to yourself          | ALL     |
| `stp.reload`         | Allows reloading plugin configuration       | OP      |
| `stp.openenchantgui` | Allows opening the enchantments GUI         | ALL     |

**Note**: Commands with ALL default permission can be used by any player, while OP commands require operator status.

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
sword:
  display-name: "&b&lESPADA &7&l| &a&lINICIAL"
  lore:
    - "&7{}"

# MODELS PRICES:
# - LINEAR: price = cost-per-level * level
# - LOGARITHMIC: price = cost-per-level * log(level + 2)
# - EXPONENTIAL: price = cost-per-level * (factor ^ level)
# - PROGRESSIVE_ARITHMETIC: price = cost-per-level + (level - 1) * increment
model: LINEAL

# ENCHANTS PROPERTIES
enchants:
  # EFFECTS
  speed:
    display: "&7Speed"
    max-level: 2
    enabled: true
    cost-per-level: 50000
    factor: 1.5
    increment: 500
    enchants-item-avaible:
      - "_PICKAXE"
      - "_SWORD"
    enchant-strict: false
  
  # MINING
  explosive:
    display: "&7Explosive"
    max-level: 50
    enabled: true
    chance: 70
    cost-per-level: 100000
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
    
  efficiency:
    display: "&7Efficiency"
    max-level: 100
    enabled: true
    cost-per-level: 1000
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true
    
  # ECONOMY
  givetoken:
    display: "&7Recolector de tokens"
    max-level: 20
    price-for-level: 10
    messageStatus: true
    enabled: true
    cost-per-level: 10000
    enchants-item-avaible:
      - "_PICKAXE"
    enchant-strict: true

# ALLOWED BLOCKS
allowed-blocks:
  - STONE
  - COBBLESTONE
  - IRON_ORE
  - GOLD_ORE
  - DIAMOND_ORE
  - EMERALD_ORE
  - COAL_ORE
  - LAPIS_ORE

# DATABASE SETTINGS
database:
  filename: "tokens.db"
  tokens-table: "player_tokens"
  pickaxes-table: "player_pickaxes"

# GUI SETTINGS  
gui:
  title: "&8Enchantments GUI"
  size: 54
  # ... (additional GUI configuration)
```
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

## 🚀 Recent Improvements

### **v1.0.2 - Compatibility & Stability Update**

- **✅ Java 8 Full Compatibility**: Removed all Java 8+ features (lambdas, streams) for maximum compatibility
- **🔧 Clean Lore System**: Completely refactored pickaxe lore handling to eliminate duplication
  - Lore is always reconstructed from config templates and current enchants
  - No more appending or cleaning old lore
  - NBT-safe for kit plugins and item serialization
- **⚙️ Robust Reload System**: Improved `/enchantsreload` command with proper error handling
- **🔗 Plugin Compatibility**: 
  - Fully compatible with AutoSell (fortune-blocks disabled)
  - Compatible with PlayerKits2 for creating kits with enchanted tools
  - Works seamlessly with Essentials, Vault, PlaceholderAPI, and WorldGuard
- **📋 Command Improvements**: `/token` now shows balance by default when used without arguments
- **📝 Documentation**: Updated README with all compatibility information and correct config examples

### **Legacy Server Optimization**
- Designed specifically for **Spigot 1.8.8** and **Java 8**
- No modern Java features that could cause compatibility issues
- Extensive testing on legacy server environments

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
