import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

//https://retrieval.covid-notification.alpha.canada.ca/retrieve/302/00000/3631313045444b345742464633504e44524a3457494855505639593136464a3846584d4c59334d30

class DetermineDiagnosisKeysURL {

    // retrieved from COVID Alert APK
    static String MCC_CODE = "302";
    static String retrieveUrl = "https://retrieval.covid-notification.alpha.canada.ca";
    static String hmacKey = "3631313045444b345742464633504e44524a3457494855505639593136464a3846584d4c59334d30";

    // See https://github.com/cds-snc/covid-shield-server/pull/176
    static String LAST_14_DAYS_PERIOD = "00000";

    static long HOURS_PER_PERIOD = 24;

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) 
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte[] hexToBytes(String s) 
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;   
    }

    public static void main(String[] args) {

        try {
            long period = 0;

            if (args.length > 0) {
                int daysAgo = Integer.parseInt(args[0]);
                period = System.currentTimeMillis() / 1000 / 3600 / HOURS_PER_PERIOD - daysAgo;
            }
                
            //const periodStr = `${period > 0 ? period : LAST_14_DAYS_PERIOD}`;
            //const message = `${MCC_CODE}:${periodStr}:${Math.floor(getMillisSinceUTCEpoch() / 1000 / 3600)}`;
            //const hmac = hmac256(message, encHex.parse(this.hmacKey)).toString(encHex);
            //const url = `${this.retrieveUrl}/retrieve/${MCC_CODE}/${periodStr}/${hmac}`;

            String periodStr = period > 0 ? Long.toString(period) : LAST_14_DAYS_PERIOD;
            System.out.println(System.currentTimeMillis() / 1000/ 3600);
            String message = MCC_CODE + ":" + periodStr + ":" + Long.toString(System.currentTimeMillis() / 1000 / 3600);

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(hexToBytes(hmacKey), "HmacSHA256");
            mac.init(secretKeySpec);
            String hmac = bytesToHex(mac.doFinal(message.getBytes()));

            String url = retrieveUrl + "/retrieve/" + MCC_CODE + "/" + periodStr + "/" + hmac;

            System.out.println(url); 
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
}