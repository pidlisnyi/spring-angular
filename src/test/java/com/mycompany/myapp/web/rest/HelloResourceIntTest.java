package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Hello;
import com.mycompany.myapp.repository.HelloRepository;
import com.mycompany.myapp.service.HelloService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the HelloResource REST controller.
 *
 * @see HelloResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class HelloResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    @Inject
    private HelloRepository helloRepository;

    @Inject
    private HelloService helloService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHelloMockMvc;

    private Hello hello;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HelloResource helloResource = new HelloResource();
        ReflectionTestUtils.setField(helloResource, "helloService", helloService);
        this.restHelloMockMvc = MockMvcBuilders.standaloneSetup(helloResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        helloRepository.deleteAll();
        hello = new Hello();
        hello.setName(DEFAULT_NAME);
        hello.setTitle(DEFAULT_TITLE);
    }

    @Test
    public void createHello() throws Exception {
        int databaseSizeBeforeCreate = helloRepository.findAll().size();

        // Create the Hello

        restHelloMockMvc.perform(post("/api/hellos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hello)))
                .andExpect(status().isCreated());

        // Validate the Hello in the database
        List<Hello> hellos = helloRepository.findAll();
        assertThat(hellos).hasSize(databaseSizeBeforeCreate + 1);
        Hello testHello = hellos.get(hellos.size() - 1);
        assertThat(testHello.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHello.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    public void getAllHellos() throws Exception {
        // Initialize the database
        helloRepository.save(hello);

        // Get all the hellos
        restHelloMockMvc.perform(get("/api/hellos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hello.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    public void getHello() throws Exception {
        // Initialize the database
        helloRepository.save(hello);

        // Get the hello
        restHelloMockMvc.perform(get("/api/hellos/{id}", hello.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hello.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    public void getNonExistingHello() throws Exception {
        // Get the hello
        restHelloMockMvc.perform(get("/api/hellos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateHello() throws Exception {
        // Initialize the database
        helloService.save(hello);

        int databaseSizeBeforeUpdate = helloRepository.findAll().size();

        // Update the hello
        Hello updatedHello = new Hello();
        updatedHello.setId(hello.getId());
        updatedHello.setName(UPDATED_NAME);
        updatedHello.setTitle(UPDATED_TITLE);

        restHelloMockMvc.perform(put("/api/hellos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHello)))
                .andExpect(status().isOk());

        // Validate the Hello in the database
        List<Hello> hellos = helloRepository.findAll();
        assertThat(hellos).hasSize(databaseSizeBeforeUpdate);
        Hello testHello = hellos.get(hellos.size() - 1);
        assertThat(testHello.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHello.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    public void deleteHello() throws Exception {
        // Initialize the database
        helloService.save(hello);

        int databaseSizeBeforeDelete = helloRepository.findAll().size();

        // Get the hello
        restHelloMockMvc.perform(delete("/api/hellos/{id}", hello.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hello> hellos = helloRepository.findAll();
        assertThat(hellos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
