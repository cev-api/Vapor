package gg.vape.config;

public enum SettingsDataType {
    GLOBAL(SettingsScope.GLOBAL, GlobalSettingsPayload.class);
    private final SettingsScope scope;
    private final Class<? extends SettingsPayload> payloadClass;
    SettingsDataType(SettingsScope scope, Class<? extends SettingsPayload> payloadClass) { this.scope = scope; this.payloadClass = payloadClass; }
    public Class<? extends SettingsPayload> getPayloadClass() { return payloadClass; }
    public SettingsScope getScope() { return scope; }
}