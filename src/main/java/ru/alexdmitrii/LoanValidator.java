package ru.alexdmitrii;

import org.w3c.dom.Document;
import ru.alexdmitrii.exceptions.IncorrectAgeException;
import ru.alexdmitrii.exceptions.IncorrectAmountLoanException;
import ru.alexdmitrii.exceptions.IncorrectINNException;
import ru.alexdmitrii.exceptions.IncorrectTermLoanException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class LoanValidator {

    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 85;
    private final String path;

    public LoanValidator(String path) {
        this.path = path;
    }

    // Метод для проверки корректности ИНН
    public static boolean isValidINN(String inn) {
        // Проверяем длину ИНН
        if (inn.length() != 10 && inn.length() != 12) {
            return false;
        }

        try {
            Long.parseLong(inn);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean validate() {
        try {

            File xmlFile = new File(this.path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            // Создание XPath для выборки данных
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            // 1. Проверка возраста заемщика
            String ageStr = xpath.evaluate("/Root/ApplicationData/Borrower/Age", document);
            int age = Integer.parseInt(ageStr);
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new IncorrectAgeException();
            }

            // 2. Проверка суммы и срока кредита
            String requestedProductId = xpath.evaluate("/Root/ApplicationData/RequestedProducts/Product/@id", document);
            String requestedAmountStr = xpath.evaluate("/Root/ApplicationData/RequestedProducts/Product/@RequestedAmount", document);
            String requestedTermStr = xpath.evaluate("/Root/ApplicationData/RequestedProducts/Product/@RequestedTerm", document);

            double requestedAmount = Double.parseDouble(requestedAmountStr);
            int requestedTerm = Integer.parseInt(requestedTermStr);

            // Получение условий по выбранному продукту
            String minAmountStr = xpath.evaluate("/Root/SystemData/LoanProducts/Product[@id='" + requestedProductId + "']/@minAmount", document);
            String maxAmountStr = xpath.evaluate("/Root/SystemData/LoanProducts/Product[@id='" + requestedProductId + "']/@maxAmount", document);
            String minTermStr = xpath.evaluate("/Root/SystemData/LoanProducts/Product[@id='" + requestedProductId + "']/@minTerm", document);
            String maxTermStr = xpath.evaluate("/Root/SystemData/LoanProducts/Product[@id='" + requestedProductId + "']/@maxTerm", document);

            double minAmount = Double.parseDouble(minAmountStr);
            double maxAmount = Double.parseDouble(maxAmountStr);
            int minTerm = Integer.parseInt(minTermStr);
            int maxTerm = Integer.parseInt(maxTermStr);

            if (requestedAmount < minAmount || requestedAmount > maxAmount) {
                throw new IncorrectAmountLoanException(minAmount, maxAmount);
            }
            if (requestedTerm < minTerm || requestedTerm > maxTerm) {
                throw new IncorrectTermLoanException(minTerm, maxTerm);
            }

            // 3. Проверка корректности ИНН
            String inn = xpath.evaluate("/Root/ApplicationData/Borrower/INN", document);
            if (!isValidINN(inn)) {
                throw new IncorrectINNException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
