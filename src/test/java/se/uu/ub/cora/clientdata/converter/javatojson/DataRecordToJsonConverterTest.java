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

import java.util.HashMap;

import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.Action;
import se.uu.ub.cora.clientdata.ActionLink;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;

public class DataRecordToJsonConverterTest {

	private DataToJsonConverterFactorySpy dataToJsonConverterFactory;
	private DataRecordToJsonConverter dataRecordToJsonConverter;

	@Test
	public void testToJson() {
		ClientDataGroup clientDataGroup = ClientDataGroup.withNameInData("groupNameInData");
		ClientDataRecord clientDataRecord = ClientDataRecord.withClientDataGroup(clientDataGroup);

		String jsonString = getRecordAsJsonString(clientDataRecord);
		assertEquals(jsonString, "{\"record\":{\"data\":{\"name\":\"groupNameInData\"}}}");
		assertEquals(dataToJsonConverterFactory.calledNumOfTimes, 1);
	}

	@Test
	public void testToJsonWithDataActionLinksIsNull() {
		ClientDataGroup clientDataGroup = ClientDataGroup.withNameInData("groupNameInData");
		ClientDataRecord clientDataRecord = ClientDataRecord.withClientDataGroup(clientDataGroup);

		clientDataRecord.setActionLinks(null);

		String jsonString = getRecordAsJsonString(clientDataRecord);

		assertEquals(jsonString, "{\"record\":{\"data\":{\"name\":\"groupNameInData\"}}}");
		assertEquals(dataToJsonConverterFactory.calledNumOfTimes, 1);
	}

	private String getRecordAsJsonString(ClientDataRecord clientDataRecord) {
		JsonBuilderFactory jsonFactory = new OrgJsonBuilderFactoryAdapter();
		dataToJsonConverterFactory = new DataToJsonConverterFactorySpy();

		dataRecordToJsonConverter = DataRecordToJsonConverter.usingJsonFactoryForClientDataRecord(
				jsonFactory, clientDataRecord, dataToJsonConverterFactory);
		return dataRecordToJsonConverter.toJson();
	}

	@Test
	public void testToJsonWithDataActionLinksButActionLinksIsEmpty() {
		ClientDataGroup clientDataGroup = ClientDataGroup.withNameInData("groupNameInData");
		ClientDataRecord clientDataRecord = ClientDataRecord.withClientDataGroup(clientDataGroup);

		clientDataRecord.setActionLinks(new HashMap<>());

		String jsonString = getRecordAsJsonString(clientDataRecord);

		assertEquals(jsonString, "{\"record\":{\"data\":{\"name\":\"groupNameInData\"}}}");
		assertEquals(dataToJsonConverterFactory.calledNumOfTimes, 1);
	}

	@Test
	public void testToJsonWithActionLinks() {
		ClientDataGroup clientDataGroup = ClientDataGroup.withNameInData("groupNameInData");
		ClientDataRecord clientDataRecord = ClientDataRecord.withClientDataGroup(clientDataGroup);

		clientDataRecord.addActionLink("read", createReadActionLink());
		String jsonString = getRecordAsJsonString(clientDataRecord);

		assertEquals(jsonString, "{\"record\":{\"data\":{\"name\":\"groupNameInData\"}"
				+ ",\"actionLinks\":{" + "\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\","
				+ "\"contentType\":\"application/metadata_record+json\","
				+ "\"url\":\"http://localhost:8080/theclient/client/record/place/place:0001\","
				+ "\"accept\":\"application/metadata_record+json\"}" + "}}}");
		assertEquals(dataRecordToJsonConverter.actionLinkConverter.dataToJsonConverterFactory,
				dataToJsonConverterFactory);
	}

	private ActionLink createReadActionLink() {
		ActionLink actionLink = ActionLink.withAction(Action.READ);
		actionLink.setAccept("application/metadata_record+json");
		actionLink.setContentType("application/metadata_record+json");
		actionLink.setRequestMethod("GET");
		actionLink.setURL("http://localhost:8080/theclient/client/record/place/place:0001");
		return actionLink;
	}

}
