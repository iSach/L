package be.isach.eru.module;

import net.dv8tion.jda.core.hooks.EventListener;

public interface Module extends EventListener {
	void enable();
	void disable();
	String getName();
}