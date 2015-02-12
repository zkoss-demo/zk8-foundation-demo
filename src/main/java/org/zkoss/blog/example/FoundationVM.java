/** FoundationVM.java.

	Purpose:
		
	Description:
		
	History:
		3:43:57 PM Feb 9, 2015, Created by jumperchen

Copyright (C) 2015 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.blog.example;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.metainfo.PageDefinition;

/**
 * @author jumperchen
 */
public class FoundationVM {
	private String currentTemplate = "Banded";
	private List<String> templates = Arrays.asList("Banded", "Blog", "Feed",
			"Grid", "Orbit Home", "Banner Home", "Sidebar", "Contact",
			"Marketing", "Realty", "So Boxy", "Store", "Workspace",
			"Marketing 2", "Product", "Portfolio");
	private static ConcurrentMap<String, String> _sources = new ConcurrentHashMap<String, String>();
	private static ReentrantLock _lock = new ReentrantLock();
	public void setCurrentTemplate(String name) {
		currentTemplate = name;
	}
	public String getCurrentTemplate() {
		return currentTemplate;
	}
	public void setTemplates(List<String> templates) {
		this.templates = templates;
	}
	public List<String> getTemplates() {
		return this.templates;
	}
	public String toLink(String name) {
		return "template/" + name.toLowerCase().replace(" ", "_") + ".zhtml";
	}
	public String toIcon(String name) {
		return "icon/" + name.toLowerCase().replace(" ", "_") + ".zhtml";
	}
	public String toSource(String name) {
		String source = _sources.get(name);
		if (source == null) {
			_lock.lock();
			try {
				final String link = toLink(name);
				source = FileUtils.readFileToString(new File(Sessions.getCurrent().getWebApp().getRealPath(link)), "UTF-8");
				_sources.putIfAbsent(name, source);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				_lock.unlock();
			}
		}
		return source;
	}
	
	@Command("changeTemplate")
	@NotifyChange("currentTemplate")
	public void doChangeTemplate(@BindingParam("template") String template) {
		currentTemplate = template;
	}
}
