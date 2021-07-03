/*
 *  This file is part of jshader, licensed under the MIT License.
 *
 *  Copyright (c) Lorenzo0111
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.lorenzo0111.js;

import me.lorenzo0111.js.plugin.JShaderPlugin;
import me.lorenzo0111.js.shader.ShadeChecker;
import me.lorenzo0111.js.shader.Shader;
import me.lorenzo0111.js.utils.VersionUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

@SuppressWarnings("unused")
public class JShader {
    private static JShader instance;
    private final JavaPlugin plugin;

    private JShader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static @NotNull JShader instance() {
        if (instance == null) instance = new JShader(JShaderPlugin.getInstance());

        return instance;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull JShader customInstance(JavaPlugin plugin) {
        return new JShader(plugin);
    }

    public void load() {
        plugin.getLogger().info("Looking for Java Version...");
        boolean legacy = VersionUtil.isLegacy();
        plugin.getLogger().info("    Java Version: " + VersionUtil.getVersion());
        plugin.getLogger().info("    Legacy: " + legacy);

        if (legacy) {
            plugin.getLogger().warning("You don't need to use this plugin because you are using a legacy java version.");
            return;
        }

        plugin.getLogger().info("Checking if JavaScript engine is already shaded..");

        if (!this.shadeOnly()) {
            plugin.getLogger().info("JavaScript engine is already shaded.");
            return;
        }

        plugin.getLogger().info("JavaScript engine shaded.");
    }

    public boolean shadeOnly() {
        ScriptEngineManager engine = new ScriptEngineManager();
        ScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine.registerEngineName("JavaScript", factory);

        boolean shaded = ShadeChecker.builder()
                .plugin(plugin)
                .checkClass(ScriptEngineManager.class)
                .build()
                .check();

        if (shaded) {
            return false;
        }

        this.getShader()
                .shade(ScriptEngineManager.class, engine);

        return true;
    }

    public Shader getShader() {
        return Shader.builder()
                .plugin(plugin)
                .build();
    }

}
