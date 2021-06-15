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

package me.lorenzo0111.js.shader;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class Shader {
    private final ServicesManager manager;
    private final JavaPlugin plugin;

    public Shader(JavaPlugin plugin, ServicesManager manager) {
        this.manager = manager;
        this.plugin = plugin;
    }

    public void shade() {
        ScriptEngineManager engine = new ScriptEngineManager();
        ScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine.registerEngineName("JavaScript", factory);
        manager.register(ScriptEngineManager.class, engine, plugin, ServicePriority.Highest);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private JavaPlugin plugin;

        public JavaPlugin plugin() {
            return plugin;
        }

        public Builder plugin(JavaPlugin plugin) {
            this.plugin = plugin;
            return this;
        }

        public Shader build() {
            return new Shader(plugin,plugin.getServer().getServicesManager());
        }
    }
}
