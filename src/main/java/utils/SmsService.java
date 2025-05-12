package utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import config.TwilioConfig;

public class SmsService {
    // Initialiser Twilio une seule fois
    static {
        Twilio.init(TwilioConfig.getAccountSid(), TwilioConfig.getAuthToken());
    }
    
    /**
     * Envoie un SMS à un numéro spécifié
     * @param to Numéro de téléphone du destinataire (format international, ex: +33612345678)
     * @param messageBody Contenu du message
     * @return ID du message envoyé
     */
    public static String sendSms(String to, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(TwilioConfig.getTwilioNumber()),
                    messageBody)
                .create();
            
            System.out.println("SMS envoyé avec l'ID: " + message.getSid());
            return message.getSid();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Envoie une notification d'ajout au panier
     * @param phoneNumber Numéro de téléphone du client
     * @param productName Nom du produit ajouté
     * @param price Prix du produit
     */
    public static void sendCartNotification(String phoneNumber, String productName, double price) {
        String message = "Vous avez ajouté " + productName + " à votre panier pour " + 
                         String.format("%.2f", price) + " DT. Merci pour votre achat!";
        sendSms(phoneNumber, message);
    }
}