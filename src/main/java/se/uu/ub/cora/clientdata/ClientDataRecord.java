/*
 * Copyright 2015 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.clientdata;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class ClientDataRecord implements ClientData {
	private ClientDataGroup clientDataGroup;
	private Set<String> keys = new LinkedHashSet<>();
	private Map<String, ActionLink> actionLinks = new LinkedHashMap<>();

	public static ClientDataRecord withClientDataGroup(ClientDataGroup clientDataGroup) {
		return new ClientDataRecord(clientDataGroup);
	}

	private ClientDataRecord(ClientDataGroup clientDataGroup) {
		this.clientDataGroup = clientDataGroup;
	}

	public ClientDataGroup getClientDataGroup() {
		return clientDataGroup;
	}

	public void addKey(String key) {
		keys.add(key);
	}

	public Set<String> getKeys() {
		return keys;
	}

	public void addActionLink(String key, ActionLink actionLink) {
		actionLinks.put(key, actionLink);
	}

	public Map<String, ActionLink> getActionLinks() {
		return actionLinks;
	}

	public ActionLink getActionLink(String key) {
		return actionLinks.get(key);
	}

	public void setActionLinks(Map<String, ActionLink> actionLinks) {
		this.actionLinks = actionLinks;
	}

}