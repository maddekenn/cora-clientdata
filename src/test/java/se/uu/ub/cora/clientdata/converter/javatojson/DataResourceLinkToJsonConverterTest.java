/*
 * Copyright 2015, 2016, 2018 Uppsala University Library
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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.Action;
import se.uu.ub.cora.clientdata.ActionLink;
import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataResourceLink;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;

public class DataResourceLinkToJsonConverterTest {
	private ClientDataResourceLink resourceLink;
	private DataResourceLinkToJsonConverter converter;
	private DataToJsonConverterFactorySpy dataToJsonConverterFactory;

	@BeforeMethod
	public void setUp() {
		resourceLink = ClientDataResourceLink.withNameInData("nameInData");
		resourceLink.addChild(ClientDataAtomic.withNameInDataAndValue("streamId", "aStreamId"));
		resourceLink.addChild(ClientDataAtomic.withNameInDataAndValue("filename", "aFilename"));
		resourceLink.addChild(ClientDataAtomic.withNameInDataAndValue("filesize", "12345"));
		resourceLink
				.addChild(ClientDataAtomic.withNameInDataAndValue("mimeType", "application/png"));

		JsonBuilderFactory jsonFactory = new OrgJsonBuilderFactoryAdapter();
		dataToJsonConverterFactory = new DataToJsonConverterFactorySpy();
		converter = DataResourceLinkToJsonConverter.usingJsonFactoryForClientDataLink(jsonFactory,
				resourceLink, dataToJsonConverterFactory);
	}

	@Test
	public void testToJson() {
		String jsonString = converter.toJson();
		String jsonFromSpy = "{\"children\":[{\"name\":\"streamId\"},{\"name\":\"filename\"},{\"name\":\"filesize\"},{\"name\":\"mimeType\"}],\"name\":\"nameInData\"}";
		assertEquals(jsonString, jsonFromSpy);
	}

	@Test
	public void testToJsonWithRepeatId() {
		resourceLink.setRepeatId("22");
		String jsonString = converter.toJson();
		String jsonFromSpy = "{\"repeatId\":\"22\",\"children\":[{\"name\":\"streamId\"},{\"name\":\"filename\"},{\"name\":\"filesize\"},{\"name\":\"mimeType\"}],\"name\":\"nameInData\"}";
		assertEquals(jsonString, jsonFromSpy);
	}

	@Test
	public void testToJsonWithEmptyRepeatId() {
		resourceLink.setRepeatId("");
		String jsonString = converter.toJson();
		String jsonFromSpy = "{\"children\":[{\"name\":\"streamId\"},{\"name\":\"filename\"},{\"name\":\"filesize\"},{\"name\":\"mimeType\"}],\"name\":\"nameInData\"}";
		assertEquals(jsonString, jsonFromSpy);
	}

	@Test
	public void testToJsonWithActionLink() {
		resourceLink.addActionLink("read", createReadActionLink());

		String jsonString = converter.toJson();
		String jsonFromSpy = "{\"children\":[{\"name\":\"streamId\"},{\"name\":\"filename\"},{\"name\":\"filesize\"},{\"name\":\"mimeType\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://localhost:8080/theclient/client/record/image/image:0001\",\"accept\":\"application/png\"}},\"name\":\"nameInData\"}";
		assertEquals(dataToJsonConverterFactory.calledNumOfTimes, 4);
		assertEquals(jsonString, jsonFromSpy);
		assertEquals(converter.actionLinkConverter.dataToJsonConverterFactory,
				dataToJsonConverterFactory);
	}

	private ActionLink createReadActionLink() {
		ActionLink actionLink = ActionLink.withAction(Action.READ);
		actionLink.setAccept("application/png");
		actionLink.setRequestMethod("GET");
		actionLink.setURL("http://localhost:8080/theclient/client/record/image/image:0001");
		return actionLink;
	}
}
