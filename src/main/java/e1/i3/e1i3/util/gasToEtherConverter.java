package e1.i3.e1i3.util;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
public class gasToEtherConverter {

    public static BigDecimal convertGasToEther(String gasHex) {
        BigInteger gasUsed = new BigInteger(gasHex.substring(2), 16);
        BigDecimal wei = Convert.toWei(gasUsed.toString(), Convert.Unit.GWEI);
        BigDecimal ether = Convert.fromWei(wei, Convert.Unit.ETHER);
        return ether;
    }

}
