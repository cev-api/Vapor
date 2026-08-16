package gg.vape.fabric;

import gg.vape.Vapor;
import gg.vape.event.impl.EventPostTick;
import gg.vape.event.impl.EventPostRenderTick;
import gg.vape.event.impl.EventPreTick;
import gg.vape.event.impl.EventPreRenderTick;
import gg.vape.event.impl.EventRender2D;
import gg.vape.mapping.EventRender3DCallback;
import gg.vape.input.GlfwToVirtualKeyCodeMap;
import gg.vape.input.InputEventDispatcher;
import gg.vape.runtime.NativeBridge;
import gg.vape.utils.RenderThreadTaskQueue;
import gg.vape.utils.render.BufferedGuiRenderPrimitives;
import gg.vape.utils.render.GuiRenderPrimitives;
import gg.vape.utils.render.RenderBatchManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GL11;

/** Normal Fabric entrypoint for the recovered client implementation. */
public final class VaporFabricClient implements net.fabricmc.api.ClientModInitializer {
    private static boolean initialized;
    private static boolean initializationScheduled;
    private static boolean initializationAttempted;
    private static final boolean[] previousKeyStates = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private static final boolean[] previousMouseStates = new boolean[8];
    private static final double[] cursorX = new double[1];
    private static final double[] cursorY = new double[1];
    private static final KeyMapping OPEN_GUI_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping("key.vapor421.open_gui", InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_RIGHT_SHIFT, KeyMapping.Category.MISC));
    private static VaporClickGuiScreen activeGuiScreen;
    private static int renderFramesAfterBootstrap;
    private static GLCapabilities renderCapabilities;
    private static boolean glCapabilityFailureLogged;
    private static DeltaTracker activeRenderDeltaTracker;

    /** True only after the Fabric entrypoint has created Vapor's wrapper state. */
    public static boolean isReady() {
        return initialized && Vapor.INSTANCE != null;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            // Match the injected lifecycle: modules prepare movement, rotations
            // and placement before LocalPlayer receives its tick.
            if (initialized && Vapor.INSTANCE != null) {
                new EventPreTick().fire();
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Minecraft fires CLIENT_STARTED before the initial registries/data
            // reload has bound item components on 26.2.  Vapor's hotbar picker
            // creates ItemStacks, so start it on the first world tick instead.
            if (!initialized && client.level != null && !initializationScheduled && !initializationAttempted) {
                initializationScheduled = true;
                initializationAttempted = true;
                client.execute(() -> {
                    initializationScheduled = false;
                    if (!initialized && client.level != null) {
                        initialize();
                    }
                });
            }
            if (!initialized || Vapor.INSTANCE == null) {
                return;
            }
            processOpenGuiKey(client);
            pollKeyboard(client.getWindow().handle());
            pollMouse(client.getWindow().handle(), client.getWindow().getWidth(),
                    client.getWindow().getHeight(), client.getWindow().getScreenWidth(),
                    client.getWindow().getScreenHeight());
            new EventPostTick().fire();
        });
    }

private static boolean ensureOpenGlCapabilities() {
        try {
            renderCapabilities = GL.getCapabilities();
            return true;
        } catch (IllegalStateException ignored) {
            // A current Minecraft context exists but has not been associated
            // with LWJGL capabilities on this callback thread yet.
        }
        if (GLFW.glfwGetCurrentContext() == 0L) {
            return false;
        }
        try {
            GL.createCapabilities();
            renderCapabilities = GL.getCapabilities();
            return true;
        } catch (Throwable error) {
            if (!glCapabilityFailureLogged) {
                glCapabilityFailureLogged = true;
                Vapor.debugLog("Unable to install Vapor OpenGL capabilities: " + error.getMessage());
            }
            return false;
        }
    }

    private static boolean restoreOpenGlCapabilities() {
        if (GLFW.glfwGetCurrentContext() == 0L) {
            return false;
        }
        if (renderCapabilities != null) {
            GL.setCapabilities(renderCapabilities);
        }
        return ensureOpenGlCapabilities();
    }

    private static void renderHud() {
        // Native HUD renderers may leave a narrow scissor rectangle active
        // (Jade does this while its tooltip is present). Vapor uses a legacy
        // immediate/batched renderer, so start its independent overlay pass
        // with an unclipped transparent HUD state.
        BufferedGuiRenderPrimitives.scissorRect = null;
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        EventRender2D.create();
    }

    /**
     * Runs after Minecraft 26.2's deferred {@code GuiRenderer} has completed.
     * The recovered ClickGUI uses its own OpenGL batches, so rendering it from
     * a HUD/screen extraction callback makes Minecraft's later GUI pass paint
     * over it. This is invoked by the Fabric render hook after that GUI pass.
     */
    public static void beginRender(DeltaTracker deltaTracker) {
        activeRenderDeltaTracker = deltaTracker;
        if (initialized && Vapor.INSTANCE != null && restoreOpenGlCapabilities()) {
            new EventPreRenderTick(deltaTracker).fire();
        }
    }

    /**
     * Runs after Minecraft 26.2's deferred GUI pass. Keeping the complete
     * legacy post-render lifecycle here gives 2D modules a real Fabric frame
     * while ensuring the ClickGUI is still above native screens.
     */
    public static void finishRenderCurrentFrame() {
        DeltaTracker deltaTracker = activeRenderDeltaTracker;
        if (deltaTracker != null) {
            finishRender(deltaTracker);
        }
    }

    public static void finishRender(DeltaTracker deltaTracker) {
        // The render hook runs before the first world tick. Initialization is
        // scheduled from the tick lifecycle after registries are ready; never
        // construct the original frame tree without the Vapor singleton.
        if (!initialized || Vapor.INSTANCE == null) {
            return;
        }
        if (!gg.vape.module.none.ClientSettings.framesInitialized) {
            if (renderFramesAfterBootstrap++ < 1 || !restoreOpenGlCapabilities()) {
                return;
            }
            try {
                gg.vape.module.none.ClientSettings.initializeFrames();
            } catch (Throwable error) {
                Vapor.debugLog("GUI bootstrap deferred: " + Vapor.formatThrowable(error));
                return;
            }
        }
        if (!initialized || Vapor.INSTANCE == null || !gg.vape.module.none.ClientSettings.framesInitialized) {
            return;
        }
        gg.vape.module.none.ClientSettings clientSettings =
                Vapor.INSTANCE.getModManager().getMod(gg.vape.module.none.ClientSettings.class);
        RenderThreadTaskQueue.runPendingTasks();
        if (activeGuiScreen == null && clientSettings != null && clientSettings.isInputEnabled()) {
            renderHud();
        }
        if (activeGuiScreen != null && clientSettings != null && !clientSettings.isInputEnabled()) {
            clientSettings.renderGui();
        }
        new EventPostRenderTick(deltaTracker.getGameTimeDeltaPartialTick(false)).fire();
        RenderThreadTaskQueue.runPendingTasks();
        if (GuiRenderPrimitives.d()) {
            RenderBatchManager.getInstance().flushGuiBatches(deltaTracker.getGameTimeDeltaPartialTick(false));
        }
    }

    /** Called from the end of GameRenderer.renderLevel, while world transforms are active. */
    public static void renderWorld() {
        if (initialized && Vapor.INSTANCE != null && restoreOpenGlCapabilities()) {
            new EventRender3DCallback(RenderSystem.getModelViewMatrixCopy()).fire();
        }
    }
    private static void processOpenGuiKey(Minecraft client) {
        while (OPEN_GUI_KEY.consumeClick()) {
            gg.vape.module.none.ClientSettings clientSettings =
                    Vapor.INSTANCE.getModManager().getMod(gg.vape.module.none.ClientSettings.class);
            if (clientSettings == null) {
                continue;
            }
            if (activeGuiScreen != null) {
                activeGuiScreen.onClose();
                continue;
            }
            // Do not replace vanilla/container screens; this behaves like a
            // normal Fabric key mapping and opens only from in-game play.
            if (gg.vape.wrapper.impl.Minecraft.currentScreen().getObject() != null) {
                continue;
            }
            clientSettings.toggle();
            if (!clientSettings.isInputEnabled()) {
                activeGuiScreen = new VaporClickGuiScreen();
                client.gui.setScreen(activeGuiScreen);
            }
        }
    }

    private static final class VaporClickGuiScreen extends Screen {
        private VaporClickGuiScreen() {
            super(Component.empty());
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }

        @Override
        public boolean isInGameUi() {
            return true;
        }

        @Override
        public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            // Vapor renders its own backdrop; do not cover it with Screen's default layer.
        }

        @Override
        public boolean keyPressed(KeyEvent event) {
            int virtualKey = GlfwToVirtualKeyCodeMap.toVirtualKey(event.key());
            if (virtualKey > 0) {
                // The recovered UI was written against the original window
                // callback dispatcher. Fabric does not send those callbacks,
                // so translate native Screen key events through the same path.
                InputEventDispatcher.getInstance().dispatch(256, virtualKey, event.scancode());
                if (isTextControlKey(virtualKey)) {
                    InputEventDispatcher.getInstance().dispatch(258, virtualKey, 0L);
                } else if ((event.modifiers() & GLFW.GLFW_MOD_CONTROL) != 0) {
                    if (event.key() == GLFW.GLFW_KEY_V) {
                        InputEventDispatcher.getInstance().dispatch(258, 22L, 0L);
                    } else if (event.key() == GLFW.GLFW_KEY_C) {
                        InputEventDispatcher.getInstance().dispatch(258, 3L, 0L);
                    }
                }
            }
            // Keep vanilla from acting on hotkeys while this non-pausing UI is open.
            return true;
        }

        @Override
        public boolean charTyped(CharacterEvent event) {
            int codepoint = event.codepoint();
            if (Character.isBmpCodePoint(codepoint)) {
                InputEventDispatcher.getInstance().dispatch(258, codepoint, 0L);
            }
            return true;
        }

        @Override
        public void onClose() {
            super.onClose();
            activeGuiScreen = null;
            gg.vape.module.none.ClientSettings clientSettings =
                    Vapor.INSTANCE.getModManager().getMod(gg.vape.module.none.ClientSettings.class);
            if (clientSettings != null && !clientSettings.isInputEnabled()) {
                clientSettings.toggle();
            }
        }

        private static boolean isTextControlKey(int virtualKey) {
            return virtualKey == 8 || virtualKey == 9 || virtualKey == 13 || virtualKey == 27
                    || virtualKey == 35 || virtualKey == 36 || virtualKey == 37 || virtualKey == 39;
        }
    }

    private static void pollKeyboard(long windowHandle) {
        InputEventDispatcher dispatcher = InputEventDispatcher.getInstance();
        if (dispatcher.getWindowHandle() != windowHandle) {
            dispatcher.setWindowHandle(windowHandle);
        }
        for (int glfwKey = 0; glfwKey <= GLFW.GLFW_KEY_LAST; ++glfwKey) {
            int virtualKey = GlfwToVirtualKeyCodeMap.toVirtualKey(glfwKey);
            // GLFW rejects values below GLFW_KEY_SPACE; only query keys that
            // the legacy dispatcher can actually represent.
            if (virtualKey <= 0) {
                continue;
            }
            boolean keyDown = GLFW.glfwGetKey(windowHandle, glfwKey) != GLFW.GLFW_RELEASE;
            if (previousKeyStates[glfwKey] == keyDown) {
                continue;
            }
            previousKeyStates[glfwKey] = keyDown;
            // Right Shift is handled by the Fabric KeyMapping above. Sending it
            // through the legacy bind dispatcher as well would toggle twice.
            if (glfwKey != GLFW.GLFW_KEY_RIGHT_SHIFT) {
                dispatcher.getKeyboardState().setKeyState(virtualKey, keyDown);
            }
        }
    }

    private static void pollMouse(long windowHandle, int windowWidth, int windowHeight,
                                  int framebufferWidth, int framebufferHeight) {
        InputEventDispatcher dispatcher = InputEventDispatcher.getInstance();
        GLFW.glfwGetCursorPos(windowHandle, cursorX, cursorY);
        // The recovered renderer converts framebuffer coordinates into its own
        // logical 2x UI space. Feeding Minecraft's GUI values applies that
        // conversion twice, shifting all hit-testing up-left.
        int mouseX = windowWidth > 0 ? (int)(cursorX[0] * framebufferWidth / windowWidth) : 0;
        int mouseY = windowHeight > 0 ? (int)(cursorY[0] * framebufferHeight / windowHeight) : 0;
        dispatcher.getMouseState().updateCursorPosition(mouseX, mouseY);
        for (int button = 0; button < previousMouseStates.length; ++button) {
            boolean buttonDown = GLFW.glfwGetMouseButton(windowHandle, button) != GLFW.GLFW_RELEASE;
            if (previousMouseStates[button] == buttonDown) {
                continue;
            }
            previousMouseStates[button] = buttonDown;
            dispatcher.getMouseState().setButtonState(button, buttonDown);
        }
    }

    private static synchronized void initialize() {
        if (initialized) {
            return;
        }
        try {
            Vapor vape = new Vapor();
            vape.loadMappings();
            vape.initializeManagers();
            initialized = true;
            System.out.println("[Vapor421] Fabric client initialized");
        } catch (Throwable error) {
            NativeBridge.sce("Fabric initialization failed: " + Vapor.formatThrowable(error));
        }
    }
}
