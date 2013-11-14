package org.hiperion.node.service.action.email;

import java.util.List;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 12:03
 */
public interface EmailNotificationActionParameter {

    String getMessageSubject();

    List<String> getRecipients();

    String getMessageText();
}
