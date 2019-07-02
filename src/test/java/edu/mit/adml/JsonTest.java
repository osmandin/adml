package edu.mit.adml;

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
        final File initialFile = new File(JsonTest.class.getResource("/sample.json").getFile());
        Reader targetReader = new FileReader(initialFile);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(targetReader);
        assertThat(jsonObject.get("title")).isEqualTo("Massachusetts Institute of Technology, Undergraduate Association records");

    }

}
