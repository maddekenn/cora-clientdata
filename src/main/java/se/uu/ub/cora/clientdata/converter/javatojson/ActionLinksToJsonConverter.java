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

package se.uu.ub.cora.clientdata.converter.javatojson;

import java.util.Map;
import java.util.Map.Entry;

import se.uu.ub.cora.clientdata.ActionLink;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.JsonObjectBuilder;

public class ActionLinksToJsonConverter extends DataToJsonConverter {

	private Map<String, ActionLink> actionLinks;
	private JsonBuilderFactory jsonBuilderFactory;
	private JsonObjectBuilder actionLinksObject;
	DataToJsonConverterFactory dataToJsonConverterFactory;

	public ActionLinksToJsonConverter(JsonBuilderFactory jsonBuilderFactory,
			Map<String, ActionLink> actionLinks,
			DataToJsonConverterFactory dataToJsonConverterFactory) {
		this.jsonBuilderFactory = jsonBuilderFactory;
		this.actionLinks = actionLinks;
		this.dataToJsonConverterFactory = dataToJsonConverterFactory;
		actionLinksObject = jsonBuilderFactory.createObjectBuilder();
	}

	@Override
	public JsonObjectBuilder toJsonObjectBuilder() {
		addActionLinksToBuilderObject();
		return actionLinksObject;
	}

	private void addActionLinksToBuilderObject() {

		for (Entry<String, ActionLink> actionLinkEntry : actionLinks.entrySet()) {
			ActionLink actionLink = actionLinkEntry.getValue();
			JsonObjectBuilder internalLinkBuilder = jsonBuilderFactory.createObjectBuilder();
			String actionString = actionLink.getAction().toString().toLowerCase();
			internalLinkBuilder.addKeyString("rel", actionString);
			internalLinkBuilder.addKeyString("url", actionLink.getURL());
			internalLinkBuilder.addKeyString("requestMethod", actionLink.getRequestMethod());
			internalLinkBuilder.addKeyString("accept", actionLink.getAccept());
			internalLinkBuilder.addKeyString("contentType", actionLink.getContentType());

			possiblyAddBodyToBuilder(actionLink, internalLinkBuilder);
			actionLinksObject.addKeyJsonObjectBuilder(actionString, internalLinkBuilder);
		}
	}

	private void possiblyAddBodyToBuilder(ActionLink actionLink,
			JsonObjectBuilder internalLinkBuilder) {
		if (actionLinkHasBody(actionLink)) {
			DataGroupToJsonConverter dataGroupToJsonConverter = DataGroupToJsonConverter
					.usingJsonFactoryAndConverterFactoryForClientDataGroup(jsonBuilderFactory,
							dataToJsonConverterFactory, actionLink.getBody());
			internalLinkBuilder.addKeyJsonObjectBuilder("body",
					dataGroupToJsonConverter.toJsonObjectBuilder());
		}
	}

	private boolean actionLinkHasBody(ActionLink actionLink) {
		return actionLink.getBody() != null;
	}

}
