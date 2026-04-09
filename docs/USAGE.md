# DefaultUserConfigAndLibs — Usage Guide
Fabric 1.21.4 library mod by Fenthix.

## Adding as a dependency

In your mod's `build.gradle`:
```groovy
repositories {
    mavenLocal()
}

dependencies {
    modImplementation "net.defaultuserconfigandlibs:defaultuserconfigandlibs:0.0.4"
}
```

In `fabric.mod.json`:
```json
"depends": {
    "defaultuserconfigandlibs": "*"
}
```

---

## Environment info

```java
String mc   = EnvInfo.getMcVersion();   // "1.21.4"
String fab  = EnvInfo.getFabricVersion();
String ver  = EnvInfo.getModVersion("my_mod_id");
boolean has = EnvInfo.isModLoaded("sodium");
String os   = EnvInfo.getOS();
String time = EnvInfo.getDateTime();                   // "2025-04-09 13:45:00"
String fmt  = EnvInfo.getDateTime("dd/MM/yyyy");       // custom format
```

---

## Drawing a progress bar

```java
// In a Screen.render() method:
ScreenUtil.drawBar(context, width / 2, y, 200, 5, 0.75f, 0xFF55FF55);
//                          ^^^cx^^^  ^y  ^w  ^h  ^progress  ^color
```

---

## Overriding the world-select button

Register during `ClientModInitializer.onInitializeClient()`:

```java
WorldSelectButtonRegistry.register(new WorldSelectButtonRegistry.WorldButtonOverride() {
    @Override
    public String getButtonLabel(LevelSummary level) {
        return myCondition(level) ? "My Label" : null;
    }

    @Override
    public String getTooltipText(LevelSummary level) {
        return myCondition(level) ? "Tooltip text here" : null;
    }
});
```

---

## Scrollable filtered world list

```java
// Inside Screen.init():
FilteredWorldListWidget list = new FilteredWorldListWidget(
    client,
    width,                                           // widget width
    height - 96,                                     // widget height
    32,                                              // top y
    20,                                              // row height
    client.getLevelStorage().getSavesDirectory(),
    path -> checkSomething(path),                    // which worlds to show
    entry -> System.out.println(entry.name)          // on selection change
);
addDrawableChild(list);

// Pre-select a world by folder name:
list.preselectByName("MyWorld");

// Refresh after external changes:
list.refresh();

// Get current selection:
FilteredWorldListWidget.WorldEntry sel = list.getSelectedOrNull();
if (sel != null) System.out.println(sel.name);
```

---

## Confirmation dialog

```java
client.setScreen(new ConfirmActionScreen(
    currentScreen,
    Text.literal("Are you sure?"),
    List.of("This action cannot be undone.", "Line 2 of body."),
    confirmed -> {
        if (confirmed) doTheThing();
    }
));
```
