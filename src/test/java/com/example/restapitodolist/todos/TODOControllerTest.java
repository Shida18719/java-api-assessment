package com.example.restapitodolist.todos;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.restapitodolist.RestapitodolistApplication;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


//Tried ou Integration Testing (following the restapiexercise test ious package)
@SpringBootTest(classes = RestapitodolistApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TODOControllerTest {

    @LocalServerPort
    private int port;

    private URI baseURI;

    @Autowired
    private TestRestTemplate restTemplate;

    private List<TODO> defaultTODOs = new ArrayList<>(){
        {
            add(new TODO("CBF intro to java", "Learn about Java", false));
            add(new TODO("CBF intro Spring Boot", "Learn about Spring", true));
            add(new TODO("CBF intro to REST-API", "Learn about REST", false));
        }
    };
    
    @MockBean
    private TODOService todoService;

    @BeforeEach
    void setUp() throws RuntimeException {
        this.baseURI = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/todos")
                .build()
                .toUri();

        when(todoService.getAllTODOs()).thenReturn(defaultTODOs);
    }

    @Test
    @Description("GET /api/todos - gets all TODOs")
    void getAllTODOs() throws URISyntaxException {
        // Act
		ResponseEntity<List<TODO>> response = restTemplate.exchange(baseURI, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<TODO>>() {
				});
		List<TODO> responseTODOs = response.getBody();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(responseTODOs);
		assertEquals(defaultTODOs.size(), responseTODOs.size());
		verify(todoService).getAllTODOs();
    }

}
