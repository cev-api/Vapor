package gg.vape.input;

import gg.vape.input.InputEventDispatcher;
import gg.vape.input.KeyboardInputState;
import gg.vape.runtime.NativeBridge;

public class KeyboardInput {
    private static KeyboardInputState cachedState;

    public static boolean isKeyDown(int keyCode) {
        return KeyboardInput.getState().isKeyDown(keyCode);
    }

    public static String getKeyName(int keyCode) {
        if (keyCode < 0) {
            int mouseButton = keyCode + 100;
            return "M" + mouseButton;
        }
        return NativeBridge.gkn(keyCode);
    }


    public static KeyboardInputState getState() {
        if (cachedState == null) {
            cachedState = InputEventDispatcher.getInstance().getKeyboardState();
        }
        return cachedState;
    }
}
