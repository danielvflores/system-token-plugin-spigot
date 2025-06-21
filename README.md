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
  - Speed, Explosive, Efficiency, Fortune, Fly (all configurable)
  - Enchantments are stored as custom NBT tags and reflected in the pickaxe lore.
  - Some enchantments (like Explosive and Fortune) have custom effects on block break.

- 🔒 **Pickaxe Protection**  
  Prevent accidental dropping of the custom pickaxe (requires double-tap Q).

- 💾 **Persistent Storage**  
  - Tokens and pickaxes are saved in SQLite databases.
  - Pickaxes are restored on player respawn after death.

- ⚙️ **Configurable**  
  - All enchantments, pickaxe display, allowed blocks, and database settings are configurable in `config.yml`.

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

- Edit `config.yml` for pickaxe display, enchantments, allowed blocks, and database settings.
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
| `/token enchant <player> <enchant> <level>` | Apply/remove enchantment | -                  |
| `/givepickaxe`       | Give yourself the custom pickaxe            | -                  |
| `/enchantsreload`    | Reload enchantments from config             | `stp.reload`       |

---

## ⚡ Example Config (`config.yml`)

```yaml
pickaxe:
  display-name: "&f&lPICO &7&l| &a&lINICIAL"
  lore:
    - ""
    - "&fDueño: &f%player%"
    - ""
    - "&a&lEncantamientos:"
    - "&2» {}"

enchants:
  speed:
    display: "&7Speed"
    max-level: 2
    enabled: true
  explosive:
    display: "&7Explosive"
    max-level: 50
    enabled: true
  efficiency:
    display: "&7Efficiency"
    max-level: 100
    enabled: true
  fortune:
    display: "&7Fortune"
    max-level: 20
    enabled: true
  fly:
    display: "&7Fly"
    max-level: 1
    enabled: true

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

database:
  host: localhost
  name: minecraft
  username: usuario
  password: contraseña
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

- [SpigotMC](https://www.spigotmc.org/)
-
