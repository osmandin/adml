package edu.mit.adml;

import org.json.JSONException;
import org.json.JSONString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;


public class JsonTest {


    @Test
    public void test() throws IOException, ParseException {
        final File initialFile = new File(JsonTest.class.getResource("/resource.json").getFile());
        Reader targetReader = new FileReader(initialFile);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(targetReader);
        assertThat(jsonObject.get("title")).isEqualTo("Massachusetts Institute of Technology, Undergraduate Association records");

        final JSONArray instances = (JSONArray) jsonObject.get("instances");
        final JSONObject inst = (JSONObject) instances.get(0);
        final JSONObject subContainer = (JSONObject) inst.get("sub_container");
        final JSONObject topContainer = (JSONObject) subContainer.get("top_container");
        assertThat(topContainer.get("ref")).isEqualTo("/repositories/2/top_containers/4229");

    }

}
