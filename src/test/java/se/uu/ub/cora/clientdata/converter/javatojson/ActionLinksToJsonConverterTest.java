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

import static org.testng.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.Action;
import se.uu.ub.cora.clientdata.ActionLink;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.testdata.ClientDataCreator;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;

public class ActionLinksToJsonConverterTest {
	@Test
	public void testConvert() {
		ActionLink actionLink = createReadActionLink();
		Map<String, ActionLink> actionLinks = new LinkedHashMap<>();
		actionLinks.put("read", actionLink);

		JsonBuilderFactory jsonFactory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverterFactory dataToJsonFactory = new DataToJsonConverterFactorySpy();
		ActionLinksToJsonConverter converter = new ActionLinksToJsonConverter(jsonFactory,
				actionLinks, dataToJsonFactory);
		assertEquals(converter.toJson(), "{"
				+ "\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\","
				+ "\"contentType\":\"application/metadata_record+json\","
				+ "\"url\":\"http://localhost:8080/theclient/client/record/place/place:0001\","
				+ "\"accept\":\"application/metadata_record+json\"}" + "}");
	}

	private ActionLink createReadActionLink() {
		ActionLink actionLink = ActionLink.withAction(Action.READ);
		actionLink.setAccept("application/metadata_record+json");
		actionLink.setContentType("application/metadata_record+json");
		actionLink.setRequestMethod("GET");
		actionLink.setURL("http://localhost:8080/theclient/client/record/place/place:0001");
		return actionLink;
	}

	@Test
	public void testConvertJsonWithBody() {
		ActionLink actionLink = createReadActionLink();
		Map<String, ActionLink> actionLinks = new LinkedHashMap<>();
		actionLinks.put("read", actionLink);
		ClientDataGroup workOrder = ClientDataCreator.createWorkOrder();
		actionLink.setBody(workOrder);

		JsonBuilderFactory jsonFactory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverterFactorySpy dataToJsonFactory = new DataToJsonConverterFactorySpy();

		ActionLinksToJsonConverter converter = new ActionLinksToJsonConverter(jsonFactory,
				actionLinks, dataToJsonFactory);
		String jsonFromSpy = "{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"body\":{\"children\":[{\"name\":\"recordType\"},{\"name\":\"recordId\"},{\"name\":\"type\"}],\"name\":\"workOrder\"},\"contentType\":\"application/metadata_record+json\",\"url\":\"http://localhost:8080/theclient/client/record/place/place:0001\",\"accept\":\"application/metadata_record+json\"}}";
		assertEquals(converter.toJson(), jsonFromSpy);
		assertEquals(dataToJsonFactory.calledNumOfTimes, 3);
	}
}
