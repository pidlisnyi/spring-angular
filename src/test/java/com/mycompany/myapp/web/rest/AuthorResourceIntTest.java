package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Author;
import com.mycompany.myapp.repository.AuthorRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AuthorResource REST controller.
 *
 * @see AuthorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class AuthorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAuthorMockMvc;

    private Author author;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthorResource authorResource = new AuthorResource();
        ReflectionTestUtils.setField(authorResource, "authorRepository", authorRepository);
        this.restAuthorMockMvc = MockMvcBuilders.standaloneSetup(authorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        authorRepository.deleteAll();
        author = new Author();
        author.setName(DEFAULT_NAME);
        author.setBirthDate(DEFAULT_BIRTH_DATE);
    }

    @Test
    public void createAuthor() throws Exception {
        int databaseSizeBeforeCreate = authorRepository.findAll().size();

        // Create the Author

        restAuthorMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(author)))
                .andExpect(status().isCreated());

        // Validate the Author in the database
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeCreate + 1);
        Author testAuthor = authors.get(authors.size() - 1);
        assertThat(testAuthor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuthor.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    public void getAllAuthors() throws Exception {
        // Initialize the database
        authorRepository.save(author);

        // Get all the authors
        restAuthorMockMvc.perform(get("/api/authors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    public void getAuthor() throws Exception {
        // Initialize the database
        authorRepository.save(author);

        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(author.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    public void getNonExistingAuthor() throws Exception {
        // Get the author
        restAuthorMockMvc.perform(get("/api/authors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAuthor() throws Exception {
        // Initialize the database
        authorRepository.save(author);
        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        // Update the author
        Author updatedAuthor = new Author();
        updatedAuthor.setId(author.getId());
        updatedAuthor.setName(UPDATED_NAME);
        updatedAuthor.setBirthDate(UPDATED_BIRTH_DATE);

        restAuthorMockMvc.perform(put("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuthor)))
                .andExpect(status().isOk());

        // Validate the Author in the database
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeUpdate);
        Author testAuthor = authors.get(authors.size() - 1);
        assertThat(testAuthor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuthor.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    public void deleteAuthor() throws Exception {
        // Initialize the database
        authorRepository.save(author);
        int databaseSizeBeforeDelete = authorRepository.findAll().size();

        // Get the author
        restAuthorMockMvc.perform(delete("/api/authors/{id}", author.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
