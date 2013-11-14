package org.hiperion.node.service.action.email;

import com.googlecode.gmail4j.EmailAddress;
import com.googlecode.gmail4j.javamail.JavaMailGmailMessage;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.exception.ParameterConversionException;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.action.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 11:21
 */
@Component
public class EmailNotificationAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationAction.class);

    private final GmailNotificationService gmailNotificationService;

    private enum EmailNotificationParameterDescription {
        SUBJECT("subject", "Subject of notification email.", ParameterType.SIMPLE),
        MESSAGE_TEXT("text", "Message notification text.", ParameterType.SIMPLE),
        RECIPIENTS("recipients","List of message recipients.", ParameterType.LIST);

        private final String parameterName;
        private final String parameterDescription;
        private final ParameterType parameterType;

        private EmailNotificationParameterDescription(String parameterName, String parameterDescription,
                                              ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    @Autowired
    public EmailNotificationAction(GmailNotificationService gmailNotificationService) {
        this.gmailNotificationService = gmailNotificationService;
    }

    @PostConstruct
    @Override
    public void init() {
        LOGGER.info("Action initialized.");
    }

    private EmailNotificationActionParameter getEmailNotificationActionParameter(ParameterContext parameterContext)
        throws ParameterConversionException {

        final String messageSubject = parameterContext.getSimpleParameters().get(
                EmailNotificationParameterDescription.SUBJECT.parameterName);
        final List<String> recipients = parameterContext.getListParameters().get(
                EmailNotificationParameterDescription.RECIPIENTS.parameterName);
        final String messageText = parameterContext.getSimpleParameters().get(
                EmailNotificationParameterDescription.MESSAGE_TEXT.parameterName);

        EmailNotificationActionParameter emailNotificationActionParameter =
                new EmailNotificationActionParameter() {
                    @Override
                    public String getMessageSubject() {
                        return messageSubject;
                    }

                    @Override
                    public List<String> getRecipients() {
                        return recipients;
                    }

                    @Override
                    public String getMessageText() {
                        return messageText;
                    }
                };
        return emailNotificationActionParameter;
    }

    @Override
    public boolean executeAction(ParameterContext parameterContext) throws HiperionException {
        JavaMailGmailMessage javaMailGmailMessage = new JavaMailGmailMessage();
        EmailNotificationActionParameter emailNotificationActionParameter =
                getEmailNotificationActionParameter(parameterContext);
        javaMailGmailMessage.setSubject(emailNotificationActionParameter.getMessageSubject());
        javaMailGmailMessage.setContentText(emailNotificationActionParameter.getMessageText());
        for(String recipientAddress : emailNotificationActionParameter.getRecipients()){
            javaMailGmailMessage.addTo(new EmailAddress(recipientAddress));
        }

        gmailNotificationService.submitForSending(javaMailGmailMessage);
        return true;
    }

    @PreDestroy
    @Override
    public void dispose() {
        LOGGER.info("Action disposed.");
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = EmailNotificationParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (EmailNotificationParameterDescription emailNotificationParameterDescription : EmailNotificationParameterDescription.values()) {
            result[i++] = new ParameterInfo(emailNotificationParameterDescription.parameterName,
                    emailNotificationParameterDescription.parameterDescription,
                    emailNotificationParameterDescription.parameterType);
        }
        return result;
    }
}
