package gg.vape.utils.render;

import gg.vape.Vapor;
import gg.vape.utils.render.GpuVendor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL;

public class OpenGlDeviceInfo {
    public static String vendorName;
    public static GpuVendor gpuVendor;
    private static int legacyValue;
    public static String rendererName;
    public static String versionName;

    public static int getLegacyConstant() {
        int ignoredLegacyValue = OpenGlDeviceInfo.getLegacyValue();
        return 112;
    }

    public static int getLegacyValue() {
        return legacyValue;
    }

    public static void collectDeviceInfo() {
        try {
            if (GL.getCapabilities() == null) {
                vendorName = "Unknown Vendor";
                rendererName = "Unknown GPU";
                versionName = "Unknown Version";
                gpuVendor = GpuVendor.UNKNOWN;
                return;
            }
            String renderer = GL11.glGetString((int)7937);
            String vendor = GL11.glGetString((int)7936);
            String version = GL11.glGetString((int)7938);
            vendorName = vendor != null ? vendor : "Unknown Vendor";
            rendererName = renderer != null ? renderer : "Unknown GPU";
            versionName = version != null ? version : "Unknown Version";
            gpuVendor = OpenGlDeviceInfo.detectGpuVendor(vendor);
        }
        catch (Exception exception) {
            Vapor.debugLog("Error getting OpenGL: " + exception.getMessage());
        }
    }

    public static void setLegacyValue(int value) {
        legacyValue = value;
    }

    static {
        OpenGlDeviceInfo.setLegacyValue(0);
        gpuVendor = GpuVendor.UNKNOWN;
        vendorName = null;
        versionName = null;
        rendererName = null;
    }

    private static Exception propagateException(Exception exception) {
        return exception;
    }

    private static GpuVendor detectGpuVendor(String vendorName) {
        if (vendorName == null) {
            return GpuVendor.UNKNOWN;
        }
        switch (vendorName) {
            case "NVIDIA Corporation": {
                return GpuVendor.NVIDIA;
            }
            case "Intel": 
            case "Intel Open Source Technology Center": {
                return GpuVendor.INTEL;
            }
            case "AMD": 
            case "ATI Technologies Inc.": {
                return GpuVendor.AMD;
            }
        }
        return GpuVendor.UNKNOWN;
    }

    public static void appendDeviceInfo(StringBuilder output) {
        OpenGlDeviceInfo.collectDeviceInfo();
        output.append("GPU Vendor: ").append(gpuVendor.name()).append(" (").append(vendorName).append(")\n");
        output.append("GPU Renderer: ").append(rendererName).append('\n');
        output.append("OpenGL Version: ").append(versionName).append('\n');
    }

    private static void logCapabilities() {
        try {
            Vapor.debugLog("MAX_TEXTURE_SIZE - " + GL11.glGetInteger((int)3379));
            Vapor.debugLog("MAX_TEXTURE_UNITS - " + GL11.glGetInteger((int)34930));
            Vapor.debugLog("MAX_VERTEX_ATTRIBS - " + GL11.glGetInteger((int)34921));
            Vapor.debugLog("MAX_COLOR_ATTACHMENTS - " + GL11.glGetInteger((int)36063));
            Vapor.debugLog("MAX_VIEWPORT_WIDTH - " + GL11.glGetInteger((int)3386));
            Vapor.debugLog("MAX_VERTEX_UNIFORM_COMPONENTS - " + GL11.glGetInteger((int)35658));
            Vapor.debugLog("MAX_FRAGMENT_UNIFORM_COMPONENTS - " + GL11.glGetInteger((int)35657));
        }
        catch (Exception exception) {
            Vapor.debugLog("Failed to collect GPU capabilities: " + exception.getMessage());
        }
    }

    public static void logDeviceInfo() {
        Vapor.debugLog("===== Graphics Information =====");
        Vapor.debugLog("Vendor: " + gpuVendor.name() + " (" + vendorName + ")");
        Vapor.debugLog("Device Name: " + rendererName);
        Vapor.debugLog("Driver Version: " + versionName);
        Vapor.debugLog("---GPU Capabilities---");
        OpenGlDeviceInfo.logCapabilities();
        Vapor.debugLog("================================");
    }
}
