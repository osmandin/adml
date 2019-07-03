package edu.mit.adml;


import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Ignore
public class HttpRequestTest {

    private static final String HTTP_LOCALHOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testListPage() throws Exception {
        assertThat(this.restTemplate.getForObject(HTTP_LOCALHOST + port + "/adml/results#",
                String.class)).containsIgnoringCase("Inventory");
    }

    @Test
    public void testEditPage() throws Exception {
        assertThat(this.restTemplate.getForObject(HTTP_LOCALHOST + port + "/adml/singleitem/?id=1",
                String.class)).containsIgnoringCase("Update Item");
    }

    /**
     * Test doing a GET
     */
    @Test
    public void testAddPage() throws Exception {
        assertThat(this.restTemplate.getForObject(HTTP_LOCALHOST + port + "/adml/add",
                String.class)).containsIgnoringCase("New item");
    }

    /**
     * Test adding a POST
     */
    @Test
    public void testAddItem() throws Exception {
        final MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        final String testDepartment = "aspace_001_component";
        requestMap.add("refID", testDepartment);
        assertThat(restTemplate.postForObject(HTTP_LOCALHOST+ port + "/adml/add", requestMap, String.class))
                .contains(testDepartment);
    }


}