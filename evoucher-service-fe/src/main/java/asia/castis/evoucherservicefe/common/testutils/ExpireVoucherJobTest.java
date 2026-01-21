package asia.castis.evoucherservicefe.common.testutils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ExpireVoucherJobTest {
    private final RestTemplate restTemplate = new RestTemplate();

    private String elasticsearchBaseUrl = "http://localhost:9200/";
    public static void main(String[] args) {
        // Insert record
        ExpireVoucherJobTest test = new ExpireVoucherJobTest();
        // Need to update voucher createDate
        createExpiredVoucher(test);
    }

    private static void createExpiredVoucher(ExpireVoucherJobTest test) {
        String documentJson = "{\n" +
                "   \"id\":\"3f4af4cc-7c20-486e-8057-50958644444\",\n" +
                "   \"publishDetailId\":378,\n" +
                "   \"goods\":{\n" +
                "      \"id\":90,\n" +
                "      \"categories\":[\n" +
                "         {\n" +
                "            \"id\":\"Coffee\",\n" +
                "            \"name\":\"COFFEE\"\n" +
                "         }\n" +
                "      ],\n" +
                "      \"brand\":{\n" +
                "         \"id\":\"001-001\",\n" +
                "         \"name\":\"Aqua Retails\",\n" +
                "         \"description\":\"AquaV\",\n" +
                "         \"supplier\":{\n" +
                "            \"id\":\"4134284726\",\n" +
                "            \"name\":\"Coffee world\",\n" +
                "            \"description\":\"We offer a wide range of quality products for your business needs.\",\n" +
                "            \"imgUrl\":\"https://example.com/abc-supplies.png\",\n" +
                "            \"posLink\":true\n" +
                "         }\n" +
                "      },\n" +
                "      \"name\":\"Welcome Gift Voucher\",\n" +
                "      \"listPrice\":0,\n" +
                "      \"sellPrice\":0,\n" +
                "      \"supplyDiscountRate\":100,\n" +
                "      \"supplyDiscountAmount\":0,\n" +
                "      \"isSupplyVatInclude\":false,\n" +
                "      \"startDate\":\"2023-08-08 00:00:00\",\n" +
                "      \"endDate\":\"2024-08-08 00:00:00\",\n" +
                "      \"isValid\":true,\n" +
                "      \"exceptStoreIds\":[\n" +
                "         \"[]\"\n" +
                "      ],\n" +
                "      \"imagePath\":\"/static/images/1691468270057.KakaoTalk_20230808_094841188.png\",\n" +
                "      \"imageName\":\"KakaoTalk_20230808_094841188.png\",\n" +
                "      \"periodType\":\"FIXED_TERM\",\n" +
                "      \"periodTerm\":100\n" +
                "   },\n" +
                "   \"shortLink\":\"https://ub.urlybud.aqua.castis.io/43mUg\",\n" +
                "   \"voucherType\":\"SI\",\n" +
                "   \"userName\":\"3I/x6yaQZ55iAKMHl9t7wQ== \",\n" +
                "   \"userMobileNumber\":\"pWA3jPE2qQ573g6JCpZoxw== \",\n" +
                "   \"subject\":\"Appreciate your visit.\",\n" +
                "   \"content\":\"We provide a gift for you. Please take it at the registration desk.\",\n" +
                "   \"voucherStatus\":\"NORMAL\",\n" +
                "   \"voucherPrice\":0,\n" +
                "   \"discountRate\":50,\n" +
                "   \"discountLimitPrice\":0,\n" +
                "   \"createDate\":\"2023-08-23 13:24:57\",\n" +
                "   \"expireDate\":null\n" +
                "}";
        test.addDocument("voucher", documentJson);
    }

    private void addDocument(String indexName, String documentJson) {
        String addDocumentUrl = elasticsearchBaseUrl + "/" + indexName + "/_doc"; // Assuming "_doc" is the document type

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(documentJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                addDocumentUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Document added successfully");
        } else {
            System.out.println("Failed to add document");
            System.out.println("Response body: " + response.getBody());
        }
    }}
