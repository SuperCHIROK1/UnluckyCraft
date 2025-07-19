# 🎲 UnluckyCraft
**A Minecraft plugin that adds random chance-based drops when breaking ores.**

---

## ⚙️ Configuration

```yaml
chances:
  diamond: 0.25
  emerald: 0.2
  gold: 0.15
  iron: 0.2
  coal: 0.3
  quartz: 0.3
  netherite: 0.05

drop-names:
  diamond: "ᴅɪᴀᴍᴏɴᴅ"
  emerald: "ᴇᴍᴇʀᴀʟᴅ"
  gold: "ɢᴏʟᴅ"
  iron: "ɪʀᴏɴ"
  coal: "ᴄᴏᴀʟ"
  quartz: "ϙᴜᴀʀᴛᴢ"
  netherite: "ɴᴇᴛʜᴇʀɪᴛᴇ"

messages:
  lucky: "&#279402ʟ&#29B801ᴜ&#2BDB01ᴄ&#2DFF00ᴋ &7• &fʏᴏᴜ ᴄʟᴀɪᴍ &#2DFF00{item}"
  unlucky: "&#940202ᴜ&#A90202ɴ&#BF0101ʟ&#D40101ᴜ&#EA0000ᴄ&#FF0000ᴋ &7• &fɴᴏᴛʜɪɴɢ ᴅʀᴏᴘᴘᴇᴅ"

sounds:
  lucky: "ENTITY_EXPERIENCE_ORB_PICKUP"
  unlucky: "BLOCK_DISPENSER_FAIL"

gui:
  title: "&8ᴄʜᴀɴᴄᴇs"
  lore:
    lmb: "&7ʟᴇғᴛ ᴄʟɪᴄᴋ&8: &f+0.05"
    rmb: "&7ʀɪɢʜᴛ ᴄʟɪᴄᴋ&8: &f-0.05"
```

## 📦 Commands

/ulc <about|reload>   # Show info or reload config  
/uls                  # Open chance-setting GUI  

## 🖼️ Preview

Menu GUI

![Menu](https://github.com/SuperCHIROK1/UnluckyCraft/blob/master/menu.png?raw=true)

Messages

![Chat](https://github.com/SuperCHIROK1/UnluckyCraft/blob/master/chat.png?raw=true)

## 📁 Installation

Put the plugin .jar into your server's /plugins/ folder

Start or restart the server

Edit /plugins/UnluckyCraft/config.yml

Run /ulc reload to apply changes
