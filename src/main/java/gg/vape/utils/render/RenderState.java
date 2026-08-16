package gg.vape.utils.render;

import org.jetbrains.annotations.Nullable;

public final class RenderState<K, V> {
    private final K key;
    @Nullable private final V value;
    private RenderState(K key, @Nullable V value) { this.key = key; this.value = value; }
    public static <K,V> RenderState<K,V> create(K key, V value) { return new RenderState<>(key, value); }
    public K getKey() { return key; }
    @Nullable public V getValue() { return value; }
}