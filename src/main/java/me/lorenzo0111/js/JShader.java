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

import me.lorenzo0111.js.shader.ShadeChecker;
import me.lorenzo0111.js.shader.Shader;
import me.lorenzo0111.js.utils.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.script.ScriptEngineManager;

public class JShader extends JavaPlugin {

    @Override
    public void onEnable() {
        this.load();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    private void load() {
        this.getLogger().info("Looking for Java Version...");
        boolean legacy = VersionUtil.isLegacy();
        this.getLogger().info("    Java Version: " + VersionUtil.getVersion());
        this.getLogger().info("    Legacy Java: " + legacy);

        if (legacy) {
            this.getLogger().warning("You don't need to use this plugin because you are using a legacy java version.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.getLogger().info("Checking if JavaScript engine is already shaded..");
        boolean shaded = ShadeChecker.builder()
                .plugin(this)
                .checkClass(ScriptEngineManager.class)
                .build()
                .check();

        if (shaded) {
            this.getLogger().info("JavaScript engine is already shaded.");
            return;
        }

        Shader.builder()
                .plugin(this)
                .build()
                .shade();

        this.getLogger().info("JavaScript engine shaded.");

    }
}
