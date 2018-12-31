package com.tamashenning.keybindings.events;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tamashenning.keybindings.KeyBindings;
import com.tamashenning.keybindings.KeyBindingsConfig;
import com.tamashenning.keybindings.models.BindingModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GuiStaticEventHandler {

    private static int exportButtonID = 1000;
    private static int importButtonID = 1001;
    private static Gson gson = new Gson();
    private static String keyBindingsPath = KeyBindings.configurationDirectory.getPath() + "\\keybindings.json";

    private static HashMap<String, KeyBinding> currentMapping;

    @SubscribeEvent
    public static void onModifyGuiEvent(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() != null && event.getGui() instanceof GuiControls) {
            KeyBindings.logger.info("Controls settings screen loaded, injecting import/export buttons.");
            KeyBindings.logger.info("Is Controlling loaded: " + Loader.isModLoaded("controlling"));

            GuiControls gui = (GuiControls)event.getGui();
            List<GuiButton> buttons = event.getButtonList();

            if (Loader.isModLoaded("controlling")) {
                GuiButton exportButton = new GuiButton(importButtonID, gui.width / 2 - 155 + 160, gui.height - 29, 45, 20, "Import");
                buttons.add(exportButton);

                GuiButton importButton = new GuiButton(exportButtonID, gui.width / 2 - 155 + 160 + 45, gui.height - 29, 45, 20, "Export");
                buttons.add(importButton);

                for (GuiButton button : buttons) {
                    if (button.id == 200) { // done button
                        button.width = button.width / 2 - 15;
                        button.x += 15;
                    }

                    if (button.id == 201) { // reset button
                        button.width = button.width / 2;
                        button.x += button.width + 15;
                    }
                }
            }
            else { // Vanila UI
                GuiButton exportButton = new GuiButton(importButtonID, gui.width / 2 - 155, gui.height - 29, 45, 20, "Import");
                buttons.add(exportButton);

                GuiButton importButton = new GuiButton(exportButtonID, gui.width / 2 - 155 + 45, gui.height - 29, 45, 20, "Export");
                buttons.add(importButton);

                for (GuiButton button : buttons) {
                    if (button.id == 200) { // done button
                        button.width = button.width - 15;
                        button.x += 15;
                    }

                    if (button.id == 201) { // reset button
                        button.width = button.width / 2;
                        button.x += button.width + 15;
                    }
                }
            }
            event.setButtonList(buttons);

            currentMapping = new HashMap<String, KeyBinding>();
            for (KeyBinding kb : Minecraft.getMinecraft().gameSettings.keyBindings) {
                currentMapping.put(kb.getKeyDescription(), kb);
            }
        }
    }

    @SubscribeEvent
    public static void onModifiedGuiButtonClickedEvent(GuiScreenEvent.ActionPerformedEvent.Post event) throws IOException {
        if (event.getGui() != null && event.getGui() instanceof GuiControls) {
            if (event.getButton().id == exportButtonID) {

                List<BindingModel> bindings = new ArrayList<BindingModel>();

                for (KeyBinding kb : Minecraft.getMinecraft().gameSettings.keyBindings) {
                   BindingModel b = new BindingModel();
                   b.keyCategory = kb.getKeyCategory();
                   b.keyCode = kb.getKeyCode();
                   b.keyDescription = kb.getKeyDescription();
                   b.keyModifier = kb.getKeyModifier();
                   bindings.add(b);
                }

                String path = KeyBindingsConfig.ExportFullPath;
                if (path == "") {
                    path = keyBindingsPath;
                }

                try (Writer w = new FileWriter(path)) {
                    gson.toJson(bindings, w);
                    w.close();
                }

                KeyBindings.logger.info("Bindings written to: " + path);
            }

            if (event.getButton().id == importButtonID) {
                String path = KeyBindingsConfig.ExportFullPath;
                if (path == "") {
                    path = keyBindingsPath;
                }

                File keybinding = new File(path);
                if (!keybinding.exists()) {
                    KeyBindings.logger.info("Key bindigs file doesn't exist. Nothing to do.");
                    return;
                }

                KeyBindings.logger.info("Bindings read from: " + path);
                try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                    Type myType = new TypeToken<Collection<BindingModel>>(){}.getType();
                    Collection<BindingModel> bindings = gson.fromJson(br, myType);

                    for(BindingModel b : bindings) {
                        if (currentMapping.containsKey(b.keyDescription)) {
                            currentMapping.get(b.keyDescription).setKeyCode(b.keyCode);
                            KeyBindings.logger.info("Setting: " + b.keyDescription);
                        }
                        else {
                            KeyBindings.logger.info("Skipped: " + b.keyDescription);
                        }
                    }
                    br.close();
                }
            }
        }
    }
}
