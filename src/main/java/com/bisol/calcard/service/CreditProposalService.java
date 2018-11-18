package com.bisol.calcard.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisol.calcard.domain.CreditProposal;
import com.bisol.calcard.messaging.CreditProposalProducerChannel;
import com.bisol.calcard.messaging.CreditProposalResultConsumerChannel;
import com.bisol.calcard.repository.CreditProposalRepository;

/**
 * Service Implementation for managing CreditProposal.
 */
@Service
@Transactional
public class CreditProposalService {

    private final Logger log = LoggerFactory.getLogger(CreditProposalService.class);

    private final CreditProposalRepository creditProposalRepository;

	private final MessageChannel producerChannel;

	private final SimpMessageSendingOperations messagingTemplate;

	public CreditProposalService(CreditProposalRepository creditProposalRepository,
			SimpMessageSendingOperations messagingTemplate,
			@Qualifier(CreditProposalProducerChannel.CHANNEL) MessageChannel producerChannel) {
        this.creditProposalRepository = creditProposalRepository;
		this.messagingTemplate = messagingTemplate;
		this.producerChannel = producerChannel;
    }

    /**
	 * Save a creditProposal and triggers processing service.
	 *
	 * @param creditProposal
	 *            the entity to save
	 * @return the persisted entity
	 */
    public CreditProposal save(CreditProposal creditProposal) {
        log.debug("Request to save CreditProposal : {}", creditProposal);
		creditProposal = creditProposalRepository.save(creditProposal);
		producerChannel.send(MessageBuilder.withPayload(creditProposal).build());
		return creditProposal;
    }

	/**
	 * Receives processing results from micro service, updates database and notifies
	 * front end via websocket.
	 * 
	 * @param creditProposal
	 *            the processed entity
	 */
	@StreamListener(CreditProposalResultConsumerChannel.CHANNEL)
	public void consume(CreditProposal creditProposal) {
		log.info("Received proposal result: {}.", creditProposal);
		creditProposal = creditProposalRepository.save(creditProposal);
		messagingTemplate.convertAndSend("/topic/credit-proposal", creditProposal);
	}

    /**
     * Get all the creditProposals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
	public Page<CreditProposal> findAll(String cpf, Pageable pageable) {
        log.debug("Request to get all CreditProposals");
		if (cpf == null || cpf.isEmpty()) {
			return creditProposalRepository.findAll(pageable);
		}

		return creditProposalRepository.findByTaxpayerId(cpf, pageable);
    }


    /**
     * Get one creditProposal by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CreditProposal> findOne(Long id) {
        log.debug("Request to get CreditProposal : {}", id);
        return creditProposalRepository.findById(id);
    }

    /**
     * Delete the creditProposal by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CreditProposal : {}", id);
        creditProposalRepository.deleteById(id);
    }
}
