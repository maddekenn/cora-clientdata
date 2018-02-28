/*
 * Copyright 2015, 2018 Uppsala University Library
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

public final class ClientDataRecord implements ClientData {
	private ClientDataGroup clientDataGroup;
	// private Map<String, ActionLink> actionLinks = new LinkedHashMap<>();
	private ClientDataActionLinks actionLinks;

	public static ClientDataRecord withClientDataGroup(ClientDataGroup clientDataGroup) {
		return new ClientDataRecord(clientDataGroup);
	}

	private ClientDataRecord(ClientDataGroup clientDataGroup) {
		this.clientDataGroup = clientDataGroup;
	}

	public ClientDataGroup getClientDataGroup() {
		return clientDataGroup;
	}

	// public void addActionLink(String key, ActionLink actionLink) {
	// actionLinks.put(key, actionLink);
	// }

	public ClientDataActionLinks getActionLinks() {
		return actionLinks;
	}
	//
	// public ActionLink getActionLink(String key) {
	// return actionLinks.get(key);
	// }

	public void setActionLinks(ClientDataActionLinks actionLinks) {
		this.actionLinks = actionLinks;
	}

}
