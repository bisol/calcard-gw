package com.bisol.calcard.messaging;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CreditProposalProducerChannel {

    String CHANNEL = "creditProposalChannel";

    @Output
    @Qualifier(CHANNEL)
	MessageChannel creditProposalChannel();
}