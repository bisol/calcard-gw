package com.bisol.calcard.web.rest;

import static com.bisol.calcard.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.bisol.calcard.CalcardApp;
import com.bisol.calcard.domain.CreditProposal;
import com.bisol.calcard.domain.enumeration.CreditProposalStatus;
import com.bisol.calcard.domain.enumeration.FederationUnit;
import com.bisol.calcard.domain.enumeration.Gender;
import com.bisol.calcard.domain.enumeration.MaritalStatus;
import com.bisol.calcard.domain.enumeration.RejectionReason;
import com.bisol.calcard.repository.CreditProposalRepository;
import com.bisol.calcard.service.CreditProposalService;
import com.bisol.calcard.web.rest.errors.ExceptionTranslator;
/**
 * Test class for the CreditProposalResource REST controller.
 *
 * @see CreditProposalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CalcardApp.class)
public class CreditProposalResourceIntTest {

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLIENT_AGE = 15;
    private static final Integer UPDATED_CLIENT_AGE = 16;

    private static final String DEFAULT_TAXPAYER_ID = "AAAAAAAAAA";
    private static final String UPDATED_TAXPAYER_ID = "BBBBBBBBBB";

    private static final Gender DEFAULT_CLIENT_GENDER = Gender.MALE;
    private static final Gender UPDATED_CLIENT_GENDER = Gender.FEMALE;

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.SINGLE;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.MARRIED;

    private static final Integer DEFAULT_DEPENDENTS = 0;
    private static final Integer UPDATED_DEPENDENTS = 1;

    private static final BigDecimal DEFAULT_INCOME = new BigDecimal(0);
    private static final BigDecimal UPDATED_INCOME = new BigDecimal(1);

    private static final FederationUnit DEFAULT_FEDERATION_UNIT = FederationUnit.AC;
    private static final FederationUnit UPDATED_FEDERATION_UNIT = FederationUnit.AL;

    private static final CreditProposalStatus DEFAULT_STATUS = CreditProposalStatus.PROCESSING;
    private static final CreditProposalStatus UPDATED_STATUS = CreditProposalStatus.APROVED;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PROCESSING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PROCESSING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final RejectionReason DEFAULT_REJECTION_REASON = RejectionReason.POLICY;
    private static final RejectionReason UPDATED_REJECTION_REASON = RejectionReason.INCOME;

    private static final BigDecimal DEFAULT_APROVED_MIN = new BigDecimal(1);
    private static final BigDecimal UPDATED_APROVED_MIN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_APROVED_MAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_APROVED_MAX = new BigDecimal(2);

    @Autowired
    private CreditProposalRepository creditProposalRepository;

    @Autowired
    private CreditProposalService creditProposalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCreditProposalMockMvc;

    private CreditProposal creditProposal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreditProposalResource creditProposalResource = new CreditProposalResource(creditProposalService);
        this.restCreditProposalMockMvc = MockMvcBuilders.standaloneSetup(creditProposalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditProposal createEntity(EntityManager em) {
        CreditProposal creditProposal = new CreditProposal()
            .clientName(DEFAULT_CLIENT_NAME)
            .clientAge(DEFAULT_CLIENT_AGE)
            .taxpayerId(DEFAULT_TAXPAYER_ID)
            .clientGender(DEFAULT_CLIENT_GENDER)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .dependents(DEFAULT_DEPENDENTS)
            .income(DEFAULT_INCOME)
            .federationUnit(DEFAULT_FEDERATION_UNIT)
            .status(DEFAULT_STATUS)
            .creationDate(DEFAULT_CREATION_DATE)
            .processingDate(DEFAULT_PROCESSING_DATE)
            .rejectionReason(DEFAULT_REJECTION_REASON)
            .aprovedMin(DEFAULT_APROVED_MIN)
            .aprovedMax(DEFAULT_APROVED_MAX);
        return creditProposal;
    }

    @Before
    public void initTest() {
        creditProposal = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreditProposal() throws Exception {
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();

        // Create the CreditProposal
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isCreated());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate + 1);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testCreditProposal.getClientAge()).isEqualTo(DEFAULT_CLIENT_AGE);
        assertThat(testCreditProposal.getTaxpayerId()).isEqualTo(DEFAULT_TAXPAYER_ID);
        assertThat(testCreditProposal.getClientGender()).isEqualTo(DEFAULT_CLIENT_GENDER);
        assertThat(testCreditProposal.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testCreditProposal.getDependents()).isEqualTo(DEFAULT_DEPENDENTS);
        assertThat(testCreditProposal.getIncome()).isEqualTo(DEFAULT_INCOME);
        assertThat(testCreditProposal.getFederationUnit()).isEqualTo(DEFAULT_FEDERATION_UNIT);
        assertThat(testCreditProposal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCreditProposal.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCreditProposal.getProcessingDate()).isEqualTo(DEFAULT_PROCESSING_DATE);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(DEFAULT_REJECTION_REASON);
        assertThat(testCreditProposal.getAprovedMin()).isEqualTo(DEFAULT_APROVED_MIN);
        assertThat(testCreditProposal.getAprovedMax()).isEqualTo(DEFAULT_APROVED_MAX);
    }

    @Test
    @Transactional
    public void createCreditProposalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creditProposalRepository.findAll().size();

        // Create the CreditProposal with an existing ID
        creditProposal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClientNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setClientName(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClientAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setClientAge(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTaxpayerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setTaxpayerId(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClientGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setClientGender(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaritalStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setMaritalStatus(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDependentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setDependents(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIncomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setIncome(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFederationUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditProposalRepository.findAll().size();
        // set the field null
        creditProposal.setFederationUnit(null);

        // Create the CreditProposal, which fails.

        restCreditProposalMockMvc.perform(post("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCreditProposals() throws Exception {
        // Initialize the database
        creditProposalRepository.saveAndFlush(creditProposal);

        // Get all the creditProposalList
        restCreditProposalMockMvc.perform(get("/api/credit-proposals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditProposal.getId().intValue())))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].clientAge").value(hasItem(DEFAULT_CLIENT_AGE)))
            .andExpect(jsonPath("$.[*].taxpayerId").value(hasItem(DEFAULT_TAXPAYER_ID.toString())))
            .andExpect(jsonPath("$.[*].clientGender").value(hasItem(DEFAULT_CLIENT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dependents").value(hasItem(DEFAULT_DEPENDENTS)))
            .andExpect(jsonPath("$.[*].income").value(hasItem(DEFAULT_INCOME.intValue())))
            .andExpect(jsonPath("$.[*].federationUnit").value(hasItem(DEFAULT_FEDERATION_UNIT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingDate").value(hasItem(DEFAULT_PROCESSING_DATE.toString())))
            .andExpect(jsonPath("$.[*].rejectionReason").value(hasItem(DEFAULT_REJECTION_REASON.toString())))
            .andExpect(jsonPath("$.[*].aprovedMin").value(hasItem(DEFAULT_APROVED_MIN.intValue())))
            .andExpect(jsonPath("$.[*].aprovedMax").value(hasItem(DEFAULT_APROVED_MAX.intValue())));
    }
    
    @Test
    @Transactional
    public void getCreditProposal() throws Exception {
        // Initialize the database
        creditProposalRepository.saveAndFlush(creditProposal);

        // Get the creditProposal
        restCreditProposalMockMvc.perform(get("/api/credit-proposals/{id}", creditProposal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creditProposal.getId().intValue()))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME.toString()))
            .andExpect(jsonPath("$.clientAge").value(DEFAULT_CLIENT_AGE))
            .andExpect(jsonPath("$.taxpayerId").value(DEFAULT_TAXPAYER_ID.toString()))
            .andExpect(jsonPath("$.clientGender").value(DEFAULT_CLIENT_GENDER.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.dependents").value(DEFAULT_DEPENDENTS))
            .andExpect(jsonPath("$.income").value(DEFAULT_INCOME.intValue()))
            .andExpect(jsonPath("$.federationUnit").value(DEFAULT_FEDERATION_UNIT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.processingDate").value(DEFAULT_PROCESSING_DATE.toString()))
            .andExpect(jsonPath("$.rejectionReason").value(DEFAULT_REJECTION_REASON.toString()))
            .andExpect(jsonPath("$.aprovedMin").value(DEFAULT_APROVED_MIN.intValue()))
            .andExpect(jsonPath("$.aprovedMax").value(DEFAULT_APROVED_MAX.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCreditProposal() throws Exception {
        // Get the creditProposal
        restCreditProposalMockMvc.perform(get("/api/credit-proposals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreditProposal() throws Exception {
        // Initialize the database
        creditProposalService.save(creditProposal);

        int databaseSizeBeforeUpdate = creditProposalRepository.findAll().size();

        // Update the creditProposal
        CreditProposal updatedCreditProposal = creditProposalRepository.findById(creditProposal.getId()).get();
        // Disconnect from session so that the updates on updatedCreditProposal are not directly saved in db
        em.detach(updatedCreditProposal);
        updatedCreditProposal
            .clientName(UPDATED_CLIENT_NAME)
            .clientAge(UPDATED_CLIENT_AGE)
            .taxpayerId(UPDATED_TAXPAYER_ID)
            .clientGender(UPDATED_CLIENT_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dependents(UPDATED_DEPENDENTS)
            .income(UPDATED_INCOME)
            .federationUnit(UPDATED_FEDERATION_UNIT)
            .status(UPDATED_STATUS)
            .creationDate(UPDATED_CREATION_DATE)
            .processingDate(UPDATED_PROCESSING_DATE)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .aprovedMin(UPDATED_APROVED_MIN)
            .aprovedMax(UPDATED_APROVED_MAX);

        restCreditProposalMockMvc.perform(put("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCreditProposal)))
            .andExpect(status().isOk());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeUpdate);
        CreditProposal testCreditProposal = creditProposalList.get(creditProposalList.size() - 1);
        assertThat(testCreditProposal.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testCreditProposal.getClientAge()).isEqualTo(UPDATED_CLIENT_AGE);
        assertThat(testCreditProposal.getTaxpayerId()).isEqualTo(UPDATED_TAXPAYER_ID);
        assertThat(testCreditProposal.getClientGender()).isEqualTo(UPDATED_CLIENT_GENDER);
        assertThat(testCreditProposal.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testCreditProposal.getDependents()).isEqualTo(UPDATED_DEPENDENTS);
        assertThat(testCreditProposal.getIncome()).isEqualTo(UPDATED_INCOME);
        assertThat(testCreditProposal.getFederationUnit()).isEqualTo(UPDATED_FEDERATION_UNIT);
        assertThat(testCreditProposal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCreditProposal.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCreditProposal.getProcessingDate()).isEqualTo(UPDATED_PROCESSING_DATE);
        assertThat(testCreditProposal.getRejectionReason()).isEqualTo(UPDATED_REJECTION_REASON);
        assertThat(testCreditProposal.getAprovedMin()).isEqualTo(UPDATED_APROVED_MIN);
        assertThat(testCreditProposal.getAprovedMax()).isEqualTo(UPDATED_APROVED_MAX);
    }

    @Test
    @Transactional
    public void updateNonExistingCreditProposal() throws Exception {
        int databaseSizeBeforeUpdate = creditProposalRepository.findAll().size();

        // Create the CreditProposal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditProposalMockMvc.perform(put("/api/credit-proposals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creditProposal)))
            .andExpect(status().isBadRequest());

        // Validate the CreditProposal in the database
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCreditProposal() throws Exception {
        // Initialize the database
        creditProposalService.save(creditProposal);

        int databaseSizeBeforeDelete = creditProposalRepository.findAll().size();

        // Get the creditProposal
        restCreditProposalMockMvc.perform(delete("/api/credit-proposals/{id}", creditProposal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CreditProposal> creditProposalList = creditProposalRepository.findAll();
        assertThat(creditProposalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditProposal.class);
        CreditProposal creditProposal1 = new CreditProposal();
        creditProposal1.setId(1L);
        CreditProposal creditProposal2 = new CreditProposal();
        creditProposal2.setId(creditProposal1.getId());
        assertThat(creditProposal1).isEqualTo(creditProposal2);
        creditProposal2.setId(2L);
        assertThat(creditProposal1).isNotEqualTo(creditProposal2);
        creditProposal1.setId(null);
        assertThat(creditProposal1).isNotEqualTo(creditProposal2);
    }
}
