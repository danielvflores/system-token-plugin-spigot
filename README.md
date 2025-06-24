# System Token Plugin (STP)

A **Minecraft Spigot 1.8.8 plugin** for a custom token-based economy and pickaxe enchantment system, designed for prison servers.  
This project is in an **early/incomplete stage** and under active development.

---

## 🌟 Features (Planned & Implemented)

- 🪙 **Token Economy**  
  Add, remove, set, and check player tokens with commands. Tokens are stored in a local SQLite database.

- ⛏️ **Custom Pickaxe System**  
  Give players a special pickaxe with custom display name, lore, and persistent NBT data.

- ✨ **Custom Enchantments**  
  - Speed, Explosive, Efficiency, Fortune, Fly, Nuke, Give-Token (all configurable)
  - Each enchantment has configurable `cost-per-level` in `config.yml`.
  - Enchantments are stored as custom NBT tags and reflected in the pickaxe lore.
  - Some enchantments (like Explosive and Fortune) have custom effects on block break.
  - Subir de nivel un encantamiento cuesta tokens según el config, y bajar de nivel reembolsa el 90% del costo.

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

---

## 🖼️ Preview

<details>
<summary>Haz clic para ver un ejemplo de pickaxe y encantamientos</summary>

![Pickaxe Preview](https://imgur.com/a/gAqzNDd)

</details>

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

- Edit `config.yml` for pickaxe display, enchantments, allowed blocks, database settings, and messages.
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

**Tab-completion** is available for all commands and subcommands.

## ⚡ Example Config (`config.yml`)

```yaml
# Configuración básica del pico
pickaxe:
  display-name: "&f&lPICO &7&l| &a&lINICIAL"
  lore:
    - ""
    - "&fDueño: &f%player%"
    - ""
    - "&a&lEncantamientos:"
    - "&2» {}"

# Encantamientos
enchants:
  speed:
    display: "&7Speed"
    max-level: 2
    enabled: true
    cost-per-level: 2000
  explosive:
    display: "&7Explosive"
    max-level: 50
    enabled: true
    chance: 70
    cost-per-level: 100000
  efficiency:
    display: "&7Efficiency"
    max-level: 100
    enabled: true
    cost-per-level: 1000
  fortune:
    display: "&7Fortune"
    max-level: 20
    enabled: true
    cost-per-level: 5000
  fly:
    display: "&7Fly"
    max-level: 1
    enabled: true
    cost-per-level: 1000000
  nuke:
    display: "&7Nuke"
    max-level: 1
    enabled: true
    chance: 0.1
    cost-per-level: 1000000
  give-token:
    display: "&7Recolector de tokens"
    max-level: 20
    price-for-level: 10
    messageStatus: true
    enabled: true
    cost-per-level: 1000

# Bloques permitidos
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

# Base de datos
database:
  host: localhost
  name: minecraft
  username: usuario
  password: contraseña

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

---

## 🔗 Links
⚒️ [Spigot Page Plugin ](https://www.spigotmc.org/resources/system-token-plugin-stp.126264/)
